[CmdletBinding()]
param()

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Write-Section {
    param([string]$Message)
    Write-Host "==> $Message" -ForegroundColor Cyan
}

function Write-Info {
    param([string]$Message)
    Write-Host "    $Message"
}

function Fail {
    param([string]$Message)
    Write-Host "ERREUR: $Message" -ForegroundColor Red
    exit 1
}

$JarUrl = "https://www.iut-info.univ-lille.fr/~yann.secq/ijava/ijava.jar"
$InstallDir = Join-Path $env:USERPROFILE ".ijava"
$BinDir = Join-Path $InstallDir "bin"
$JarPath = Join-Path $InstallDir "ijava.jar"
$PowerShellWrapper = Join-Path $BinDir "ijava.ps1"
$CmdWrapper = Join-Path $BinDir "ijava.cmd"
$AliasMarkerStart = "# >>> ijava aliases >>>"
$AliasMarkerEnd = "# <<< ijava aliases <<<"
$ProfileCandidates = @(
    $PROFILE,
    (Join-Path $env:USERPROFILE "Documents\\PowerShell\\Microsoft.PowerShell_profile.ps1"),
    (Join-Path $env:USERPROFILE "Documents\\WindowsPowerShell\\Microsoft.PowerShell_profile.ps1")
) | Where-Object { $_ } | Sort-Object -Unique

Write-Section "Vérification de Java"
$javaCmd = Get-Command java -ErrorAction SilentlyContinue
if (-not $javaCmd) {
    Fail "Java n'est pas installé. Téléchargez-le depuis https://adoptium.net ou https://www.oracle.com/java/technologies/downloads/"
}
$javaVersionOutput = & java -version 2>&1
$javaVersion = ($javaVersionOutput | Select-Object -First 1 | Out-String).Trim()
Write-Info "Java détecté: $javaVersion"

Write-Section "Préparation des répertoires d'installation"
New-Item -ItemType Directory -Path $InstallDir -Force | Out-Null
New-Item -ItemType Directory -Path $BinDir -Force | Out-Null
Write-Info "Répertoire d'installation: $InstallDir"

Write-Section "Téléchargement du toolkit iJava"
Invoke-WebRequest -Uri $JarUrl -OutFile $JarPath
Write-Info "Fichier JAR sauvegardé dans $JarPath"

Write-Section "Création des lanceurs"
$wrapperContent = @'
Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$InstallDir = Join-Path $env:USERPROFILE ".ijava"
$BinDir = Join-Path $InstallDir "bin"
$JarPath = Join-Path $InstallDir "ijava.jar"
$JarUrl = "https://www.iut-info.univ-lille.fr/~yann.secq/ijava/ijava.jar"
$AliasMarkerStart = "# >>> ijava aliases >>>"
$AliasMarkerEnd = "# <<< ijava aliases <<<"
$ProfileCandidates = @(
    (Join-Path $env:USERPROFILE "Documents\\PowerShell\\Microsoft.PowerShell_profile.ps1"),
    (Join-Path $env:USERPROFILE "Documents\\WindowsPowerShell\\Microsoft.PowerShell_profile.ps1")
) | Where-Object { $_ } | Sort-Object -Unique

function Write-Info {
    param([string]$Message)
    Write-Host "[ijava] $Message"
}

function Download-Jar {
    Write-Info "Telechargement de la derniere version du toolkit..."
    Invoke-WebRequest -Uri $JarUrl -OutFile $JarPath
    Write-Info "Mise a jour terminee."
}

function Remove-ProfileBlock {
    param([string]$ProfilePath,[string]$Start,[string]$End)
    if (-not (Test-Path $ProfilePath)) { return }
    $content = Get-Content $ProfilePath -Raw -ErrorAction SilentlyContinue
    if (-not $content) { return }
    
    # Utiliser regex pour supprimer le bloc
    $pattern = [regex]::Escape($Start) + '.*?' + [regex]::Escape($End)
    $newContent = $content -replace "(?s)$pattern", ''
    # Nettoyer les lignes vides multiples
    $newContent = $newContent -replace '(\r?\n){3,}', "`r`n`r`n"
    $newContent = $newContent.Trim()
    
    if ($newContent) {
        Set-Content -Path $ProfilePath -Value $newContent -NoNewline
    } else {
        # Si le profil est complètement vide après nettoyage, le supprimer
        Remove-Item -Path $ProfilePath -Force -ErrorAction SilentlyContinue
    }
}

