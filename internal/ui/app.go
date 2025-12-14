package ui

import (
	tea "github.com/charmbracelet/bubbletea"
	"github.com/yannouuuu/ijava-enhanced/internal/config"
	"github.com/yannouuuu/ijava-enhanced/internal/installer"
	"github.com/yannouuuu/ijava-enhanced/internal/ui/screens"
)

// Screen représente les différents écrans de l'application
type Screen int

const (
	ScreenWelcome Screen = iota
	ScreenPrerequisites
	ScreenConfigure
	ScreenConfirm
	ScreenDownload
	ScreenInstall
	ScreenSuccess
)

// AppModel est le modèle principal de l'application
type AppModel struct {
	currentScreen Screen
	width         int
	height        int

	// Modèles des écrans
	welcomeModel        screens.WelcomeModel
	prerequisitesModel  screens.PrerequisitesModel
	configureModel      screens.ConfigureModel
	confirmModel        screens.ConfirmModel
	downloadModel       screens.DownloadModel
	installModel        screens.InstallModel
	successModel        screens.SuccessModel

	// Données partagées
	javaInfo *installer.JavaInfo
	shells   []installer.Shell
	cfg      *config.InstallConfig
	paths    *installer.InstallPaths
	validation *installer.ValidationReport
}

// NewAppModel crée un nouveau modèle d'application
func NewAppModel() AppModel {
	return AppModel{
		currentScreen: ScreenWelcome,
		welcomeModel:  screens.NewWelcomeModel(),
	}
}

func (m AppModel) Init() tea.Cmd {
	return m.welcomeModel.Init()
}

func (m AppModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	var cmd tea.Cmd

	switch msg := msg.(type) {
	case tea.KeyMsg:
		// Gestion globale des touches de quit
		switch msg.String() {
		case "ctrl+c":
			return m, tea.Quit
		case "q":
			// Permettre 'q' pour quitter sauf sur l'écran de succès (où q est déjà géré)
			if m.currentScreen != ScreenSuccess {
				return m, tea.Quit
			}
		case "esc":
			// ESC pour quitter sur tous les écrans sauf succès
			if m.currentScreen != ScreenSuccess {
				return m, tea.Quit
			}
		}

	case tea.WindowSizeMsg:
		m.width = msg.Width
		m.height = msg.Height

	case screens.NextScreenMsg:
		// Navigation vers l'écran suivant
		return m.nextScreen()
	}

	// Déléguer la mise à jour à l'écran actuel
	switch m.currentScreen {
	case ScreenWelcome:
		model, cmd := m.welcomeModel.Update(msg)
		m.welcomeModel = model.(screens.WelcomeModel)
		return m, cmd

	case ScreenPrerequisites:
		model, cmd := m.prerequisitesModel.Update(msg)
		m.prerequisitesModel = model.(screens.PrerequisitesModel)
		return m, cmd

	case ScreenConfigure:
		model, cmd := m.configureModel.Update(msg)
		m.configureModel = model.(screens.ConfigureModel)
		return m, cmd

	case ScreenConfirm:
		model, cmd := m.confirmModel.Update(msg)
		m.confirmModel = model.(screens.ConfirmModel)
		return m, cmd

	case ScreenDownload:
		model, cmd := m.downloadModel.Update(msg)
		m.downloadModel = model.(screens.DownloadModel)
		return m, cmd

	case ScreenInstall:
		model, cmd := m.installModel.Update(msg)
		m.installModel = model.(screens.InstallModel)
		return m, cmd

	case ScreenSuccess:
		model, cmd := m.successModel.Update(msg)
		m.successModel = model.(screens.SuccessModel)
		return m, cmd
	}

	return m, cmd
}

func (m AppModel) View() string {
	// Afficher la vue de l'écran actuel
	switch m.currentScreen {
	case ScreenWelcome:
		return m.welcomeModel.View()
	case ScreenPrerequisites:
		return m.prerequisitesModel.View()
	case ScreenConfigure:
		return m.configureModel.View()
	case ScreenConfirm:
		return m.confirmModel.View()
	case ScreenDownload:
		return m.downloadModel.View()
	case ScreenInstall:
		return m.installModel.View()
	case ScreenSuccess:
		return m.successModel.View()
	default:
		return "Écran inconnu"
	}
}

// nextScreen passe à l'écran suivant
func (m AppModel) nextScreen() (tea.Model, tea.Cmd) {
	switch m.currentScreen {
	case ScreenWelcome:
		// Passer aux prérequis
		m.currentScreen = ScreenPrerequisites
		m.prerequisitesModel = screens.NewPrerequisitesModel()
		return m, m.prerequisitesModel.Init()

	case ScreenPrerequisites:
		// Récupérer les infos Java et shells
		m.javaInfo = m.prerequisitesModel.GetJavaInfo()
		m.shells = m.prerequisitesModel.GetShells()

		// Passer à la configuration
		m.currentScreen = ScreenConfigure
		javaInstalled := m.javaInfo != nil && m.javaInfo.Installed
		m.configureModel = screens.NewConfigureModel(m.shells, javaInstalled)
		return m, m.configureModel.Init()

	case ScreenConfigure:
		// Récupérer la configuration
		m.cfg = m.configureModel.GetConfig()

		// SI AUCUN SHELL SÉLECTIONNÉ, utiliser le shell actuel par défaut
		if len(m.cfg.SelectedShells) == 0 {
			currentShell := installer.GetCurrentShell()
			for _, shell := range m.shells {
				if shell.Name == currentShell && shell.Detected {
					m.cfg.SelectedShells = []string{shell.Name}
					break
				}
			}
		}

		// Passer à l'écran de confirmation
		m.currentScreen = ScreenConfirm
		m.paths = installer.GetInstallPaths(m.cfg.InstallDir)
		m.confirmModel = screens.NewConfirmModel(m.cfg, m.paths, m.shells)
		return m, m.confirmModel.Init()

	case ScreenConfirm:
		// Installer Java si demandé
		if m.cfg.AutoInstallJava && (m.javaInfo == nil || !m.javaInfo.Installed) {
			_ = installer.InstallJava()
		}

		// Passer au téléchargement
		m.currentScreen = ScreenDownload
		m.downloadModel = screens.NewDownloadModel()
		return m, m.downloadModel.Init()

	case ScreenDownload:
		// TÉLÉCHARGEMENT du JAR
		m.paths = installer.GetInstallPaths(m.cfg.InstallDir)
		_ = installer.CreateDirectories(m.paths)
		_ = installer.DownloadFile(config.JarURL, m.paths.JarPath, nil)

		// Passer à l'installation
		m.currentScreen = ScreenInstall
		m.installModel = screens.NewInstallModel()
		return m, m.installModel.Init()

	case ScreenInstall:
		// INSTALLATION 
		_ = installer.CreateWrapper(m.paths)

		// Configurer les shells sélectionnés (ou shell actuel par défaut)
		for _, shellName := range m.cfg.SelectedShells {
			for _, shell := range m.shells {
				if shell.Name == shellName && shell.Detected {
					_ = installer.ConfigureShell(shell, m.cfg.InstallDir, m.cfg.CreateAliases)
					break
				}
			}
		}

		// Valider l'installation
		m.validation = installer.ValidateInstallation(m.paths)

		// Passer à l'écran de succès
		m.currentScreen = ScreenSuccess
		m.successModel = screens.NewSuccessModel(m.paths, m.validation)
		return m, m.successModel.Init()

	case ScreenSuccess:
		// Fin
		return m, tea.Quit
	}

	return m, nil
}
