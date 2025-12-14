package screens

import (
	"fmt"
	"strings"

	tea "github.com/charmbracelet/bubbletea"
	"github.com/charmbracelet/lipgloss"
	"github.com/yannouuuu/ijava-enhanced/internal/config"
	"github.com/yannouuuu/ijava-enhanced/internal/installer"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/components"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/styles"
)

type ConfirmModel struct {
	width  int
	height int
	config *config.InstallConfig
	paths  *installer.InstallPaths
	shells []installer.Shell
}

func NewConfirmModel(cfg *config.InstallConfig, paths *installer.InstallPaths, shells []installer.Shell) ConfirmModel {
	return ConfirmModel{
		config: cfg,
		paths:  paths,
		shells: shells,
	}
}

func (m ConfirmModel) Init() tea.Cmd {
	return nil
}

func (m ConfirmModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.width = msg.Width
		m.height = msg.Height
		return m, nil

	case tea.KeyMsg:
		if msg.String() == "enter" || msg.String() == "y" {
			return m, func() tea.Msg { return NextScreenMsg{} }
		}
	}

	return m, nil
}

func (m ConfirmModel) View() string {
	var content string

	content += components.RenderHeaderSimple("Confirmer l'installation", m.width)
	content += "\n\n"

	// Récapitulatif détaillé
	summary := ""
	summary += styles.LabelStyle.Render("Récapitulatif:") + "\n\n"
	
	summary += styles.RenderKeyValue("Type", m.config.InstallType) + "\n"
	summary += styles.RenderKeyValue("Répertoire", m.paths.InstallDir) + "\n"
	
	// Afficher les shells sélectionnés
	selectedCount := len(m.config.SelectedShells)
	if selectedCount > 0 {
		shellNames := []string{}
		for _, shellName := range m.config.SelectedShells {
			for _, shell := range m.shells {
				if shell.Name == shellName {
					shellNames = append(shellNames, shell.DisplayName)
					break
				}
			}
		}
		summary += styles.RenderKeyValue("Shells", fmt.Sprintf("%d: %s", selectedCount, strings.Join(shellNames, ", "))) + "\n"
	} else {
		// Détecter le shell actuel qui sera utilisé par défaut
		currentShell := installer.GetCurrentShell()
		summary += styles.RenderKeyValue("Shells", fmt.Sprintf("Auto (%s détecté)", currentShell)) + "\n"
	}
	
	if m.config.CreateAliases {
		summary += styles.RenderKeyValue("Alias", "Oui (ijavai, ijavac, ijavat, ijavae, ijavas)") + "\n"
	} else {
		summary += styles.RenderKeyValue("Alias", "Non") + "\n"
	}

	summaryBox := lipgloss.NewStyle().
		Border(lipgloss.RoundedBorder()).
		BorderForeground(styles.ColorInfo).
		Padding(1, 2).
		Width(m.width - 10)

	content += summaryBox.Render(summary)
	content += "\n\n"

	// Question de confirmation
	question := styles.WarningTextStyle.Render("Voulez-vous démarrer l'installation ?")
	content += lipgloss.Place(m.width, 0, lipgloss.Center, lipgloss.Top, question)
	content += "\n\n"

	content += components.RenderFooter("enter confirmer et démarrer", "q/esc annuler")

	return content
}
