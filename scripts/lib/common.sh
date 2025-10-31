#!/usr/bin/env bash
# ==============================================================================
# iJava Enhanced - Bibliothèque de fonctions communes
# ==============================================================================
# Description: Fonctions utilitaires partagées par les scripts d'installation
# Auteur: Yann Renard
# ==============================================================================

# Codes couleurs ANSI
readonly COLOR_RESET='\033[0m'
readonly COLOR_BOLD='\033[1m'
readonly COLOR_DIM='\033[2m'
readonly COLOR_RED='\033[0;31m'
readonly COLOR_GREEN='\033[0;32m'
readonly COLOR_YELLOW='\033[0;33m'
readonly COLOR_BLUE='\033[0;34m'
readonly COLOR_MAGENTA='\033[0;35m'
readonly COLOR_CYAN='\033[0;36m'
readonly COLOR_WHITE='\033[0;37m'

# Symboles Unicode pour une meilleure UX
readonly SYMBOL_SUCCESS="✓"
readonly SYMBOL_ERROR="✗"
readonly SYMBOL_INFO="ℹ"
readonly SYMBOL_ARROW="→"
readonly SYMBOL_CHECK="✔"
readonly SYMBOL_CROSS="✖"
readonly SYMBOL_STAR="★"
readonly SYMBOL_GEAR="⚙"
readonly SYMBOL_DOWNLOAD="⬇"
readonly SYMBOL_ROCKET="🚀"

# Configuration par défaut
readonly IJAVA_VERSION="1.0.0"
readonly JAR_URL="https://www.iut-info.univ-lille.fr/~yann.secq/ijava/ijava.jar"
readonly DEFAULT_INSTALL_DIR="${HOME}/.ijava2"

# ==============================================================================
# FONCTIONS DE LOGGING
# ==============================================================================

# Affiche une bannière stylisée
print_banner() {
    local version="${1:-$IJAVA_VERSION}"
    echo ""
    echo -e "${COLOR_CYAN}${COLOR_BOLD}"
    echo "╔════════════════════════════════════════════════════════════════╗"
    echo "║                                                                ║"
    echo "║                   iJava Enhanced Installer                     ║"
    echo "║                         Version ${version}                           ║"
    echo "║                                                                ║"
    echo "╚════════════════════════════════════════════════════════════════╝"
    echo -e "${COLOR_RESET}"
    echo ""
}

# Affiche une section
log_section() {
    echo ""
    echo -e "${COLOR_BOLD}${COLOR_BLUE}╭─────────────────────────────────────────────────────────╮${COLOR_RESET}"
    echo -e "${COLOR_BOLD}${COLOR_BLUE}│${COLOR_RESET} ${COLOR_BOLD}$1${COLOR_RESET}"
    echo -e "${COLOR_BOLD}${COLOR_BLUE}╰─────────────────────────────────────────────────────────╯${COLOR_RESET}"
}

# Message de succès
log_success() {
    echo -e "${COLOR_GREEN}${SYMBOL_SUCCESS}${COLOR_RESET} ${COLOR_BOLD}$1${COLOR_RESET}"
}

# Message d'information
log_info() {
    echo -e "${COLOR_CYAN}${SYMBOL_INFO}${COLOR_RESET}  $1"
}

# Message d'avertissement
log_warning() {
    echo -e "${COLOR_YELLOW}⚠${COLOR_RESET}  ${COLOR_YELLOW}$1${COLOR_RESET}"
}

# Message d'erreur
log_error() {
    echo -e "${COLOR_RED}${SYMBOL_ERROR}${COLOR_RESET} ${COLOR_BOLD}${COLOR_RED}ERREUR:${COLOR_RESET} $1" >&2
}

# Message de détail (plus discret)
log_detail() {
    echo -e "${COLOR_DIM}  ${SYMBOL_ARROW} $1${COLOR_RESET}"
}

# Message de progression
log_progress() {
    echo -e "${COLOR_MAGENTA}${SYMBOL_GEAR}${COLOR_RESET}  $1..."
}

# ==============================================================================
# FONCTIONS UTILITAIRES
# ==============================================================================

# Vérifie si une commande existe
has_command() {
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

    echo -ne "${COLOR_YELLOW}?${COLOR_RESET}  ${COLOR_BOLD}$prompt${COLOR_RESET} "
    read -r response

    if [[ -z "$response" ]]; then
        response="$default"
    fi

    case "$response" in
        [yYoO]*) return 0 ;;
        *) return 1 ;;
    esac
}

