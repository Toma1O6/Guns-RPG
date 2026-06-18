# Guns RPG 技能树/物品 2D 图标 → gunsrpg/textures/icons
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$baseUrl = "https://raw.githubusercontent.com/Toma1O6/Guns-RPG/1.16.5/src/main/resources/assets/gunsrpg"
$iconDir = Join-Path $root "src\main\resources\assets\gunsrpg\textures\icons"
New-Item -ItemType Directory -Force -Path $iconDir | Out-Null

$icons = @(
    "m1911", "r45", "desert_eagle", "thompson", "vector", "akm", "vss", "kar98k",
    "winchester", "awm", "s686", "s1897", "minigun", "hk416", "aug", "ump45",
    "mk14ebr", "sks", "chukonu", "crossbow", "unknown"
)

function Get-Png($name) {
    $local = Join-Path $iconDir "$name.png"
    if ((Test-Path $local) -and ((Get-Item $local).Length -gt 50)) { return }
    $url = "$baseUrl/textures/icons/$name.png"
    Write-Host "GET $url"
    Invoke-WebRequest -Uri $url -OutFile $local -UseBasicParsing -TimeoutSec 60
}

foreach ($name in $icons) {
    try { Get-Png $name } catch { Write-Host "  skip $name" }
}
Write-Host "GunsRPG skill icons copied."
