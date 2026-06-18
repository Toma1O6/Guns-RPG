# 从 weapon_texture_map.png 裁切各枪 2D 槽位图标（Guns RPG 图集 UV）
$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Drawing
$root = Split-Path -Parent $PSScriptRoot
$src = Join-Path $root "src\main\resources\assets\gunsrpg\textures\item\weapon_texture_map.png"
$texDir = Join-Path $root "src\main\resources\assets\gunsrpg\textures\item"
$iconDir = Join-Path $texDir "icon"
$modelDir = Join-Path $root "src\main\resources\assets\gunsrpg\models\item\icon"
New-Item -ItemType Directory -Force -Path $iconDir, $modelDir | Out-Null
if (-not (Test-Path $src)) {
    & (Join-Path $root "tools\copy_gunsrpg_textures.ps1")
}

$crops = @{
    "m1911" = @(28, 32, 56, 56)
    "r45" = @(28, 32, 56, 56)
    "desert_eagle" = @(28, 32, 56, 56)
    "thompson" = @(60, 100, 64, 48)
    "vector" = @(60, 100, 64, 48)
    "akm" = @(8, 140, 72, 52)
    "vss" = @(100, 100, 72, 48)
    "kar98k" = @(128, 138, 96, 56)
    "winchester" = @(88, 32, 80, 52)
    "s686" = @(160, 80, 64, 48)
    "minigun" = @(0, 180, 80, 48)
}

$bmp = [System.Drawing.Bitmap]::FromFile($src)
foreach ($entry in $crops.GetEnumerator()) {
    $name = $entry.Key
    $x, $y, $w, $h = $entry.Value
    $out = Join-Path $iconDir "$name.png"
    $flatOut = Join-Path $texDir "${name}_gun_icon.png"
    $crop = New-Object System.Drawing.Bitmap $w, $h
    $g = [System.Drawing.Graphics]::FromImage($crop)
    $g.Clear([System.Drawing.Color]::FromArgb(0, 0, 0, 0))
    $destRect = New-Object System.Drawing.Rectangle(0, 0, $w, $h)
    $srcRect = New-Object System.Drawing.Rectangle($x, $y, $w, $h)
    $g.DrawImage($bmp, $destRect, $srcRect, [System.Drawing.GraphicsUnit]::Pixel)
    $crop.Save($out, [System.Drawing.Imaging.ImageFormat]::Png)
    $crop.Save($flatOut, [System.Drawing.Imaging.ImageFormat]::Png)
    $g.Dispose()
    $crop.Dispose()
    $json = @"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "gunsrpg:item/${name}_gun_icon"
  }
}
"@
    [IO.File]::WriteAllText((Join-Path $modelDir "$name.json"), $json)
    Write-Host "icon $name"
}
$bmp.Dispose()
Write-Host "Gun icons extracted."