# Affiche une barre de progression
show_spinner() {
    local pid=$1
    local message="${2:-Traitement en cours}"
    local delay=0.1
    local spinstr='⠋⠙⠹⠸⠼⠴⠦⠧⠇⠏'

    while ps -p "$pid" > /dev/null 2>&1; do
        local temp=${spinstr#?}
        printf "\r${COLOR_CYAN}%s${COLOR_RESET}  %s" "${spinstr:0:1}" "$message"
        spinstr=$temp${spinstr%"$temp"}
        sleep $delay
    done
    printf "\r"
}

# ==============================================================================
# FONCTIONS DE VÉRIFICATION
# ==============================================================================

# Tente d'installer Java automatiquement
install_java() {
    log_section "Installation automatique de Java"

    if ! has_command sudo; then
        log_error "La commande 'sudo' est requise pour l'installation automatique."
        log_info "Veuillez installer Java manuellement et relancer le script."
        return 1
    fi

    local os
    os="$(uname -s)"

    case "$os" in
        Linux)
            if has_command apt-get; then
                log_progress "Utilisation de APT (Debian/Ubuntu)"
                sudo apt-get update
                sudo apt-get install -y openjdk-21-jdk
            elif has_command dnf; then
                log_progress "Utilisation de DNF (Fedora/CentOS)"
                sudo dnf install -y java-latest-openjdk-devel
            elif has_command yum; then
                log_progress "Utilisation de YUM (RHEL/CentOS 7)"
                sudo yum install -y java-11-openjdk-devel
            elif has_command pacman; then
                log_progress "Utilisation de Pacman (Arch Linux)"
                sudo pacman -Syu --noconfirm jdk-openjdk
            else
                log_error "Gestionnaire de paquets non supporté pour l'installation automatique."
                log_info "Veuillez installer Java manuellement."
                return 1
            fi
            ;;
        Darwin)
            if has_command brew; then
                log_progress "Utilisation de Homebrew (macOS)"
                brew install openjdk
            else
                log_error "Homebrew n'est pas installé."
                log_info "Installez Homebrew (https://brew.sh) ou installez Java manuellement."
                return 1
            fi
            ;;
        *)
            log_error "Système d'exploitation non supporté pour l'installation automatique de Java: $os"
            return 1
            ;;
    esac

    # Vérification post-installation
    if has_command java; then
        log_success "Java a été installé avec succès."
        return 0
    else
        log_error "L'installation de Java semble avoir échoué."
        return 1
    fi
}

# Demande à l'utilisateur s'il veut installer Java
prompt_and_install_java() {
    echo ""
    log_warning "Java n'est pas détecté sur votre système."
    if confirm "Voulez-vous tenter une installation automatique de Java (OpenJDK) ?"; then
        install_java
        return $?
    else
        log_error "Java est requis pour continuer."
        log_info "Veuillez l'installer manuellement et relancer le script."
        return 1
    fi
}

# Vérifie que Java est installé
check_java() {
    log_section "Vérification des prérequis"
    log_progress "Recherche de Java"

    if ! has_command java; then
        prompt_and_install_java || return 1
        # On re-vérifie après l'installation
        log_progress "Nouvelle vérification de Java"
    fi

    local java_version
    java_version="$(java -version 2>&1 | head -n 1 | tr -d '\r')"
    log_success "Java détecté"
    log_detail "$java_version"

    return 0
}

# Vérifie l'espace disque disponible
check_disk_space() {
    local required_mb="${1:-50}"
    local install_dir="${2:-$HOME}"

    if has_command df; then
        local available_mb
        available_mb=$(df -m "$install_dir" | awk 'NR==2 {print $4}')

        if [[ "$available_mb" -lt "$required_mb" ]]; then
            log_warning "Espace disque faible : ${available_mb}MB disponibles"
            return 1
        fi
    fi

    return 0
}

# ==============================================================================
# FONCTIONS DE TÉLÉCHARGEMENT
# ==============================================================================

