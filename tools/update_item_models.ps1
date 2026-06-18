# 根据已下载贴图重写 models/item/*.json
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$modelDir = Join-Path $root "src\main\resources\assets\gunsrpg\models\item"

$simple = @(
    "gun_parts", "barrel", "magazine", "wooden_stock", "long_barrel", "small_iron_stock",
    "small_bullet_casing", "large_bullet_casing", "shotgun_shell", "bolt_fletching",
    "wooden_9mm", "wooden_45acp", "wooden_556mm", "wooden_762mm", "wooden_12g",
    "stone_9mm", "stone_45acp", "stone_556mm", "stone_762mm", "stone_12g",
    "iron_9mm", "iron_45acp", "iron_556mm", "iron_762mm", "iron_12g"
)
$firearms = @(
    "m1911", "r45", "desert_eagle", "thompson", "vector", "akm", "vss",
    "kar98k", "winchester", "s686", "minigun"
)

function Write-Model($id, $parent, $tex) {
    $json = @"
{
  "parent": "$parent",
  "textures": {
    "layer0": "$tex"
  }
}
"@
    [IO.File]::WriteAllText((Join-Path $modelDir "$id.json"), $json + "`n")
}

foreach ($id in $simple) {
    Write-Model $id "minecraft:item/generated" "gunsrpg:item/$id"
}
foreach ($id in $firearms) {
    $json = @"
{
  "parent": "gunsrpg:item/firearm_builtin"
}
"@
    [IO.File]::WriteAllText((Join-Path $modelDir "$id.json"), $json + "`n")
}
Write-Model "gunsmith_table" "minecraft:item/generated" "gunsrpg:block/gunsmith_table_icon"
Write-Host "Item models updated."
