package main

import (
	"fmt"
	"os"

	tea "github.com/charmbracelet/bubbletea"
	"github.com/yannouuuu/ijava-enhanced/internal/ui"
)

func main() {
	// Créer le modèle principal
	model := ui.NewAppModel()

	// Créer le programme Bubble Tea
	p := tea.NewProgram(
		model,
		tea.WithAltScreen(),       // Utiliser un écran alternatif
		tea.WithMouseCellMotion(), // Activer le support de la souris
	)

	// Exécuter le programme
	if _, err := p.Run(); err != nil {
		fmt.Fprintf(os.Stderr, "Erreur: %v\n", err)
		os.Exit(1)
	}
}
