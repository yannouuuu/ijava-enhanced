package components

import (
	"fmt"
	"math"
	"time"

	"github.com/charmbracelet/lipgloss"
	"github.com/yannouuuu/ijava-enhanced/internal/config"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/styles"
)

var startTime = time.Now()

// getPulseColor retourne une couleur pulsante basée sur le temps
func getPulseColor() lipgloss.Color {
	elapsed := time.Since(startTime).Seconds()
	pulse := math.Sin(elapsed*2) * 0.5 + 0.5
	
	// Transition cyan → magenta
	r := int(100 + pulse*155)
	g := int(217 - pulse*100)
	b := int(255)
	
	return lipgloss.Color(fmt.Sprintf("#%02X%02X%02X", r, g, b))
}

// RenderHeader affiche le header principal avec animation
func RenderHeader(width int) string {
	titleText := "iJava Enhanced Installer"
	versionText := "v" + config.Version
	
	innerContent := lipgloss.JoinVertical(
		lipgloss.Center,
		lipgloss.NewStyle().Foreground(styles.ColorPrimary).Bold(true).Render(titleText),
		lipgloss.NewStyle().Foreground(styles.ColorSecondary).Render(versionText),
	)
	
	// Box avec bordure multicolore pulsante
	box := lipgloss.NewStyle().
		Border(lipgloss.DoubleBorder()).
		BorderForeground(getPulseColor()).
		Padding(1, 3).
		Align(lipgloss.Center)
	
	return lipgloss.Place(width, 0, lipgloss.Center, lipgloss.Top, box.Render(innerContent)) + "\n"
}

// RenderHeaderSimple affiche un header simplifié avec bordure pulsante
func RenderHeaderSimple(title string, width int) string {
	// Box avec bordure multicolore pulsante (même couleur que header principal)
	box := lipgloss.NewStyle().
		Border(lipgloss.RoundedBorder()).
		BorderForeground(getPulseColor()).
		Padding(0, 2).
		Foreground(styles.ColorPrimary).
		Bold(true).
		Align(lipgloss.Center).
		MarginBottom(1)
	
	boxWidth := width
	if boxWidth > 60 {
		boxWidth = 60
	}
	
	return lipgloss.Place(width, 0, lipgloss.Center, lipgloss.Top, box.Width(boxWidth-4).Render(title))
}

// RenderStep affiche l'étape actuelle
func RenderStep(currentStep, totalSteps int, stepName string, width int) string {
	stepText := lipgloss.NewStyle().
		Foreground(styles.ColorSecondary).
		Bold(true).
		Render(stepName)

	progressDots := ""
	for i := 0; i < totalSteps; i++ {
		if i < currentStep {
			progressDots += "█"
		} else {
			progressDots += "░"
		}
		if i < totalSteps-1 {
			progressDots += " "
		}
	}

	stepInfo := lipgloss.NewStyle().
		Foreground(getPulseColor()).
		Render(progressDots)

	return lipgloss.JoinVertical(
		lipgloss.Left,
		stepText,
		stepInfo,
	)
}
