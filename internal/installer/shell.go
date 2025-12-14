package installer

import (
	"bufio"
	"fmt"
	"os"
	"path/filepath"
	"runtime"
	"strings"

	"github.com/yannouuuu/ijava-enhanced/internal/config"
)

// Shell représente un shell détecté
type Shell struct {
	Name        string
	DisplayName string
	ConfigPath  string
	Detected    bool
	PathExport  string
	AliasFormat string
}

// DetectShells détecte les shells disponibles sur le système
func DetectShells() []Shell {
	if runtime.GOOS == "windows" {
		return detectWindowsShells()
	}
	return detectUnixShells()
}

// detectUnixShells détecte les shells Unix (Linux/macOS)
func detectUnixShells() []Shell {
	home := os.Getenv("HOME")
	shells := []Shell{}

	candidates := []struct {
		name        string
		displayName string
		configPath  string
		pathExport  string
		aliasFormat string
	}{
		{
			name:        "bash",
			displayName: "Bash",
			configPath:  filepath.Join(home, ".bashrc"),
			pathExport:  `export PATH="$HOME/.ijava2/bin:$PATH"`,
			aliasFormat: `alias %s="%s"`,
		},
		{
			name:        "zsh",
			displayName: "Zsh",
			configPath:  filepath.Join(home, ".zshrc"),
			pathExport:  `export PATH="$HOME/.ijava2/bin:$PATH"`,
			aliasFormat: `alias %s="%s"`,
		},
		{
			name:        "fish",
			displayName: "Fish",
			configPath:  filepath.Join(home, ".config/fish/config.fish"),
			pathExport:  `set -gx PATH $HOME/.ijava2/bin $PATH`,
			aliasFormat: `alias %s "%s"`,
		},
	}

	for _, candidate := range candidates {
		shell := Shell{
			Name:        candidate.name,
			DisplayName: candidate.displayName,
			ConfigPath:  candidate.configPath,
			PathExport:  candidate.pathExport,
			AliasFormat: candidate.aliasFormat,
			Detected:    fileExists(candidate.configPath),
		}
		shells = append(shells, shell)
	}

	return shells
}

// detectWindowsShells détecte les shells Windows
func detectWindowsShells() []Shell {
	profile := os.ExpandEnv("$PROFILE")

	return []Shell{
		{
			Name:        "powershell",
			DisplayName: "PowerShell",
			ConfigPath:  profile,
			PathExport:  `$env:PATH = "$env:USERPROFILE\.ijava2\bin;" + $env:PATH`,
			AliasFormat: `Set-Alias -Name %s -Value %s`,
			Detected:    true, // PowerShell toujours présent sur Windows
		},
	}
}

// ConfigureShell configure un shell spécifique
func ConfigureShell(shell Shell, installDir string, createAliases bool) error {
	// Créer le répertoire parent si nécessaire
	configDir := filepath.Dir(shell.ConfigPath)
	if err := os.MkdirAll(configDir, 0755); err != nil {
		return fmt.Errorf("impossible de créer le répertoire de configuration: %w", err)
	}

	// Lire le contenu actuel du fichier de profil
	content := ""
	if fileExists(shell.ConfigPath) {
		data, err := os.ReadFile(shell.ConfigPath)
		if err != nil {
			return fmt.Errorf("impossible de lire le fichier de profil: %w", err)
		}
		content = string(data)
	}

	// Retirer l'ancien bloc si existant
	content = removeBlock(content, config.MarkerStart, config.MarkerEnd)

	// Construire le nouveau bloc
	var block strings.Builder
	block.WriteString("\n")
	block.WriteString(config.MarkerStart + "\n")
	block.WriteString(shell.PathExport + "\n")

	if createAliases {
		block.WriteString("\n# Alias pratiques pour iJava\n")
		aliases := map[string]string{
			"ijavai": "ijava init",
			"ijavac": "ijava compile",
			"ijavat": "ijava test",
			"ijavae": "ijava execute",
			"ijavas": "ijava status",
		}

		for alias, cmd := range aliases {
			aliasLine := fmt.Sprintf(shell.AliasFormat, alias, cmd)
			block.WriteString(aliasLine + "\n")
		}
	}

	block.WriteString(config.MarkerEnd + "\n")

	// Ajouter le nouveau bloc
	content += block.String()

	// Écrire le fichier mis à jour
	if err := os.WriteFile(shell.ConfigPath, []byte(content), 0644); err != nil {
		return fmt.Errorf("impossible d'écrire le fichier de profil: %w", err)
	}

	return nil
}

// RemoveShellConfig retire la configuration iJava d'un shell
func RemoveShellConfig(shell Shell) error {
	if !fileExists(shell.ConfigPath) {
		return nil
	}

	data, err := os.ReadFile(shell.ConfigPath)
	if err != nil {
		return fmt.Errorf("impossible de lire le fichier de profil: %w", err)
	}

	content := string(data)
	content = removeBlock(content, config.MarkerStart, config.MarkerEnd)

	if err := os.WriteFile(shell.ConfigPath, []byte(content), 0644); err != nil {
		return fmt.Errorf("impossible d'écrire le fichier de profil: %w", err)
	}

	return nil
}

// removeBlock retire un bloc entre deux marqueurs
func removeBlock(content, startMarker, endMarker string) string {
	scanner := bufio.NewScanner(strings.NewReader(content))
	var result strings.Builder
	inBlock := false

	for scanner.Scan() {
		line := scanner.Text()

		if strings.TrimSpace(line) == startMarker {
			inBlock = true
			continue
		}

		if strings.TrimSpace(line) == endMarker {
			inBlock = false
			continue
		}

		if !inBlock {
			result.WriteString(line + "\n")
		}
	}

	return strings.TrimRight(result.String(), "\n")
}

// fileExists vérifie si un fichier existe
func fileExists(path string) bool {
	_, err := os.Stat(path)
	return err == nil
}

// GetCurrentShell détecte le shell actuellement utilisé
func GetCurrentShell() string {
	// Sur Unix, vérifier la variable SHELL
	if runtime.GOOS != "windows" {
		if shell := os.Getenv("SHELL"); shell != "" {
			return filepath.Base(shell)
		}
	}

	// Sur Windows, c'est PowerShell
	if runtime.GOOS == "windows" {
		return "powershell"
	}

	return "unknown"
}
