#!/usr/bin/env bash
# ==============================================================================
# iJava Enhanced - Script d'installation pour Linux
# ==============================================================================
# Description: Installe le toolkit iJava et configure l'environnement shell
# Système: Linux (toutes distributions)
# Prérequis: Java 11+, bash, curl ou wget
# ==============================================================================

set -euo pipefail

# ==============================================================================
# CONFIGURATION
# ==============================================================================

# Détecte si le script est exécuté via pipe (curl | bash)
if [[ -n "${BASH_SOURCE[0]:-}" ]] && [[ -f "${BASH_SOURCE[0]:-}" ]]; then
    # Exécution locale (fichier sur le disque)
    readonly SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    readonly LIB_DIR="$SCRIPT_DIR/scripts/lib"
    readonly IS_PIPED=false
else
    # Exécution via pipe (curl | bash)
    readonly SCRIPT_DIR=""
    readonly LIB_DIR=""
    readonly IS_PIPED=true
fi

# Chargement de la bibliothèque commune
if [[ "$IS_PIPED" == true ]]; then
    # Télécharge common.sh temporairement
    readonly TEMP_COMMON="$(mktemp)"
    readonly COMMON_URL="https://raw.githubusercontent.com/yannouuuu/ijava-enhanced/main/scripts/lib/common.sh"
    
    if command -v curl >/dev/null 2>&1; then
        curl -fsSL "$COMMON_URL" -o "$TEMP_COMMON" || {
            echo "ERREUR: Impossible de télécharger la bibliothèque commune"
            echo "URL: $COMMON_URL"
            rm -f "$TEMP_COMMON"
            exit 1
        }
    elif command -v wget >/dev/null 2>&1; then
        wget -q "$COMMON_URL" -O "$TEMP_COMMON" || {
            echo "ERREUR: Impossible de télécharger la bibliothèque commune"
            echo "URL: $COMMON_URL"
            rm -f "$TEMP_COMMON"
            exit 1
        }
    else
        echo "ERREUR: curl ou wget requis pour l'installation"
        exit 1
    fi
    
    # shellcheck source=scripts/lib/common.sh
    source "$TEMP_COMMON"
    
    # Nettoyage du fichier temporaire à la sortie
    trap 'rm -f "$TEMP_COMMON"' EXIT
elif [[ -f "$LIB_DIR/common.sh" ]]; then
    # Chargement local
    # shellcheck source=scripts/lib/common.sh
    source "$LIB_DIR/common.sh"
else
    echo "ERREUR: Bibliothèque commune introuvable : $LIB_DIR/common.sh"
    echo "Assurez-vous que le dépôt est complet."
    exit 1
fi

# Configuration spécifique à Linux
readonly INSTALL_DIR="${IJAVA_HOME:-$HOME/.ijava2}"
readonly BIN_DIR="$INSTALL_DIR/bin"
readonly JAR_PATH="$INSTALL_DIR/ijava.jar"
readonly WRAPPER_PATH="$BIN_DIR/ijava"
readonly PROFILE_FILES=("$HOME/.bashrc" "$HOME/.zshrc" "$HOME/.profile")

# Marqueurs pour les fichiers de profil
readonly PATH_MARKER_START="# >>> ijava path >>>"
readonly PATH_MARKER_END="# <<< ijava path <<<"
readonly ALIAS_MARKER_START="# >>> ijava aliases >>>"
readonly ALIAS_MARKER_END="# <<< ijava aliases <<<"

# ==============================================================================
# FONCTIONS D'INSTALLATION
# ==============================================================================

# Prépare les répertoires d'installation
prepare_directories() {
    log_section "Préparation de l'environnement"
    
    ensure_directory "$INSTALL_DIR" "répertoire d'installation" || exit 1
    ensure_directory "$BIN_DIR" "répertoire des exécutables" || exit 1
    
    check_disk_space 50 "$INSTALL_DIR" || {
        log_warning "Espace disque faible, mais on continue..."
    }
    
    log_success "Environnement prêt"
}

# Télécharge le toolkit iJava
download_toolkit() {
    log_section "Téléchargement du toolkit iJava"
    
    download_file "$JAR_URL" "$JAR_PATH" "toolkit iJava" || {
        log_error "Impossible de télécharger le toolkit"
        log_info "Vérifiez votre connexion internet et réessayez"
        exit 1
    }
}

