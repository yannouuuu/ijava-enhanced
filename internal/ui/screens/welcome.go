package screens

import (
	"fmt"
	"math"
	"strings"
	"time"

	tea "github.com/charmbracelet/bubbletea"
	"github.com/charmbracelet/harmonica"
	"github.com/charmbracelet/lipgloss"
	"github.com/yannouuuu/ijava-enhanced/internal/config"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/styles"
)

type WelcomeModel struct {
	width      int
	height     int
	ready      bool
	progress   float64
	spring     harmonica.Spring
	startTime  time.Time
	countdown  int
	frame      int
}

func NewWelcomeModel() WelcomeModel {
	return WelcomeModel{
		spring:    harmonica.NewSpring(harmonica.FPS(60), 4.0, 0.8),
		startTime: time.Now(),
		countdown: 5,
	}
}

type animTickMsg time.Time

func animate() tea.Cmd {
	return tea.Tick(time.Second/30, func(t time.Time) tea.Msg {
		return animTickMsg(t)
	})
}

func (m WelcomeModel) Init() tea.Cmd {
	return tea.Batch(
		animate(),
		tea.Tick(time.Second*5, func(t time.Time) tea.Msg {
			return NextScreenMsg{}
		}),
	)
}

func (m WelcomeModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.width = msg.Width
		m.height = msg.Height
		m.ready = true
		return m, nil

	case animTickMsg:
		elapsed := time.Since(m.startTime).Seconds()
		m.frame++
		
		m.countdown = 5 - int(elapsed)
		if m.countdown < 0 {
			m.countdown = 0
		}

		// Animation pendant 3 secondes pour que tout se développe bien
		if elapsed < 3.0 {
			targetProgress := elapsed / 3.0
			m.progress, _ = m.spring.Update(targetProgress, m.progress, 1.0/30.0)
		} else {
			m.progress = 1.0
		}
		
		return m, animate()

	case tea.KeyMsg:
		if msg.String() == "enter" {
			return m, func() tea.Msg { return NextScreenMsg{} }
		}
	}

	return m, nil
}

