# 恢复 disable-other-gun-mods.ps1 禁用的 jar（不含 gunsrpg/CGM）
$ErrorActionPreference = "Stop"
$mods = Join-Path (Split-Path (Split-Path $PSScriptRoot)) "mods"
$RestorePatterns = @(
    "greenboys_legendary_guns",
    "chromaticarsenal",
    "wf_firearms"
)

Get-ChildItem $mods -Filter "*.jar.disabled" | ForEach-Object {
    $base = $_.Name -replace '\.disabled$',''
    $lower = $base.ToLowerInvariant()
    $match = $false
    foreach ($p in $RestorePatterns) {
        if ($lower.Contains($p.ToLowerInvariant())) { $match = $true; break }
    }
    if (-not $match) { return }
    $dest = Join-Path $mods $base
    if (Test-Path $dest) { Remove-Item $dest -Force }
    Rename-Item $_.FullName $dest
    Write-Host "Enabled: $base"
}
