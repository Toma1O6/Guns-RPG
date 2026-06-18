# 从 1.16.5 备份导入烹饪配方，并拉取食物贴图/模型
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$srcRecipes = "d:\minecraft\.minecraft\versions\1.20.1-Forge_47.4.20\backups\gunsrpg-1.16.5-1.1.0\data\gunsrpg\recipes\culinary"
$dstRecipes = Join-Path $root "src\main\resources\data\gunsrpg\recipes\culinary"
$modelDir = Join-Path $root "src\main\resources\assets\gunsrpg\models\item"
$texDir = Join-Path $root "src\main\resources\assets\gunsrpg\textures\item"
$baseUrl = "https://raw.githubusercontent.com/Toma1O6/Guns-RPG/1.16.5/src/main/resources/assets/gunsrpg"
New-Item -ItemType Directory -Force -Path $dstRecipes, $modelDir, $texDir | Out-Null

$foods = @(
    "bacon_burger", "fish_and_chips", "garden_soup", "chicken_dinner", "deluxe_meal",
    "meaty_stew_xxl", "rabbit_creamy_soup", "shepherds_pie", "fruit_salad", "egg_salad",
    "chocolate_glazed_apple_pie", "fried_egg", "fries", "chicken_nuggets", "schnitzel",
    "raw_doughnut", "doughnut", "sushi_maki"
)

function Get-Png($name) {
    $local = Join-Path $texDir "$name.png"
    if ((Test-Path $local) -and ((Get-Item $local).Length -gt 80)) { return }
    $url = "$baseUrl/textures/item/$name.png"
    Write-Host "GET $url"
    Invoke-WebRequest -Uri $url -OutFile $local -UseBasicParsing -TimeoutSec 120
}

function Write-Model($id) {
    $path = Join-Path $modelDir "$id.json"
    if (Test-Path $path) { return }
    @"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "gunsrpg:item/$id"
  }
}
"@ | Set-Content -Path $path -Encoding UTF8
}

foreach ($f in $foods) {
    Get-Png $f
    Write-Model $f
}

Copy-Item -Path (Join-Path $srcRecipes "*.json") -Destination $dstRecipes -Force
Get-ChildItem $dstRecipes -Filter *.json | ForEach-Object {
    $j = Get-Content $_.FullName -Raw | ConvertFrom-Json
    $skill = ""
    if ($j.skill) { $skill = ($j.skill -replace "^gunsrpg:", "") }
    elseif ($j.requirements) {
        foreach ($req in $j.requirements) {
            if ($req.type -eq "gunsrpg:skill" -and $req.predicate.skill) {
                $skill = ($req.predicate.skill -replace "^gunsrpg:", "")
            }
        }
    }
    $out = [ordered]@{
        type = "gunsrpg:culinary"
        pattern = $j.pattern
        key = $j.key
        result = $j.result
    }
    if ($skill) { $out.skill = $skill }
    ($out | ConvertTo-Json -Depth 10) + "`n" | Set-Content $_.FullName -Encoding UTF8
}

# 火炉中间品 → 原版熔炉
$smelt = @{
    fried_egg = @{ in = "minecraft:egg"; time = 200; exp = 0.35 }
    fries = @{ in = "minecraft:potato"; time = 200; exp = 0.35 }
    chicken_nuggets = @{ in = "minecraft:chicken"; time = 200; exp = 0.35 }
    schnitzel = @{ in = "minecraft:porkchop"; time = 200; exp = 0.35 }
    doughnut = @{ in = "gunsrpg:raw_doughnut"; time = 200; exp = 0.35 }
}
$smeltDir = Join-Path $root "src\main\resources\data\gunsrpg\recipes\smelting"
New-Item -ItemType Directory -Force -Path $smeltDir | Out-Null
foreach ($id in $smelt.Keys) {
    $s = $smelt[$id]
    @"
{
  "type": "minecraft:smelting",
  "ingredient": { "item": "$($s.in)" },
  "result": "gunsrpg:$id",
  "experience": $($s.exp),
  "cookingtime": $($s.time)
}
"@ | Set-Content (Join-Path $smeltDir "$id.json") -Encoding UTF8
}

Write-Host "Culinary import done."
