<br/>
<p align="center">
    <picture>
        <source media="(prefers-color-scheme: dark)" srcset="https://github.com/yannouuuu/IUT-SAE1.01/raw/main/.github/assets/header_univlille_light.png" width="200px">
        <img alt="UnivLilleLogo" src="https://github.com/yannouuuu/IUT-SAE1.01/raw/main/.github/assets/header_univlille_dark.png" width="200px">
    </picture>
  <h1 align="center">iJava Enhanced</h1>
</p>

**iJava Enhanced** est un installeur interactif pour [iJava](https://www.iut-info.univ-lille.fr/~yann.secq/ijava/), le toolkit Java pédagogique développé par Yann Secq à l'Université de Lille.

Installez facilement **iJava** sur votre système avec une interface TUI élégante et des animations fluides !

---

## Fonctionnalités

- **Interface TUI interactive** - Guidage pas à pas avec Bubble Tea et Lipgloss
- **Détection automatique de Java** - Proposition d'installation si absent
- **Configuration des shells** - Support de bash, zsh, fish et PowerShell
- **Installation personnalisée** - Choix du répertoire, des shells et des alias
- **Validation complète** - Vérification de l'installation
- **Support multi-plateforme** - Linux, macOS, Windows (Intel & ARM)
- **Wrapper intelligent** - Commandes `ijava update`, `ijava uninstall`, `ijava --info`

---

## Installation

### Installation rapide (recommandée)

**Linux / macOS:**
```bash
curl -fsSL https://raw.githubusercontent.com/yannouuuu/ijava-enhanced/main/scripts/install.sh | bash
```

**Windows (PowerShell):**
```powershell
iex (iwr -useb https://raw.githubusercontent.com/yannouuuu/ijava-enhanced/main/scripts/install.ps1).Content
```

### Installation manuelle

Téléchargez le binaire correspondant à votre plateforme depuis [Releases](https://github.com/yannouuuu/ijava-enhanced/releases/latest) et exécutez-le directement.

**Binaires disponibles :**

| Plateforme | Architecture | Binaire | Taille |
|------------|--------------|---------|--------|
| Linux | x86_64 | `ijava-installer-linux-amd64` | ~9.2 MB |
| Linux | ARM64 | `ijava-installer-linux-arm64` | ~8.7 MB |
| macOS | Intel | `ijava-installer-darwin-amd64` | ~9.3 MB |
| macOS | Apple Silicon | `ijava-installer-darwin-arm64` | ~8.8 MB |
| Windows | x86_64 | `ijava-installer-windows-amd64.exe` | ~9.5 MB |
| Windows | ARM64 (Surface) | `ijava-installer-windows-arm64.exe` | ~8.8 MB |

**Exemple (Linux) :**
```bash
# Télécharger
curl -fsSL https://github.com/yannouuuu/ijava-enhanced/releases/latest/download/ijava-installer-linux-amd64 -o ijava-installer

# Rendre exécutable et lancer
chmod +x ijava-installer
./ijava-installer
```

**Exemple (macOS Apple Silicon) :**
```bash
# Télécharger
curl -fsSL https://github.com/yannouuuu/ijava-enhanced/releases/latest/download/ijava-installer-darwin-arm64 -o ijava-installer

# Rendre exécutable et lancer
chmod +x ijava-installer
./ijava-installer
```

**Exemple (Windows) :**
```powershell
# Télécharger
Invoke-WebRequest -Uri "https://github.com/yannouuuu/ijava-enhanced/releases/latest/download/ijava-installer-windows-amd64.exe" -OutFile "ijava-installer.exe"

# Exécuter
.\ijava-installer.exe
```

---

## Utilisation

### Première utilisation

Après l'installation, rechargez votre shell :

```bash
source ~/.bashrc  # ou source ~/.zshrc selon votre shell
```

Testez l'installation :

```bash
ijava --info
```

### Commandes principales

```bash
ijava init              # Initialiser un projet iJava
ijava compile           # Compiler les sources
ijava test              # Lancer les tests
ijava execute           # Exécuter le programme principal
ijava status            # Afficher l'avancement du tp
```

### Commandes du wrapper

```bash
ijava update            # Mettre à jour le toolkit iJava
ijava uninstall         # Désinstaller complètement iJava
ijava --info            # Afficher les informations
```

### Alias pratiques (si activés)

```bash
ijavai                  # Alias pour ijava init
ijavac                  # Alias pour ijava compile
ijavat                  # Alias pour ijava test
ijavae                  # Alias pour ijava execute
ijavas                  # Alias pour ijava status
```

---

## Désinstallation

Pour désinstaller complètement iJava de votre système :

```bash
ijava uninstall
```

Le script de désinstallation va :
- 🗑️ Supprimer tous les fichiers iJava (`~/.ijava2`)
- 🧹 Nettoyer les profils shell (PATH et alias)
- ✨ Remettre votre système dans son état initial

**Note :** Redémarrez votre terminal après la désinstallation.

---

## Stack

L'installeur utilise les bibliothèques de l'écosystème [Charm](https://github.com/charmbracelet) :

- [Bubble Tea](https://github.com/charmbracelet/bubbletea) - Framework TUI
- [Lipgloss](https://github.com/charmbracelet/lipgloss) - Styling et layouts
- [Huh](https://github.com/charmbracelet/huh) - Formulaires interactifs
- [Bubbles](https://github.com/charmbracelet/bubbles) - Composants (progress, spinner)
- [Harmonica](https://github.com/charmbracelet/harmonica) - Animations spring physics

---

## Développement

### Prérequis

- Go 1.21+
- [jadx](https://github.com/skylot/jadx) (optionnel, pour analyser les JARs)

### Build depuis les sources

```bash
# Cloner le dépôt
git clone https://github.com/yannouuuu/ijava-enhanced.git
cd ijava-enhanced

# Installer les dépendances
go mod download

# Compiler l'installeur
go build -o ijava-installer ./cmd/installer

# Lancer l'installeur
./ijava-installer
```

### Build multi-plateforme

```bash
# Linux x86_64
GOOS=linux GOARCH=amd64 CGO_ENABLED=0 go build -ldflags="-s -w" -o ijava-installer-linux-amd64 ./cmd/installer

# macOS Apple Silicon
GOOS=darwin GOARCH=arm64 CGO_ENABLED=0 go build -ldflags="-s -w" -o ijava-installer-darwin-arm64 ./cmd/installer

# Windows x86_64
GOOS=windows GOARCH=amd64 CGO_ENABLED=0 go build -ldflags="-s -w" -o ijava-installer-windows-amd64.exe ./cmd/installer

# Windows ARM64 (Surface Pro X)
GOOS=windows GOARCH=arm64 CGO_ENABLED=0 go build -ldflags="-s -w" -o ijava-installer-windows-arm64.exe ./cmd/installer
```

### Build tous les binaires d'un coup

```bash
# Script de build complet
for GOOS in linux darwin windows; do
  for GOARCH in amd64 arm64; do
    EXT=""
    [ "$GOOS" = "windows" ] && EXT=".exe"
    
    OUTPUT="ijava-installer-${GOOS}-${GOARCH}${EXT}"
    echo "Building $OUTPUT..."
    
    GOOS=$GOOS GOARCH=$GOARCH CGO_ENABLED=0 go build -ldflags="-s -w" -o $OUTPUT ./cmd/installer
  done
done

echo "✅ Tous les binaires créés !"
ls -lh ijava-installer-*
```

### Workflows GitHub Actions

**Build automatique (CI) :**
- Déclenché sur push/PR
- Crée des artifacts téléchargeables
- Rétention 90 jours

**Release manuelle :**
```bash
# Via GitHub UI
Actions → Release Installer → Run workflow → installer-v2.0.0

# Via CLI
gh workflow run release-installer.yml -f version=installer-v2.0.0
```

---

## À propos d'iJava

iJava est un toolkit Java pédagogique développé par **Yann Secq** pour l'enseignement de la programmation Java à l'IUT de Lille.

🔗 **Site officiel :** https://www.iut-info.univ-lille.fr/~yann.secq/ijava/

---

## Auteurs

**iJava Enhanced Installer :**
- Yann Renard ([@yannouuuu](https://github.com/yannouuuu))

**iJava (toolkit original) :**
- Yann Secq ([@yannsecq](https://www.linkedin.com/in/yannsecq)) - Université de Lille

---

## Support

Si vous rencontrez des problèmes :

1. Vérifiez que Java est installé : `java -version`
2. Consultez les [Issues](https://github.com/yannouuuu/ijava-enhanced/issues)
3. Ouvrez une nouvelle issue si nécessaire
