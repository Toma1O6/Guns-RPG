# 从 Guns-RPG 1.16.5 仓库下载贴图到 gunsrpg（仅资源，不修改参考包）
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$baseUrl = "https://raw.githubusercontent.com/Toma1O6/Guns-RPG/1.16.5/src/main/resources/assets/gunsrpg"
$itemTex = Join-Path $root "src\main\resources\assets\gunsrpg\textures\item"
$guiTex = Join-Path $root "src\main\resources\assets\gunsrpg\textures\gui"
$blockTex = Join-Path $root "src\main\resources\assets\gunsrpg\textures\block"
New-Item -ItemType Directory -Force -Path $itemTex, $guiTex, $blockTex | Out-Null

function Get-Png($remotePath, $localPath) {
    $url = "$baseUrl/$remotePath"
    $dir = Split-Path -Parent $localPath
    if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
    if ((Test-Path $localPath) -and ((Get-Item $localPath).Length -gt 100)) { return }
    Write-Host "GET $url"
    for ($try = 1; $try -le 5; $try++) {
        try {
            Invoke-WebRequest -Uri $url -OutFile $localPath -UseBasicParsing -TimeoutSec 120
            if ((Get-Item $localPath).Length -gt 50) { return }
        } catch {
            Write-Host "  retry $try : $($_.Exception.Message)"
            Start-Sleep -Seconds (2 * $try)
        }
    }
    throw "Failed to download $url"
}

$items = @(
    "gun_parts", "barrel", "magazine", "wooden_stock", "long_barrel", "small_iron_stock",
    "small_bullet_casing", "large_bullet_casing", "shotgun_shell", "bolt_fletching",
    "wooden_9mm", "wooden_45acp", "wooden_556mm", "wooden_762mm", "wooden_12g",
    "stone_9mm", "stone_45acp", "stone_556mm", "stone_762mm", "stone_12g",
    "iron_9mm", "iron_45acp", "iron_556mm", "iron_762mm", "iron_12g"
)
foreach ($name in $items) {
    Get-Png "textures/item/$name.png" (Join-Path $itemTex "$name.png")
}

Get-Png "textures/screen/skilled_workbench.png" (Join-Path $guiTex "skilled_workbench.png")
# 方块用 crystal_map 图集（原版锻造台模型），物品图标暂用 gun_parts
Get-Png "textures/item/gun_parts.png" (Join-Path $blockTex "gunsmith_table_icon.png")
Get-Png "textures/block/crystal_map.png" (Join-Path $blockTex "crystal_map.png")
Get-Png "textures/block/repair_station.png" (Join-Path $blockTex "repair_station.png")
if (-not (Test-Path (Join-Path $blockTex "medical_station.png"))) {
    Copy-Item (Join-Path $blockTex "repair_station.png") (Join-Path $blockTex "medical_station.png") -Force
}
# 枪械物品栏/创造栏：BEWLR 3D + 此图集（与 Guns RPG 一致，不用零件拼 2D 图标）
Get-Png "textures/item/weapon_texture_map.png" (Join-Path $itemTex "weapon_texture_map.png")

Write-Host "GunsRPG textures copied."
