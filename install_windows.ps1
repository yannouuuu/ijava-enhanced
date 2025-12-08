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

$COMMON_MODULE_CONTENT = @'
# ==============================================================================
# iJava Enhanced - Module PowerShell commun
# ==============================================================================

$OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$script:IJAVA_VERSION = "1.0.0"
$script:JAR_URL = "https://www.iut-info.univ-lille.fr/~yann.secq/ijava/ijava.jar"
$script:DEFAULT_INSTALL_DIR = Join-Path $env:USERPROFILE ".ijava2"

# ==============================================================================
# FONCTIONS DE LOGGING
# ==============================================================================

function Write-Banner {
    param(
        [string]$Version = $script:IJAVA_VERSION
    )
    
    $title = "iJava Enhanced Installer"
    $versionText = "v$Version"
    $totalWidth = 64
    $contentWidth = $title.Length + $versionText.Length + 5  # 5 pour "⚡ " et "  "
    $padding = [Math]::Floor(($totalWidth - $contentWidth) / 2)
    $paddingRight = $totalWidth - $contentWidth - $padding
    
    Write-Host ""
    Write-Host "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" -ForegroundColor Cyan
    Write-Host "┃" -ForegroundColor Cyan -NoNewline
    Write-Host (" " * $padding) -NoNewline
    Write-Host "⚡ " -ForegroundColor Yellow -NoNewline
    Write-Host $title -ForegroundColor White -NoNewline
    Write-Host "  " -NoNewline
    Write-Host $versionText -ForegroundColor DarkCyan -NoNewline
    Write-Host (" " * $paddingRight) -NoNewline
    Write-Host "┃" -ForegroundColor Cyan
    Write-Host "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" -ForegroundColor Cyan
    Write-Host ""
}

function Write-Section {
    param([string]$Message)
    
    Write-Host ""
    Write-Host "▶ " -ForegroundColor Cyan -NoNewline
    Write-Host $Message -ForegroundColor White
}

function Write-Success {
    param([string]$Message)
    Write-Host "✓ " -ForegroundColor Green -NoNewline
    Write-Host $Message
}

function Write-Info {
    param([string]$Message)
    Write-Host "ℹ  $Message" -ForegroundColor Cyan
}

function Write-Warning2 {
    param([string]$Message)
    Write-Host "⚠  " -ForegroundColor Yellow -NoNewline
    Write-Host $Message -ForegroundColor Yellow
}

function Write-Error2 {
    param([string]$Message)
    Write-Host "✗ ERREUR: $Message" -ForegroundColor Red
}

function Write-Detail {
    param([string]$Message)
    Write-Host "  → $Message" -ForegroundColor DarkGray
}

function Write-Progress2 {
    param([string]$Message)
    Write-Host "⚙  $Message..." -ForegroundColor Magenta
}

# ==============================================================================
# FONCTIONS UTILITAIRES
# ==============================================================================

function Test-CommandExists {
    param([string]$Command)
    $null -ne (Get-Command $Command -ErrorAction SilentlyContinue)
}

function Confirm-Action {
    param(
        [string]$Prompt = "Continuer?",
        [string]$Default = "O"
    )
    
    $choices = if ($Default -eq "O") { "[O/n]" } else { "[o/N]" }
    Write-Host "?  $Prompt $choices " -ForegroundColor Yellow -NoNewline
    
    $response = Read-Host
    if ([string]::IsNullOrWhiteSpace($response)) {
        $response = $Default
    }
    
    return $response -match "^[oOyY]"
}

