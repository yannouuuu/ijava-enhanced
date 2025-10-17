#!/usr/bin/env bash
set -euo pipefail

JAR_URL="https://www.iut-info.univ-lille.fr/~yann.secq/ijava/ijava.jar"
INSTALL_DIR="${IJAVA_HOME:-$HOME/.ijava}"
BIN_DIR="$INSTALL_DIR/bin"
JAR_PATH="$INSTALL_DIR/ijava.jar"
WRAPPER_PATH="$BIN_DIR/ijava"
PROFILE_FILES=("$HOME/.zshrc" "$HOME/.bashrc" "$HOME/.bash_profile")
PATH_MARKER_START="# >>> ijava path >>>"
PATH_MARKER_END="# <<< ijava path <<<"
ALIAS_MARKER_START="# >>> ijava aliases >>>"
ALIAS_MARKER_END="# <<< ijava aliases <<<"

log() {
    printf '==> %s\n' "$1"
}

info() {
    printf '    %s\n' "$1"
}

error() {
    printf 'ERROR: %s\n' "$1" >&2
}

require_java() {
    log "Checking Java runtime"
    if ! command -v java >/dev/null 2>&1; then
        error "Java runtime not found. Install it from https://adoptium.net or https://www.oracle.com/java/"
        exit 1
    fi
    local version
    version="$(java -version 2>&1 | head -n 1 | tr -d '\r')"
    info "Java detected: ${version}"
}

ensure_directories() {
    log "Preparing install directories"
    mkdir -p "$BIN_DIR"
    info "Using install dir: $INSTALL_DIR"
}

has_cmd() {
    command -v "$1" >/dev/null 2>&1
}

download_file() {
    local url="$1"
    local dst="$2"
    if has_cmd curl; then
        curl -fsSL "$url" -o "$dst"
    elif has_cmd wget; then
        wget -q "$url" -O "$dst"
    else
        error "Neither curl nor wget is available. Install one of them and retry."
        exit 1
    fi
}

download_toolkit() {
    log "Fetching ijava toolkit"
    download_file "$JAR_URL" "$JAR_PATH"
    info "Saved jar to $JAR_PATH"
}

write_wrapper() {
    log "Writing launcher"
    cat <<'EOF' >"$WRAPPER_PATH"
#!/usr/bin/env bash
set -euo pipefail

INSTALL_DIR="${IJAVA_HOME:-$HOME/.ijava}"
BIN_DIR="$INSTALL_DIR/bin"
JAR_PATH="$INSTALL_DIR/ijava.jar"
JAR_URL="https://www.iut-info.univ-lille.fr/~yann.secq/ijava/ijava.jar"
PATH_MARKER_START="# >>> ijava path >>>"
PATH_MARKER_END="# <<< ijava path <<<"
ALIAS_MARKER_START="# >>> ijava aliases >>>"
ALIAS_MARKER_END="# <<< ijava aliases <<<"
PROFILE_FILES=("$HOME/.zshrc" "$HOME/.bashrc" "$HOME/.bash_profile")

has_cmd() {
    command -v "$1" >/dev/null 2>&1
}

download_latest() {
    if has_cmd curl; then
        curl -fsSL "$JAR_URL" -o "$JAR_PATH"
    elif has_cmd wget; then
        wget -q "$JAR_URL" -O "$JAR_PATH"
    else
        echo "[ijava] Cannot update: curl or wget required." >&2
        exit 1
    fi
    echo "[ijava] Toolkit updated."
}

remove_profile_block() {
    local file="$1"
    local start="$2"
    local end="$3"
    [ -f "$file" ] || return 0
    local tmp
    tmp="$(mktemp)"
    awk -v s="$start" -v e="$end" '
        $0==s { flag=1; next }
        $0==e { flag=0; next }
        !flag { print }
    ' "$file" >"$tmp"
    mv "$tmp" "$file"
}

ensure_jar() {
    if [ ! -f "$JAR_PATH" ]; then
        echo "[ijava] Toolkit jar missing, downloading..."
        download_latest
    fi
}

case "${1:-}" in
    update)
        download_latest
        exit 0
        ;;
    uninstall)
        echo "[ijava] Removing installed files..."
        rm -f "$JAR_PATH"
        rm -f "$BIN_DIR/ijava"
        for file in "${PROFILE_FILES[@]}"; do
            remove_profile_block "$file" "$PATH_MARKER_START" "$PATH_MARKER_END"
            remove_profile_block "$file" "$ALIAS_MARKER_START" "$ALIAS_MARKER_END"
        done
        if [ -d "$BIN_DIR" ] && [ -z "$(ls -A "$BIN_DIR" 2>/dev/null)" ]; then
            rmdir "$BIN_DIR"
        fi
        if [ -d "$INSTALL_DIR" ] && [ -z "$(ls -A "$INSTALL_DIR" 2>/dev/null)" ]; then
            rmdir "$INSTALL_DIR"
        fi
        echo "[ijava] Uninstall complete. Restart your shell."
        exit 0
        ;;
    *)
        ensure_jar
        exec java -jar "$JAR_PATH" "$@"
        ;;
esac
EOF
    chmod +x "$WRAPPER_PATH"
}

append_if_missing() {
    local file="$1"
    local marker_start="$2"
    local marker_end="$3"
    local content="$4"
    if [ ! -f "$file" ]; then
        touch "$file"
    fi
    if grep -Fq "$marker_start" "$file"; then
        info "Marker already present in $file"
    else
        {
            printf '\n%s\n' "$marker_start"
            printf '%s\n' "$content"
            printf '%s\n' "$marker_end"
        } >>"$file"
        info "Updated $file"
    fi
}

configure_shell_profiles() {
    log "Updating shell profiles"
    local path_line='export PATH="$HOME/.ijava/bin:$PATH"'
    local alias_block
    alias_block="$(cat <<'EOF'
alias ijavai="ijava init"
alias ijavac="ijava compile"
alias ijavat="ijava test"
alias ijavae="ijava execute"
alias ijavas="ijava status"
EOF
)"
    for file in "${PROFILE_FILES[@]}"; do
        append_if_missing "$file" "$PATH_MARKER_START" "$PATH_MARKER_END" "$path_line"
        append_if_missing "$file" "$ALIAS_MARKER_START" "$ALIAS_MARKER_END" "$alias_block"
    done
    if [[ ":$PATH:" != *":$BIN_DIR:"* ]]; then
        export PATH="$BIN_DIR:$PATH"
    fi
    alias ijavai="ijava init"
    alias ijavac="ijava compile"
    alias ijavat="ijava test"
    alias ijavae="ijava execute"
    alias ijavas="ijava status"
}

final_message() {
    printf '\nInstallation complete! Available commands:\n'
    printf '  - ijava <command>\n'
    printf '  - ijava update\n'
    printf '  - ijava uninstall\n'
    printf 'Aliases: ijavai, ijavac, ijavat, ijavae, ijavas\n'
    printf '\nOpen a new terminal session or source your profile to use the toolkit.\n'
}

main() {
    require_java
    ensure_directories
    download_toolkit
    write_wrapper
    configure_shell_profiles
    final_message
}

main "$@"
