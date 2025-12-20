package screens

import (
	"fmt"
	"time"

	tea "github.com/charmbracelet/bubbletea"
	"github.com/yannouuuu/ijava-enhanced/internal/config"
	"github.com/yannouuuu/ijava-enhanced/internal/installer"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/components"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/styles"
)

type InstallModel struct {
	width       int
	height      int
	currentStep int
	steps       []string
	logs        []string
	done        bool
	err         error

	cfg    *config.InstallConfig
	paths  *installer.InstallPaths
	shells []installer.Shell
}

func NewInstallModel(cfg *config.InstallConfig, paths *installer.InstallPaths, shells []installer.Shell) InstallModel {
	return InstallModel{
		steps: []string{
			"Création du wrapper",
			"Configuration des shells",
			"Finalisation",
		},
		logs:   []string{},
		cfg:    cfg,
		paths:  paths,
		shells: shells,
	}
}

func (m InstallModel) Init() tea.Cmd {
	return func() tea.Msg {
		return performStepMsg{step: 0}
	}
}

type performStepMsg struct {
	step int
}

type stepResultMsg struct {
	step int
	log  string
	err  error
}

func (m InstallModel) performStep(step int) tea.Cmd {
	return func() tea.Msg {
		var log string
		var err error

		// Petit délai pour que l'utilisateur voie l'étape
		time.Sleep(500 * time.Millisecond)

		switch step {
		case 0: // Wrapper
			log = "Création du script wrapper ijava..."
			err = installer.CreateWrapper(m.paths)
		case 1: // Shells
			log = "Configuration des shells..."
			for _, shellName := range m.cfg.SelectedShells {
				for _, shell := range m.shells {
					if shell.Name == shellName && shell.Detected {
						e := installer.ConfigureShell(shell, m.cfg.InstallDir, m.cfg.CreateAliases)
						if e != nil {
							if err == nil {
								err = e
							} else {
								err = fmt.Errorf("%v; %v", err, e)
							}
						}
					}
				}
			}
		case 2: // Finalisation
			log = "Installation terminée."
		}

		return stepResultMsg{step: step, log: log, err: err}
	}
}

func (m InstallModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.width = msg.Width
		m.height = msg.Height
		return m, nil

	case performStepMsg:
		m.currentStep = msg.step
		return m, m.performStep(msg.step)

	case stepResultMsg:
		if msg.err != nil {
			m.err = msg.err
			m.logs = append(m.logs, fmt.Sprintf("Erreur: %v", msg.err))
			// On ne s'arrête pas forcément sur une erreur de shell, mais c'est mieux de le signaler
			// Pour l'instant on continue
		} else {
			m.logs = append(m.logs, msg.log)
		}

		if m.currentStep < len(m.steps)-1 {
			nextStep := m.currentStep + 1
			return m, func() tea.Msg { return performStepMsg{step: nextStep} }
		} else {
			m.done = true
			return m, tea.Tick(time.Second*1, func(t time.Time) tea.Msg {
				return NextScreenMsg{}
			})
		}
	}

	return m, nil
}

func (m InstallModel) View() string {
	var content string

	content += components.RenderHeaderSimple("Installation en cours", m.width)
	content += "\n\n"

	if m.err != nil {
		content += styles.RenderError(fmt.Sprintf("Erreur: %v", m.err))
		content += "\n"
	}

	// Afficher les étapes
	content += components.RenderStepIndicator(m.steps, m.currentStep)
	content += "\n\n"

	// Afficher les logs récents
	if len(m.logs) > 0 {
		content += styles.LabelStyle.Render("Dernières actions:") + "\n"
		maxLogs := 5
		startIdx := len(m.logs) - maxLogs
		if startIdx < 0 {
			startIdx = 0
		}

		for _, log := range m.logs[startIdx:] {
			content += styles.MutedTextStyle.Render("  " + log)
			content += "\n"
		}
	}

	if m.done {
		content += "\n"
		content += styles.RenderSuccess("Installation terminée avec succès !")
	}

	content += "\n"
	content += components.RenderFooter("q/esc/ctrl+c quitter")

	return content
}
