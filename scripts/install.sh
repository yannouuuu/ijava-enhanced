#!/usr/bin/env bash
# ==============================================================================
# iJava Enhanced Installer - Downloader
# ==============================================================================
# Télécharge et exécute le bon binaire de l'installeur selon votre OS/architecture
# ==============================================================================

set -euo pipefail

readonly REPO="yannouuuu/ijava-enhanced"
readonly RELEASE_TAG="latest"

# Couleurs
readonly C_RESET='\033[0m'
readonly C_GREEN='\033[0;32m'
readonly C_YELLOW='\033[0;33m'
readonly C_CYAN='\033[0;36m'
readonly C_RED='\033[0;31m'

log_info() {
    echo -e "${C_CYAN}ℹ${C_RESET}  $*"
}

log_success() {
    echo -e "${C_GREEN}✓${C_RESET}  $*"
}

log_error() {
    echo -e "${C_RED}✗${C_RESET}  $*" >&2
}

# Détection de l'OS
OS="$(uname -s)"
ARCH="$(uname -m)"

log_info "Détection de votre système..."
echo "  OS: $OS"
echo "  Architecture: $ARCH"
echo ""

case "$OS" in
    Linux)  OS="linux" ;;
    Darwin) OS="darwin" ;;
    *)
        log_error "OS non supporté: $OS"
        echo "Systèmes supportés: Linux, macOS"
        exit 1
        ;;
esac

case "$ARCH" in
    x86_64)  ARCH="amd64" ;;
    arm64)   ARCH="arm64" ;;
    aarch64) ARCH="arm64" ;;
    *)
        log_error "Architecture non supportée: $ARCH"
        echo "Architectures supportées: x86_64, arm64, aarch64"
        exit 1
        ;;
esac

BINARY="ijava-installer-${OS}-${ARCH}"
URL="https://github.com/${REPO}/releases/${RELEASE_TAG}/download/${BINARY}"
TEMP_FILE="/tmp/ijava-installer"

log_info "Téléchargement de l'installeur iJava Enhanced..."
echo "  Binaire: $BINARY"
echo "  URL: $URL"
echo ""

# Téléchargement
if command -v curl >/dev/null 2>&1; then
    if curl -fsSL "$URL" -o "$TEMP_FILE"; then
        log_success "Téléchargement terminé"
    else
        log_error "Échec du téléchargement"
        echo "Vérifiez que la release existe sur GitHub"
        exit 1
    fi
elif command -v wget >/dev/null 2>&1; then
    if wget -q "$URL" -O "$TEMP_FILE"; then
        log_success "Téléchargement terminé"
    else
        log_error "Échec du téléchargement"
        echo "Vérifiez que la release existe sur GitHub"
        exit 1
    fi
else
    log_error "curl ou wget requis pour le téléchargement"
    exit 1
fi

# Rendre exécutable
chmod +x "$TEMP_FILE"

echo ""
log_info "Lancement de l'installeur..."
echo ""

# Exécuter l'installeur
exec "$TEMP_FILE"
