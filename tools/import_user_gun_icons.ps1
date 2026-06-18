# 将用户提供的 9 张枪械 UI 图导入为 textures/item/{key}_gun_icon.png（32×32，灰底透明）
$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Drawing

$root = Split-Path -Parent $PSScriptRoot
$assets = "C:\Users\Windows 10\.cursor\projects\d-minecraft-minecraft-versions\assets"
$sources = @(
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-a8febd2b-9568-41ad-8750-bfc355c0b51d.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-71716df0-1e91-4815-b37c-1780d2915e55.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-27a4f246-2326-4462-a4ad-71ca09c978d9.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-b1682fee-2180-48db-839d-d5950d4224a2.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-7fe473a2-d149-439f-9e0e-8d46b2f0a421.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-8d8889b0-6729-4f8a-8361-245016434fe2.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-b15c8eff-4876-4cc6-8ceb-ecd989a27cc0.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-3ed783a5-0388-4814-a8d2-45a226ede5e7.png"),
    (Join-Path $assets "c__Users_Windows_10_AppData_Roaming_Cursor_User_workspaceStorage_6cf3f9aec268f2774443a956d50c47f4_images_image-de10721b-5a9f-4e3f-82b7-d26911bfa071.png")
)
$names = @("m1911", "desert_eagle", "ump45", "thompson", "vector", "akm", "hk416", "aug", "sks")

$texDir = Join-Path $root "src\main\resources\assets\gunsrpg\textures\item"
$iconModelDir = Join-Path $root "src\main\resources\assets\gunsrpg\models\item\icon"
$itemModelDir = Join-Path $root "src\main\resources\assets\gunsrpg\models\item"
New-Item -ItemType Directory -Force -Path $texDir, $iconModelDir | Out-Null

function Test-IsBg([System.Drawing.Color]$c) {
    if ($c.A -lt 16) { return $true }
    $avg = ($c.R + $c.G + $c.B) / 3.0
    if ($avg -lt 95 -or $avg -gt 195) { return $false }
    return ([Math]::Abs($c.R - $c.G) -lt 28 -and [Math]::Abs($c.G - $c.B) -lt 28)
}

function Import-Icon($srcPath, $name) {
    if (-not (Test-Path $srcPath)) { throw "Missing source: $srcPath" }
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

    $outPath = Join-Path $texDir "${name}_gun_icon.png"
    $out.Save($outPath, [System.Drawing.Imaging.ImageFormat]::Png)
    $out.Dispose()
    Write-Host "Wrote $outPath"

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
    Import-Icon $sources[$i] $names[$i]
}

Write-Host "Done: $($names.Count) gun icons."