# Télécharge un fichier avec curl ou wget
download_file() {
    local url="$1"
    local destination="$2"
    local description="${3:-fichier}"

    log_progress "Téléchargement de $description"

    if has_command curl; then
        if curl -fsSL --progress-bar "$url" -o "$destination"; then
            log_success "Téléchargement terminé"
            log_detail "Sauvegardé dans : $destination"
            return 0
        fi
    elif has_command wget; then
        if wget -q --show-progress "$url" -O "$destination" 2>&1; then
            log_success "Téléchargement terminé"
            log_detail "Sauvegardé dans : $destination"
            return 0
        fi
    else
        log_error "Aucun outil de téléchargement disponible"
        log_info "Installez curl ou wget et réessayez"
        return 1
    fi

    log_error "Échec du téléchargement"
    return 1
}

# ==============================================================================
# FONCTIONS DE GESTION DE FICHIERS
# ==============================================================================

# Crée un répertoire
ensure_directory() {
    local dir="$1"
    local description="${2:-répertoire}"

    if [[ -d "$dir" ]]; then
        log_detail "$description existe déjà : $dir"
        return 0
    fi

    if mkdir -p "$dir" 2>/dev/null; then
        log_success "Création de $description"
        log_detail "$dir"
        return 0
    else
        log_error "Impossible de créer $description : $dir"
        return 1
    fi
}

# Vérifie si un fichier peut être écrit
can_write_file() {
    local file="$1"
    local dir
    dir="$(dirname "$file")"

    if [[ -f "$file" ]]; then
        [[ -w "$file" ]]
    else
        [[ -w "$dir" ]]
    fi
}

# Sauvegarde un fichier avant modification
backup_file() {
    local file="$1"

    if [[ ! -f "$file" ]]; then
        return 0
    fi

    local backup="${file}.backup.$(date +%Y%m%d_%H%M%S)"

    if cp "$file" "$backup" 2>/dev/null; then
        log_detail "Sauvegarde créée : $backup"
        return 0
    else
        log_warning "Impossible de créer une sauvegarde de $file"
        return 1
    fi
}

# ==============================================================================
# FONCTIONS DE GESTION DE PROFILS SHELL
# ==============================================================================

# Supprime un bloc entre deux marqueurs dans un fichier
remove_profile_block() {
    local file="$1"
    local marker_start="$2"
    local marker_end="$3"

    [[ -f "$file" ]] || return 0

    local tmp
    tmp="$(mktemp)"

    awk -v start="$marker_start" -v end="$marker_end" '
        $0 == start { skip=1; next }
        $0 == end { skip=0; next }
        !skip { print }
    ' "$file" > "$tmp"

    mv "$tmp" "$file"
}

# Ajoute du contenu à un fichier de profil s'il n'existe pas
append_to_profile() {
    local file="$1"
    local marker_start="$2"
    local marker_end="$3"
    local content="$4"

    # Crée le fichier s'il n'existe pas
    if [[ ! -f "$file" ]]; then
        touch "$file" 2>/dev/null || return 1
    fi

    # Vérifie si le contenu existe déjà
    if grep -Fq "$marker_start" "$file"; then
        log_detail "Configuration déjà présente dans $(basename "$file")"
        return 0
    fi

    # Ajoute le contenu
    {
        echo ""
        echo "$marker_start"
        echo "$content"
        echo "$marker_end"
    } >> "$file"

    log_success "Profil mis à jour : $(basename "$file")"
    return 0
}

# ==============================================================================
# FONCTIONS D'AFFICHAGE FINAL
# ==============================================================================

# Affiche un message de succès final
show_success_message() {
    echo ""
    echo -e "${COLOR_GREEN}${COLOR_BOLD}"
    echo "╔════════════════════════════════════════════════════════════════╗"
    echo "║                                                                ║"
    echo "║        ${SYMBOL_ROCKET}  Installation réussie avec succès !  ${SYMBOL_ROCKET}                ║"
    echo "║                                                                ║"
    echo "╚════════════════════════════════════════════════════════════════╝"
    echo -e "${COLOR_RESET}"
}