# Crée le script wrapper
create_wrapper() {
    log_section "Création du lanceur intelligent"
    
    log_progress "Génération du wrapper"
    
    cat > "$WRAPPER_PATH" << 'WRAPPER_EOF'
#!/usr/bin/env bash
# ==============================================================================
# iJava Enhanced par Yann Renard (version Linux)
# ==============================================================================
# Ce script encapsule le toolkit iJava et ajoute des fonctionnalités avancées
# ==============================================================================

set -euo pipefail

# Configuration
readonly INSTALL_DIR="${IJAVA_HOME:-$HOME/.ijava2}"
readonly BIN_DIR="$INSTALL_DIR/bin"
readonly JAR_PATH="$INSTALL_DIR/ijava.jar"
readonly JAR_URL="https://www.iut-info.univ-lille.fr/~yann.secq/ijava/ijava.jar"
readonly PATH_MARKER_START="# >>> ijava path >>>"
readonly PATH_MARKER_END="# <<< ijava path <<<"
readonly ALIAS_MARKER_START="# >>> ijava aliases >>>"
readonly ALIAS_MARKER_END="# <<< ijava aliases <<<"
readonly PROFILE_FILES=("$HOME/.bashrc" "$HOME/.zshrc" "$HOME/.profile")
readonly VERSION="1.2.0"

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

# Demande une confirmation utilisateur
confirm() {
    local prompt="${1:-Continuer?}"
    local default="${2:-y}"

    if [[ "$default" == "y" ]]; then
        prompt="$prompt [O/n]"
    else
        prompt="$prompt [o/N]"
    fi

    echo -ne "${C_YELLOW}?${C_RESET}  ${C_BOLD}$prompt${C_RESET} "
    read -r response

    if [[ -z "$response" ]]; then
        response="$default"
    fi

    case "$response" in
        [yYoO]*) return 0 ;;
        *) return 1 ;;
    esac
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

