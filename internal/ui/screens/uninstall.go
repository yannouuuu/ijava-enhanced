package screens

import (
	"fmt"
	"time"

	tea "github.com/charmbracelet/bubbletea"
	"github.com/yannouuuu/ijava-enhanced/internal/installer"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/components"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/styles"
)

type UninstallModel struct {
	width       int
	height      int
	currentStep int
	steps       []string
	logs        []string
	done        bool
	err         error
}

func NewUninstallModel() UninstallModel {
	return UninstallModel{
		steps: []string{
			"Désinstallation en cours",
			"Nettoyage terminé",
		},
		logs: []string{},
	}
}

type uninstallLogMsg string
type uninstallErrorMsg error
type uninstallCompleteMsg struct{}

func runUninstall() tea.Cmd {
	return func() tea.Msg {
		// Créer un canal pour les logs
		logChan := make(chan string, 10)
		errChan := make(chan error, 1)
		doneChan := make(chan struct{})

		// Lancer la désinstallation dans une goroutine
		go func() {
			err := installer.Uninstall(func(msg string) {
				logChan <- msg
			})
			if err != nil {
				errChan <- err
			}
			close(doneChan)
		}()

		// Écouter les logs et les renvoyer comme messages
		// Note: Ceci est une simplification, idéalement on utiliserait un Program.Send
		// Mais comme on est dans une Cmd, on ne peut retourner qu'un seul Msg.
		// Pour faire du streaming de logs, il faudrait une architecture plus complexe.
		// Ici on va faire simple : on exécute tout et on retourne le résultat.
		// Pour avoir les logs en temps réel, il faudrait que Uninstall prenne un channel ou callback qui envoie des tea.Msg via program.Send()
		// Mais on n'a pas accès au program ici.
		
		// Approche alternative : on exécute Uninstall et on capture tout, mais ça bloque l'UI.
		// Mieux : on utilise une commande qui retourne un message pour chaque log ? Non, une commande retourne UN message.
		
		// On va tricher un peu : on fait l'uninstall synchrone ici (dans la goroutine du Cmd)
		// et on retourne juste le résultat final. Les logs ne seront pas affichés en temps réel
		// sauf si on refactorise Uninstall pour être asynchrone.
		
		// Pour l'instant, faisons simple et robuste :
		err := installer.Uninstall(nil)
		if err != nil {
			return uninstallErrorMsg(err)
		}
		return uninstallCompleteMsg{}
	}
}

// Version améliorée avec logs en temps réel (nécessite que Uninstall soit rapide ou qu'on accepte de ne pas voir les logs défiler un par un)
// Pour faire propre avec Bubble Tea, on va juste lancer l'uninstall et attendre la fin.
func (m UninstallModel) Init() tea.Cmd {
	return tea.Batch(
		tea.Tick(time.Millisecond*500, func(t time.Time) tea.Msg {
			return uninstallStepMsg{step: 0, log: "Démarrage de la désinstallation..."}
		}),
		func() tea.Msg {
			err := installer.Uninstall(nil)
			if err != nil {
				return uninstallErrorMsg(err)
			}
			return uninstallCompleteMsg{}
		},
	)
}

type uninstallStepMsg struct {
	step int
	log  string
}

func (m UninstallModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.width = msg.Width
		m.height = msg.Height
		return m, nil

	case uninstallStepMsg:
		m.currentStep = msg.step
		if msg.log != "" {
			m.logs = append(m.logs, msg.log)
		}
		return m, nil

	case uninstallErrorMsg:
		m.err = msg
		m.logs = append(m.logs, fmt.Sprintf("Erreur: %v", msg))
		return m, nil

	case uninstallCompleteMsg:
		m.done = true
		m.currentStep = 1
		m.logs = append(m.logs, "Désinstallation terminée avec succès.")
		return m, tea.Tick(time.Second*2, func(t time.Time) tea.Msg {
			return NextScreenMsg{}
		})
	}

	return m, nil
}

func (m UninstallModel) View() string {
	var content string

	content += components.RenderHeaderSimple("Désinstallation en cours", m.width)
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
			content += styles.RenderSuccess("Désinstallation terminée avec succès !")
		}
	}

	content += "\n"
	content += components.RenderFooter("q/esc/ctrl+c quitter")

	return content
}
