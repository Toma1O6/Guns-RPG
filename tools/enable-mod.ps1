# 恢复 gunsrpg：构建并部署，或仅启用已存在的 .disabled jar
$ErrorActionPreference = "Stop"
$root = Split-Path (Split-Path $PSScriptRoot)
$mods = Join-Path $root "mods"
$disabled = Get-ChildItem $mods -Filter "gunsrpg-*.jar.disabled" | Sort-Object LastWriteTime -Descending | Select-Object -First 1
if ($disabled) {
    $dest = Join-Path $mods ($disabled.Name -replace '\.disabled$','')
    if (Test-Path $dest) { Remove-Item $dest -Force }
    Rename-Item $disabled.FullName $dest
    Write-Host "Enabled: $($disabled.Name)"
} else {
    & (Join-Path $PSScriptRoot "deploy.ps1")
}