function Test-JavaInstalled {
    Write-Section "Vérification des prérequis"
    Write-Progress2 "Recherche de Java"
    
    $env:Path = [System.Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + 
                [System.Environment]::GetEnvironmentVariable("Path", "User")
    
    if (-not (Test-CommandExists "java")) {
        Write-Warning2 "Java n'est pas détecté sur votre système."
        Write-Host ""
        Write-Info "Java 11 ou supérieur est requis pour iJava."
        Write-Info "Téléchargez et installez Java depuis:"
        Write-Host "  → https://adoptium.net/" -ForegroundColor Cyan
        Write-Host "  → https://www.oracle.com/java/technologies/downloads/" -ForegroundColor Cyan
        Write-Host ""
        Write-Info "Après l'installation, redémarrez PowerShell et relancez ce script."
        return $false
    }
    
    try {
        $oldPreference = $ErrorActionPreference
        $ErrorActionPreference = 'Continue'
        $javaVersion = & java -version 2>&1 | Select-Object -First 1 | Out-String
        $ErrorActionPreference = $oldPreference
        
        $javaVersion = $javaVersion.Trim()
        Write-Success "Java détecté"
        Write-Detail $javaVersion
        return $true
    }
    catch {
        Write-Error2 "Impossible de vérifier la version de Java"
        Write-Detail $_.Exception.Message
        return $false
    }
}

function Test-DiskSpace {
    param(
        [int]$RequiredMB = 50,
        [string]$Path = $env:USERPROFILE
    )
    
    try {
        $drive = (Get-Item $Path).PSDrive.Name
        $disk = Get-PSDrive $drive
        $availableMB = [math]::Round($disk.Free / 1MB)
        
        if ($availableMB -lt $RequiredMB) {
            Write-Warning2 "Espace disque faible : ${availableMB}MB disponibles"
            return $false
        }
        
        return $true
    }
    catch {
        Write-Warning2 "Impossible de vérifier l'espace disque"
        return $true
    }
}

function Get-FileFromUrl {
    param(
        [string]$Url,
        [string]$Destination,
        [string]$Description = "fichier"
    )
    
    Write-Progress2 "Téléchargement de $Description"
    
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        
        $parentDir = Split-Path $Destination -Parent
        if (-not (Test-Path $parentDir)) {
            New-Item -ItemType Directory -Path $parentDir -Force | Out-Null
        }
        
        Invoke-WebRequest -Uri $Url -OutFile $Destination -UseBasicParsing
        
        if (Test-Path $Destination) {
            Write-Success "Téléchargement terminé"
            Write-Detail "Sauvegardé dans : $Destination"
            return $true
        }
        else {
            Write-Error2 "Le fichier n'a pas été créé"
            return $false
        }
    }
    catch {
        Write-Error2 "Échec du téléchargement"
        Write-Detail $_.Exception.Message
        return $false
    }
}

function Ensure-Directory {
    param(
        [string]$Path,
        [string]$Description = "répertoire"
    )
    
    if (Test-Path $Path) {
        Write-Detail "$Description existe déjà : $Path"
        return $true
    }
    
    try {
        New-Item -ItemType Directory -Path $Path -Force | Out-Null
        Write-Success "Création de $Description"
        Write-Detail $Path
        return $true
    }
    catch {
        Write-Error2 "Impossible de créer $Description : $Path"
        Write-Detail $_.Exception.Message
        return $false
    }
}

function Backup-File {
    param([string]$FilePath)
    
    if (-not (Test-Path $FilePath)) {
        return $true
    }
    
    $timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
    $backup = "$FilePath.backup.$timestamp"
    
    try {
        Copy-Item $FilePath $backup
        Write-Detail "Sauvegarde créée : $backup"
        return $true
    }
    catch {
        Write-Warning2 "Impossible de créer une sauvegarde de $FilePath"
        return $false
    }
}

