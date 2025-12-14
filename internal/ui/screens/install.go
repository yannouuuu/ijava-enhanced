package screens

import (
	"fmt"
	"time"

	tea "github.com/charmbracelet/bubbletea"
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
}

func NewInstallModel() InstallModel {
	return InstallModel{
		steps: []string{
			"Création des répertoires",
			"Installation du JAR",
			"Création du wrapper",
			"Configuration du PATH",
			"Configuration des alias",
		},
		logs: []string{},
	}
}

func (m InstallModel) Init() tea.Cmd {
	return tea.Tick(time.Millisecond*500, func(t time.Time) tea.Msg {
		return installStepMsg{step: 0, log: "Préparation des répertoires..."}
	})
}

type installStepMsg struct {
	step int
	log  string
}

type installCompleteMsg struct{}

func (m InstallModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.width = msg.Width
		m.height = msg.Height
		return m, nil

	case installStepMsg:
		m.currentStep = msg.step
		if msg.log != "" {
			m.logs = append(m.logs, msg.log)
		}

		// Passer à l'étape suivante automatiquement
		if m.currentStep < len(m.steps) {
			nextStep := m.currentStep + 1
			var nextLog string

			switch nextStep {
			case 1:
				nextLog = "Téléchargement du toolkit iJava..."
			case 2:
				nextLog = "Création du script wrapper ijava..."
			case 3:
				nextLog = "Ajout au PATH..."
			case 4:
				nextLog = "Configuration des alias..."
			case 5:
				nextLog = "Finalisation..."
			}

			return m, tea.Tick(time.Millisecond*800, func(t time.Time) tea.Msg {
				return installStepMsg{step: nextStep, log: nextLog}
			})
		} else {
			// Installation terminée
			m.done = true
			return m, tea.Tick(time.Second*2, func(t time.Time) tea.Msg {
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
	} else {
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
	}

	content += "\n"
	content += components.RenderFooter("q/esc/ctrl+c quitter")

	return content
}
