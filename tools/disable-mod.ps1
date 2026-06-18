# 从整合包 mods/ 暂时禁用 gunsrpg（保留 jar，改后缀 .disabled）
$ErrorActionPreference = "Stop"
$root = Split-Path (Split-Path $PSScriptRoot)
$mods = Join-Path $root "mods"
Get-ChildItem $mods -Filter "gunsrpg-*.jar" | ForEach-Object {
    $dest = $_.FullName + ".disabled"
    if (Test-Path $dest) { Remove-Item $dest -Force }
    Rename-Item $_.FullName $dest
    Write-Host "Disabled: $($_.Name) -> $($_.Name).disabled"
}
if (-not (Get-ChildItem $mods -Filter "gunsrpg-*.jar.disabled")) {
    Write-Host "No gunsrpg jar found in mods/ (already disabled?)"
}