# Affiche les prochaines étapes
show_next_steps() {
    local install_dir="${1:-$DEFAULT_INSTALL_DIR}"

    echo ""
    echo -e "${COLOR_BOLD}${COLOR_CYAN}Prochaines étapes :${COLOR_RESET}"
    echo ""
    echo -e "${COLOR_YELLOW}1.${COLOR_RESET} Rechargez votre shell :"
    echo -e "   ${COLOR_DIM}source ~/.bashrc${COLOR_RESET}  ${COLOR_DIM}# ou${COLOR_RESET}"
    echo -e "   ${COLOR_DIM}source ~/.zshrc${COLOR_RESET}"
    echo ""
    echo -e "${COLOR_YELLOW}2.${COLOR_RESET} Testez l'installation :"
    echo -e "   ${COLOR_GREEN}ijava --info${COLOR_RESET}"
    echo ""
    echo -e "${COLOR_YELLOW}3.${COLOR_RESET} Commandes disponibles :"
    echo -e "   ${COLOR_CYAN}ijava${COLOR_RESET} <commande>      ${COLOR_DIM}# Exécuter une commande iJava${COLOR_RESET}"
    echo -e "   ${COLOR_CYAN}ijava update${COLOR_RESET}         ${COLOR_DIM}# Mettre à jour le toolkit${COLOR_RESET}"
    echo -e "   ${COLOR_CYAN}ijava uninstall${COLOR_RESET}      ${COLOR_DIM}# Désinstaller iJava${COLOR_RESET}"
    echo ""
    echo -e "${COLOR_YELLOW}4.${COLOR_RESET} Alias pratiques disponibles :"
    echo -e "   ${COLOR_GREEN}ijavai${COLOR_RESET}  ${SYMBOL_ARROW} ijava init"
    echo -e "   ${COLOR_GREEN}ijavac${COLOR_RESET}  ${SYMBOL_ARROW} ijava compile"
    echo -e "   ${COLOR_GREEN}ijavat${COLOR_RESET}  ${SYMBOL_ARROW} ijava test"
    echo -e "   ${COLOR_GREEN}ijavae${COLOR_RESET}  ${SYMBOL_ARROW} ijava execute"
    echo -e "   ${COLOR_GREEN}ijavas${COLOR_RESET}  ${SYMBOL_ARROW} ijava status"
    echo ""
    echo -e "${COLOR_DIM}Installation dans : $install_dir${COLOR_RESET}"
    echo ""
}

# Affiche un résumé de l'installation
show_installation_summary() {
    local install_dir="$1"
    local jar_path="$2"
    local bin_dir="$3"

    echo ""
    echo -e "${COLOR_BOLD}${COLOR_CYAN}Résumé de l'installation :${COLOR_RESET}"
    echo ""
    echo -e "  ${COLOR_BOLD}Répertoire principal :${COLOR_RESET}"
    echo -e "  ${COLOR_DIM}$install_dir${COLOR_RESET}"
    echo ""
    echo -e "  ${COLOR_BOLD}Toolkit iJava :${COLOR_RESET}"
    echo -e "  ${COLOR_DIM}$jar_path${COLOR_RESET}"
    echo ""
    echo -e "  ${COLOR_BOLD}Exécutables :${COLOR_RESET}"
    echo -e "  ${COLOR_DIM}$bin_dir${COLOR_RESET}"
    echo ""
}

# ==============================================================================
# FONCTIONS DE VALIDATION
# ==============================================================================

# Valide l'installation
validate_installation() {
    local jar_path="$1"
    local wrapper_path="$2"

    log_section "Validation de l'installation"

    local all_ok=true

    # Vérifie le JAR
    if [[ -f "$jar_path" ]]; then
        log_success "Toolkit iJava présent"
        log_detail "$(du -h "$jar_path" | cut -f1) - $jar_path"
    else
        log_error "Toolkit iJava manquant : $jar_path"
        all_ok=false
    fi

    # Vérifie le wrapper
    if [[ -f "$wrapper_path" && -x "$wrapper_path" ]]; then
        log_success "Lanceur exécutable présent"
        log_detail "$wrapper_path"
    else
        log_error "Lanceur manquant ou non exécutable : $wrapper_path"
        all_ok=false
    fi

    if [[ "$all_ok" == true ]]; then
        log_success "Validation réussie"
        return 0
    else
        log_error "Validation échouée"
        return 1
    fi
}

# ==============================================================================
# EXPORT DES FONCTIONS
# ==============================================================================

# Export des fonctions pour qu'elles soient disponibles dans les scripts appelants
export -f print_banner
export -f log_section log_success log_info log_warning log_error log_detail log_progress
export -f has_command confirm show_spinner
export -f check_java check_disk_space
export -f download_file
export -f ensure_directory can_write_file backup_file
export -f remove_profile_block append_to_profile
export -f show_success_message show_next_steps show_installation_summary
export -f validate_installation
