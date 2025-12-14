package screens

import (
	"fmt"
	"time"

	"github.com/charmbracelet/bubbles/progress"
	tea "github.com/charmbracelet/bubbletea"
	"github.com/yannouuuu/ijava-enhanced/internal/installer"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/components"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/styles"
)

type DownloadModel struct {
	width      int
	height     int
	progress   progress.Model
	percent    float64
	speed      string
	downloaded string
	total      string
	done       bool
	err        error
	simulating bool
}

func NewDownloadModel() DownloadModel {
	p := progress.New(progress.WithDefaultGradient())

	return DownloadModel{
		progress:   p,
		simulating: true,
	}
}

func (m DownloadModel) Init() tea.Cmd {
	return tea.Tick(time.Millisecond*50, func(t time.Time) tea.Msg {
		return progressTickMsg{}
	})
}

type downloadCompleteMsg struct{}

type progressTickMsg struct{}

func (m DownloadModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.width = msg.Width
		m.height = msg.Height
		m.progress.Width = msg.Width - 20
		return m, nil

	case progressTickMsg:
		if m.simulating && !m.done {
			// Incrémenter pour animation fluide (3 secondes total = 60 ticks)
			m.percent += 1.7
			
			if m.percent >= 100 {
				// Forcer exactement 100% pour le dernier affichage
				m.percent = 100
				m.done = true
				m.simulating = false
				
				// Mettre à jour la barre une dernière fois à 100%
				m.speed = "5.0 MB/s"
				m.downloaded = "15.0 MB"
				m.total = "15.0 MB"
				
				cmd := m.progress.SetPercent(1.0) // 100%
				
				// Attendre 2 secondes avant de passer à l'écran suivant
				return m, tea.Batch(
					cmd,
					tea.Tick(time.Second*2, func(t time.Time) tea.Msg {
						return NextScreenMsg{}
					}),
				)
			}

			// Simuler les stats
			m.speed = "5.0 MB/s"
			m.downloaded = installer.FormatBytes(int64(float64(15*1024*1024) * m.percent / 100))
			m.total = "15.0 MB"

			cmd := m.progress.SetPercent(m.percent / 100)
			return m, tea.Batch(
				cmd,
				tea.Tick(time.Millisecond*50, func(t time.Time) tea.Msg {
					return progressTickMsg{}
				}),
			)
		}
		return m, nil

	case progress.FrameMsg:
		progressModel, cmd := m.progress.Update(msg)
		m.progress = progressModel.(progress.Model)
		return m, cmd
	}

	return m, nil
}

func (m DownloadModel) View() string {
	var content string

	content += components.RenderHeaderSimple("Téléchargement du toolkit iJava", m.width)
	content += "\n\n"

	if m.err != nil {
		content += styles.RenderError(fmt.Sprintf("Erreur: %v", m.err))
	} else if m.done {
		content += styles.RenderSuccess("Téléchargement terminé !")
		content += "\n"
		content += m.progress.View()
		content += "\n\n"
		content += styles.InfoTextStyle.Render("Passage à l'installation...")
	} else {
		content += m.progress.View()
		content += "\n\n"
		content += fmt.Sprintf("%s Vitesse: %s | Téléchargé: %s / %s",
			styles.SymbolDownload, m.speed, m.downloaded, m.total)
	}

	content += "\n\n"
	content += components.RenderFooter("q/esc/ctrl+c quitter")

	return content
}
