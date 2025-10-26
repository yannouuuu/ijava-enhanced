#!/usr/bin/env bash
# Script de traitement et d'archivage d'une version ijava
# Décompile le JAR et crée les artefacts nécessaires
#
# Usage: ./process_ijava.sh --dataset-dir <dir> [--force]

set -euo pipefail

readonly BLUE='\033[0;34m'
readonly GREEN='\033[0;32m'
readonly NC='\033[0m'

log_info() {
    echo -e "${BLUE}ℹ${NC}  $*"
}

log_success() {
    echo -e "${GREEN}✓${NC}  $*"
}

# Parsing des arguments
dataset_dir=""
force=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --dataset-dir)
            dataset_dir="$2"
            shift 2
            ;;
        --force)
            force=true
            shift
            ;;
        *)
            echo "Option inconnue: $1"
            exit 1
            ;;
    esac
done

if [ -z "$dataset_dir" ]; then
    echo "Usage: $0 --dataset-dir <dir> [--force]"
    exit 1
fi

# Trouver le dernier dossier de version
latest_version=$(find "$dataset_dir" -maxdepth 1 -type d -name "ijava-*" | sort -r | head -n 1)

if [ -z "$latest_version" ]; then
    echo "Aucune version trouvée dans $dataset_dir"
    exit 1
fi

log_info "Traitement de $(basename "$latest_version")"

jar_file="$latest_version/ijava.jar"
decompiled_dir="$latest_version/decompiled"

if [ ! -f "$jar_file" ]; then
    echo "Erreur: $jar_file n'existe pas"
    exit 1
fi

if [ -d "$decompiled_dir" ] && [ "$force" = false ]; then
    log_success "Déjà décompilé (utilisez --force pour forcer)"
    exit 0
fi

log_info "Décompilation en cours..."
mkdir -p "$decompiled_dir"

if jadx --output-dir "$decompiled_dir" "$jar_file" > "$latest_version/jadx.log" 2>&1; then
    log_success "Décompilation réussie"
else
    echo "Erreur lors de la décompilation"
    cat "$latest_version/jadx.log"
    exit 1
fi

log_success "Traitement terminé: $decompiled_dir"

