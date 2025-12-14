package components

import (
	"strings"

	"github.com/charmbracelet/lipgloss"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/styles"
)

// RenderFooter affiche le footer avec les raccourcis clavier
func RenderFooter(commands ...string) string {
	if len(commands) == 0 {
		commands = []string{
			"ctrl+c quitter",
		}
	}

	var parts []string
	for _, cmd := range commands {
		parts = append(parts, styles.RenderCommand(cmd[:strings.Index(cmd, " ")], cmd[strings.Index(cmd, " ")+1:]))
	}

	footer := lipgloss.JoinHorizontal(lipgloss.Left, parts...)

	return styles.FooterStyle.Render(footer)
}

// RenderHelp affiche un texte d'aide
func RenderHelp(text string) string {
	return styles.FooterStyle.Render(text)
}
