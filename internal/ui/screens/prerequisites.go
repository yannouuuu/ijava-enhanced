package screens

import (
	"fmt"

	"github.com/charmbracelet/bubbles/spinner"
	"github.com/charmbracelet/huh"
	tea "github.com/charmbracelet/bubbletea"
	"github.com/yannouuuu/ijava-enhanced/internal/installer"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/components"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/styles"
)

type PrerequisitesModel struct {
	width    int
	height   int
	spinner  spinner.Model
	
	step            int
	checking        bool
	javaInfo        *installer.JavaInfo
	shells          []installer.Shell
	installJava     bool
	installingJava  bool
	javaInstallDone bool
	error           error
	
	javaForm *huh.Form
}

func NewPrerequisitesModel() PrerequisitesModel {
	s := spinner.New()
	s.Spinner = spinner.Dot
	s.Style = styles.InfoTextStyle

	return PrerequisitesModel{
		spinner:  s,
		checking: true,
		step:     0,
	}
}

func (m PrerequisitesModel) Init() tea.Cmd {
	return tea.Batch(
		m.spinner.Tick,
		checkPrerequisites,
	)
}

type prerequisitesCheckedMsg struct {
	javaInfo *installer.JavaInfo
	shells   []installer.Shell
	err      error
}

type javaInstalledMsg struct {
	success bool
	err     error
}

func checkPrerequisites() tea.Msg {
	javaInfo, err := installer.DetectJava()
	if err != nil {
		return prerequisitesCheckedMsg{err: err}
	}

	shells := installer.DetectShells()

	return prerequisitesCheckedMsg{
		javaInfo: javaInfo,
		shells:   shells,
	}
}

func installJavaCmd() tea.Msg {
	err := installer.InstallJava()
	if err != nil {
		return javaInstalledMsg{success: false, err: err}
	}
	
	javaInfo, err := installer.DetectJava()
	if err != nil || !javaInfo.Installed {
		return javaInstalledMsg{success: false, err: fmt.Errorf("Java installé mais non détecté")}
	}
	
	return javaInstalledMsg{success: true}
}

func (m PrerequisitesModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.width = msg.Width
		m.height = msg.Height
		return m, nil

	case prerequisitesCheckedMsg:
		m.checking = false
		m.javaInfo = msg.javaInfo
		m.shells = msg.shells
		m.error = msg.err

		if m.javaInfo != nil && m.javaInfo.Installed {
			// Java installé - PAS DE TRANSITION AUTO
			m.step = 3
			return m, nil
		} else {
			// Java non installé, proposer installation
			m.step = 1
			m.javaForm = huh.NewForm(
				huh.NewGroup(
					huh.NewConfirm().
						Title("Java n'est pas installé").
						Description("Voulez-vous installer Java automatiquement ?").
						Affirmative("Oui, installer").
						Negative("Non, continuer sans Java").
						Value(&m.installJava),
				),
			)
			return m, m.javaForm.Init()
		}

	case tea.KeyMsg:
		// Enter explicite pour continuer après vérification
		if m.step == 3 && msg.String() == "enter" {
			return m, func() tea.Msg { return NextScreenMsg{} }
		}
		
		if m.step == 1 && m.javaForm != nil {
			form, cmd := m.javaForm.Update(msg)
			if f, ok := form.(*huh.Form); ok {
				m.javaForm = f
				
				if m.javaForm.State == huh.StateCompleted {
					if m.installJava {
						m.step = 2
						m.installingJava = true
						return m, tea.Batch(
							m.spinner.Tick,
							installJavaCmd,
						)
					} else {
						m.step = 3
						return m, nil
					}
				}
				
				return m, cmd
			}
		}

	case javaInstalledMsg:
		m.installingJava = false
		m.javaInstallDone = true
		
		if msg.success {
			javaInfo, _ := installer.DetectJava()
			m.javaInfo = javaInfo
		} else {
			m.error = msg.err
		}
		m.step = 3
		return m, nil

	case spinner.TickMsg:
		if m.checking || m.installingJava {
			var cmd tea.Cmd
			m.spinner, cmd = m.spinner.Update(msg)
			return m, cmd
		}
	}

	return m, nil
}

func (m PrerequisitesModel) View() string {
	var content string

	content += components.RenderHeaderSimple("Vérification des prérequis", m.width)
	content += "\n\n"

	switch m.step {
	case 0:
		content += fmt.Sprintf("%s Vérification de l'environnement...\n", m.spinner.View())
		
	case 1:
		content += m.javaForm.View()
		
	case 2:
		content += fmt.Sprintf("%s Installation de Java en cours...\n", m.spinner.View())
		content += "\n"
		content += styles.InfoTextStyle.Render("Cela peut prendre quelques minutes...")
		
	case 3:
		if m.javaInfo != nil && m.javaInfo.Installed {
			content += styles.RenderSuccess(fmt.Sprintf("Java détecté: %s", m.javaInfo.Version))
		} else if m.javaInstallDone && m.error != nil {
			content += styles.RenderError(fmt.Sprintf("Échec installation Java: %v", m.error))
			content += "\n\n"
			content += styles.WarningTextStyle.Render("⚠ Vous devrez installer Java manuellement")
		} else {
			content += styles.RenderWarning("Java non détecté")
			content += "\n\n"
			content += styles.InfoTextStyle.Render("L'installation continuera mais Java sera requis pour utiliser iJava")
		}
		content += "\n\n"

		content += styles.LabelStyle.Render("Shells détectés:") + "\n"
		detectedCount := 0
		for _, shell := range m.shells {
			if shell.Detected {
				content += styles.RenderListItem(fmt.Sprintf("%s (%s)", shell.DisplayName, shell.ConfigPath))
				content += "\n"
				detectedCount++
			}
		}
		if detectedCount == 0 {
			content += styles.MutedTextStyle.Render("  Aucun shell détecté")
			content += "\n"
		}

		content += "\n"
		content += styles.WarningTextStyle.Render("⚠ Appuyez sur ENTER pour continuer")
	}

	content += "\n"
	content += components.RenderFooter("enter continuer", "q/esc/ctrl+c quitter")

	return content
}

func (m PrerequisitesModel) GetJavaInfo() *installer.JavaInfo {
	return m.javaInfo
}

func (m PrerequisitesModel) GetShells() []installer.Shell {
	return m.shells
}
