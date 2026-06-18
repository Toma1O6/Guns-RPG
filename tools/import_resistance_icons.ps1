# 从 Guns-RPG 1.16.5 拉取抗性/医疗技能树图标
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$iconDir = Join-Path $root "src\main\resources\assets\gunsrpg\textures\icons"
$baseUrl = "https://raw.githubusercontent.com/Toma1O6/Guns-RPG/1.16.5/src/main/resources/assets/gunsrpg/textures/icons"
New-Item -ItemType Directory -Force -Path $iconDir | Out-Null

$icons = @(
    "bleeding_resistance_i", "bleeding_resistance_ii", "bleeding_resistance_iii",
    "fracture_resistance_i", "fracture_resistance_ii", "fracture_resistance_iii",
    "poison_resistance_i", "poison_resistance_ii", "poison_resistance_iii",
    "infection_resistance_i", "infection_resistance_ii", "infection_resistance_iii",
    "acrobatics_i", "acrobatics_ii", "acrobatics_iii",
    "medic", "doctor", "medical_station",
    "pharmacist_i", "pharmacist_ii", "pharmacist_iii", "pharmacist_iv", "pharmacist_v",
    "efficient_meds",
    "bleed", "fracture", "poison", "infection"
)

foreach ($name in $icons) {
    $local = Join-Path $iconDir "$name.png"
    if ((Test-Path $local) -and ((Get-Item $local).Length -gt 200)) { continue }
    $url = "$baseUrl/$name.png"
    Write-Host "GET $url"
    try {
        Invoke-WebRequest -Uri $url -OutFile $local -UseBasicParsing -TimeoutSec 60
    } catch {
        Write-Warning "Skip $name"
    }
}
Write-Host "Resistance/medical skill icons done."
