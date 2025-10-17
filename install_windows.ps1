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
    Write-Host "ERROR: $Message" -ForegroundColor Red
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

Write-Section "Checking Java runtime"
$javaCmd = Get-Command java -ErrorAction SilentlyContinue
if (-not $javaCmd) {
    Fail "Java runtime not found. Install from https://adoptium.net or https://www.oracle.com/java/technologies/downloads/"
}
$javaVersionOutput = & java -version 2>&1
$javaVersion = ($javaVersionOutput | Select-Object -First 1 | Out-String).Trim()
Write-Info "Java detected: $javaVersion"

Write-Section "Preparing install directories"
New-Item -ItemType Directory -Path $InstallDir -Force | Out-Null
New-Item -ItemType Directory -Path $BinDir -Force | Out-Null
Write-Info "Using install dir: $InstallDir"

Write-Section "Downloading ijava toolkit"
Invoke-WebRequest -Uri $JarUrl -OutFile $JarPath
Write-Info "Saved jar to $JarPath"

Write-Section "Creating launchers"
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
    Write-Info "Downloading latest toolkit..."
    Invoke-WebRequest -Uri $JarUrl -OutFile $JarPath
    Write-Info "Update complete."
}

function Remove-ProfileBlock {
    param([string]$ProfilePath,[string]$Start,[string]$End)
    if (-not (Test-Path $ProfilePath)) { return }
    $content = Get-Content $ProfilePath -ErrorAction SilentlyContinue
    if (-not $content) { return }
    $inside = $false
    $result = foreach ($line in $content) {
        if ($line -eq $Start) { $inside = $true; continue }
        if ($line -eq $End) { $inside = $false; continue }
        if (-not $inside) { $line }
    }
    $result | Set-Content -Path $ProfilePath -Encoding ASCII
}

function Remove-PathEntry {
    param([string]$Entry)
    $current = [Environment]::GetEnvironmentVariable("PATH","User")
    if (-not $current) { return }
    $parts = $current.Split([System.IO.Path]::PathSeparator, [System.StringSplitOptions]::RemoveEmptyEntries) | Where-Object { $_.TrimEnd('\\') -ne $Entry.TrimEnd('\\') }
    [Environment]::SetEnvironmentVariable("PATH", [string]::Join([System.IO.Path]::PathSeparator,$parts), "User")
}

function Ensure-Jar {
    if (-not (Test-Path $JarPath)) {
        Write-Info "Toolkit jar missing. Downloading now..."
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
    "uninstall" {
        Write-Info "Removing installed files..."
        if (Test-Path $JarPath) { Remove-Item $JarPath -Force }
        $cmdWrapper = Join-Path $BinDir "ijava.cmd"
        $psWrapper = Join-Path $BinDir "ijava.ps1"
        if (Test-Path $cmdWrapper) { Remove-Item $cmdWrapper -Force }
        if (Test-Path $psWrapper) { Remove-Item $psWrapper -Force }
        foreach ($profilePath in $ProfileCandidates) {
            Remove-ProfileBlock -ProfilePath $profilePath -Start $AliasMarkerStart -End $AliasMarkerEnd
        }
        Remove-PathEntry -Entry $BinDir
        if ((Test-Path $BinDir) -and -not (Get-ChildItem $BinDir -Force -ErrorAction SilentlyContinue)) { Remove-Item $BinDir -Force }
        if ((Test-Path $InstallDir) -and -not (Get-ChildItem $InstallDir -Force -ErrorAction SilentlyContinue)) { Remove-Item $InstallDir -Force }
        Write-Info "Uninstall complete. Restart your PowerShell session."
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
  echo [ijava] Launcher not found. Reinstall the toolkit.
  exit /b 1
)
pwsh.exe -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT%" %*
set "EXITCODE=%ERRORLEVEL%"
endlocal & exit /b %EXITCODE%
'@
Set-Content -Path $CmdWrapper -Value $cmdContent -Encoding ASCII
Write-Info "Created ijava.cmd and ijava.ps1 launchers"

Write-Section "Updating PATH"
$currentUserPath = [Environment]::GetEnvironmentVariable("PATH","User")
if ([string]::IsNullOrWhiteSpace($currentUserPath)) {
    [Environment]::SetEnvironmentVariable("PATH", $BinDir, "User")
    Write-Info "User PATH initialized with $BinDir"
} else {
    $parts = $currentUserPath.Split([System.IO.Path]::PathSeparator, [System.StringSplitOptions]::RemoveEmptyEntries)
    $hasBin = $parts | Where-Object { $_.TrimEnd('\\') -ieq $BinDir.TrimEnd('\\') }
    if ($hasBin) {
        Write-Info "User PATH already contains $BinDir"
    } else {
        $updatedParts = $parts + $BinDir
        $newPath = [string]::Join([System.IO.Path]::PathSeparator, $updatedParts)
        [Environment]::SetEnvironmentVariable("PATH", $newPath, "User")
        Write-Info "User PATH updated. Restart PowerShell to reload environment."
    }
}
if (-not ( ($env:PATH -split [System.IO.Path]::PathSeparator) | Where-Object { $_.TrimEnd('\\') -ieq $BinDir.TrimEnd('\\') } )) {
    $env:PATH = "$BinDir" + [System.IO.Path]::PathSeparator + $env:PATH
}

Write-Section "Configuring aliases"
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
        Write-Info "Created profile: $profilePath"
    }
    $content = Get-Content $profilePath -ErrorAction SilentlyContinue
    if ($content -contains $AliasMarkerStart) {
        Write-Info "Aliases already defined in $profilePath"
    } else {
        Add-Content -Path $profilePath -Value "`n$aliasBlock`n"
        Write-Info "Appended aliases to $profilePath"
    }
}

function ijavai { ijava init @args }
function ijavac { ijava compile @args }
function ijavat { ijava test @args }
function ijavae { ijava execute @args }
function ijavas { ijava status @args }

Write-Section "Done"
Write-Host "ijava is ready. Available commands:" -ForegroundColor Green
Write-Host "  - ijava <command>" -ForegroundColor Green
Write-Host "  - ijava update" -ForegroundColor Green
Write-Host "  - ijava uninstall" -ForegroundColor Green
Write-Host "Aliases: ijavai, ijavac, ijavat, ijavae, ijavas" -ForegroundColor Green
Write-Host "Open a new PowerShell window to load PATH and aliases." -ForegroundColor Green
