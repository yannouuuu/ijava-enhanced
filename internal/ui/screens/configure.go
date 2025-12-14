package screens

import (
	"github.com/charmbracelet/huh"
	tea "github.com/charmbracelet/bubbletea"
	"github.com/yannouuuu/ijava-enhanced/internal/config"
	"github.com/yannouuuu/ijava-enhanced/internal/installer"
)

type ConfigureModel struct {
	width  int
	height int
	form   *huh.Form
	config *config.InstallConfig
	shells []installer.Shell
	done   bool
}

func NewConfigureModel(shells []installer.Shell, javaInstalled bool) ConfigureModel {
	cfg := config.NewDefaultConfig()

	shellOptions := []huh.Option[string]{}
	for _, shell := range shells {
		if shell.Detected {
			shellOptions = append(shellOptions, huh.NewOption(shell.DisplayName+" ("+shell.ConfigPath+")", shell.Name))
		}
	}

	form := huh.NewForm(
		huh.NewGroup(
			huh.NewSelect[string]().
				Title("Type d'installation").
				Options(
					huh.NewOption("Installation rapide (recommandée)", "express"),
					huh.NewOption("Installation personnalisée", "custom"),
				).
				Value(&cfg.InstallType),
		),

		huh.NewGroup(
			huh.NewInput().
				Title("Répertoire d'installation").
				Description("Chemin relatif depuis votre dossier personnel").
				Value(&cfg.InstallDir).
				Placeholder(".ijava2"),
		).WithHideFunc(func() bool {
			return cfg.InstallType != "custom"
		}),

		huh.NewGroup(
			huh.NewMultiSelect[string]().
				Title("Shells à configurer").
				Description("Sélectionnez les shells dans lesquels ajouter iJava au PATH").
				Options(shellOptions...).
				Value(&cfg.SelectedShells),

			huh.NewConfirm().
				Title("Créer les alias pratiques?").
				Description("ijavai, ijavac, ijavat, ijavae, ijavas").
				Value(&cfg.CreateAliases),
		),

		huh.NewGroup(
			huh.NewConfirm().
				Title("Installer Java automatiquement?").
				Description("Java n'est pas détecté. Voulez-vous l'installer maintenant?").
				Value(&cfg.AutoInstallJava),
		).WithHideFunc(func() bool {
			return javaInstalled
		}),
	)

	return ConfigureModel{
		form:   form,
		config: cfg,
		shells: shells,
	}
}

func (m ConfigureModel) Init() tea.Cmd {
	return m.form.Init()
}

func (m ConfigureModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	var cmds []tea.Cmd

	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.width = msg.Width
		m.height = msg.Height
		return m, nil
	}

	form, cmd := m.form.Update(msg)
	if f, ok := form.(*huh.Form); ok {
		m.form = f
		cmds = append(cmds, cmd)
	}

	if m.form.State == huh.StateCompleted {
		m.done = true
		return m, func() tea.Msg { return NextScreenMsg{} }
	}

	return m, tea.Batch(cmds...)
}

func (m ConfigureModel) View() string {
	if m.form.State == huh.StateCompleted {
		return "Configuration enregistrée..."
	}
	return m.form.View()
}

func (m ConfigureModel) GetConfig() *config.InstallConfig {
	return m.config
}
