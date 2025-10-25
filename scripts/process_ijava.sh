#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
DEFAULT_DATASET_DIR="$REPO_ROOT/datasets/ijava"

DATASET_DIR="$DEFAULT_DATASET_DIR"
FORCE_REBUILD=false
JADX_BIN="${JADX_BIN:-jadx}"
declare -a JADX_ARGS=()
SKIP_JADX=false

usage() {
    cat <<'EOF'
Usage: process_ijava.sh [options]

Options:
  --dataset-dir PATH   Répertoire contenant les sous-dossiers horodatés avec ijava.jar (défaut: datasets/ijava)
  --force              Reprocesser tous les JARs même si déjà traités
  --skip-jadx          Saute la décompilation (extraction des .class uniquement)
  --jadx-bin PATH      Binaire jadx à utiliser (défaut: valeur de $JADX_BIN ou 'jadx')
  --jadx-arg ARG       Argument supplémentaire transmis à jadx (répétable)
  -h, --help           Affiche cette aide

La sortie crée/actualise pour chaque dossier horodaté:
  - classes/   : extraction brute des .class du JAR
  - sources/   : code Java décompilé via jadx (si disponible)
  - .processed.sha256 : hash du JAR traité pour éviter les retraitements
EOF
}

log() {
    printf '==> %s\n' "$1"
}

info() {
    printf '    %s\n' "$1"
}

warn() {
    printf 'AVERTISSEMENT: %s\n' "$1" >&2
}

error() {
    printf 'ERREUR: %s\n' "$1" >&2
}

cleanup_on_error() {
    local tmp_dir="$1"
    [ -n "$tmp_dir" ] && [ -d "$tmp_dir" ] && rm -rf "$tmp_dir"
}

while [[ $# -gt 0 ]]; do
    case "$1" in
        --dataset-dir)
            shift || { error "Argument manquant pour --dataset-dir"; exit 1; }
            DATASET_DIR="$(readlink -f "$1")"
            ;;
        --force)
            FORCE_REBUILD=true
            ;;
        --skip-jadx)
            SKIP_JADX=true
            ;;
        --jadx-bin)
            shift || { error "Argument manquant pour --jadx-bin"; exit 1; }
            JADX_BIN="$1"
            ;;
        --jadx-arg)
            shift || { error "Argument manquant pour --jadx-arg"; exit 1; }
            JADX_ARGS+=("$1")
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            error "Option inconnue: $1"
            usage
            exit 1
            ;;
    esac
    shift
done

if [ ! -d "$DATASET_DIR" ]; then
    error "Répertoire dataset introuvable: $DATASET_DIR"
    exit 1
fi

if [ "${#JADX_ARGS[@]}" -gt 0 ] && [ "$SKIP_JADX" = true ]; then
    warn "--skip-jadx ignore les --jadx-arg fournis"
fi

if [ "$SKIP_JADX" = false ] && ! command -v "$JADX_BIN" >/dev/null 2>&1; then
    warn "Jadx introuvable (binaire: $JADX_BIN). La décompilation sera ignorée."
    SKIP_JADX=true
fi

shopt -s nullglob
mapfile -t RUNS < <(find "$DATASET_DIR" -mindepth 1 -maxdepth 1 -type d | sort)
shopt -u nullglob

if [ "${#RUNS[@]}" -eq 0 ]; then
    warn "Aucun sous-dossier trouvé dans $DATASET_DIR"
    exit 0
fi

for run_dir in "${RUNS[@]}"; do
    JAR_PATH="$run_dir/ijava.jar"
    SHA_FILE="$run_dir/ijava.jar.sha256"
    SENTINEL="$run_dir/.processed.sha256"
    CLASSES_DIR="$run_dir/classes"
    SOURCES_DIR="$run_dir/sources"

    if [ ! -f "$JAR_PATH" ]; then
        warn "Aucun ijava.jar dans $run_dir, saut"
        continue
    fi

    if [ -f "$SHA_FILE" ]; then
        JAR_HASH="$(cut -d' ' -f1 "$SHA_FILE")"
    else
        JAR_HASH="$(sha256sum "$JAR_PATH" | cut -d' ' -f1)"
    fi

    if [ "$FORCE_REBUILD" = false ] && [ -f "$SENTINEL" ] && grep -qx "$JAR_HASH" "$SENTINEL"; then
        info "Aucun changement pour $(basename "$run_dir"), on saute"
        continue
    fi

    log "Traitement de $(basename "$run_dir")"
    info "Hash détecté: $JAR_HASH"

    # Extraction des classes
    classes_tmp="$(mktemp -d "$run_dir/classes.tmp.XXXXXX")"
    trap 'cleanup_on_error "$classes_tmp"' ERR
    unzip -qq "$JAR_PATH" -d "$classes_tmp"
    trap - ERR
    rm -rf "$CLASSES_DIR"
    mv "$classes_tmp" "$CLASSES_DIR"
    info "Classes mises à jour dans $CLASSES_DIR"

    if [ "$SKIP_JADX" = false ]; then
        sources_tmp="$(mktemp -d "$run_dir/sources.tmp.XXXXXX")"
        trap 'cleanup_on_error "$sources_tmp"' ERR
        "$JADX_BIN" -d "$sources_tmp" "$JAR_PATH" "${JADX_ARGS[@]}"
        trap - ERR
        rm -rf "$SOURCES_DIR"
        mv "$sources_tmp" "$SOURCES_DIR"
        info "Sources décompilées dans $SOURCES_DIR"
    else
        info "Décompilation sautée"
    fi

    printf '%s\n' "$JAR_HASH" > "$SENTINEL"
done

log "Traitement terminé"

