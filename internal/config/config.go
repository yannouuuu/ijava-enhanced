package config

// Version de l'installeur (peut être surchargé à la compilation via -ldflags)
var Version = "2025.12.3"

const (
	// URL du JAR iJava officiel
	JarURL = "https://www.iut-info.univ-lille.fr/~yann.secq/ijava/ijava.jar"

	// Répertoire d'installation par défaut
	DefaultInstallDir = ".ijava2"

	// Nom du binaire wrapper
	WrapperName = "ijava"

	// Marqueurs pour les fichiers de profil shell
	MarkerStart = "# >>> ijava enhanced >>>"
	MarkerEnd   = "# <<< ijava enhanced <<<"

	// Espace disque minimum requis (en MB)
	MinDiskSpaceMB = 50
)

// InstallConfig contient la configuration de l'installation
type InstallConfig struct {
	InstallDir      string
	InstallType     string   // "express" ou "custom"
	SelectedShells  []string
	CreateAliases   bool
	AutoInstallJava bool
}

// NewDefaultConfig retourne une configuration par défaut
func NewDefaultConfig() *InstallConfig {
	return &InstallConfig{
		InstallDir:      DefaultInstallDir,
		InstallType:     "express",
		SelectedShells:  []string{},
		CreateAliases:   true,
		AutoInstallJava: false,
	}
}
