# 从 III 级图标生成 IV（金色）/ V（紫色）级
param(
    [string]$IconsDir = "$PSScriptRoot\..\src\main\resources\assets\gunsrpg\textures\icons"
)

Add-Type -AssemblyName System.Drawing

function Tint-Image($srcPath, $dstPath, $mode) {
    $bmp = [System.Drawing.Bitmap]::FromFile($srcPath)
    $out = New-Object System.Drawing.Bitmap $bmp.Width, $bmp.Height
    for ($y = 0; $y -lt $bmp.Height; $y++) {
        for ($x = 0; $x -lt $bmp.Width; $x++) {
            $c = $bmp.GetPixel($x, $y)
            if ($c.A -eq 0) {
                $out.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(0, 0, 0, 0))
                continue
            }
            if ($mode -eq 'iv') {
                $r = [Math]::Min(255, [int]($c.R * 1.12 + 38))
                $g = [Math]::Min(255, [int]($c.G * 1.02 + 28))
                $b = [Math]::Max(0, [int]($c.B * 0.5))
            } else {
                $r = [Math]::Min(255, [int]($c.R * 0.82 + 55))
                $g = [Math]::Max(0, [int]($c.G * 0.48))
                $b = [Math]::Min(255, [int]($c.B * 1.05 + 72))
            }
            $out.SetPixel($x, $y, [System.Drawing.Color]::FromArgb($c.A, $r, $g, $b))
        }
    }
    $out.Save($dstPath, [System.Drawing.Imaging.ImageFormat]::Png)
    $bmp.Dispose(); $out.Dispose()
}

foreach ($prefix in @('careful_gunner', 'lucky_shooter')) {
    $src = Join-Path $IconsDir "$prefix`_iii.png"
    if (-not (Test-Path $src)) { Write-Error "Missing $src"; continue }
    Tint-Image $src (Join-Path $IconsDir "$prefix`_iv.png") 'iv'
    Tint-Image $src (Join-Path $IconsDir "$prefix`_v.png") 'v'
    Write-Host "Generated $prefix iv/v"
}
