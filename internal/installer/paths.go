package installer

import (
	"fmt"
	"os"
	"path/filepath"
	"runtime"
	"text/template"

	"github.com/yannouuuu/ijava-enhanced/internal/config"
)

// InstallPaths contient tous les chemins d'installation
type InstallPaths struct {
	InstallDir  string
	BinDir      string
	JarPath     string
	WrapperPath string
}

// GetInstallPaths retourne les chemins d'installation
func GetInstallPaths(baseDir string) *InstallPaths {
	home, _ := os.UserHomeDir()
	installDir := filepath.Join(home, baseDir)

	paths := &InstallPaths{
		InstallDir: installDir,
		BinDir:     filepath.Join(installDir, "bin"),
		JarPath:    filepath.Join(installDir, "ijava.jar"),
	}

	if runtime.GOOS == "windows" {
		paths.WrapperPath = filepath.Join(paths.BinDir, "ijava.bat")
	} else {
		paths.WrapperPath = filepath.Join(paths.BinDir, "ijava")
	}

	return paths
}

// CreateDirectories crée tous les répertoires nécessaires
func CreateDirectories(paths *InstallPaths) error {
	dirs := []string{
		paths.InstallDir,
		paths.BinDir,
	}

	for _, dir := range dirs {
		if err := os.MkdirAll(dir, 0755); err != nil {
			return fmt.Errorf("impossible de créer le répertoire %s: %w", dir, err)
		}
	}

	return nil
}

// CreateWrapper crée le script wrapper pour iJava
func CreateWrapper(paths *InstallPaths) error {
	if runtime.GOOS == "windows" {
		return createWindowsWrapper(paths)
	}
	return createUnixWrapper(paths)
}

// createUnixWrapper crée le wrapper Unix (bash)
func createUnixWrapper(paths *InstallPaths) error {
	const wrapperTemplate = `#!/usr/bin/env bash
# ==============================================================================
# iJava Enhanced Wrapper v{{.Version}}
# ==============================================================================
# Ce script encapsule le toolkit iJava et ajoute des fonctionnalités avancées
# ==============================================================================

set -euo pipefail

# Configuration
readonly INSTALL_DIR="{{.InstallDir}}"
readonly BIN_DIR="{{.BinDir}}"
readonly JAR_PATH="{{.JarPath}}"
readonly JAR_URL="{{.JarURL}}"
readonly VERSION="{{.Version}}"

# Couleurs
readonly C_RESET='\033[0m'
readonly C_BOLD='\033[1m'
readonly C_GREEN='\033[0;32m'
readonly C_YELLOW='\033[0;33m'
readonly C_CYAN='\033[0;36m'
readonly C_RED='\033[0;31m'

# Utilitaires
has_cmd() {
    command -v "$1" >/dev/null 2>&1
}

# Télécharge la dernière version
download_latest() {
    echo -e "${C_CYAN}⬇${C_RESET}  Téléchargement de la dernière version..."
    
    if has_cmd curl; then
        curl -fsSL --progress-bar "$JAR_URL" -o "$JAR_PATH"
    elif has_cmd wget; then
        wget -q --show-progress "$JAR_URL" -O "$JAR_PATH"
    else
        echo -e "${C_RED}✗${C_RESET} Impossible de mettre à jour: curl ou wget requis" >&2
        exit 1
    fi
    
    echo -e "${C_GREEN}✓${C_RESET} ${C_BOLD}Toolkit mis à jour avec succès !${C_RESET}"
}

# Vérifie et télécharge le JAR si nécessaire
ensure_jar() {
    if [[ ! -f "$JAR_PATH" ]]; then
        echo -e "${C_YELLOW}⚠${C_RESET}  Toolkit manquant, téléchargement en cours..."
        download_latest
    fi
}

# Commande: update / self-update
cmd_update() {
    download_latest
    exit 0
}

# Commande: --info
cmd_info() {
    echo ""
    echo -e "${C_CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${C_RESET}"
    echo -e "${C_CYAN}${C_BOLD}  iJava Enhanced Wrapper ${C_RESET}${C_CYAN}v${VERSION}${C_RESET}"
    echo -e "${C_CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${C_RESET}"
    echo ""
    echo -e "${C_BOLD}Installation${C_RESET}"
    echo -e "  ${C_CYAN}→${C_RESET} $INSTALL_DIR"
    echo ""
    echo -e "${C_BOLD}Fichier JAR${C_RESET}"
    echo -e "  ${C_CYAN}→${C_RESET} $JAR_PATH"
    echo ""
    echo -e "${C_BOLD}Commandes du wrapper${C_RESET}"
    echo -e "  ${C_GREEN}ijava update${C_RESET} / ${C_GREEN}self-update${C_RESET}  Met à jour le toolkit"
    echo -e "  ${C_GREEN}ijava uninstall${C_RESET}             Désinstalle iJava"
    echo -e "  ${C_GREEN}ijava --info${C_RESET}                Affiche ces informations"
    echo ""
    
    if [[ -f "$JAR_PATH" ]]; then
        echo -e "${C_CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${C_RESET}"
        echo -e "${C_BOLD}Informations du toolkit iJava${C_RESET}"
        echo -e "${C_CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${C_RESET}"
        java -jar "$JAR_PATH" --info 2>/dev/null || java -jar "$JAR_PATH" help 2>/dev/null || true
    else
        echo -e "${C_YELLOW}⚠${C_RESET}  Le fichier JAR du toolkit n'est pas installé."
    fi
    echo ""
    exit 0
}

# Commande: uninstall
cmd_uninstall() {
    echo ""
    echo -e "${C_CYAN}${C_BOLD}Désinstallation d'iJava Enhanced${C_RESET}"
    echo ""
    echo -e "${C_RED}⚠${C_RESET}  Cette action supprimera tous les fichiers iJava."
    read -p "Êtes-vous sûr ? [o/N] " -r response
    
    if [[ ! "$response" =~ ^[oOyY]$ ]]; then
        echo "Annulation."
        exit 0
    fi

    echo -e "${C_YELLOW}⚙${C_RESET}  Suppression des fichiers..."
    rm -rf "$INSTALL_DIR"
    
    echo -e "${C_GREEN}✓${C_RESET} ${C_BOLD}Désinstallation terminée !${C_RESET}"
    echo ""
    echo "Note: Les configurations shell doivent être retirées manuellement."
    echo ""
    exit 0
}

# Point d'entrée principal
main() {
    case "${1:-}" in
        update|self-update)
            cmd_update
            ;;
        --info)
            cmd_info
            ;;
        uninstall)
            cmd_uninstall
            ;;
        *)
            ensure_jar
            exec java -jar "$JAR_PATH" "$@"
            ;;
    esac
}

main "$@"
`

	tmpl, err := template.New("wrapper").Parse(wrapperTemplate)
	if err != nil {
		return fmt.Errorf("erreur lors du parsing du template: %w", err)
	}

	file, err := os.Create(paths.WrapperPath)
	if err != nil {
		return fmt.Errorf("impossible de créer le wrapper: %w", err)
	}
	defer file.Close()

	data := map[string]string{
		"Version":    config.Version,
		"InstallDir": paths.InstallDir,
		"BinDir":     paths.BinDir,
		"JarPath":    paths.JarPath,
		"JarURL":     config.JarURL,
	}

	if err := tmpl.Execute(file, data); err != nil {
		return fmt.Errorf("erreur lors de l'écriture du wrapper: %w", err)
	}

	// Rendre le wrapper exécutable
	if err := os.Chmod(paths.WrapperPath, 0755); err != nil {
		return fmt.Errorf("impossible de rendre le wrapper exécutable: %w", err)
	}

	return nil
}

