# 下载 TaCZ（Timeless and Classics Zero）1.20.1 Forge 到整合包 mods/
# 用法: .\tools\download-tacz.ps1
$ErrorActionPreference = 'Stop'
$packRoot = Split-Path $PSScriptRoot -Parent | Split-Path -Parent
$modsDir = Join-Path $packRoot 'mods'
if (-not (Test-Path $modsDir)) { New-Item -ItemType Directory -Path $modsDir | Out-Null }

$slug = 'timeless-and-classics-zero'
$api = "https://api.modrinth.com/v2/project/$slug/version?game_versions=%5B%221.20.1%22%5D&loaders=%5B%22forge%22%5D"
$versions = Invoke-RestMethod -Uri $api -Headers @{ 'User-Agent' = 'gunsrpg-pack/1.0' }
if (-not $versions -or $versions.Count -eq 0) {
  throw "Modrinth 未找到 $slug 的 1.20.1 Forge 版本"
}
$ver = $versions[0]
$file = $ver.files | Where-Object { $_.primary } | Select-Object -First 1
if (-not $file) { $file = $ver.files[0] }
$dest = Join-Path $modsDir $file.filename
Write-Host "下载 TaCZ $($ver.version_number) -> $dest"
Invoke-WebRequest -Uri $file.url -OutFile $dest -UseBasicParsing
Write-Host "Done. Check tacz_backend.json and disable_tacz_native_crafting.js"
