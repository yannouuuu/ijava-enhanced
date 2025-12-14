# ==============================================================================
# iJava Enhanced Installer - Downloader (Windows)
# ==============================================================================
# Télécharge et exécute l'installeur iJava pour Windows
# ==============================================================================

$ErrorActionPreference = "Stop"

$repo = "yannouuuu/ijava-enhanced"
$releaseTag = "latest"
$binary = "ijava-installer-windows-amd64.exe"
$url = "https://github.com/$repo/releases/$releaseTag/download/$binary"
$tempFile = "$env:TEMP\ijava-installer.exe"

function Write-ColorOutput {
    param(
        [string]$Message,
        [string]$Color = "White",
        [string]$Symbol = ""
    )
    
    if ($Symbol) {
        Write-Host "$Symbol " -NoNewline -ForegroundColor $Color
    }
    Write-Host $Message
}

Write-Host ""
Write-ColorOutput "Détection de votre système..." -Color Cyan -Symbol "ℹ"
Write-Host "  OS: Windows"
Write-Host "  Architecture: x86_64"
Write-Host ""

Write-ColorOutput "Téléchargement de l'installeur iJava Enhanced..." -Color Cyan -Symbol "ℹ"
Write-Host "  Binaire: $binary"
Write-Host "  URL: $url"
Write-Host ""

try {
    # Téléchargement
    $progressPreference = 'SilentlyContinue'
    Invoke-WebRequest -Uri $url -OutFile $tempFile -UseBasicParsing
    $progressPreference = 'Continue'
    
    Write-ColorOutput "Téléchargement terminé" -Color Green -Symbol "✓"
    Write-Host ""
    
    Write-ColorOutput "Lancement de l'installeur..." -Color Cyan -Symbol "ℹ"
    Write-Host ""
    
    # Exécuter l'installeur
    & $tempFile
    
    # Nettoyer le fichier temporaire
    if (Test-Path $tempFile) {
        Remove-Item $tempFile -Force
    }
    
} catch {
    Write-ColorOutput "Erreur lors du téléchargement: $_" -Color Red -Symbol "✗"
    Write-Host "Vérifiez que la release existe sur GitHub"
    exit 1
}
