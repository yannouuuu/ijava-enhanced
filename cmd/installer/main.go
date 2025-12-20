package main

import (
	"flag"
	"fmt"
	"os"

	tea "github.com/charmbracelet/bubbletea"
	"github.com/yannouuuu/ijava-enhanced/internal/installer"
	"github.com/yannouuuu/ijava-enhanced/internal/ui"
)

func main() {
	uninstallPtr := flag.Bool("uninstall", false, "Désinstaller iJava Enhanced")
	flag.Parse()

	if *uninstallPtr {
		fmt.Println("Désinstallation de iJava Enhanced...")
		if err := installer.Uninstall(nil); err != nil {
			fmt.Fprintf(os.Stderr, "Erreur lors de la désinstallation : %v\n", err)
			os.Exit(1)
		}
		fmt.Println("Désinstallation terminée avec succès.")
		return
	}

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
