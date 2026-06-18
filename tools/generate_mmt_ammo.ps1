# 生成 MMT 弹药：物品模型、枪械台配方、下载贴图
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$baseUrl = "https://raw.githubusercontent.com/Toma1O6/Guns-RPG/1.16.5/src/main/resources/assets/gunsrpg"
$modelDir = Join-Path $root "src\main\resources\assets\gunsrpg\models\item"
$recipeDir = Join-Path $root "src\main\resources\data\gunsrpg\recipes\gunsmith"
$texDir = Join-Path $root "src\main\resources\assets\gunsrpg\textures\item"
New-Item -ItemType Directory -Force -Path $modelDir, $recipeDir, $texDir | Out-Null

$calibers = @("9mm", "45acp", "556mm", "762mm", "12g")
$materials = @(
    @{ prefix = "bronze"; skill = "iron_ammo_smith"; key = "b"; item = "mmt:bronze_ingot" },
    @{ prefix = "brass"; skill = "iron_ammo_smith"; key = "b"; item = "mmt:brass_ingot" },
    @{ prefix = "invar"; skill = "lapis_ammo_smith"; key = "b"; item = "mmt:invar_ingot" },
    @{ prefix = "steel"; skill = "gold_ammo_smith"; key = "b"; item = "mmt:steel_ingot" },
    @{ prefix = "lapis"; skill = "lapis_ammo_smith"; key = "b"; item = "minecraft:lapis_lazuli" },
    @{ prefix = "gold"; skill = "gold_ammo_smith"; key = "o"; item = "minecraft:gold_ingot" },
    @{ prefix = "redstone"; skill = "redstone_ammo_smith"; key = "r"; item = "minecraft:redstone" },
    @{ prefix = "emerald"; skill = "emerald_ammo_smith"; key = "m"; item = "minecraft:emerald" },
    @{ prefix = "quartz"; skill = "quartz_ammo_smith"; key = "q"; item = "minecraft:quartz" },
    @{ prefix = "diamond"; skill = "diamond_ammo_smith"; key = "d"; item = "minecraft:diamond" },
    @{ prefix = "netherite"; skill = "netherite_ammo_smith"; key = "n"; item = "minecraft:netherite_ingot" }
)

function Get-Png($name) {
    $local = Join-Path $texDir "$name.png"
    if ((Test-Path $local) -and ((Get-Item $local).Length -gt 80)) { return }
    $url = "$baseUrl/textures/item/$name.png"
    Write-Host "GET $url"
    try {
        Invoke-WebRequest -Uri $url -OutFile $local -UseBasicParsing -TimeoutSec 60
        if ((Get-Item $local).Length -gt 80) { return }
    } catch {}
    $cal = if ($name -match "_([^_]+)$") { $Matches[1] } else { "9mm" }
    $fallback = Join-Path $texDir "iron_$cal.png"
    if (Test-Path $fallback) {
        Copy-Item $fallback $local -Force
        Write-Host "fallback $local <- iron_$cal"
    }
}

function Write-Model($id) {
    $json = @"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "gunsrpg:item/$id"
  }
}
"@
    [IO.File]::WriteAllText((Join-Path $modelDir "$id.json"), $json)
}

function Write-PistolRecipe($prefix, $skill, $matKey, $ingredient, $caliber) {
    $id = "${prefix}_$caliber"
    $json = @"
{
    "type":  "gunsrpg:gunsmith",
    "pattern":  [
                    "$matKey",
                    "g",
                    "s"
                ],
    "key":  {
                "g":  { "item":  "minecraft:gunpowder" },
                "s":  { "item":  "minecraft:paper" },
                "$matKey":  { "item":  "$ingredient" }
            },
    "result":  { "count": 1, "item": "gunsrpg:$id" },
    "skill":  "$skill"
}
"@
    [IO.File]::WriteAllText((Join-Path $recipeDir "$id.json"), $json)
}

function Write-RifleRecipe($prefix, $skill, $matKey, $ingredient, $caliber) {
    $id = "${prefix}_$caliber"
    $json = @"
{
    "type":  "gunsrpg:gunsmith",
    "pattern":  [
                    "$matKey ",
                    "gg",
                    "p "
                ],
    "key":  {
                "g":  { "item":  "minecraft:gunpowder" },
                "p":  { "item":  "minecraft:paper" },
                "$matKey":  { "item":  "$ingredient" }
            },
    "result":  { "count": 1, "item": "gunsrpg:$id" },
    "skill":  "$skill"
}
"@
    [IO.File]::WriteAllText((Join-Path $recipeDir "$id.json"), $json)
}

function Write-ShotgunRecipe($prefix, $skill, $matKey, $ingredient) {
    $id = "${prefix}_12g"
    $json = @"
{
    "type":  "gunsrpg:gunsmith",
    "pattern":  [
                    "$matKey  ",
                    "ggg",
                    "s  "
                ],
    "key":  {
                "g":  { "item":  "minecraft:gunpowder" },
                "s":  { "item":  "gunsrpg:shotgun_shell" },
                "$matKey":  { "item":  "$ingredient" }
            },
    "result":  { "count": 1, "item": "gunsrpg:$id" },
    "skill":  "$skill"
}
"@
    [IO.File]::WriteAllText((Join-Path $recipeDir "$id.json"), $json)
}

foreach ($mat in $materials) {
    foreach ($cal in $calibers) {
        $id = "$($mat.prefix)_$cal"
        Get-Png $id
        Write-Model $id
        if ($cal -eq "12g") {
            Write-ShotgunRecipe $mat.prefix $mat.skill $mat.key $mat.item
        } elseif ($cal -eq "556mm" -or $cal -eq "762mm") {
            Write-RifleRecipe $mat.prefix $mat.skill $mat.key $mat.item $cal
        } else {
            Write-PistolRecipe $mat.prefix $mat.skill $mat.key $mat.item $cal
        }
        Write-Host "generated $id"
    }
}

Write-Host "MMT ammo content generated."