function Add-ToUserPath {
    param([string]$Directory)
    
    $userPath = [Environment]::GetEnvironmentVariable("Path", "User")
    
    $paths = $userPath -split ";" | Where-Object { $_ }
    if ($paths -contains $Directory) {
        Write-Detail "Le répertoire est déjà dans le PATH"
        return $true
    }
    
    try {
        $newPath = if ($userPath) { "$userPath;$Directory" } else { $Directory }
        [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
        
        $env:Path = [System.Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + $newPath
        
        Write-Success "PATH mis à jour"
        Write-Detail "Ajouté : $Directory"
        return $true
    }
    catch {
        Write-Error2 "Impossible de mettre à jour le PATH"
        Write-Detail $_.Exception.Message
        return $false
    }
}

function Remove-FromUserPath {
    param([string]$Directory)
    
    try {
        $userPath = [Environment]::GetEnvironmentVariable("Path", "User")
        $paths = $userPath -split ";" | Where-Object { $_ -and $_ -ne $Directory }
        $newPath = $paths -join ";"
        
        [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
        
        $env:Path = [System.Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + $newPath
        
        return $true
    }
    catch {
        Write-Warning2 "Impossible de retirer le répertoire du PATH"
        return $false
    }
}

function Update-CurrentSessionPath {
    $env:Path = [System.Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + 
                [System.Environment]::GetEnvironmentVariable("Path", "User")
}

function Get-PowerShellProfile {
    return $PROFILE.CurrentUserAllHosts
}

function Add-ToProfile {
    param(
        [string]$ProfilePath,
        [string]$MarkerStart,
        [string]$MarkerEnd,
        [string]$Content
    )
    
    if (-not (Test-Path $ProfilePath)) {
        $profileDir = Split-Path $ProfilePath -Parent
        if (-not (Test-Path $profileDir)) {
            New-Item -ItemType Directory -Path $profileDir -Force | Out-Null
        }
        New-Item -ItemType File -Path $ProfilePath -Force | Out-Null
    }
    
    $profileContent = Get-Content $ProfilePath -Raw -ErrorAction SilentlyContinue
    
    if ($profileContent -and $profileContent.Contains($MarkerStart)) {
        Write-Detail "Configuration déjà présente dans le profil"
        return $true
    }
    
    try {
        $newContent = @"

$MarkerStart
$Content
$MarkerEnd
"@
        Add-Content -Path $ProfilePath -Value $newContent -Encoding UTF8
        Write-Success "Profil PowerShell mis à jour"
        return $true
    }
    catch {
        Write-Warning2 "Impossible de mettre à jour le profil PowerShell"
        Write-Detail $_.Exception.Message
        return $false
    }
}

function Remove-FromProfile {
    param(
        [string]$ProfilePath,
        [string]$MarkerStart,
        [string]$MarkerEnd
    )
    
    if (-not (Test-Path $ProfilePath)) {
        return $true
    }
    
    try {
        $content = Get-Content $ProfilePath -Raw
        
        $pattern = "(?s)$([regex]::Escape($MarkerStart)).*?$([regex]::Escape($MarkerEnd))"
        $newContent = $content -replace $pattern, ""
        $newContent = $newContent -replace "(\r?\n){3,}", "`n`n"
        
        Set-Content -Path $ProfilePath -Value $newContent.Trim() -Encoding UTF8
        return $true
    }
    catch {
        Write-Warning2 "Impossible de nettoyer le profil PowerShell"
        return $false
    }
}

function Show-SuccessMessage {
    $message = "🚀  Installation réussie avec succès !  🚀"
    $totalWidth = 64
    $padding = [Math]::Floor(($totalWidth - $message.Length) / 2)
    $paddingRight = $totalWidth - $message.Length - $padding
    
    Write-Host ""
    Write-Host "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" -ForegroundColor Green
    Write-Host "┃" -ForegroundColor Green -NoNewline
    Write-Host (" " * $padding) -NoNewline
    Write-Host $message -ForegroundColor White -NoNewline
    Write-Host (" " * $paddingRight) -NoNewline
    Write-Host "┃" -ForegroundColor Green
    Write-Host "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" -ForegroundColor Green
    Write-Host ""
}

function Show-InstallationSummary {
    param(
        [string]$InstallDir,
        [string]$JarPath,
        [string]$BinDir
    )
    
    Write-Host ""
    Write-Host "Résumé de l'installation :" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "  Répertoire principal :"
    Write-Host "  $InstallDir" -ForegroundColor DarkGray
    Write-Host ""
    Write-Host "  Toolkit iJava :"
    Write-Host "  $JarPath" -ForegroundColor DarkGray
    Write-Host ""
    Write-Host "  Exécutables :"
    Write-Host "  $BinDir" -ForegroundColor DarkGray
    Write-Host ""
}

function Show-NextSteps {
    param([string]$InstallDir)
    
    Write-Host ""
    Write-Host "Prochaines étapes :" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "1." -ForegroundColor Yellow -NoNewline
    Write-Host " Redémarrez PowerShell pour appliquer les changements"
    Write-Host ""
    Write-Host "2." -ForegroundColor Yellow -NoNewline
    Write-Host " Testez l'installation :"
    Write-Host "   ijava --info" -ForegroundColor Green
    Write-Host ""
    Write-Host "3." -ForegroundColor Yellow -NoNewline
    Write-Host " Commandes disponibles :"
    Write-Host "   ijava" -ForegroundColor Cyan -NoNewline
    Write-Host " <commande>      " -ForegroundColor DarkGray -NoNewline
    Write-Host "# Exécuter une commande iJava" -ForegroundColor DarkGray
    Write-Host "   ijava update" -ForegroundColor Cyan -NoNewline
    Write-Host "         " -ForegroundColor DarkGray -NoNewline
    Write-Host "# Mettre à jour le toolkit" -ForegroundColor DarkGray
    Write-Host "   ijava uninstall" -ForegroundColor Cyan -NoNewline
    Write-Host "      " -ForegroundColor DarkGray -NoNewline
    Write-Host "# Désinstaller iJava" -ForegroundColor DarkGray
    Write-Host ""
    Write-Host "4." -ForegroundColor Yellow -NoNewline
    Write-Host " Alias pratiques disponibles :"
    Write-Host "   ijavai" -ForegroundColor Green -NoNewline
    Write-Host "  → ijava init"
    Write-Host "   ijavac" -ForegroundColor Green -NoNewline
    Write-Host "  → ijava compile"
    Write-Host "   ijavat" -ForegroundColor Green -NoNewline
    Write-Host "  → ijava test"
    Write-Host "   ijavae" -ForegroundColor Green -NoNewline
    Write-Host "  → ijava execute"
    Write-Host "   ijavas" -ForegroundColor Green -NoNewline
    Write-Host "  → ijava status"
    Write-Host ""
    Write-Host "Installation dans : $InstallDir" -ForegroundColor DarkGray
    Write-Host ""
}

# ==============================================================================
# FONCTIONS DE VALIDATION
# ==============================================================================

function Test-Installation {
    param(
        [string]$JarPath,
        [string]$WrapperPath
    )
    
    Write-Section "Validation de l'installation"
    
    $allOk = $true
    
    if (Test-Path $JarPath) {
        $size = [math]::Round((Get-Item $JarPath).Length / 1MB, 2)
        Write-Success "Toolkit iJava présent"
        Write-Detail "${size}MB - $JarPath"
    }
    else {
        Write-Error2 "Toolkit iJava manquant : $JarPath"
        $allOk = $false
    }
    
    if (Test-Path $WrapperPath) {
        Write-Success "Lanceur présent"
        Write-Detail $WrapperPath
    }
    else {
        Write-Error2 "Lanceur manquant : $WrapperPath"
        $allOk = $false
    }
    
    if ($allOk) {
        Write-Success "Validation réussie"
        return $true
    }
    else {
        Write-Error2 "Validation échouée"
        return $false
    }
}

Export-ModuleMember -Function @(
    'Write-Banner',
    'Write-Section',
    'Write-Success',
    'Write-Info',
    'Write-Warning2',
    'Write-Error2',
    'Write-Detail',
    'Write-Progress2',
    'Test-CommandExists',
    'Confirm-Action',
    'Test-JavaInstalled',
    'Test-DiskSpace',
    'Get-FileFromUrl',
    'Ensure-Directory',
    'Backup-File',
    'Add-ToUserPath',
    'Remove-FromUserPath',
    'Update-CurrentSessionPath',
    'Get-PowerShellProfile',
    'Add-ToProfile',
    'Remove-FromProfile',
    'Show-SuccessMessage',
    'Show-InstallationSummary',
    'Show-NextSteps',
    'Test-Installation'
)

Export-ModuleMember -Variable @(
    'IJAVA_VERSION',
    'JAR_URL',
    'DEFAULT_INSTALL_DIR'
)
'@

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
    try {
        Set-Content -Path $TEMP_MODULE -Value $COMMON_MODULE_CONTENT -Encoding UTF8
        Import-Module $TEMP_MODULE -Force
    }
    catch {
        Write-Host "ERREUR: Impossible de charger le module commun" -ForegroundColor Red
        Write-Host "Détails: $($_.Exception.Message)" -ForegroundColor Red
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
        Invoke-WebRequest -Uri $JAR_URL -OutFile $JAR_PATH -UseBasicParsing
        
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
