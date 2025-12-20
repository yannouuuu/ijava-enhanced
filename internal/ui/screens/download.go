package screens

import (
	"fmt"
	"os"
	"path/filepath"
	"time"

	"github.com/charmbracelet/bubbles/progress"
	"github.com/charmbracelet/bubbles/spinner"
	tea "github.com/charmbracelet/bubbletea"
	"github.com/yannouuuu/ijava-enhanced/internal/installer"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/components"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/styles"
)

type DownloadModel struct {
	width    int
	height   int
	progress progress.Model
	spinner  spinner.Model

	url         string
	destPath    string
	installJava bool

	state      downloadState
	percent    float64
	speed      string
	downloaded string
	total      string
	err        error

	progressChan chan installer.DownloadProgress
	errChan      chan error
}

type downloadState int

const (
	stateWaiting downloadState = iota
	stateInstallingJava
	stateDownloading
	stateDone
)

func NewDownloadModel(url, destPath string, installJava bool) DownloadModel {
	p := progress.New(progress.WithDefaultGradient())
	s := spinner.New()
	s.Spinner = spinner.Dot
	s.Style = styles.InfoTextStyle

	return DownloadModel{
		progress:     p,
		spinner:      s,
		url:          url,
		destPath:     destPath,
		installJava:  installJava,
		state:        stateWaiting,
		progressChan: make(chan installer.DownloadProgress),
		errChan:      make(chan error, 1),
	}
}

func (m DownloadModel) Init() tea.Cmd {
	var cmds []tea.Cmd
	cmds = append(cmds, m.spinner.Tick)

	if m.installJava {
		m.state = stateInstallingJava
		cmds = append(cmds, downloadInstallJavaCmd)
	} else {
		m.state = stateDownloading
		cmds = append(cmds, m.startDownload())
	}

	return tea.Batch(cmds...)
}

type javaInstallFinishedMsg struct{ err error }
type downloadFinishedMsg struct{ err error }
type progressMsg installer.DownloadProgress

func downloadInstallJavaCmd() tea.Msg {
	err := installer.InstallJava()
	return javaInstallFinishedMsg{err: err}
}

func (m DownloadModel) startDownload() tea.Cmd {
	go func() {
		// S'assurer que le répertoire existe
		if err := os.MkdirAll(filepath.Dir(m.destPath), 0755); err != nil {
			m.errChan <- err
			return
		}

		err := installer.DownloadFile(m.url, m.destPath, func(p installer.DownloadProgress) {
			m.progressChan <- p
		})
		if err != nil {
			m.errChan <- err
		}
		close(m.progressChan)
		close(m.errChan)
	}()

	return tea.Batch(
		waitForProgress(m.progressChan),
		waitForError(m.errChan),
	)
}

func waitForProgress(ch <-chan installer.DownloadProgress) tea.Cmd {
	return func() tea.Msg {
		p, ok := <-ch
		if !ok {
			return nil
		}
		return progressMsg(p)
	}
}

func waitForError(ch <-chan error) tea.Cmd {
	return func() tea.Msg {
		err, ok := <-ch
		if !ok {
			return downloadFinishedMsg{err: nil} // Succès si aucune erreur envoyée
		}
		return downloadFinishedMsg{err: err}
	}
}

func (m DownloadModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.width = msg.Width
		m.height = msg.Height
		m.progress.Width = msg.Width - 20
		return m, nil

	case spinner.TickMsg:
		var cmd tea.Cmd
		m.spinner, cmd = m.spinner.Update(msg)
		return m, cmd

	case javaInstallFinishedMsg:
		if msg.err != nil {
			m.err = msg.err
			m.state = stateDone // Ou état d'erreur
			return m, nil
		}
		// Java installé, démarrer le téléchargement
		m.state = stateDownloading
		return m, m.startDownload()

	case progressMsg:
		m.percent = msg.Percent
		m.speed = fmt.Sprintf("%.1f MB/s", msg.BytesPerSec/1024/1024)
		m.downloaded = installer.FormatBytes(msg.Downloaded)
		m.total = installer.FormatBytes(msg.Total)

		cmd := m.progress.SetPercent(m.percent / 100)
		return m, tea.Batch(cmd, waitForProgress(m.progressChan))

	case downloadFinishedMsg:
		if msg.err != nil {
			m.err = msg.err
		}
		m.state = stateDone
		m.percent = 100
		cmd := m.progress.SetPercent(1.0)

		return m, tea.Batch(
			cmd,
			tea.Tick(time.Second*1, func(t time.Time) tea.Msg {
				return NextScreenMsg{}
			}),
		)

	case progress.FrameMsg:
		progressModel, cmd := m.progress.Update(msg)
		m.progress = progressModel.(progress.Model)
		return m, cmd
	}

	return m, nil
}

func (m DownloadModel) View() string {
	var content string

	content += components.RenderHeaderSimple("Téléchargement", m.width)
	content += "\n\n"

	if m.err != nil {
		content += styles.RenderError(fmt.Sprintf("Erreur: %v", m.err))
		content += "\n\n"
		content += components.RenderFooter("q quitter")
		return content
	}

	switch m.state {
	case stateInstallingJava:
		content += fmt.Sprintf("%s Installation de Java en cours...\n", m.spinner.View())
		content += styles.InfoTextStyle.Render("Cette opération peut prendre plusieurs minutes.")
		content += "\n"
		content += styles.MutedTextStyle.Render("Veuillez patienter et ne pas fermer la fenêtre.")

	case stateDownloading:
		content += fmt.Sprintf("%s Téléchargement du toolkit iJava...\n", m.spinner.View())
		content += "\n"
		content += m.progress.View()
		content += "\n\n"
		content += fmt.Sprintf("%s Vitesse: %s | Téléchargé: %s / %s",
			styles.SymbolDownload, m.speed, m.downloaded, m.total)

	case stateDone:
		content += styles.RenderSuccess("Téléchargement terminé !")
		content += "\n"
		content += m.progress.View()
		content += "\n\n"
		content += styles.InfoTextStyle.Render("Passage à l'installation...")
	}

	content += "\n\n"
	content += components.RenderFooter("q/esc/ctrl+c quitter")

	return content
}
