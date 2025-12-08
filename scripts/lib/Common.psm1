# ==============================================================================
# iJava Enhanced - Module PowerShell commun
# ==============================================================================

$OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$script:IJAVA_VERSION = "1.0.0"
$script:JAR_URL = "https://www.iut-info.univ-lille.fr/~yann.secq/ijava/ijava.jar"
$script:DEFAULT_INSTALL_DIR = "$env:USERPROFILE\.ijava2"

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
        $tempFile = [System.IO.Path]::GetTempFileName()
        $process = Start-Process -FilePath "java" -ArgumentList "-version" -RedirectStandardError $tempFile -NoNewWindow -PassThru -Wait
        
        $javaVersion = Get-Content $tempFile -ErrorAction SilentlyContinue | Select-Object -First 1 | Out-String
        Remove-Item $tempFile -ErrorAction SilentlyContinue
        
        if ([string]::IsNullOrWhiteSpace($javaVersion)) {
             $javaVersion = "Version inconnue"
        }

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
