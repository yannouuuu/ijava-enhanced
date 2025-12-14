package components

import (
	"fmt"
	"strings"

	"github.com/charmbracelet/lipgloss"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/styles"
)

// RenderStepIndicator affiche un indicateur d'étapes
func RenderStepIndicator(steps []string, currentStep int) string {
	var parts []string

	for i, step := range steps {
		var stepText string

		if i < currentStep {
			// Étape complétée
			stepText = styles.SuccessTextStyle.Render(styles.SymbolCheck + " " + step)
		} else if i == currentStep {
			// Étape en cours
			stepText = styles.InfoTextStyle.Render(styles.SymbolGear + " " + step)
		} else {
			// Étape à venir
			stepText = styles.MutedTextStyle.Render(styles.SymbolBullet + " " + step)
		}

		parts = append(parts, stepText)
	}

	return lipgloss.JoinVertical(lipgloss.Left, parts...)
}

// RenderProgressBar affiche une barre de progression
func RenderProgressBar(percent float64, width int) string {
	if width < 10 {
		width = 40
	}

	filled := int(float64(width) * percent / 100)
	if filled > width {
		filled = width
	}

	bar := strings.Repeat("█", filled) + strings.Repeat("░", width-filled)

	barStyle := lipgloss.NewStyle().
		Foreground(styles.ColorPrimary)

	percentStyle := lipgloss.NewStyle().
		Foreground(styles.ColorInfo).
		Bold(true)

	return barStyle.Render(bar) + " " + percentStyle.Render(fmt.Sprintf("%.1f%%", percent))
}

// RenderSpinner affiche un spinner (frame statique pour le rendu)
func RenderSpinner(frame int, message string) string {
	frames := []string{"⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"}
	spinner := frames[frame%len(frames)]

	spinnerStyle := lipgloss.NewStyle().
		Foreground(styles.ColorPrimary).
		Bold(true)

	return spinnerStyle.Render(spinner) + " " + message
}