function Remove-PathEntry {
    param([string]$Entry)
    $current = [Environment]::GetEnvironmentVariable("PATH","User")
    if (-not $current) { return }
    $parts = $current.Split([System.IO.Path]::PathSeparator, [System.StringSplitOptions]::RemoveEmptyEntries) | Where-Object { 
        $_.TrimEnd('\\') -ne $Entry.TrimEnd('\\') -and 
        $_.TrimEnd('\\') -notlike "*\.ijava\bin"
    }
    [Environment]::SetEnvironmentVariable("PATH", [string]::Join([System.IO.Path]::PathSeparator,$parts), "User")
    Write-Info "Supprime du PATH"
}

function Ensure-Jar {
    if (-not (Test-Path $JarPath)) {
        Write-Info "Le fichier JAR du toolkit est manquant. Telechargement en cours..."
        Download-Jar
    }
}

if ($args.Count -eq 0) {
    Ensure-Jar
    & java -jar $JarPath
    exit $LASTEXITCODE
}

switch ($args[0].ToLowerInvariant()) {
    "update" {
        Download-Jar
    }
    "self-update" {
        Download-Jar
    }
    "--info" {
        Write-Host ""
        Write-Host "================================================" -ForegroundColor Cyan
        Write-Host "     iJava Enhanced Wrapper v1.0.0" -ForegroundColor Cyan
        Write-Host "================================================" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Installation : " -NoNewline -ForegroundColor Yellow
        Write-Host "$InstallDir" -ForegroundColor White
        Write-Host "Fichier JAR  : " -NoNewline -ForegroundColor Yellow
        Write-Host "$JarPath" -ForegroundColor White
        Write-Host ""
        Write-Host "Commandes du wrapper :" -ForegroundColor Cyan
        Write-Host "  - ijava update / self-update  " -NoNewline -ForegroundColor Green
        Write-Host "-> Met a jour le toolkit iJava" -ForegroundColor Gray
        Write-Host "  - ijava uninstall             " -NoNewline -ForegroundColor Green
        Write-Host "-> Desinstalle iJava du systeme" -ForegroundColor Gray
        Write-Host "  - ijava --info                " -NoNewline -ForegroundColor Green
        Write-Host "-> Affiche ces informations" -ForegroundColor Gray
        Write-Host ""
        if (Test-Path $JarPath) {
            Write-Host "Informations du toolkit iJava :" -ForegroundColor Cyan
            Write-Host "------------------------------------------------" -ForegroundColor DarkGray
            & java -jar $JarPath --info 2>$null
            if ($LASTEXITCODE -ne 0) {
                & java -jar $JarPath help 2>$null
            }
        } else {
            Write-Host "ATTENTION: Le fichier JAR du toolkit n'est pas installe." -ForegroundColor Red
        }
        Write-Host ""
        exit 0
    }
    "uninstall" {
        Write-Info "Desinstallation d'iJava..."
        Write-Info ""
        
        # Supprimer les profils PowerShell
        Write-Info "Nettoyage des profils PowerShell..."
        foreach ($profilePath in $ProfileCandidates) {
            Remove-ProfileBlock -ProfilePath $profilePath -Start $AliasMarkerStart -End $AliasMarkerEnd
        }
        
        # Nettoyer le PATH
        Write-Info "Suppression du PATH..."
        Remove-PathEntry -Entry $BinDir
        
        # Supprimer les fichiers
        Write-Info "Suppression des fichiers d'installation..."
        if (Test-Path $InstallDir) { 
            Remove-Item -Path $InstallDir -Recurse -Force -ErrorAction SilentlyContinue
            Write-Info "Supprime: $InstallDir"
        }
        
        # Supprimer les fonctions de la session courante
        Remove-Item Function:\ijavai -ErrorAction SilentlyContinue
        Remove-Item Function:\ijavac -ErrorAction SilentlyContinue
        Remove-Item Function:\ijavat -ErrorAction SilentlyContinue
        Remove-Item Function:\ijavae -ErrorAction SilentlyContinue
        Remove-Item Function:\ijavas -ErrorAction SilentlyContinue
        
        Write-Info ""
        Write-Info "Desinstallation terminee !"
        Write-Info "Veuillez FERMER et ROUVRIR PowerShell pour finaliser la suppression."
        Write-Info "La commande 'ijava' ne sera plus disponible."
        exit 0
    }
    default {
        Ensure-Jar
        & java -jar $JarPath @args
        exit $LASTEXITCODE
    }
}
'@
Set-Content -Path $PowerShellWrapper -Value $wrapperContent -Encoding ASCII

