$ErrorActionPreference = "Stop"
$packRoot = Split-Path (Split-Path $PSScriptRoot)
$jar = Get-ChildItem (Join-Path $PSScriptRoot "..\build\libs\gunsrpg-*.jar") | Sort-Object LastWriteTime -Descending | Select-Object -First 1
if (-not $jar) { throw "Run gradlew build first." }

$targets = @(
    (Join-Path $packRoot "mods"),
    (Join-Path (Split-Path $packRoot) "mods")
)

foreach ($mods in $targets) {
    if (-not (Test-Path $mods)) {
        New-Item -ItemType Directory -Path $mods -Force | Out-Null
    }
    Copy-Item $jar.FullName (Join-Path $mods $jar.Name) -Force
    $legacy = Join-Path $mods "wf_firearms-0.1.0-alpha.jar"
    if (Test-Path $legacy) {
        Rename-Item $legacy "wf_firearms-0.1.0-alpha.jar.disabled" -Force
    }
    Write-Host "Deployed $($jar.Name) -> $mods"
}
