#!/usr/bin/env bash
# Script de comparaison entre deux fichiers JAR
# Compare le contenu des fichiers .class après décompilation avec jadx
#
# Usage: ./diff_jars.sh <jar1> <jar2>
# Exit codes:
#   0 = Les JARs sont identiques (même contenu .class)
#   1 = Les JARs sont différents
#   2 = Erreur (fichiers manquants, etc.)

set -euo pipefail

readonly RED='\033[0;31m'
readonly GREEN='\033[0;32m'
readonly YELLOW='\033[1;33m'
readonly BLUE='\033[0;34m'
readonly NC='\033[0m' # No Color

log_info() {
    echo -e "${BLUE}ℹ${NC}  $*"
}

log_success() {
    echo -e "${GREEN}✓${NC}  $*"
}

log_warning() {
    echo -e "${YELLOW}⚠${NC}  $*"
}

log_error() {
    echo -e "${RED}✗${NC}  $*" >&2
}

# Vérification des arguments
if [ $# -ne 2 ]; then
    log_error "Usage: $0 <jar1> <jar2>"
    exit 2
fi

jar1="$1"
jar2="$2"

# Vérification de l'existence des fichiers
if [ ! -f "$jar1" ]; then
    log_error "Le fichier '$jar1' n'existe pas"
    exit 2
fi

if [ ! -f "$jar2" ]; then
    log_error "Le fichier '$jar2' n'existe pas"
    exit 2
fi

# Vérification de jadx
if ! command -v jadx &> /dev/null; then
    log_error "jadx n'est pas installé ou n'est pas dans le PATH"
    exit 2
fi

log_info "Comparaison de deux archives JAR..."
echo "  📦 JAR 1: $(basename "$jar1")"
echo "  📦 JAR 2: $(basename "$jar2")"
echo

# Création de dossiers temporaires
temp_dir=$(mktemp -d)
trap 'rm -rf "$temp_dir"' EXIT

decompiled1="$temp_dir/jar1_decompiled"
decompiled2="$temp_dir/jar2_decompiled"

mkdir -p "$decompiled1" "$decompiled2"

# Décompilation du premier JAR
log_info "Décompilation du premier JAR..."
if jadx --output-dir "$decompiled1" "$jar1" > "$temp_dir/jadx1.log" 2>&1; then
    log_success "Premier JAR décompilé"
else
    log_error "Échec de la décompilation du premier JAR"
    cat "$temp_dir/jadx1.log"
    exit 2
fi

# Décompilation du second JAR
log_info "Décompilation du second JAR..."
if jadx --output-dir "$decompiled2" "$jar2" > "$temp_dir/jadx2.log" 2>&1; then
    log_success "Second JAR décompilé"
else
    log_error "Échec de la décompilation du second JAR"
    cat "$temp_dir/jadx2.log"
    exit 2
fi

# Comparaison des fichiers .java décompilés (plus fiable que les .class binaires)
log_info "Analyse des différences de code source..."

# On ignore les métadonnées qui peuvent varier (timestamps, etc.)
diff_output="$temp_dir/diff.txt"

if diff -r -q \
    --exclude="*.xml" \
    --exclude="*.json" \
    --exclude="*.MF" \
    --exclude="resources.arsc" \
    "$decompiled1/sources" "$decompiled2/sources" > "$diff_output" 2>&1; then
    log_success "Les deux JARs contiennent le même code source"
    echo
    echo -e "${GREEN}════════════════════════════════════════${NC}"
    echo -e "${GREEN}  Les JARs sont fonctionnellement identiques${NC}"
    echo -e "${GREEN}════════════════════════════════════════${NC}"
    exit 0
else
    log_warning "Des différences ont été détectées dans le code source"
    echo
    echo -e "${YELLOW}════════════════════════════════════════${NC}"
    echo -e "${YELLOW}  Différences détectées:${NC}"
    echo -e "${YELLOW}════════════════════════════════════════${NC}"
    
    # Affichage humanisé des différences
    if [ -s "$diff_output" ]; then
        while IFS= read -r line; do
            if [[ "$line" == *"Only in"* ]]; then
                echo "  📄 $line"
            elif [[ "$line" == *"differ"* ]]; then
                echo "  ✏️  $line"
            else
                echo "  $line"
            fi
        done < "$diff_output"
    fi
    
    echo -e "${YELLOW}════════════════════════════════════════${NC}"
    exit 1
fi

