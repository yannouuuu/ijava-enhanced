package styles

import (
	"github.com/charmbracelet/lipgloss"
)

// Palette de couleurs
var (
	ColorPrimary   = lipgloss.Color("#00D9FF") // Cyan vibrant
	ColorSecondary = lipgloss.Color("#FF6AC1") // Rose
	ColorSuccess   = lipgloss.Color("#00FF9F") // Vert
	ColorWarning   = lipgloss.Color("#FFD700") // Jaune/Or
	ColorError     = lipgloss.Color("#FF3366") // Rouge
	ColorInfo      = lipgloss.Color("#00D9FF") // Cyan
	ColorMuted     = lipgloss.Color("#6C7086") // Gris
	ColorText      = lipgloss.Color("#CDD6F4") // Blanc cassé
	ColorBorder    = lipgloss.Color("#45475A") // Gris foncé
)

// Styles globaux
var (
	// Titre principal
	TitleStyle = lipgloss.NewStyle().
			Bold(true).
			Foreground(ColorPrimary).
			MarginTop(1).
			MarginBottom(1)

	// Sous-titre
	SubtitleStyle = lipgloss.NewStyle().
			Foreground(ColorSecondary).
			Italic(true)

	// Box avec bordure
	BoxStyle = lipgloss.NewStyle().
			Border(lipgloss.RoundedBorder()).
			BorderForeground(ColorBorder).
			Padding(1, 2).
			MarginTop(1).
			MarginBottom(1)

	// Box de succès
	SuccessBoxStyle = lipgloss.NewStyle().
			Border(lipgloss.RoundedBorder()).
			BorderForeground(ColorSuccess).
			Padding(1, 2).
			MarginTop(1).
			MarginBottom(1)

	// Box d'erreur
	ErrorBoxStyle = lipgloss.NewStyle().
			Border(lipgloss.RoundedBorder()).
			BorderForeground(ColorError).
			Padding(1, 2).
			MarginTop(1).
			MarginBottom(1)

	// Box d'avertissement
	WarningBoxStyle = lipgloss.NewStyle().
			Border(lipgloss.RoundedBorder()).
			BorderForeground(ColorWarning).
			Padding(1, 2).
			MarginTop(1).
			MarginBottom(1)

	// Texte de succès
	SuccessTextStyle = lipgloss.NewStyle().
				Foreground(ColorSuccess).
				Bold(true)

	// Texte d'erreur
	ErrorTextStyle = lipgloss.NewStyle().
			Foreground(ColorError).
			Bold(true)

	// Texte d'avertissement
	WarningTextStyle = lipgloss.NewStyle().
				Foreground(ColorWarning)

	// Texte d'information
	InfoTextStyle = lipgloss.NewStyle().
			Foreground(ColorInfo)

	// Texte atténué
	MutedTextStyle = lipgloss.NewStyle().
			Foreground(ColorMuted)

	// Label (clé dans clé:valeur)
	LabelStyle = lipgloss.NewStyle().
			Foreground(ColorPrimary).
			Bold(true)

	// Valeur (valeur dans clé:valeur)
	ValueStyle = lipgloss.NewStyle().
			Foreground(ColorText)

	// Bouton/Option sélectionnée
	SelectedStyle = lipgloss.NewStyle().
			Foreground(ColorPrimary).
			Bold(true).
			Underline(true)

	// Bouton/Option non sélectionnée
	UnselectedStyle = lipgloss.NewStyle().
			Foreground(ColorMuted)

	// Code/Chemin de fichier
	CodeStyle = lipgloss.NewStyle().
			Foreground(ColorSecondary).
			Background(lipgloss.Color("#1E1E2E")).
			Padding(0, 1)

	// Badge
	BadgeStyle = lipgloss.NewStyle().
			Foreground(lipgloss.Color("#1E1E2E")).
			Background(ColorPrimary).
			Padding(0, 1).
			Bold(true)

	// Badge de succès
	SuccessBadgeStyle = lipgloss.NewStyle().
				Foreground(lipgloss.Color("#1E1E2E")).
				Background(ColorSuccess).
				Padding(0, 1).
				Bold(true)

	// Badge d'erreur
	ErrorBadgeStyle = lipgloss.NewStyle().
			Foreground(lipgloss.Color("#1E1E2E")).
			Background(ColorError).
			Padding(0, 1).
			Bold(true)

	// Liste à puces
	ListItemStyle = lipgloss.NewStyle().
			PaddingLeft(2).
			Foreground(ColorText)

	// Diviseur
	DividerStyle = lipgloss.NewStyle().
			Foreground(ColorBorder).
			MarginTop(1).
			MarginBottom(1)

	// Footer/Aide en bas de l'écran
	FooterStyle = lipgloss.NewStyle().
			Foreground(ColorMuted).
			Italic(true).
			MarginTop(2)

	// Style pour les commandes/raccourcis
	KeyStyle = lipgloss.NewStyle().
			Foreground(ColorPrimary).
			Bold(true).
			Background(lipgloss.Color("#1E1E2E")).
			Padding(0, 1)

	// Style pour la description des commandes
	DescStyle = lipgloss.NewStyle().
			Foreground(ColorMuted)
)

// Symboles Unicode
const (
	SymbolCheck    = "✓"
	SymbolCross    = "✗"
	SymbolArrow    = "→"
	SymbolBullet   = "•"
	SymbolInfo     = "ℹ"
	SymbolWarning  = "⚠"
	SymbolError    = "✗"
	SymbolDownload = "⬇"
	SymbolRocket   = "🚀"
	SymbolGear     = "⚙"
	SymbolSparkle  = "✨"
)

// Helper functions

// RenderKeyValue affiche une paire clé:valeur stylisée
func RenderKeyValue(key, value string) string {
	return LabelStyle.Render(key+":") + " " + ValueStyle.Render(value)
}

// RenderSuccess affiche un message de succès avec symbole
func RenderSuccess(message string) string {
	return SuccessTextStyle.Render(SymbolCheck+" "+message)
}

// RenderError affiche un message d'erreur avec symbole
func RenderError(message string) string {
	return ErrorTextStyle.Render(SymbolError+" "+message)
}

// RenderWarning affiche un message d'avertissement avec symbole
func RenderWarning(message string) string {
	return WarningTextStyle.Render(SymbolWarning+" "+message)
}

// RenderInfo affiche un message d'information avec symbole
func RenderInfo(message string) string {
	return InfoTextStyle.Render(SymbolInfo+" "+message)
}

// RenderListItem affiche un élément de liste
func RenderListItem(text string) string {
	return ListItemStyle.Render(SymbolBullet + " " + text)
}

// RenderDivider affiche un diviseur
func RenderDivider(width int) string {
	line := ""
	for i := 0; i < width; i++ {
		line += "─"
	}
	return DividerStyle.Render(line)
}

// RenderCommand affiche une commande avec sa description
func RenderCommand(key, desc string) string {
	return KeyStyle.Render(key) + " " + DescStyle.Render(desc)
}

// GetTerminalWidth retourne la largeur du terminal
func GetTerminalWidth() int {
	// Par défaut 80 colonnes, sera mis à jour dynamiquement dans l'UI
	return 80
}

// CenterText centre un texte dans une largeur donnée
func CenterText(text string, width int) string {
	return lipgloss.NewStyle().
		Width(width).
		Align(lipgloss.Center).
		Render(text)
}
