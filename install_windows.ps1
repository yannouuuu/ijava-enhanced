# ==============================================================================
# iJava Enhanced - Script d'installation pour Windows
# ==============================================================================

#Requires -Version 5.1

$OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$PSDefaultParameterValues['*:Encoding'] = 'utf8'

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
$IS_PIPED = -not $PSScriptRoot

if ($IS_PIPED) {
    $SCRIPT_DIR = $null
    $LIB_DIR = $null
    $TEMP_MODULE = Join-Path $env:TEMP "IJavaCommon_$(Get-Random).psm1"
}
else {
    $SCRIPT_DIR = $PSScriptRoot
    $LIB_DIR = Join-Path $SCRIPT_DIR "scripts\lib"
    $TEMP_MODULE = $null
}

if ($IS_PIPED) {
    $COMMON_URL = "https://raw.githubusercontent.com/yannouuuu/ijava-enhanced/main/scripts/lib/Common.psm1"
    
    try {
        Write-Host "Téléchargement du module commun..." -ForegroundColor Cyan
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        $webClient = New-Object System.Net.WebClient
        $webClient.Encoding = [System.Text.Encoding]::UTF8
        $webClient.DownloadFile($COMMON_URL, $TEMP_MODULE)
        $webClient.Dispose()
        
        Import-Module $TEMP_MODULE -Force
    }
    catch {
        Write-Host "ERREUR: Impossible de télécharger le module commun" -ForegroundColor Red
        Write-Host "URL: $COMMON_URL" -ForegroundColor Red
        if (Test-Path $TEMP_MODULE) {
            Remove-Item $TEMP_MODULE -Force
        }
        exit 1
    }
}
else {
    $commonModule = Join-Path $LIB_DIR "Common.psm1"
    if (Test-Path $commonModule) {
        Import-Module $commonModule -Force
    }
    else {
        Write-Host "ERREUR: Module commun introuvable : $commonModule" -ForegroundColor Red
        Write-Host "Assurez-vous que le dépôt est complet." -ForegroundColor Red
        exit 1
    }
}

$INSTALL_DIR = if ($env:IJAVA_HOME) { $env:IJAVA_HOME } else { Join-Path $env:USERPROFILE ".ijava2" }
$BIN_DIR = Join-Path $INSTALL_DIR "bin"
$JAR_PATH = Join-Path $INSTALL_DIR "ijava.jar"
$WRAPPER_PATH = Join-Path $BIN_DIR "ijava.ps1"
$WRAPPER_CMD = Join-Path $BIN_DIR "ijava.cmd"

$PATH_MARKER_START = "# >>> ijava path >>>"
$PATH_MARKER_END = "# <<< ijava path <<<"
$ALIAS_MARKER_START = "# >>> ijava aliases >>>"
$ALIAS_MARKER_END = "# <<< ijava aliases <<<"

function Initialize-InstallEnvironment {
    Write-Section "Préparation de l'environnement"
    
    if (-not (Ensure-Directory $INSTALL_DIR "répertoire d'installation")) {
        exit 1
    }
    
    if (-not (Ensure-Directory $BIN_DIR "répertoire des exécutables")) {
        exit 1
    }
    
    if (-not (Test-DiskSpace -RequiredMB 50 -Path $INSTALL_DIR)) {
        Write-Warning2 "Espace disque faible, mais on continue..."
    }
    
    Write-Success "Environnement prêt"
}

function Install-IJavaToolkit {
    Write-Section "Téléchargement du toolkit iJava"
    
    if (-not (Get-FileFromUrl -Url $JAR_URL -Destination $JAR_PATH -Description "toolkit iJava")) {
        Write-Error2 "Impossible de télécharger le toolkit"
        Write-Info "Vérifiez votre connexion internet et réessayez"
        exit 1
    }
}

