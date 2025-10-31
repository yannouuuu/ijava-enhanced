# ==============================================================================
# iJava Enhanced - Script d'installation pour Windows
# ==============================================================================
# Description: Installe le toolkit iJava et configure l'environnement PowerShell
# Système: Windows 10/11
# Prérequis: Java 11+, PowerShell 5.1+
# ==============================================================================

#Requires -Version 5.1

[CmdletBinding()]
param(
    [switch]$Force,
    [switch]$Silent
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
$ProgressPreference = "SilentlyContinue"

# ==============================================================================
# CONFIGURATION
# ==============================================================================

$Script:Config = @{
    Version         = "1.0.0"
    JarUrl          = "https://www.iut-info.univ-lille.fr/~yann.secq/ijava/ijava.jar"
    InstallDir      = Join-Path $env:USERPROFILE ".ijava2"
    AliasMarkerStart = "# >>> ijava aliases >>>"
    AliasMarkerEnd   = "# <<< ijava aliases <<<"
}

$Script:Config.BinDir = Join-Path $Script:Config.InstallDir "bin"
$Script:Config.JarPath = Join-Path $Script:Config.InstallDir "ijava.jar"
$Script:Config.PowerShellWrapper = Join-Path $Script:Config.BinDir "ijava.ps1"
$Script:Config.CmdWrapper = Join-Path $Script:Config.BinDir "ijava.cmd"

# Profils PowerShell possibles
$Script:ProfileCandidates = @(
    $PROFILE,
    (Join-Path $env:USERPROFILE "Documents\PowerShell\Microsoft.PowerShell_profile.ps1"),
    (Join-Path $env:USERPROFILE "Documents\WindowsPowerShell\Microsoft.PowerShell_profile.ps1")
) | Where-Object { $_ } | Sort-Object -Unique

# ==============================================================================
# FONCTIONS D'AFFICHAGE
# ==============================================================================

function Write-Banner {
    param([string]$Version = "1.0.0")

    Write-Host ""
    Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
    Write-Host "║                                                                ║" -ForegroundColor Cyan
    Write-Host "║                   iJava Enhanced Installer                     ║" -ForegroundColor Cyan
    Write-Host "║                         Version $Version                         ║" -ForegroundColor Cyan
    Write-Host "║                      (Windows Edition)                         ║" -ForegroundColor Cyan
    Write-Host "║                                                                ║" -ForegroundColor Cyan
    Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
    Write-Host ""
}

function Write-Section {
    param([string]$Message)
    Write-Host ""
    Write-Host "╭─────────────────────────────────────────────────────────╮" -ForegroundColor Blue
    Write-Host "│ " -NoNewline -ForegroundColor Blue
    Write-Host $Message -ForegroundColor White
    Write-Host "╰─────────────────────────────────────────────────────────╯" -ForegroundColor Blue
}

function Write-Success {
    param([string]$Message)
    Write-Host "✓ " -NoNewline -ForegroundColor Green
    Write-Host $Message -ForegroundColor White
}

function Write-Info {
    param([string]$Message)
    Write-Host "ℹ " -NoNewline -ForegroundColor Cyan
    Write-Host "  $Message" -ForegroundColor Gray
}

function Write-Detail {
    param([string]$Message)
    Write-Host "  → $Message" -ForegroundColor DarkGray
}

function Write-Warning {
    param([string]$Message)
    Write-Host "⚠ " -NoNewline -ForegroundColor Yellow
    Write-Host "  $Message" -ForegroundColor Yellow
}

function Write-Error {
    param([string]$Message)
    Write-Host "✗ " -NoNewline -ForegroundColor Red
    Write-Host "ERREUR: $Message" -ForegroundColor Red
}

function Write-Progress {
    param([string]$Message)
    Write-Host "⚙ " -NoNewline -ForegroundColor Magenta
    Write-Host "  $Message..." -ForegroundColor White
}

# ==============================================================================
# FONCTIONS DE VÉRIFICATION
# ==============================================================================

function Install-Java {
    Write-Section "Installation automatique de Java"

    $wingetCmd = Get-Command winget -ErrorAction SilentlyContinue
    if (-not $wingetCmd) {
        Write-Error "Winget n'est pas disponible sur votre système."
        Write-Info "Winget est le gestionnaire de paquets de Windows et est requis pour l'installation automatique."
        Write-Info "Veuillez installer Java manuellement depuis https://adoptium.net et relancer le script."
        return $false
    }

    if (-not (Test-AdminRights)) {
        Write-Warning "Les droits administrateur sont requis pour installer Java."
        Write-Info "Tentative de relance du script en tant qu'administrateur..."

        # Relaunch as admin
        try {
            $newProcess = @{
                FilePath     = "powershell.exe"
                ArgumentList = "-NoProfile -ExecutionPolicy Bypass -File `"$($MyInvocation.MyCommand.Path)`""
                Verb         = "RunAs"
                ErrorAction  = "Stop"
            }
            Start-Process @newProcess
            # Exit current script
            exit
        }
        catch {
            Write-Error "Échec de la tentative de relance en mode administrateur."
            Write-Info "Veuillez faire un clic droit sur le script et choisir 'Exécuter en tant qu'administrateur'."
            return $false
        }
    }

    $packageId = "Microsoft.OpenJDK.21"
    Write-Progress "Installation de Java via Winget (Package: $packageId)"
    Write-Info "Cela peut prendre plusieurs minutes..."

    try {
        $wingetArgs = "install --id $packageId -e --accept-package-agreements --accept-source-agreements --silent"
        Write-Detail "Exécution: winget $wingetArgs"

        $process = Start-Process -FilePath "winget" -ArgumentList $wingetArgs -Wait -PassThru -WindowStyle Hidden

        if ($process.ExitCode -eq 0) {
            Write-Success "Java a été installé avec succès via Winget."
            # Il faut rafraîchir les variables d'environnement pour trouver la nouvelle commande java
            Write-Info "Mise à jour des variables d'environnement..."
            $env:Path = [System.Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path", "User")
            return $true
        }
        else {
            Write-Error "L'installation de Java via Winget a échoué avec le code d'erreur : $($process.ExitCode)"
            return $false
        }
    }
    catch {
        Write-Error "Une erreur est survenue lors de l'exécution de Winget : $_"
        return $false
    }
}

function Prompt-AndInstallJava {
    Write-Warning "Java n'est pas détecté sur votre système."
    $response = Read-Host "? Voulez-vous tenter une installation automatique de Java (Microsoft OpenJDK) via Winget ? [O/n]"
    if ($response -eq '' -or $response -match '^[yYoO]$') {
        return Install-Java
    }
    else {
        Write-Error "Java est requis pour continuer."
        Write-Info "Veuillez l'installer manuellement et relancer le script."
        return $false
    }
}

function Test-JavaInstalled {
    Write-Section "Vérification des prérequis"
    Write-Progress "Recherche de Java"

    $javaCmd = Get-Command java -ErrorAction SilentlyContinue
    if (-not $javaCmd) {
        if (-not (Prompt-AndInstallJava)) {
            return $false
        }
        # Re-check after attempting installation
        Write-Progress "Nouvelle vérification de Java"
        $javaCmd = Get-Command java -ErrorAction SilentlyContinue
        if (-not $javaCmd) {
            Write-Error "Java n'est toujours pas détecté après la tentative d'installation."
            return $false
        }
    }

    try {
        $javaVersionOutput = & java -version 2>&1
        $javaVersion = ($javaVersionOutput | Select-Object -First 1 | Out-String).Trim()
        Write-Success "Java détecté"
        Write-Detail $javaVersion
        return $true
    }
    catch {
        Write-Error "Impossible de vérifier la version de Java"
        return $false
    }
}

function Test-DiskSpace {
    param([int]$RequiredMB = 50)

    try {
        $drive = (Get-Item $env:USERPROFILE).PSDrive
        $freeSpaceMB = [math]::Round($drive.Free / 1MB, 2)

        if ($freeSpaceMB -lt $RequiredMB) {
            Write-Warning "Espace disque faible : ${freeSpaceMB}MB disponibles"
            return $false
        }

        return $true
    }
    catch {
        Write-Warning "Impossible de vérifier l'espace disque"
        return $true
    }
}

function Test-AdminRights {
    $currentPrincipal = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
    return $currentPrincipal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
}

# ==============================================================================
# FONCTIONS D'INSTALLATION
# ==============================================================================

function Initialize-Directories {
    Write-Section "Préparation de l'environnement"

    try {
        # Vérifie l'espace disque
        Test-DiskSpace -RequiredMB 50 | Out-Null

        # Crée les répertoires
        if (-not (Test-Path $Script:Config.InstallDir)) {
            New-Item -ItemType Directory -Path $Script:Config.InstallDir -Force | Out-Null
            Write-Success "Création du répertoire d'installation"
            Write-Detail $Script:Config.InstallDir
        }
        else {
            Write-Detail "Répertoire d'installation existant : $($Script:Config.InstallDir)"
        }

        if (-not (Test-Path $Script:Config.BinDir)) {
            New-Item -ItemType Directory -Path $Script:Config.BinDir -Force | Out-Null
            Write-Success "Création du répertoire des exécutables"
            Write-Detail $Script:Config.BinDir
        }
        else {
            Write-Detail "Répertoire des exécutables existant : $($Script:Config.BinDir)"
        }

        Write-Success "Environnement prêt"
        return $true
    }
    catch {
        Write-Error "Impossible de créer les répertoires : $_"
        return $false
    }
}

function Install-Toolkit {
    Write-Section "Téléchargement du toolkit iJava"
    Write-Progress "Téléchargement en cours"

    try {
        Invoke-WebRequest -Uri $Script:Config.JarUrl -OutFile $Script:Config.JarPath -UseBasicParsing

        if (Test-Path $Script:Config.JarPath) {
            $fileSize = (Get-Item $Script:Config.JarPath).Length / 1KB
            Write-Success "Téléchargement terminé"
            Write-Detail "Sauvegardé dans : $($Script:Config.JarPath)"
            Write-Detail "Taille : $([math]::Round($fileSize, 2)) KB"
            return $true
        }
        else {
            Write-Error "Le fichier téléchargé est introuvable"
            return $false
        }
    }
    catch {
        Write-Error "Échec du téléchargement : $_"
        Write-Info "Vérifiez votre connexion internet et réessayez"
        return $false
    }
}

function Install-Wrapper {
    Write-Section "Création du lanceur intelligent"
    Write-Progress "Génération du wrapper PowerShell"

    $wrapperContent = @'
# ==============================================================================
# iJava Enhanced par Yann Renard (version Windows)
# ==============================================================================
# Ce script encapsule le toolkit iJava et ajoute des fonctionnalités avancées
# ==============================================================================

#Requires -Version 5.1
Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$Script:Config = @{
    Version          = "1.0.0"
    InstallDir       = Join-Path $env:USERPROFILE ".ijava2"
    JarUrl           = "https://www.iut-info.univ-lille.fr/~yann.secq/ijava/ijava.jar"
    AliasMarkerStart = "# >>> ijava aliases >>>"
    AliasMarkerEnd   = "# <<< ijava aliases <<<"
}

$Script:Config.BinDir = Join-Path $Script:Config.InstallDir "bin"
$Script:Config.JarPath = Join-Path $Script:Config.InstallDir "ijava.jar"

$Script:ProfileCandidates = @(
    (Join-Path $env:USERPROFILE "Documents\PowerShell\Microsoft.PowerShell_profile.ps1"),
    (Join-Path $env:USERPROFILE "Documents\WindowsPowerShell\Microsoft.PowerShell_profile.ps1")
) | Where-Object { $_ } | Sort-Object -Unique

function Write-IjavaInfo {
    param([string]$Message)
    Write-Host "[ijava] $Message" -ForegroundColor Cyan
}

function Write-IjavaError {
    param([string]$Message)
    Write-Host "[ijava] ERREUR: $Message" -ForegroundColor Red
}

function Invoke-DownloadLatest {
    Write-IjavaInfo "Téléchargement de la dernière version du toolkit..."
    try {
        Invoke-WebRequest -Uri $Script:Config.JarUrl -OutFile $Script:Config.JarPath -UseBasicParsing
        Write-Host "[ijava] " -NoNewline -ForegroundColor Cyan
        Write-Host "✓ Mise à jour terminée avec succès !" -ForegroundColor Green
    }
    catch {
        Write-IjavaError "Échec du téléchargement : $_"
        exit 1
    }
}

function Remove-ProfileBlock {
    param(
        [string]$ProfilePath,
        [string]$Start,
        [string]$End
    )

    if (-not (Test-Path $ProfilePath)) { return }

    try {
        $content = Get-Content $ProfilePath -Raw -ErrorAction SilentlyContinue
        if (-not $content) { return }

        $pattern = [regex]::Escape($Start) + '.*?' + [regex]::Escape($End)
        $newContent = $content -replace "(?s)$pattern", ''
        $newContent = $newContent -replace '(\r?\n){3,}', "`r`n`r`n"
        $newContent = $newContent.Trim()

        if ($newContent) {
            Set-Content -Path $ProfilePath -Value $newContent -NoNewline
        }
        else {
            Remove-Item -Path $ProfilePath -Force -ErrorAction SilentlyContinue
        }
    }
    catch {
        Write-IjavaError "Erreur lors du nettoyage de $ProfilePath : $_"
    }
}

function Remove-PathEntry {
    param([string]$Entry)

    try {
        $current = [Environment]::GetEnvironmentVariable("PATH", "User")
        if (-not $current) { return }

        $parts = $current.Split([System.IO.Path]::PathSeparator, [System.StringSplitOptions]::RemoveEmptyEntries) |
            Where-Object {
                $_.TrimEnd('\') -ne $Entry.TrimEnd('\') -and
                $_.TrimEnd('\') -notlike "*\.ijava2\bin"
            }

        $newPath = [string]::Join([System.IO.Path]::PathSeparator, $parts)
        [Environment]::SetEnvironmentVariable("PATH", $newPath, "User")
        Write-IjavaInfo "Supprimé du PATH utilisateur"
    }
    catch {
        Write-IjavaError "Erreur lors de la suppression du PATH : $_"
    }
}

function Test-JarExists {
    if (-not (Test-Path $Script:Config.JarPath)) {
        Write-IjavaInfo "Le fichier JAR du toolkit est manquant"
        Invoke-DownloadLatest
    }
}

# Point d'entrée principal
if ($args.Count -eq 0) {
    Test-JarExists
    & java -jar $Script:Config.JarPath
    exit $LASTEXITCODE
}

switch ($args[0].ToLowerInvariant()) {
    "update" {
        Invoke-DownloadLatest
    }
    "self-update" {
        Invoke-DownloadLatest
    }
    "--info" {
        Write-Host ""
        Write-Host "╔════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
        Write-Host "║                                                        ║" -ForegroundColor Cyan
        Write-Host "║          iJava Enhanced Wrapper v$($Script:Config.Version)               ║" -ForegroundColor Cyan
        Write-Host "║                  (Windows Edition)                     ║" -ForegroundColor Cyan
        Write-Host "║                                                        ║" -ForegroundColor Cyan
        Write-Host "╚════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Installation : " -NoNewline -ForegroundColor Yellow
        Write-Host $Script:Config.InstallDir -ForegroundColor White
        Write-Host "Fichier JAR  : " -NoNewline -ForegroundColor Yellow
        Write-Host $Script:Config.JarPath -ForegroundColor White
        Write-Host ""
        Write-Host "Commandes du wrapper :" -ForegroundColor Cyan
        Write-Host "  ijava update / self-update  " -NoNewline -ForegroundColor Green
        Write-Host "→ Met à jour le toolkit" -ForegroundColor Gray
        Write-Host "  ijava uninstall             " -NoNewline -ForegroundColor Green
        Write-Host "→ Désinstalle iJava" -ForegroundColor Gray
        Write-Host "  ijava --info                " -NoNewline -ForegroundColor Green
        Write-Host "→ Affiche ces informations" -ForegroundColor Gray
        Write-Host ""

        if (Test-Path $Script:Config.JarPath) {
            Write-Host "Informations du toolkit iJava :" -ForegroundColor Cyan
            Write-Host "────────────────────────────────────────────────────────" -ForegroundColor DarkGray
            & java -jar $Script:Config.JarPath --info 2>$null
            if ($LASTEXITCODE -ne 0) {
                & java -jar $Script:Config.JarPath help 2>$null
            }
        }
        else {
            Write-Host "⚠ Le fichier JAR du toolkit n'est pas installé." -ForegroundColor Yellow
        }
        Write-Host ""
        exit 0
    }
    "uninstall" {
        Write-Host ""
        Write-Host "╔════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
        Write-Host "║         Désinstallation d'iJava Enhanced              ║" -ForegroundColor Cyan
        Write-Host "╚════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
        Write-Host ""

        Write-IjavaInfo "Suppression des fichiers du wrapper..."
        if (Test-Path $Script:Config.JarPath) {
            Remove-Item -Path $Script:Config.JarPath -Force -ErrorAction SilentlyContinue
        }
        if (Test-Path $Script:Config.BinDir) {
            Remove-Item -Path "$($Script:Config.BinDir)\ijava" -Force -ErrorAction SilentlyContinue
            Remove-Item -Path "$($Script:Config.BinDir)\ijava.cmd" -Force -ErrorAction SilentlyContinue
            Remove-Item -Path "$($Script:Config.BinDir)\ijava.ps1" -Force -ErrorAction SilentlyContinue
        }

        Write-IjavaInfo "Nettoyage des profils PowerShell..."
        foreach ($profilePath in $Script:ProfileCandidates) {
            Remove-ProfileBlock -ProfilePath $profilePath -Start $Script:Config.PathMarkerStart -End $Script:Config.PathMarkerEnd
            Remove-ProfileBlock -ProfilePath $profilePath -Start $Script:Config.AliasMarkerStart -End $Script:Config.AliasMarkerEnd
        }

        Write-IjavaInfo "Suppression du PATH..."
        Remove-PathEntry -Entry $Script:Config.BinDir

        # Vérifier s'il reste des fichiers utilisateur dans .ijava2
        $ijava2Dir = "$env:USERPROFILE\.ijava2"
        if (Test-Path $ijava2Dir) {
            $remainingContent = Get-ChildItem -Path $ijava2Dir -ErrorAction SilentlyContinue
            if ($remainingContent) {
                Write-Host ""
                $response = Read-Host "? Supprimer le contenu restant de ~\.ijava2 (logs, TPs, exercices) ? [O/n]"
                if ($response -eq "" -or $response -match "^[yYoO]$") {
                    Write-IjavaInfo "Suppression du contenu utilisateur..."
                    Remove-Item -Path "$ijava2Dir\*" -Recurse -Force -ErrorAction SilentlyContinue
                    Write-Host "✓ Contenu utilisateur supprimé" -ForegroundColor Green
                    # Supprimer le répertoire s'il est vide
                    $remainingAfterCleanup = Get-ChildItem -Path $ijava2Dir -ErrorAction SilentlyContinue
                    if (-not $remainingAfterCleanup) {
                        Remove-Item -Path $ijava2Dir -Force -ErrorAction SilentlyContinue
                    }
                }
                else {
                    Write-Host "ℹ Contenu utilisateur conservé dans : $ijava2Dir" -ForegroundColor Cyan
                }
            }
        }

        Write-IjavaInfo "Suppression des fonctions de la session courante..."
        Remove-Item Function:\ijavai -ErrorAction SilentlyContinue
        Remove-Item Function:\ijavac -ErrorAction SilentlyContinue
        Remove-Item Function:\ijavat -ErrorAction SilentlyContinue
        Remove-Item Function:\ijavae -ErrorAction SilentlyContinue
        Remove-Item Function:\ijavas -ErrorAction SilentlyContinue

        Write-IjavaInfo "Suppression des répertoires vides..."
        if (Test-Path $Script:Config.BinDir) {
            $binDirContents = Get-ChildItem -Path $Script:Config.BinDir -ErrorAction SilentlyContinue
            if (-not $binDirContents) {
                Remove-Item -Path $Script:Config.BinDir -Force -ErrorAction SilentlyContinue
            }
        }
        if (Test-Path $Script:Config.InstallDir) {
            $installDirContents = Get-ChildItem -Path $Script:Config.InstallDir -ErrorAction SilentlyContinue
            if (-not $installDirContents) {
                Remove-Item -Path $Script:Config.InstallDir -Force -ErrorAction SilentlyContinue
            }
        }

        Write-Host ""
        Write-Host "✓ Désinstallation terminée avec succès !" -ForegroundColor Green
        Write-Host ""
        Write-Host "Veuillez FERMER et ROUVRIR PowerShell pour finaliser la suppression." -ForegroundColor Yellow
        Write-Host ""
        exit 0
    }
    default {
        Test-JarExists
        & java -jar $Script:Config.JarPath @args
        exit $LASTEXITCODE
    }
}
'@

    try {
        Set-Content -Path $Script:Config.PowerShellWrapper -Value $wrapperContent -Encoding UTF8
        Write-Success "Wrapper PowerShell créé"
        Write-Detail $Script:Config.PowerShellWrapper
    }
    catch {
        Write-Error "Impossible de créer le wrapper PowerShell : $_"
        return $false
    }

    # Crée le wrapper CMD pour compatibilité
    Write-Progress "Génération du wrapper CMD"

    $cmdContent = @"
@echo off
setlocal
set "SCRIPT=%USERPROFILE%\.ijava2\bin\ijava.ps1"
if not exist "%SCRIPT%" (
    echo [ijava] Lanceur introuvable. Reinstallez le toolkit.
    exit /b 1
)
pwsh.exe -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT%" %*
if errorlevel 1 (
    powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT%" %*
)
set "EXITCODE=%ERRORLEVEL%"
endlocal & exit /b %EXITCODE%
"@

    try {
        Set-Content -Path $Script:Config.CmdWrapper -Value $cmdContent -Encoding ASCII
        Write-Success "Wrapper CMD créé"
        Write-Detail $Script:Config.CmdWrapper
        return $true
    }
    catch {
        Write-Error "Impossible de créer le wrapper CMD : $_"
        return $false
    }
}

function Update-SystemPath {
    Write-Section "Mise à jour du PATH système"

    try {
        $currentUserPath = [Environment]::GetEnvironmentVariable("PATH", "User")

        if ([string]::IsNullOrWhiteSpace($currentUserPath)) {
            [Environment]::SetEnvironmentVariable("PATH", $Script:Config.BinDir, "User")
            Write-Success "PATH utilisateur initialisé"
            Write-Detail $Script:Config.BinDir
        }
        else {
            $parts = $currentUserPath.Split([System.IO.Path]::PathSeparator, [System.StringSplitOptions]::RemoveEmptyEntries)
            $hasBin = $parts | Where-Object { $_.TrimEnd('\') -ieq $Script:Config.BinDir.TrimEnd('\') }

            if ($hasBin) {
                Write-Info "Le PATH contient déjà le répertoire d'installation"
            }
            else {
                $updatedParts = $parts + $Script:Config.BinDir
                $newPath = [string]::Join([System.IO.Path]::PathSeparator, $updatedParts)
                [Environment]::SetEnvironmentVariable("PATH", $newPath, "User")
                Write-Success "PATH utilisateur mis à jour"
                Write-Detail "Redémarrez PowerShell pour charger le nouveau PATH"
            }
        }

        # Met à jour le PATH pour la session actuelle
        if (-not (($env:PATH -split [System.IO.Path]::PathSeparator) | Where-Object { $_.TrimEnd('\') -ieq $Script:Config.BinDir.TrimEnd('\') })) {
            $env:PATH = "$($Script:Config.BinDir)$([System.IO.Path]::PathSeparator)$env:PATH"
            Write-Detail "PATH mis à jour pour cette session"
        }

        return $true
    }
    catch {
        Write-Error "Impossible de mettre à jour le PATH : $_"
        return $false
    }
}

function Install-Aliases {
    Write-Section "Configuration des alias"

    $aliasBlock = @"
$($Script:Config.AliasMarkerStart)
# Alias pratiques pour iJava
function ijavai { ijava init @args }
function ijavac { ijava compile @args }
function ijavat { ijava test @args }
function ijavae { ijava execute @args }
function ijavas { ijava status @args }
$($Script:Config.AliasMarkerEnd)
"@

    $aliasesAdded = 0

    foreach ($profilePath in $Script:ProfileCandidates) {
        if (-not $profilePath) { continue }

        try {
            $directory = Split-Path $profilePath -Parent
            if ($directory -and -not (Test-Path $directory)) {
                New-Item -ItemType Directory -Path $directory -Force | Out-Null
            }

            if (-not (Test-Path $profilePath)) {
                New-Item -ItemType File -Path $profilePath -Force | Out-Null
                Write-Detail "Profil créé : $profilePath"
            }

            $content = Get-Content $profilePath -ErrorAction SilentlyContinue
            if ($content -contains $Script:Config.AliasMarkerStart) {
                Write-Detail "Alias déjà présents dans $(Split-Path $profilePath -Leaf)"
            }
            else {
                Add-Content -Path $profilePath -Value "`n$aliasBlock`n"
                Write-Success "Alias ajoutés à $(Split-Path $profilePath -Leaf)"
                $aliasesAdded++
            }
        }
        catch {
            Write-Warning "Impossible de modifier $profilePath : $_"
        }
    }

    # Crée les alias pour la session actuelle
    try {
        function global:ijavai { ijava init @args }
        function global:ijavac { ijava compile @args }
        function global:ijavat { ijava test @args }
        function global:ijavae { ijava execute @args }
        function global:ijavas { ijava status @args }
        Write-Detail "Alias disponibles dans cette session"
    }
    catch {
        Write-Warning "Impossible de créer les alias pour cette session"
    }

    if ($aliasesAdded -gt 0) {
        Write-Success "Configuration des alias terminée"
    }
    else {
        Write-Info "Configuration des alias déjà à jour"
    }

    return $true
}

function Test-Installation {
    Write-Section "Validation de l'installation"

    $allOk = $true

    # Vérifie le JAR
    if (Test-Path $Script:Config.JarPath) {
        $fileSize = (Get-Item $Script:Config.JarPath).Length / 1KB
        Write-Success "Toolkit iJava présent"
        Write-Detail "$([math]::Round($fileSize, 2)) KB - $($Script:Config.JarPath)"
    }
    else {
        Write-Error "Toolkit iJava manquant : $($Script:Config.JarPath)"
        $allOk = $false
    }

    # Vérifie les wrappers
    if (Test-Path $Script:Config.PowerShellWrapper) {
        Write-Success "Wrapper PowerShell présent"
        Write-Detail $Script:Config.PowerShellWrapper
    }
    else {
        Write-Error "Wrapper PowerShell manquant"
        $allOk = $false
    }

    if (Test-Path $Script:Config.CmdWrapper) {
        Write-Success "Wrapper CMD présent"
        Write-Detail $Script:Config.CmdWrapper
    }
    else {
        Write-Error "Wrapper CMD manquant"
        $allOk = $false
    }

    if ($allOk) {
        Write-Success "Validation réussie"
    }
    else {
        Write-Error "Validation échouée"
    }

    return $allOk
}

function Show-SuccessMessage {
    Write-Host ""
    Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Green
    Write-Host "║                                                                ║" -ForegroundColor Green
    Write-Host "║        🚀  Installation réussie avec succès !  🚀                ║" -ForegroundColor Green
    Write-Host "║                                                                ║" -ForegroundColor Green
    Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Green
    Write-Host ""
}

function Show-NextSteps {
    Write-Host ""
    Write-Host "Prochaines étapes :" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "1. " -NoNewline -ForegroundColor Yellow
    Write-Host "Rechargez PowerShell :"
    Write-Host "   Fermez et rouvrez votre fenêtre PowerShell" -ForegroundColor DarkGray
    Write-Host ""
    Write-Host "2. " -NoNewline -ForegroundColor Yellow
    Write-Host "Testez l'installation :"
    Write-Host "   ijava --info" -ForegroundColor Green
    Write-Host ""
    Write-Host "3. " -NoNewline -ForegroundColor Yellow
    Write-Host "Commandes disponibles :"
    Write-Host "   ijava " -NoNewline -ForegroundColor Cyan
    Write-Host "<commande>      " -NoNewline
    Write-Host "# Exécuter une commande iJava" -ForegroundColor DarkGray
    Write-Host "   ijava update" -NoNewline -ForegroundColor Cyan
    Write-Host "         # Mettre à jour le toolkit" -ForegroundColor DarkGray
    Write-Host "   ijava uninstall" -NoNewline -ForegroundColor Cyan
    Write-Host "      # Désinstaller iJava" -ForegroundColor DarkGray
    Write-Host ""
    Write-Host "4. " -NoNewline -ForegroundColor Yellow
    Write-Host "Alias pratiques disponibles :"
    Write-Host "   ijavai" -NoNewline -ForegroundColor Green
    Write-Host "  → ijava init" -ForegroundColor DarkGray
    Write-Host "   ijavac" -NoNewline -ForegroundColor Green
    Write-Host "  → ijava compile" -ForegroundColor DarkGray
    Write-Host "   ijavat" -NoNewline -ForegroundColor Green
    Write-Host "  → ijava test" -ForegroundColor DarkGray
    Write-Host "   ijavae" -NoNewline -ForegroundColor Green
    Write-Host "  → ijava execute" -ForegroundColor DarkGray
    Write-Host "   ijavas" -NoNewline -ForegroundColor Green
    Write-Host "  → ijava status" -ForegroundColor DarkGray
    Write-Host ""
    Write-Host "Installation dans : $($Script:Config.InstallDir)" -ForegroundColor DarkGray
    Write-Host ""
}

function Show-InstallationSummary {
    Write-Host ""
    Write-Host "Résumé de l'installation :" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "  Répertoire principal :" -ForegroundColor White
    Write-Host "  $($Script:Config.InstallDir)" -ForegroundColor DarkGray
    Write-Host ""
    Write-Host "  Toolkit iJava :" -ForegroundColor White
    Write-Host "  $($Script:Config.JarPath)" -ForegroundColor DarkGray
    Write-Host ""
    Write-Host "  Exécutables :" -ForegroundColor White
    Write-Host "  $($Script:Config.BinDir)" -ForegroundColor DarkGray
    Write-Host ""
}

# ==============================================================================
# FONCTION PRINCIPALE
# ==============================================================================

function Invoke-Installation {
    # Affiche la bannière
    Write-Banner -Version $Script:Config.Version

    # Vérifie les prérequis
    if (-not (Test-JavaInstalled)) {
        exit 1
    }

    # Avertissement si pas en admin (mais on continue)
    if (-not (Test-AdminRights)) {
        Write-Warning "Vous n'êtes pas en mode administrateur"
        Write-Info "L'installation continuera en mode utilisateur"
    }

    # Installation
    if (-not (Initialize-Directories)) { exit 1 }
    if (-not (Install-Toolkit)) { exit 1 }
    if (-not (Install-Wrapper)) { exit 1 }
    if (-not (Update-SystemPath)) { exit 1 }
    if (-not (Install-Aliases)) { exit 1 }

    # Validation
    if (-not (Test-Installation)) {
        Write-Error "L'installation n'a pas pu être validée"
        exit 1
    }

    # Messages finaux
    Show-SuccessMessage
    Show-InstallationSummary
    Show-NextSteps

    Write-Host "Merci d'avoir installé iJava Enhanced ! 🚀" -ForegroundColor Green
    Write-Host ""
}

# ==============================================================================
# POINT D'ENTRÉE
# ==============================================================================

# Gestion des interruptions
trap {
    Write-Host ""
    Write-Error "Installation interrompée"
    exit 130
}

# Exécution
Invoke-Installation