// createWindowsWrapper crée le wrapper Windows (batch)
func createWindowsWrapper(paths *InstallPaths) error {
	const wrapperTemplate = `@echo off
REM ==============================================================================
REM iJava Enhanced Wrapper v{{.Version}} (Windows)
REM ==============================================================================

setlocal enabledelayedexpansion

set "INSTALL_DIR={{.InstallDir}}"
set "JAR_PATH={{.JarPath}}"
set "JAR_URL={{.JarURL}}"
set "VERSION={{.Version}}"

if "%1"=="update" goto :update
if "%1"=="self-update" goto :update
if "%1"=="--info" goto :info
if "%1"=="uninstall" goto :uninstall
if "%1"=="compile" goto :compile

if not exist "%JAR_PATH%" (
    echo Toolkit manquant, telechargement en cours...
    powershell -Command "Invoke-WebRequest -Uri '%JAR_URL%' -OutFile '%JAR_PATH%'"
)

java -jar "%JAR_PATH%" %*
goto :eof

:compile
javac -cp ".;%JAR_PATH%" %2 %3 %4 %5 %6 %7 %8 %9
goto :eof

:update
echo Telechargement de la derniere version...
powershell -Command "Invoke-WebRequest -Uri '%JAR_URL%' -OutFile '%JAR_PATH%'"
echo Toolkit mis a jour avec succes !
goto :eof

:info
echo.
echo ================================================
echo   iJava Enhanced Wrapper v%VERSION%
echo ================================================
echo.
echo Installation : %INSTALL_DIR%
echo Fichier JAR  : %JAR_PATH%
echo.
if exist "%JAR_PATH%" (
    java -jar "%JAR_PATH%" --info
)
goto :eof

:uninstall
echo.
echo Desinstallation d'iJava Enhanced
set /p "confirm=Etes-vous sur ? [o/N] "
if /i not "%confirm%"=="o" if /i not "%confirm%"=="y" (
    echo Annulation.
    goto :eof
)
echo Suppression des fichiers...
rd /s /q "%INSTALL_DIR%"
echo Desinstallation terminee !
goto :eof
`

	tmpl, err := template.New("wrapper").Parse(wrapperTemplate)
	if err != nil {
		return fmt.Errorf("erreur lors du parsing du template: %w", err)
	}

	file, err := os.Create(paths.WrapperPath)
	if err != nil {
		return fmt.Errorf("impossible de créer le wrapper: %w", err)
	}
	defer file.Close()

	data := map[string]string{
		"Version":    config.Version,
		"InstallDir": paths.InstallDir,
		"JarPath":    paths.JarPath,
		"JarURL":     config.JarURL,
	}

	if err := tmpl.Execute(file, data); err != nil {
		return fmt.Errorf("erreur lors de l'écriture du wrapper: %w", err)
	}

	return nil
}

// CheckDiskSpace vérifie l'espace disque disponible
func CheckDiskSpace(path string, requiredMB int64) (bool, int64, error) {
	// Note: Cette fonction nécessiterait des packages spécifiques pour chaque OS
	// Pour simplifier, on retourne toujours true
	// Dans une vraie implémentation, utiliser syscall.Statfs sur Unix
	// et GetDiskFreeSpaceEx sur Windows
	return true, 1000, nil
}
