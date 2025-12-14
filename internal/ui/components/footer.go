package components

import (
	"strings"

	"github.com/charmbracelet/lipgloss"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/styles"
)

// RenderFooter affiche le footer avec les raccourcis clavier
// Chaque commande doit être au format "touche description" (ex: "enter continuer")
func RenderFooter(commands ...string) string {
	if len(commands) == 0 {
		commands = []string{
			"ctrl+c quitter",
		}
	}

	var parts []string
	for _, cmd := range commands {
		// Trouver l'espace séparateur
		spaceIdx := strings.Index(cmd, " ")
		
		var key, desc string
		if spaceIdx == -1 {
			// Pas d'espace trouvé - toute la chaîne est la commande
			key = cmd
			desc = ""
		} else {
			// Séparer la commande et la description
			key = cmd[:spaceIdx]
			desc = cmd[spaceIdx+1:]
		}
		
		parts = append(parts, styles.RenderCommand(key, desc))
	}

	footer := lipgloss.JoinHorizontal(lipgloss.Left, parts...)

	return styles.FooterStyle.Render(footer)
}

// RenderHelp affiche un texte d'aide
func RenderHelp(text string) string {
	return styles.FooterStyle.Render(text)
}