func (m WelcomeModel) View() string {
	if !m.ready {
		return "Chargement..."
	}

	var content strings.Builder

	// ═══════════════════════════════════════════════════════════
	// HEADER AVEC BORDURE MULTICOLORE PULSANTE
	// ═══════════════════════════════════════════════════════════
	pulse := math.Sin(float64(m.frame)*0.1) * 0.5 + 0.5
	
	// Transition cyan → magenta → cyan
	r := int(100 + pulse*155)
	g := int(217 - pulse*100)
	b := int(255)
	borderColor := lipgloss.Color(fmt.Sprintf("#%02X%02X%02X", r, g, b))
	
	titleText := "iJava Enhanced Installer"
	versionText := "v" + config.Version
	
	innerContent := lipgloss.JoinVertical(
		lipgloss.Center,
		lipgloss.NewStyle().Foreground(styles.ColorPrimary).Bold(true).Render(titleText),
		lipgloss.NewStyle().Foreground(styles.ColorSecondary).Render(versionText),
	)
	
	box := lipgloss.NewStyle().
		Border(lipgloss.DoubleBorder()).
		BorderForeground(borderColor).
		Padding(1, 3).
		Align(lipgloss.Center)
	
	content.WriteString(lipgloss.Place(m.width, 0, lipgloss.Center, lipgloss.Top, box.Render(innerContent)))
	content.WriteString("\n\n")

	// ═══════════════════════════════════════════════════════════
	// BARRE MULTICOLORE EN DÉGRADÉ QUI GRANDIT
	// ═══════════════════════════════════════════════════════════
	if m.progress > 0.2 {
		barWidth := int((m.progress - 0.2) * 80)
		if barWidth > 80 {
			barWidth = 80
		}
		
		// Créer une barre avec dégradé de couleurs
		var gradientBar strings.Builder
		colors := []lipgloss.Color{
			lipgloss.Color("#00D9FF"), // Cyan
			lipgloss.Color("#00FFCC"), // Cyan-vert
			lipgloss.Color("#00FF9F"), // Vert
			lipgloss.Color("#CCFF00"), // Jaune-vert
			lipgloss.Color("#FFD700"), // Or
			lipgloss.Color("#FF6AC1"), // Rose
			lipgloss.Color("#FF00FF"), // Magenta
		}
		
		for i := 0; i < barWidth; i++ {
			colorIdx := (i * len(colors)) / 80
			if colorIdx >= len(colors) {
				colorIdx = len(colors) - 1
			}
			
			charStyle := lipgloss.NewStyle().Foreground(colors[colorIdx])
			gradientBar.WriteString(charStyle.Render("█"))
		}
		
		content.WriteString(lipgloss.Place(m.width, 0, lipgloss.Center, lipgloss.Top, gradientBar.String()))
		content.WriteString("\n\n")
	}

	// ═══════════════════════════════════════════════════════════
	// TITRE AVEC SLIDE-IN
	// ═══════════════════════════════════════════════════════════
	if m.progress > 0.4 {
		title := "Installation moderne et interactive"
		titleStyle := lipgloss.NewStyle().
			Foreground(styles.ColorPrimary).
			Bold(true)
		
		content.WriteString(lipgloss.Place(m.width, 0, lipgloss.Center, lipgloss.Top, titleStyle.Render(title)))
		content.WriteString("\n\n")
	}

	// ═══════════════════════════════════════════════════════════
	// CARACTÉRISTIQUES AVEC BARRES PROGRESSIVES
	// ═══════════════════════════════════════════════════════════
	if m.progress > 0.6 {
		features := []struct {
			text  string
			color lipgloss.Color
		}{
			{"Interface TUI élégante", styles.ColorInfo},
			{"Configuration intelligente", styles.ColorSecondary},
			{"Installation rapide", styles.ColorSuccess},
		}
		
		for i, feat := range features {
			featureProgress := (m.progress - 0.6 - float64(i)*0.1) * 5
			if featureProgress < 0 {
				featureProgress = 0
			}
			if featureProgress > 1 {
				featureProgress = 1
			}
			
			if featureProgress > 0 {
				barLen := int(featureProgress * 40)
				bar := strings.Repeat("█", barLen) + strings.Repeat("░", 40-barLen)
				barStyle := lipgloss.NewStyle().Foreground(feat.color)
				
				line := lipgloss.JoinHorizontal(
					lipgloss.Left,
					lipgloss.NewStyle().Width(32).Foreground(feat.color).Render(feat.text),
					barStyle.Render(bar),
				)
				
				content.WriteString(lipgloss.Place(m.width, 0, lipgloss.Center, lipgloss.Top, line))
				content.WriteString("\n")
			}
		}
		content.WriteString("\n")
	}

	// ═══════════════════════════════════════════════════════════
	// COUNTDOWN STYLÉ
	// ═══════════════════════════════════════════════════════════
	if m.progress >= 0.9 {
		pulse := math.Sin(float64(m.frame)*0.2) * 0.5 + 0.5
		
		var circles string
		if pulse > 0.66 {
			circles = "◉ ◉ ◉"
		} else if pulse > 0.33 {
			circles = "◎ ◎ ◎"
		} else {
			circles = "○ ○ ○"
		}
		
		var countdownMsg string
		if m.countdown > 0 {
			countdownMsg = fmt.Sprintf("%s  Démarrage dans %d  %s", 
				circles, m.countdown, circles)
		} else {
			countdownMsg = "◉ ◉ ◉  Démarrage...  ◉ ◉ ◉"
		}
		
		countdownStyle := lipgloss.NewStyle().
			Foreground(styles.ColorWarning).
			Bold(true).
			Border(lipgloss.RoundedBorder()).
			BorderForeground(styles.ColorWarning).
			Padding(0, 2)
		
		content.WriteString(lipgloss.Place(m.width, 0, lipgloss.Center, lipgloss.Top, countdownStyle.Render(countdownMsg)))
		content.WriteString("\n\n")
	}

	// ═══════════════════════════════════════════════════════════
	// FOOTER
	// ═══════════════════════════════════════════════════════════
	footer := lipgloss.NewStyle().
		Foreground(styles.ColorMuted).
		Render("enter → continuer maintenant  •  q/esc/ctrl+c → quitter")
	
	content.WriteString("\n")
	content.WriteString(lipgloss.Place(m.width, 0, lipgloss.Center, lipgloss.Top, footer))

	return content.String()
}

type NextScreenMsg struct{}