# Supprime un bloc de profil
remove_profile_block() {
    local file="$1" start="$2" end="$3"
    [[ -f "$file" ]] || return 0
    
    local tmp
    tmp="$(mktemp)"
    awk -v s="$start" -v e="$end" '
        $0==s { flag=1; next }
        $0==e { flag=0; next }
        !flag { print }
    ' "$file" > "$tmp"
    mv "$tmp" "$file"
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
    echo -e "${C_CYAN}${C_BOLD}╔════════════════════════════════════════════════════════╗${C_RESET}"
    echo -e "${C_CYAN}${C_BOLD}║                                                        ║${C_RESET}"
    echo -e "${C_CYAN}${C_BOLD}║          iJava Enhanced Wrapper v${VERSION}             ║${C_RESET}"
    echo -e "${C_CYAN}${C_BOLD}║                                                        ║${C_RESET}"
    echo -e "${C_CYAN}${C_BOLD}╚════════════════════════════════════════════════════════╝${C_RESET}"
    echo ""
    echo -e "${C_BOLD}Installation :${C_RESET} $INSTALL_DIR"
    echo -e "${C_BOLD}Fichier JAR  :${C_RESET} $JAR_PATH"
    echo ""
    echo -e "${C_BOLD}${C_CYAN}Commandes du wrapper :${C_RESET}"
    echo -e "  ${C_GREEN}ijava update${C_RESET} / ${C_GREEN}self-update${C_RESET}  → Met à jour le toolkit"
    echo -e "  ${C_GREEN}ijava uninstall${C_RESET}             → Désinstalle iJava"
    echo -e "  ${C_GREEN}ijava --info${C_RESET}                → Affiche ces informations"
    echo ""
    
    if [[ -f "$JAR_PATH" ]]; then
        echo -e "${C_BOLD}${C_CYAN}Informations du toolkit iJava :${C_RESET}"
        echo "────────────────────────────────────────────────────────"
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

    echo -e "${C_YELLOW}⚙${C_RESET}  Suppression des fichiers du wrapper..."
    rm -f "$JAR_PATH"
    rm -f "$BIN_DIR/ijava"

    echo -e "${C_YELLOW}⚙${C_RESET}  Nettoyage des profils shell..."
    for file in "${PROFILE_FILES[@]}"; do
        if [[ -f "$file" ]]; then
            remove_profile_block "$file" "$PATH_MARKER_START" "$PATH_MARKER_END"
            remove_profile_block "$file" "$ALIAS_MARKER_START" "$ALIAS_MARKER_END"
        fi
    done

    # Vérifier s'il reste des fichiers utilisateur dans .ijava2
    if [[ -d "$HOME/.ijava2" ]] && [[ -n "$(ls -A "$HOME/.ijava2" 2>/dev/null)" ]]; then
        echo ""
        if confirm "Supprimer le contenu restant de ~/.ijava2 (logs, TPs, exercices) ?"; then
            echo -e "${C_YELLOW}⚙${C_RESET}  Suppression du contenu utilisateur..."
            rm -rf "$HOME/.ijava2"/*
            echo -e "${C_GREEN}✓${C_RESET}  Contenu utilisateur supprimé"
            # Supprimer le répertoire s'il est vide
            [[ -z "$(ls -A "$HOME/.ijava2" 2>/dev/null)" ]] && rmdir "$HOME/.ijava2"
        else
            echo -e "${C_CYAN}ℹ${C_RESET}  Contenu utilisateur conservé dans : $HOME/.ijava2"
        fi
    fi

    echo -e "${C_YELLOW}⚙${C_RESET}  Suppression des répertoires vides..."
    [[ -d "$BIN_DIR" ]] && [[ -z "$(ls -A "$BIN_DIR" 2>/dev/null)" ]] && rmdir "$BIN_DIR"
    [[ -d "$INSTALL_DIR" ]] && [[ -z "$(ls -A "$INSTALL_DIR" 2>/dev/null)" ]] && rmdir "$INSTALL_DIR"

    echo ""
    echo -e "${C_GREEN}✓${C_RESET} ${C_BOLD}Désinstallation terminée !${C_RESET}"
    echo ""
    echo "Redémarrez votre shell pour finaliser la suppression."
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
WRAPPER_EOF
    
    chmod +x "$WRAPPER_PATH" || {
        log_error "Impossible de rendre le wrapper exécutable"
        exit 1
    }
    
    log_success "Lanceur créé et configuré"
    log_detail "$WRAPPER_PATH"
}

# Configure les profils shell
configure_shell() {
    log_section "Configuration des profils shell"
    
    local path_config="export PATH=\"\$HOME/.ijava2/bin:\$PATH\""
    local alias_config
    alias_config=$(cat <<'ALIAS_EOF'
# Alias pratiques pour iJava
alias ijavai="ijava init"
alias ijavac="ijava compile"
alias ijavat="ijava test"
alias ijavae="ijava execute"
alias ijavas="ijava status"
ALIAS_EOF
)
    
    local updated=0
    
    for profile in "${PROFILE_FILES[@]}"; do
        if [[ -f "$profile" ]]; then
            # Ajoute le PATH
            append_to_profile "$profile" "$PATH_MARKER_START" "$PATH_MARKER_END" "$path_config" && ((updated++)) || true
            
            # Ajoute les alias
            append_to_profile "$profile" "$ALIAS_MARKER_START" "$ALIAS_MARKER_END" "$alias_config" || true
        fi
    done
    
    # Configure le PATH pour la session actuelle
    if [[ ":$PATH:" != *":$BIN_DIR:"* ]]; then
        export PATH="$BIN_DIR:$PATH"
        log_detail "PATH mis à jour pour cette session"
    fi
    
    # Configure les alias pour la session actuelle
    alias ijavai="ijava init" 2>/dev/null || true
    alias ijavac="ijava compile" 2>/dev/null || true
    alias ijavat="ijava test" 2>/dev/null || true
    alias ijavae="ijava execute" 2>/dev/null || true
    alias ijavas="ijava status" 2>/dev/null || true
    
    if [[ $updated -gt 0 ]]; then
        log_success "Configuration shell terminée"
    else
        log_info "Configuration shell déjà à jour"
    fi
}

# ==============================================================================
# FONCTION PRINCIPALE
# ==============================================================================

main() {
    # Affiche la bannière
    print_banner "$IJAVA_VERSION"
    
    # Vérifie les prérequis
    check_java || exit 1
    
    # Installation
    prepare_directories
    download_toolkit
    create_wrapper
    configure_shell
    
    # Validation
    validate_installation "$JAR_PATH" "$WRAPPER_PATH" || {
        log_error "L'installation n'a pas pu être validée"
        exit 1
    }
    
    # Messages finaux
    show_success_message
    show_installation_summary "$INSTALL_DIR" "$JAR_PATH" "$BIN_DIR"
    show_next_steps "$INSTALL_DIR"
    
    echo -e "${COLOR_BOLD}${COLOR_GREEN}Merci d'avoir installé iJava Enhanced ! ${SYMBOL_ROCKET}${COLOR_RESET}"
    echo ""
}

# ==============================================================================
# POINT D'ENTRÉE
# ==============================================================================

# Gestion des signaux pour un arrêt propre
trap 'echo ""; log_error "Installation interrompue par l''utilisateur"; exit 130' INT TERM

# Exécution
main "$@"