$cmdContent = @'
@echo off
setlocal
set "SCRIPT=%USERPROFILE%\.ijava\bin\ijava.ps1"
if not exist "%SCRIPT%" (
  echo [ijava] Lanceur introuvable. Réinstallez le toolkit.
  exit /b 1
)
pwsh.exe -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT%" %*
set "EXITCODE=%ERRORLEVEL%"
endlocal & exit /b %EXITCODE%
'@
Set-Content -Path $CmdWrapper -Value $cmdContent -Encoding ASCII
Write-Info "Lanceurs ijava.cmd et ijava.ps1 créés"

Write-Section "Mise à jour du PATH"
$currentUserPath = [Environment]::GetEnvironmentVariable("PATH","User")
if ([string]::IsNullOrWhiteSpace($currentUserPath)) {
    [Environment]::SetEnvironmentVariable("PATH", $BinDir, "User")
    Write-Info "PATH utilisateur initialisé avec $BinDir"
} else {
    $parts = $currentUserPath.Split([System.IO.Path]::PathSeparator, [System.StringSplitOptions]::RemoveEmptyEntries)
    $hasBin = $parts | Where-Object { $_.TrimEnd('\\') -ieq $BinDir.TrimEnd('\\') }
    if ($hasBin) {
        Write-Info "Le PATH utilisateur contient déjà $BinDir"
    } else {
        $updatedParts = $parts + $BinDir
        $newPath = [string]::Join([System.IO.Path]::PathSeparator, $updatedParts)
        [Environment]::SetEnvironmentVariable("PATH", $newPath, "User")
        Write-Info "PATH utilisateur mis à jour. Redémarrez PowerShell pour recharger l'environnement."
    }
}
if (-not ( ($env:PATH -split [System.IO.Path]::PathSeparator) | Where-Object { $_.TrimEnd('\\') -ieq $BinDir.TrimEnd('\\') } )) {
    $env:PATH = "$BinDir" + [System.IO.Path]::PathSeparator + $env:PATH
}

Write-Section "Configuration des alias"
$aliasBlock = @"
$AliasMarkerStart
function ijavai { ijava init @args }
function ijavac { ijava compile @args }
function ijavat { ijava test @args }
function ijavae { ijava execute @args }
function ijavas { ijava status @args }
$AliasMarkerEnd
"@
foreach ($profilePath in $ProfileCandidates) {
    if (-not $profilePath) { continue }
    $directory = Split-Path $profilePath -Parent
    if ($directory -and -not (Test-Path $directory)) {
        New-Item -ItemType Directory -Path $directory -Force | Out-Null
    }
    if (-not (Test-Path $profilePath)) {
        New-Item -ItemType File -Path $profilePath -Force | Out-Null
        Write-Info "Profil créé: $profilePath"
    }
    $content = Get-Content $profilePath -ErrorAction SilentlyContinue
    if ($content -contains $AliasMarkerStart) {
        Write-Info "Alias déjà définis dans $profilePath"
    } else {
        Add-Content -Path $profilePath -Value "`n$aliasBlock`n"
        Write-Info "Alias ajoutés à $profilePath"
    }
}

function ijavai { ijava init @args }
function ijavac { ijava compile @args }
function ijavat { ijava test @args }
function ijavae { ijava execute @args }
function ijavas { ijava status @args }

Write-Section "Terminé"
Write-Host "ijava est prêt. Commandes disponibles:" -ForegroundColor Green
Write-Host "  - ijava <commande>" -ForegroundColor Green
Write-Host "  - ijava update" -ForegroundColor Green
Write-Host "  - ijava uninstall" -ForegroundColor Green
Write-Host "Alias: ijavai, ijavac, ijavat, ijavae, ijavas" -ForegroundColor Green
Write-Host "Ouvrez une nouvelle fenêtre PowerShell pour charger le PATH et les alias." -ForegroundColor Green
