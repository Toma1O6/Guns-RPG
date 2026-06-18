# 从 TACZ 源码树复制 AK47 参考文件（json/lua，不含贴图）到 gunsrpg reference 目录
$ErrorActionPreference = "Stop"
$packRoot = Split-Path (Split-Path $PSScriptRoot)
$taczRoot = Join-Path $packRoot "code\TACZ-1.20.1\src\main\resources\assets\tacz\custom\tacz_default_gun"
$dest = Join-Path $PSScriptRoot "..\reference\tacz\ak47"
if (-not (Test-Path $taczRoot)) { throw "TACZ source not found: $taczRoot" }
if (Test-Path $dest) { Remove-Item $dest -Recurse -Force }
New-Item -ItemType Directory -Force -Path $dest | Out-Null

$files = @(
    "data\tacz\index\guns\ak47.json",
    "data\tacz\data\guns\ak47_data.json",
    "data\tacz\recipes\guns\ak47.json",
    "assets\tacz\display\guns\ak47_display.json",
    "assets\tacz\geo_models\gun\ak47_geo.json",
    "assets\tacz\geo_models\gun\lod\ak47.json",
    "assets\tacz\animations\ak47.animation.json",
    "assets\tacz\scripts\ak47_state_machine.lua"
)
foreach ($f in $files) {
    $src = Join-Path $taczRoot $f
    if (-not (Test-Path $src)) { Write-Warning "Missing: $f"; continue }
    $out = Join-Path $dest $f
    $dir = Split-Path $out -Parent
    New-Item -ItemType Directory -Force -Path $dir | Out-Null
    Copy-Item $src $out -Force
    Write-Host "Copied $f"
}
@"
TaCZ AK47 参考副本（只读，不打进 mod jar）
来源: code/TACZ-1.20.1/.../tacz_default_gun/
贴图需从 TaCZ 发布 jar 解压: assets/tacz/textures/gun/...
许可: CC BY-NC-ND 4.0 — 勿改图再分发
"@ | Set-Content (Join-Path $dest "README.txt") -Encoding UTF8
Write-Host "Reference -> $dest"
