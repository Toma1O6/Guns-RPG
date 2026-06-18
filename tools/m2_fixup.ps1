$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$recipeDir = Join-Path $root "src\main\resources\data\gunsrpg\recipes\gunsmith"
$modelDir = Join-Path $root "src\main\resources\assets\gunsrpg\models\item"

# 配方：cgm 占位 → wf 物品；按文件名修正 result
$cgmMap = @{
    "cgm:assault_rifle" = "gunsrpg:akm"
    "cgm:pistol" = "gunsrpg:m1911"
    "cgm:shotgun" = "gunsrpg:s686"
    "cgm:rifle" = "gunsrpg:kar98k"
    "cgm:heavy_rifle" = "gunsrpg:winchester"
    "cgm:machine_pistol" = "gunsrpg:thompson"
    "cgm:basic_bullet" = "gunsrpg:wooden_9mm"
    "cgm:advanced_bullet" = "gunsrpg:wooden_556mm"
    "cgm:shell" = "gunsrpg:shotgun_shell"
}

$gunResults = @{
    "akm.json" = "gunsrpg:akm"
    "desert_eagle.json" = "gunsrpg:desert_eagle"
    "r45.json" = "gunsrpg:r45"
    "thompson.json" = "gunsrpg:thompson"
    "vector.json" = "gunsrpg:vector"
    "kar98k.json" = "gunsrpg:kar98k"
    "vss.json" = "gunsrpg:vss"
    "winchester.json" = "gunsrpg:winchester"
    "s686.json" = "gunsrpg:s686"
}

$ammoResults = @(
    "wooden_9mm","wooden_45acp","wooden_556mm","wooden_762mm","wooden_12g",
    "stone_9mm","stone_45acp","stone_556mm","stone_762mm","stone_12g",
    "iron_9mm","iron_45acp","iron_556mm","iron_762mm","iron_12g",
    "shotgun_shell","gun_parts","barrel","magazine","wooden_stock",
    "long_barrel","small_iron_stock","small_bullet_casing","large_bullet_casing","bolt_fletching"
)

foreach ($file in Get-ChildItem $recipeDir -Filter "*.json") {
    $text = [IO.File]::ReadAllText($file.FullName)
    foreach ($k in $cgmMap.Keys) {
        $text = $text.Replace($k, $cgmMap[$k])
    }
    $name = $file.Name
    if ($gunResults.ContainsKey($name)) {
        $id = $gunResults[$name]
        $text = $text -replace '"result"\s*:\s*\{[^}]*"item"\s*:\s*"[^"]+"', "`"result`": { `"count`": 1, `"item`": `"$id`""
    }
    $base = [IO.Path]::GetFileNameWithoutExtension($name)
    if ($ammoResults -contains $base) {
        $id = "gunsrpg:$base"
        $text = $text -replace '("result"\s*:\s*\{[^}]*"item"\s*:\s*")[^"]+(")', "`${1}$id`${2}"
    }
    [IO.File]::WriteAllText($file.FullName, $text)
}

# 物品模型：从 Guns RPG 贴图生成（勿再用原版剑占位）
& (Join-Path (Split-Path -Parent $PSScriptRoot) "tools\copy_gunsrpg_textures.ps1")
& (Join-Path (Split-Path -Parent $PSScriptRoot) "tools\update_item_models.ps1")

Write-Host "M2 fixup done: recipes + models"
