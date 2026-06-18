# Q 版枪械条带图 -> 32x32 物品图标（按枪体包围盒裁切，填满格子）
$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Drawing

$root = Split-Path -Parent $PSScriptRoot
$sheet = Join-Path $root "tools\gunsrpg_q_guns_sheet.png"
if (-not (Test-Path $sheet)) {
    $sheet = "C:\Users\Windows 10\.cursor\projects\d-minecraft-minecraft-versions\assets\gunsrpg_q_guns_sheet.png"
}
if (-not (Test-Path $sheet)) { throw "找不到 gunsrpg_q_guns_sheet.png" }

# 用户自定义图标由 import_user_gun_icons*.ps1 维护，勿从 Q 条带覆盖
$userIcons = @(
    "m1911", "desert_eagle", "ump45", "thompson", "vector", "akm", "hk416", "aug", "sks",
    "vss", "mk14ebr", "kar98k", "winchester", "awm", "s1897", "s686", "s12k",
    "grenade_launcher", "rocket_launcher"
)
$names = @("r45", "minigun")
$outDir = Join-Path $root "src\main\resources\assets\gunsrpg\textures\item"
$iconModelDir = Join-Path $root "src\main\resources\assets\gunsrpg\models\item\icon"
New-Item -ItemType Directory -Force -Path $outDir, $iconModelDir | Out-Null

$target = 32
$srcBmp = [System.Drawing.Bitmap]::FromFile($sheet)
$cellW = [int][Math]::Floor($srcBmp.Width / $names.Count)

function Test-IsBg([System.Drawing.Color]$c) {
    if ($c.A -lt 16) { return $true }
    $avg = ($c.R + $c.G + $c.B) / 3
    return ($avg -gt 215 -and [Math]::Abs($c.R - $c.G) -lt 25 -and [Math]::Abs($c.G - $c.B) -lt 25)
}

function Test-IsGunPixel([System.Drawing.Color]$c) {
    if (Test-IsBg $c) { return $false }
    $avg = ($c.R + $c.G + $c.B) / 3
    if ($avg -lt 38 -or $avg -gt 210) { return $false }
    $max = [Math]::Max($c.R, [Math]::Max($c.G, $c.B))
    $min = [Math]::Min($c.R, [Math]::Min($c.G, $c.B))
    return (($max - $min) -ge 6)
}

function Get-GunBounds($bmp) {
    $rowHits = New-Object 'System.Collections.Generic.List[int]'
    for ($y = 0; $y -lt $bmp.Height; $y++) {
        $hits = 0
        for ($x = 0; $x -lt $bmp.Width; $x++) {
            if (Test-IsGunPixel ($bmp.GetPixel($x, $y))) { $hits++ }
        }
        if ($hits -ge 4) { $rowHits.Add($y) }
    }
    if ($rowHits.Count -eq 0) { return $null }

    $y0 = $rowHits[0]
    $y1 = $rowHits[$rowHits.Count - 1]
    $minX = $bmp.Width; $maxX = 0
    for ($y = $y0; $y -le $y1; $y++) {
        for ($x = 0; $x -lt $bmp.Width; $x++) {
            if (Test-IsGunPixel ($bmp.GetPixel($x, $y))) {
                if ($x -lt $minX) { $minX = $x }
                if ($x -gt $maxX) { $maxX = $x }
            }
        }
    }
    if ($maxX -lt $minX) { return $null }
    return @{ X = $minX; Y = $y0; W = ($maxX - $minX + 1); H = ($y1 - $y0 + 1) }
}

foreach ($i in 0..($names.Count - 1)) {
    $name = $names[$i]
    $cell = New-Object System.Drawing.Bitmap $cellW, $srcBmp.Height
    $g = [System.Drawing.Graphics]::FromImage($cell)
    $g.DrawImage($srcBmp, 0, 0, (New-Object System.Drawing.Rectangle ($i * $cellW), 0, $cellW, $srcBmp.Height), [System.Drawing.GraphicsUnit]::Pixel)
    $g.Dispose()

    $bounds = Get-GunBounds $cell
    if ($null -eq $bounds) {
        Write-Warning "no gun pixels in cell $name — fallback full inner crop"
        $bounds = @{ X = [int]($cellW * 0.12); Y = [int]($srcBmp.Height * 0.42); W = [int]($cellW * 0.76); H = [int]($srcBmp.Height * 0.14) }
    }

    $trim = New-Object System.Drawing.Bitmap $bounds.W, $bounds.H
    $gt = [System.Drawing.Graphics]::FromImage($trim)
    $gt.DrawImage($cell, 0, 0, (New-Object System.Drawing.Rectangle $bounds.X, $bounds.Y, $bounds.W, $bounds.H), [System.Drawing.GraphicsUnit]::Pixel)
    $gt.Dispose()
    $cell.Dispose()

    for ($y = 0; $y -lt $trim.Height; $y++) {
        for ($x = 0; $x -lt $trim.Width; $x++) {
            $c = $trim.GetPixel($x, $y)
            if ((Test-IsBg $c) -or ($c.R + $c.G + $c.B) / 3 -lt 35) {
                $trim.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(0, 0, 0, 0))
            }
        }
    }

    $icon = New-Object System.Drawing.Bitmap $target, $target
    $gi = [System.Drawing.Graphics]::FromImage($icon)
    $gi.Clear([System.Drawing.Color]::FromArgb(0, 0, 0, 0))
    $gi.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $scale = [Math]::Min(($target - 1) / $trim.Width, ($target - 1) / $trim.Height)
    $dw = [Math]::Max(1, [int]($trim.Width * $scale))
    $dh = [Math]::Max(1, [int]($trim.Height * $scale))
    $ox = [int](($target - $dw) / 2)
    $oy = [int](($target - $dh) / 2)
    $gi.DrawImage($trim, $ox, $oy, $dw, $dh)
    $gi.Dispose()
    $trim.Dispose()

    $icon.Save((Join-Path $outDir "${name}_gun_icon.png"), [System.Drawing.Imaging.ImageFormat]::Png)
    $icon.Dispose()

    $json = @"
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "gunsrpg:item/${name}_gun_icon"
  }
}
"@
    [IO.File]::WriteAllText((Join-Path $iconModelDir "$name.json"), $json)
    [IO.File]::WriteAllText((Join-Path $root "src\main\resources\assets\gunsrpg\models\item\$name.json"), $json)
    Write-Host ("wrote {0} ({1}x{2} -> {3}x{3})" -f $name, $bounds.W, $bounds.H, $target)
}

$srcBmp.Dispose()
Write-Host "Done."
