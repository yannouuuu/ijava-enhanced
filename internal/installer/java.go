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

func installJavaMacOS() error {
	// Vérifier si Homebrew est installé
	if _, err := exec.LookPath("brew"); err != nil {
		// Homebrew n'est pas installé, on l'installe
		fmt.Println("Homebrew non détecté. Installation automatique...")
		installCmd := exec.Command("/bin/bash", "-c", "NONINTERACTIVE=1 /bin/bash -c \"$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)\"")
		if err := installCmd.Run(); err != nil {
			return fmt.Errorf("échec de l'installation de Homebrew: %w", err)
		}

		// Ajouter Homebrew au PATH pour la session courante (tentative)
		// Note: Cela dépend de l'architecture (Intel vs Apple Silicon)
		// On essaie les chemins communs
		paths := []string{"/opt/homebrew/bin/brew", "/usr/local/bin/brew"}
		for _, p := range paths {
			if _, err := exec.LookPath(p); err == nil {
				break
			}
		}
	}

	brewCmd := "brew"
	if _, err := exec.LookPath("brew"); err != nil {
		if _, err := exec.LookPath("/opt/homebrew/bin/brew"); err == nil {
			brewCmd = "/opt/homebrew/bin/brew"
		} else if _, err := exec.LookPath("/usr/local/bin/brew"); err == nil {
			brewCmd = "/usr/local/bin/brew"
		}
	}

	cmd := exec.Command(brewCmd, "install", "openjdk")
	if err := cmd.Run(); err != nil {
		return fmt.Errorf("échec de l'installation de Java via Homebrew: %w", err)
	}

	return nil
}

// installJavaWindows installe Java sur Windows
func installJavaWindows() error {
	// Sur Windows, on utilise winget comme demandé
	if _, err := exec.LookPath("winget"); err == nil {
		// Installation de Microsoft.OpenJDK.25 (ou version demandée)
		cmd := exec.Command("winget", "install", "Microsoft.OpenJDK.25")
		if err := cmd.Run(); err != nil {
			// Fallback sur une version LTS si la 25 échoue (car elle n'existe peut-être pas encore)
			fmt.Println("Échec de l'installation de OpenJDK 25, tentative avec OpenJDK 21 (LTS)...")
			cmd = exec.Command("winget", "install", "Microsoft.OpenJDK.21")
			if err := cmd.Run(); err != nil {
				return fmt.Errorf("échec de l'installation de Java via winget: %w", err)
			}
		}
		return nil
	}

	return fmt.Errorf("winget non disponible. Veuillez installer Java manuellement ou installer winget")
}

// HasCommand vérifie si une commande est disponible dans le PATH
func HasCommand(cmd string) bool {
	_, err := exec.LookPath(cmd)
	return err == nil
}
