package installer

import (
	"fmt"
	"os/exec"
	"runtime"
	"strings"
)

// JavaInfo contient les informations sur l'installation Java
type JavaInfo struct {
	Installed bool
	Version   string
	Path      string
}

// DetectJava vérifie si Java est installé et retourne ses informations
func DetectJava() (*JavaInfo, error) {
	info := &JavaInfo{
		Installed: false,
	}

	// Vérifier si la commande java existe
	javaPath, err := exec.LookPath("java")
	if err != nil {
		return info, nil // Java non installé
	}

	info.Path = javaPath
	info.Installed = true

	// Récupérer la version
	cmd := exec.Command("java", "-version")
	output, err := cmd.CombinedOutput()
	if err != nil {
		return info, fmt.Errorf("erreur lors de la récupération de la version Java: %w", err)
	}

	// Parser la version depuis la sortie
	lines := strings.Split(string(output), "\n")
	if len(lines) > 0 {
		info.Version = strings.TrimSpace(lines[0])
	}

	return info, nil
}

// InstallJava tente d'installer Java automatiquement selon l'OS
func InstallJava() error {
	switch runtime.GOOS {
	case "linux":
		return installJavaLinux()
	case "darwin":
		return installJavaMacOS()
	case "windows":
		return installJavaWindows()
	default:
		return fmt.Errorf("système d'exploitation non supporté: %s", runtime.GOOS)
	}
}

// installJavaLinux installe Java sur Linux
func installJavaLinux() error {
	// Détecter le gestionnaire de paquets
	managers := []struct {
		cmd     string
		install []string
	}{
		{"apt-get", []string{"sudo", "apt-get", "update", "&&", "sudo", "apt-get", "install", "-y", "openjdk-21-jdk"}},
		{"dnf", []string{"sudo", "dnf", "install", "-y", "java-latest-openjdk-devel"}},
		{"yum", []string{"sudo", "yum", "install", "-y", "java-11-openjdk-devel"}},
		{"pacman", []string{"sudo", "pacman", "-Syu", "--noconfirm", "jdk-openjdk"}},
	}

	for _, mgr := range managers {
		if _, err := exec.LookPath(mgr.cmd); err == nil {
			// Gestionnaire trouvé, installer Java
			cmdStr := strings.Join(mgr.install, " ")
			cmd := exec.Command("sh", "-c", cmdStr)
			if err := cmd.Run(); err != nil {
				return fmt.Errorf("échec de l'installation avec %s: %w", mgr.cmd, err)
			}
			return nil
		}
	}

	return fmt.Errorf("aucun gestionnaire de paquets supporté trouvé")
}

// installJavaMacOS installe Java sur macOS
func installJavaMacOS() error {
	// Vérifier si Homebrew est installé
	if _, err := exec.LookPath("brew"); err != nil {
		return fmt.Errorf("Homebrew n'est pas installé. Installez-le depuis https://brew.sh")
	}

	cmd := exec.Command("brew", "install", "openjdk")
	if err := cmd.Run(); err != nil {
		return fmt.Errorf("échec de l'installation de Java via Homebrew: %w", err)
	}

	return nil
}

// installJavaWindows installe Java sur Windows
func installJavaWindows() error {
	// Sur Windows, on suggère un téléchargement manuel ou winget
	if _, err := exec.LookPath("winget"); err == nil {
		cmd := exec.Command("winget", "install", "Microsoft.OpenJDK.21")
		if err := cmd.Run(); err != nil {
			return fmt.Errorf("échec de l'installation de Java via winget: %w", err)
		}
		return nil
	}

	return fmt.Errorf("winget non disponible. Téléchargez Java manuellement depuis https://adoptium.net")
}

// HasCommand vérifie si une commande est disponible dans le PATH
func HasCommand(cmd string) bool {
	_, err := exec.LookPath(cmd)
	return err == nil
}
