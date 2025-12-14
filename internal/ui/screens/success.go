package screens

import (
	"strings"

	"github.com/charmbracelet/lipgloss"
	tea "github.com/charmbracelet/bubbletea"
	"github.com/yannouuuu/ijava-enhanced/internal/installer"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/components"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/styles"
)

type SuccessModel struct {
	width      int
	height     int
	paths      *installer.InstallPaths
	validation *installer.ValidationReport
}

func NewSuccessModel(paths *installer.InstallPaths, validation *installer.ValidationReport) SuccessModel {
	return SuccessModel{
		paths:      paths,
		validation: validation,
	}
}

func (m SuccessModel) Init() tea.Cmd {
	return nil
}

func (m SuccessModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.width = msg.Width
		m.height = msg.Height
		return m, nil

	case tea.KeyMsg:
		if msg.String() == "q" || msg.String() == "enter" {
			return m, tea.Quit
		}
	}

	return m, nil
}

func (m SuccessModel) View() string {
	var content strings.Builder

	// Bannière de succès avec vraie box Lipgloss
	successMsg := styles.SymbolRocket + "  Installation réussie avec succès !  " + styles.SymbolRocket
	
	successBox := lipgloss.NewStyle().
		Border(lipgloss.RoundedBorder()).
		BorderForeground(styles.ColorSuccess).
		Padding(1, 4).
		Foreground(styles.ColorSuccess).
		Bold(true).
		Align(lipgloss.Center)
	
	content.WriteString(lipgloss.Place(m.width, 0, lipgloss.Center, lipgloss.Top, successBox.Render(successMsg)))
	content.WriteString("\n\n")

	// Résumé de l'installation
	content.WriteString(styles.LabelStyle.Render("Résumé de l'installation:"))
	content.WriteString("\n\n")

	content.WriteString(styles.RenderKeyValue("Répertoire", m.paths.InstallDir))
	content.WriteString("\n")
	content.WriteString(styles.RenderKeyValue("Toolkit JAR", m.paths.JarPath))
	content.WriteString("\n")
	content.WriteString(styles.RenderKeyValue("Wrapper", m.paths.WrapperPath))
	content.WriteString("\n\n")

	// Validation
	if m.validation.AllSuccess {
		content.WriteString(styles.RenderSuccess("Toutes les vérifications ont réussi"))
	} else {
		content.WriteString(styles.RenderWarning("Certaines vérifications ont échoué"))
	}
	content.WriteString("\n\n")

	// Prochaines étapes
	content.WriteString(styles.LabelStyle.Render("Prochaines étapes:"))
	content.WriteString("\n\n")

	steps := []string{
		"Rechargez votre shell: source ~/.bashrc (ou source ~/.zshrc)",
		"Testez l'installation: ijava --info",
		"Commencez à utiliser iJava: ijava init",
	}

	for i, step := range steps {
		content.WriteString(styles.InfoTextStyle.Render(styles.SymbolArrow + " "))
		content.WriteString(styles.ValueStyle.Render(step))
		if i < len(steps)-1 {
			content.WriteString("\n")
		}
	}

	content.WriteString("\n\n")

	// Commandes disponibles
	content.WriteString(styles.LabelStyle.Render("Commandes disponibles:"))
	content.WriteString("\n\n")

	commands := []struct {
		cmd  string
		desc string
	}{
		{"ijava update", "Mettre à jour le toolkit"},
		{"ijava uninstall", "Désinstaller iJava"},
		{"ijava --info", "Afficher les informations"},
	}

	for _, cmd := range commands {
		content.WriteString("  ")
		content.WriteString(styles.CodeStyle.Render(cmd.cmd))
		content.WriteString(" ")
		content.WriteString(styles.MutedTextStyle.Render(cmd.desc))
		content.WriteString("\n")
	}

	content.WriteString("\n")
	content.WriteString(styles.SuccessTextStyle.Render("Merci d'avoir installé iJava Enhanced ! " + styles.SymbolSparkle))
	content.WriteString("\n\n")

	content.WriteString(components.RenderFooter("q quitter", "enter quitter"))

	return content.String()
}
