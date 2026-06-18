# 第二批用户枪械 UI 图 -> textures/item/{key}_gun_icon.png；榴弹/火箭筒同步技能树 icons
$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Drawing

$root = Split-Path -Parent $PSScriptRoot
$assets = "C:\Users\Windows 10\.cursor\projects\d-minecraft-minecraft-versions\assets"
$sources = @(
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-bc06f7dc-6634-4076-ab4a-9ab670807900.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-f83e3c93-a06d-4bca-9268-2638627ae60f.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-21c905aa-2420-494c-8dc6-011b94362d2a.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-8b042e37-be9e-4f20-bcf7-2786f9fb95c3.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-7b97e13c-1c8d-4dbd-9dfc-d0c7728423bd.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-1a0dec8f-0b48-407f-a892-8c67e8489b7a.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-0b3f7e7d-7a99-4573-88d4-0074fec25890.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-fdb45812-2227-482e-bf04-c9fad63871fb.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-fb5dbe68-497a-4868-a0bc-d29f8433a115.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-09011431-f9f8-4005-a80b-41d0d342c3a3.png")
)
$names = @(
    "vss", "mk14ebr", "kar98k", "winchester", "awm", "s1897", "s686", "s12k",
    "grenade_launcher", "rocket_launcher"
)

$texDir = Join-Path $root "src\main\resources\assets\gunsrpg\textures\item"
$iconsDir = Join-Path $root "src\main\resources\assets\gunsrpg\textures\icons"
$iconModelDir = Join-Path $root "src\main\resources\assets\gunsrpg\models\item\icon"
$itemModelDir = Join-Path $root "src\main\resources\assets\gunsrpg\models\item"
New-Item -ItemType Directory -Force -Path $texDir, $iconsDir, $iconModelDir | Out-Null

function Test-IsBg([System.Drawing.Color]$c) {
    if ($c.A -lt 16) { return $true }
    $avg = ($c.R + $c.G + $c.B) / 3.0
    if ($avg -lt 95 -or $avg -gt 195) { return $false }
    return ([Math]::Abs($c.R - $c.G) -lt 28 -and [Math]::Abs($c.G - $c.B) -lt 28)
}

function Convert-ToIcon32($srcPath) {
    $src = [System.Drawing.Bitmap]::FromFile($srcPath)
    $target = 32
    $out = New-Object System.Drawing.Bitmap $target, $target, ([System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    $g = [System.Drawing.Graphics]::FromImage($out)
    $g.Clear([System.Drawing.Color]::FromArgb(0, 0, 0, 0))
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.DrawImage($src, 0, 0, $target, $target)
    $g.Dispose()
    $src.Dispose()
    for ($y = 0; $y -lt $target; $y++) {
        for ($x = 0; $x -lt $target; $x++) {
            $c = $out.GetPixel($x, $y)
            if (Test-IsBg $c) {
                $out.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(0, 0, 0, 0))
            }
        }
    }
    return $out
}

function Write-ItemModels($name) {
    $json = @"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "gunsrpg:item/${name}_gun_icon"
  }
}
"@
    Set-Content -Path (Join-Path $itemModelDir "$name.json") -Value $json -Encoding UTF8
    Set-Content -Path (Join-Path $iconModelDir "$name.json") -Value $json -Encoding UTF8
}

for ($i = 0; $i -lt $names.Count; $i++) {
    $name = $names[$i]
    if (-not (Test-Path $sources[$i])) { throw "Missing source: $($sources[$i])" }
    $bmp = Convert-ToIcon32 $sources[$i]
    $gunPath = Join-Path $texDir "${name}_gun_icon.png"
    $bmp.Save($gunPath, [System.Drawing.Imaging.ImageFormat]::Png)
    Write-Host "Wrote $gunPath"
    if ($name -eq "grenade_launcher" -or $name -eq "rocket_launcher") {
        $asmPath = Join-Path $iconsDir "${name}_assembly.png"
        $bmp.Save($asmPath, [System.Drawing.Imaging.ImageFormat]::Png)
        Write-Host "Wrote $asmPath"
    }
    $bmp.Dispose()
    Write-ItemModels $name
}

Write-Host "Done: $($names.Count) icons (batch 2)."