function New-WrapperScript {
    Write-Section "Création du lanceur intelligent"
    
    Write-Progress2 "Génération du wrapper PowerShell"
    
    $wrapperContent = @'
# ==============================================================================
# iJava Enhanced par Yann Renard (version Windows)
# ==============================================================================

#Requires -Version 5.1

$OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

$INSTALL_DIR = if ($env:IJAVA_HOME) { $env:IJAVA_HOME } else { Join-Path $env:USERPROFILE ".ijava2" }
$BIN_DIR = Join-Path $INSTALL_DIR "bin"
$JAR_PATH = Join-Path $INSTALL_DIR "ijava.jar"
$JAR_URL = "https://www.iut-info.univ-lille.fr/~yann.secq/ijava/ijava.jar"
$VERSION = "1.2.0"

function Test-Cmd {
    param([string]$Command)
    $null -ne (Get-Command $Command -ErrorAction SilentlyContinue)
}

function Confirm-Prompt {
    param(
        [string]$Prompt = "Continuer?",
        [string]$Default = "O"
    )
    $choices = if ($Default -eq "O") { "[O/n]" } else { "[o/N]" }
    Write-Host "?  $Prompt $choices " -ForegroundColor Yellow -NoNewline
    $response = Read-Host
    if ([string]::IsNullOrWhiteSpace($response)) { $response = $Default }
    return $response -match "^[oOyY]"
}

function Update-IJava {
    Write-Host "⬇  Téléchargement de la dernière version..." -ForegroundColor Cyan
    
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        $webClient = New-Object System.Net.WebClient
        $webClient.Encoding = [System.Text.Encoding]::UTF8
        $webClient.DownloadFile($JAR_URL, $JAR_PATH)
        $webClient.Dispose()
        
        Write-Host "✓ Toolkit mis à jour avec succès !" -ForegroundColor Green
        return $true
    }
    catch {
        Write-Host "✗ Impossible de mettre à jour le toolkit" -ForegroundColor Red
        Write-Host "  Erreur: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

function Ensure-Jar {
    if (-not (Test-Path $JAR_PATH)) {
        Write-Host "⚠  Toolkit manquant, téléchargement en cours..." -ForegroundColor Yellow
        Update-IJava | Out-Null
    }
}

function Invoke-Update {
    Update-IJava
    exit 0
}

function Show-Info {
    $title = "iJava Enhanced Wrapper"
    $versionText = "v$VERSION"
    $subtitle = "Windows Edition  •  par Yann Renard"
    $totalWidth = 64
    
    # Calcul pour la première ligne (titre + version)
    $line1Content = "⚡ $title  $versionText"
    $padding1 = [Math]::Floor(($totalWidth - $line1Content.Length) / 2)
    $paddingRight1 = $totalWidth - $line1Content.Length - $padding1
    
    # Calcul pour la deuxième ligne (sous-titre)
    $padding2 = [Math]::Floor(($totalWidth - $subtitle.Length) / 2)
    $paddingRight2 = $totalWidth - $subtitle.Length - $padding2
    
    Write-Host ""
    Write-Host "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" -ForegroundColor Cyan
    Write-Host "┃" -ForegroundColor Cyan -NoNewline
    Write-Host (" " * $padding1) -NoNewline
    Write-Host "⚡ " -ForegroundColor Yellow -NoNewline
    Write-Host $title -ForegroundColor White -NoNewline
    Write-Host "  " -NoNewline
    Write-Host $versionText -ForegroundColor DarkCyan -NoNewline
    Write-Host (" " * $paddingRight1) -NoNewline
    Write-Host "┃" -ForegroundColor Cyan
    Write-Host "┃" -ForegroundColor Cyan -NoNewline
    Write-Host (" " * $padding2) -NoNewline
    Write-Host $subtitle -ForegroundColor DarkGray -NoNewline
    Write-Host (" " * $paddingRight2) -NoNewline
    Write-Host "┃" -ForegroundColor Cyan
    Write-Host "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Installation : " -NoNewline
    Write-Host $INSTALL_DIR
    Write-Host "Fichier JAR  : " -NoNewline
    Write-Host $JAR_PATH
    Write-Host ""
    Write-Host "▶ " -ForegroundColor Cyan -NoNewline
    Write-Host "Commandes du wrapper" -ForegroundColor White
    Write-Host "  ijava update" -ForegroundColor Green -NoNewline
    Write-Host " / " -NoNewline
    Write-Host "self-update" -ForegroundColor Green -NoNewline
    Write-Host "  → Met à jour le toolkit"
    Write-Host "  ijava uninstall" -ForegroundColor Green -NoNewline
    Write-Host "             → Désinstalle iJava"
    Write-Host "  ijava --info" -ForegroundColor Green -NoNewline
    Write-Host "                → Affiche ces informations"
    Write-Host ""
    
    if (Test-Path $JAR_PATH) {
        Write-Host "▶ " -ForegroundColor Cyan -NoNewline
        Write-Host "Commandes iJava disponibles" -ForegroundColor White
        Write-Host ""
        Write-Host "  init       Initialize current exercise or specified exercise from skeleton"
        Write-Host "  compile    Compile the current exercise or specified Java file"
        Write-Host "  test       Run a *Test class"
        Write-Host "  execute    Execute the current exercise or specified Java class"
        Write-Host "  status     Show sessions overview or TP status"
        Write-Host "  start      Initialize workspace and show status"
        Write-Host "  stop       Stop the web server daemon"
        Write-Host ""
        Write-Host "Pour plus d'informations : " -NoNewline
        Write-Host "ijava <command> --help" -ForegroundColor Cyan
    }
    else {
        Write-Host "⚠  Le fichier JAR du toolkit n'est pas installé." -ForegroundColor Yellow
    }
    Write-Host ""
    exit 0
}

function Remove-ProfileBlock {
    param(
        [string]$FilePath,
        [string]$MarkerStart,
        [string]$MarkerEnd
    )
    
    if (-not (Test-Path $FilePath)) {
        return
    }
    
    try {
        $content = Get-Content $FilePath -Raw
        $pattern = "(?s)$([regex]::Escape($MarkerStart)).*?$([regex]::Escape($MarkerEnd))"
        $newContent = $content -replace $pattern, ""
        $newContent = $newContent -replace "(\r?\n){3,}", "`n`n"
        Set-Content -Path $FilePath -Value $newContent.Trim() -Encoding UTF8
    }
    catch {
        Write-Host "⚠  Impossible de nettoyer le profil" -ForegroundColor Yellow
    }
}

function Invoke-Uninstall {
    Write-Host ""
    Write-Host "Désinstallation d'iJava Enhanced" -ForegroundColor Cyan
    Write-Host ""
    
    Write-Host "⚙  Suppression des fichiers du wrapper..." -ForegroundColor Yellow
    if (Test-Path $JAR_PATH) { Remove-Item $JAR_PATH -Force }
    if (Test-Path "$BIN_DIR\ijava.ps1") { Remove-Item "$BIN_DIR\ijava.ps1" -Force }
    if (Test-Path "$BIN_DIR\ijava.cmd") { Remove-Item "$BIN_DIR\ijava.cmd" -Force }
    
    Write-Host "⚙  Nettoyage du profil PowerShell..." -ForegroundColor Yellow
    $profilePath = $PROFILE.CurrentUserAllHosts
    if (Test-Path $profilePath) {
        Remove-ProfileBlock -FilePath $profilePath -MarkerStart "# >>> ijava path >>>" -MarkerEnd "# <<< ijava path <<<"
        Remove-ProfileBlock -FilePath $profilePath -MarkerStart "# >>> ijava aliases >>>" -MarkerEnd "# <<< ijava aliases <<<"
    }
    
    Write-Host "⚙  Nettoyage du PATH..." -ForegroundColor Yellow
    try {
        $userPath = [Environment]::GetEnvironmentVariable("Path", "User")
        $paths = $userPath -split ";" | Where-Object { $_ -and $_ -ne $BIN_DIR }
        $newPath = $paths -join ";"
        [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
    }
    catch {
        Write-Host "⚠  Impossible de nettoyer le PATH" -ForegroundColor Yellow
    }
    
    if ((Test-Path $INSTALL_DIR) -and (@(Get-ChildItem $INSTALL_DIR -Force).Count -gt 0)) {
        Write-Host ""
        if (Confirm-Prompt "Supprimer le contenu restant de .ijava2 (logs, TPs, exercices) ?") {
            Write-Host "⚙  Suppression du contenu utilisateur..." -ForegroundColor Yellow
            Remove-Item "$INSTALL_DIR\*" -Recurse -Force -ErrorAction SilentlyContinue
            Write-Host "✓  Contenu utilisateur supprimé" -ForegroundColor Green
            
            if (@(Get-ChildItem $BIN_DIR -Force -ErrorAction SilentlyContinue).Count -eq 0) {
                Remove-Item $BIN_DIR -Force -ErrorAction SilentlyContinue
            }
            if (@(Get-ChildItem $INSTALL_DIR -Force -ErrorAction SilentlyContinue).Count -eq 0) {
                Remove-Item $INSTALL_DIR -Force -ErrorAction SilentlyContinue
            }
        }
        else {
            Write-Host "ℹ  Contenu utilisateur conservé dans : $INSTALL_DIR" -ForegroundColor Cyan
        }
    }
    
    Write-Host "⚙  Suppression des répertoires vides..." -ForegroundColor Yellow
    if ((Test-Path $BIN_DIR) -and (@(Get-ChildItem $BIN_DIR -Force -ErrorAction SilentlyContinue).Count -eq 0)) {
        Remove-Item $BIN_DIR -Force -ErrorAction SilentlyContinue
    }
    if ((Test-Path $INSTALL_DIR) -and (@(Get-ChildItem $INSTALL_DIR -Force -ErrorAction SilentlyContinue).Count -eq 0)) {
        Remove-Item $INSTALL_DIR -Force -ErrorAction SilentlyContinue
    }
    
    Write-Host ""
    Write-Host "✓ Désinstallation terminée !" -ForegroundColor Green
    Write-Host ""
    Write-Host "Redémarrez PowerShell pour finaliser la suppression."
    Write-Host ""
    exit 0
}

function Main {
    param([string[]]$Arguments)
    
    if ($Arguments.Count -gt 0) {
        switch ($Arguments[0]) {
            { $_ -in "update", "self-update" } {
                Invoke-Update
            }
            "--info" {
                Show-Info
            }
            "uninstall" {
                Invoke-Uninstall
            }
            default {
                Ensure-Jar
                & java -jar $JAR_PATH @Arguments
                exit $LASTEXITCODE
            }
        }
    }
    else {
        Ensure-Jar
        & java -jar $JAR_PATH
        exit $LASTEXITCODE
    }
}

Main -Arguments $args
'@
    
    try {
        Set-Content -Path $WRAPPER_PATH -Value $wrapperContent -Encoding UTF8
        Write-Success "Wrapper PowerShell créé"
        Write-Detail $WRAPPER_PATH
    }
    catch {
        Write-Error2 "Impossible de créer le wrapper PowerShell"
        Write-Detail $_.Exception.Message
        exit 1
    }
    
    Write-Progress2 "Génération du wrapper CMD"
    
    $cmdContent = @"
@echo off
setlocal enabledelayedexpansion

chcp 65001 >nul 2>&1

powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0ijava.ps1" %*
exit /b %ERRORLEVEL%
"@
    
    try {
        Set-Content -Path $WRAPPER_CMD -Value $cmdContent -Encoding UTF8
        Write-Success "Wrapper CMD créé"
        Write-Detail $WRAPPER_CMD
    }
    catch {
        Write-Error2 "Impossible de créer le wrapper CMD"
        Write-Detail $_.Exception.Message
        exit 1
    }
}

function Set-EnvironmentConfiguration {
    Write-Section "Configuration de l'environnement"
    
    if (-not (Add-ToUserPath $BIN_DIR)) {
        Write-Warning2 "Le PATH n'a pas pu être mis à jour automatiquement"
        Write-Info "Vous devrez ajouter manuellement $BIN_DIR à votre PATH"
    }
    
    $profilePath = Get-PowerShellProfile
    
    $pathConfig = @"
# Ajoute iJava au PATH de la session
`$ijavaPath = "$BIN_DIR"
if (`$env:Path -notlike "*`$ijavaPath*") {
    `$env:Path = "`$ijavaPath;" + `$env:Path
}
"@
    
    $aliasConfig = @"
# Alias pratiques pour iJava
function ijavai { & ijava init @args }
function ijavac { & ijava compile @args }
function ijavat { & ijava test @args }
function ijavae { & ijava execute @args }
function ijavas { & ijava status @args }
"@
    
    Add-ToProfile -ProfilePath $profilePath -MarkerStart $PATH_MARKER_START -MarkerEnd $PATH_MARKER_END -Content $pathConfig | Out-Null
    Add-ToProfile -ProfilePath $profilePath -MarkerStart $ALIAS_MARKER_START -MarkerEnd $ALIAS_MARKER_END -Content $aliasConfig | Out-Null
    
    New-Alias -Name ijavai -Value "$BIN_DIR\ijava.ps1" -Scope Global -Force -ErrorAction SilentlyContinue

    New-Alias -Name ijavac -Value "$BIN_DIR\ijava.ps1" -Scope Global -Force -ErrorAction SilentlyContinue
    New-Alias -Name ijavat -Value "$BIN_DIR\ijava.ps1" -Scope Global -Force -ErrorAction SilentlyContinue
    New-Alias -Name ijavae -Value "$BIN_DIR\ijava.ps1" -Scope Global -Force -ErrorAction SilentlyContinue
    New-Alias -Name ijavas -Value "$BIN_DIR\ijava.ps1" -Scope Global -Force -ErrorAction SilentlyContinue
    
    Write-Success "Configuration de l'environnement terminée"
}

function Show-WindowsNotes {
    Write-Host ""
    Write-Host "📝 Notes spécifiques Windows :" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "  • " -ForegroundColor Yellow -NoNewline
    Write-Host "Le PATH utilisateur a été mis à jour automatiquement"
    Write-Host "  • " -ForegroundColor Yellow -NoNewline
    Write-Host "Redémarrez PowerShell pour que les changements prennent effet"
    Write-Host "  • " -ForegroundColor Yellow -NoNewline
    Write-Host "Vous pouvez utiliser " -NoNewline
    Write-Host "ijava.ps1" -ForegroundColor Green -NoNewline
    Write-Host " ou " -NoNewline
    Write-Host "ijava.cmd" -ForegroundColor Green -NoNewline
    Write-Host " indifféremment"
    Write-Host ""
}

function Main {
    try {
        Write-Banner -Version $IJAVA_VERSION
        
        if (-not (Test-JavaInstalled)) {
            exit 1
        }
        
        Initialize-InstallEnvironment
        Install-IJavaToolkit
        New-WrapperScript
        Set-EnvironmentConfiguration
        
        if (-not (Test-Installation -JarPath $JAR_PATH -WrapperPath $WRAPPER_PATH)) {
            Write-Error2 "L'installation n'a pas pu être validée"
            exit 1
        }
        
        Show-SuccessMessage
        Show-InstallationSummary -InstallDir $INSTALL_DIR -JarPath $JAR_PATH -BinDir $BIN_DIR
        Show-WindowsNotes
        Show-NextSteps -InstallDir $INSTALL_DIR
        
        Write-Host "Merci d'avoir installé iJava Enhanced ! 🚀" -ForegroundColor Green
        Write-Host ""
    }
    catch {
        Write-Host ""
        Write-Error2 "Une erreur inattendue s'est produite"
        Write-Detail $_.Exception.Message
        Write-Host ""
        Write-Host "Trace complète :" -ForegroundColor Red
        Write-Host $_.ScriptStackTrace -ForegroundColor Red
        exit 1
    }
    finally {
        if ($IS_PIPED -and $TEMP_MODULE -and (Test-Path $TEMP_MODULE)) {
            Remove-Item $TEMP_MODULE -Force -ErrorAction SilentlyContinue
        }
    }
}

trap {
    Write-Host ""
    Write-Error2 "Installation interrompue par l'utilisateur"
    if ($IS_PIPED -and $TEMP_MODULE -and (Test-Path $TEMP_MODULE)) {
        Remove-Item $TEMP_MODULE -Force -ErrorAction SilentlyContinue
    }
    exit 130
}

Main
