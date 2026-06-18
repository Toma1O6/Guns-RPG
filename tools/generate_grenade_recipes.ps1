# 按 config/gunsrpg/grenade_materials.json 与内置铁/金生成枪械台手雷配方
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$recipeDir = Join-Path $root "src\main\resources\data\gunsrpg\recipes\gunsmith"
New-Item -ItemType Directory -Force -Path $recipeDir | Out-Null

$materials = @(
    @{ id = "iron"; ingot = "minecraft:iron_ingot"; nugget = "minecraft:iron_nugget" },
    @{ id = "gold"; ingot = "minecraft:gold_ingot"; nugget = "minecraft:gold_nugget" }
)

function Write-Standard($mat) {
    $id = "grenade_$($mat.id)"
    $top = if ($mat.id -eq "gold") { "o" } else { "i" }
    @"
{
  "type": "gunsrpg:gunsmith",
  "pattern": [
    " $top ",
    "ptp",
    "nfn"
  ],
  "key": {
    "$top": { "item": "$($mat.ingot)" },
    "p": { "item": "$($mat.ingot)" },
    "t": { "item": "minecraft:tnt" },
    "n": { "item": "$($mat.nugget)" },
    "f": { "item": "minecraft:flint_and_steel" }
  },
  "result": { "item": "gunsrpg:$id", "count": 1 },
  "skill": "grenades"
}
"@ | Set-Content -Encoding UTF8 (Join-Path $recipeDir "$id.json")
}

function Write-Fragmentation($mat) {
    $base = "grenade_$($mat.id)"
    $id = "fragmentation_grenade_$($mat.id)"
    @"
{
  "type": "gunsrpg:gunsmith",
  "pattern": [
    "ppp",
    "pgp",
    "ppp"
  ],
  "key": {
    "p": { "item": "$($mat.ingot)" },
    "g": { "item": "gunsrpg:$base" }
  },
  "result": { "item": "gunsrpg:$id", "count": 1 },
  "skill": "impact_grenades"
}
"@ | Set-Content -Encoding UTF8 (Join-Path $recipeDir "$id.json")
}

function Write-Heavy($mat) {
    $base = "grenade_$($mat.id)"
    $id = "heavy_grenade_$($mat.id)"
    @"
{
  "type": "gunsrpg:gunsmith",
  "pattern": [
    "p t p",
    "t g t",
    "p t p"
  ],
  "key": {
    "p": { "item": "$($mat.ingot)" },
    "t": { "item": "minecraft:tnt" },
    "g": { "item": "gunsrpg:$base" }
  },
  "result": { "item": "gunsrpg:$id", "count": 1 },
  "skill": "massive_grenades"
}
"@ | Set-Content -Encoding UTF8 (Join-Path $recipeDir "$id.json")
}

foreach ($m in $materials) {
    Write-Standard $m
    Write-Fragmentation $m
    Write-Heavy $m
}
Write-Host "Grenade gunsmith recipes generated."
