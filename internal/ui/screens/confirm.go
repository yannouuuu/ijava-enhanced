package screens

import (
	"fmt"

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
}

func NewConfirmModel(cfg *config.InstallConfig, paths *installer.InstallPaths) ConfirmModel {
	return ConfirmModel{
		config: cfg,
		paths:  paths,
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

	// Récapitulatif dans une box
	summary := ""
	summary += styles.LabelStyle.Render("Récapitulatif:") + "\n\n"
	summary += styles.RenderKeyValue("Type", m.config.InstallType) + "\n"
	summary += styles.RenderKeyValue("Répertoire", m.paths.InstallDir) + "\n"
	summary += styles.RenderKeyValue("Shells", fmt.Sprintf("%d configuré(s)", len(m.config.SelectedShells))) + "\n"
	
	if m.config.CreateAliases {
		summary += styles.RenderKeyValue("Alias", "Oui (ijavai, ijavac, etc.)") + "\n"
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
	question := styles.WarningTextStyle.Render("⚠ Voulez-vous vraiment démarrer l'installation ?")
	content += lipgloss.Place(m.width, 0, lipgloss.Center, lipgloss.Top, question)
	content += "\n\n"

	content += components.RenderFooter("enter confirmer et démarrer", "q/esc annuler")

	return content
}
