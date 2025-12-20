package installer

import (
	"fmt"
	"os"

	"github.com/yannouuuu/ijava-enhanced/internal/config"
)

func Uninstall(logFunc func(string)) error {
	if logFunc == nil {
		logFunc = func(s string) { fmt.Println(s) }
	}

	shells := DetectShells()
	for _, shell := range shells {
		if shell.Detected {
			logFunc(fmt.Sprintf("Suppression de la configuration de %s...", shell.DisplayName))
			if err := RemoveShellConfig(shell); err != nil {
				logFunc(fmt.Sprintf("Attention : Échec de la suppression de la configuration de %s : %v", shell.DisplayName, err))
			}
		}
	}

	paths := GetInstallPaths(config.DefaultInstallDir)
	if _, err := os.Stat(paths.InstallDir); !os.IsNotExist(err) {
		logFunc(fmt.Sprintf("Suppression du répertoire d'installation : %s", paths.InstallDir))
		if err := os.RemoveAll(paths.InstallDir); err != nil {
			return fmt.Errorf("échec de la suppression du répertoire d'installation : %w", err)
		}
	} else {
		logFunc(fmt.Sprintf("Répertoire d'installation non trouvé : %s", paths.InstallDir))
	}

	return nil
}
