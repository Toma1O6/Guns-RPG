# 生存栏 I–V 五级图标分色（从 _i 底图生成）
param(
    [string]$IconsDir = "$PSScriptRoot\..\src\main\resources\assets\gunsrpg\textures\icons"
)

Add-Type -AssemblyName System.Drawing

function Tint-Image($srcPath, $dstPath, [string]$tier) {
    $bmp = [System.Drawing.Bitmap]::FromFile($srcPath)
    $out = New-Object System.Drawing.Bitmap $bmp.Width, $bmp.Height
    for ($y = 0; $y -lt $bmp.Height; $y++) {
        for ($x = 0; $x -lt $bmp.Width; $x++) {
            $c = $bmp.GetPixel($x, $y)
            if ($c.A -eq 0) {
                $out.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(0, 0, 0, 0))
                continue
            }
            switch ($tier) {
                'i' {
                    $r = [Math]::Min(255, [int]($c.R * 1.05 + 28))
                    $g = [Math]::Min(255, [int]($c.G * 0.75 + 12))
                    $b = [Math]::Max(0, [int]($c.B * 0.45))
                }
                'ii' {
                    $r = [Math]::Min(255, [int]($c.R * 0.75 + 55))
                    $g = [Math]::Min(255, [int]($c.G * 0.78 + 58))
                    $b = [Math]::Min(255, [int]($c.B * 0.82 + 62))
                }
                'iii' {
                    $r = [Math]::Max(0, [int]($c.R * 0.55 + 18))
                    $g = [Math]::Min(255, [int]($c.G * 1.15 + 35))
                    $b = [Math]::Max(0, [int]($c.B * 0.65 + 12))
                }
                'iv' {
                    $r = [Math]::Min(255, [int]($c.R * 1.12 + 38))
                    $g = [Math]::Min(255, [int]($c.G * 1.02 + 28))
                    $b = [Math]::Max(0, [int]($c.B * 0.5))
                }
                default {
                    $r = [Math]::Min(255, [int]($c.R * 0.82 + 55))
                    $g = [Math]::Max(0, [int]($c.G * 0.48))
                    $b = [Math]::Min(255, [int]($c.B * 1.05 + 72))
                }
            }
            $out.SetPixel($x, $y, [System.Drawing.Color]::FromArgb($c.A, $r, $g, $b))
        }
    }
    $tmp = [System.IO.Path]::Combine([System.IO.Path]::GetTempPath(), [System.IO.Path]::GetRandomFileName() + ".png")
    $out.Save($tmp, [System.Drawing.Imaging.ImageFormat]::Png)
    $bmp.Dispose()
    $out.Dispose()
    Copy-Item -LiteralPath $tmp -Destination $dstPath -Force
    Remove-Item -LiteralPath $tmp -Force
}

$romanChains = @(
    'agility', 'adrenaline_rush', 'like_a_cat', 'second_chance', 'well_fed', 'skull_crusher', 'strong_muscles'
)
foreach ($prefix in $romanChains) {
    $src = Join-Path $IconsDir "$prefix`_i.png"
    if (-not (Test-Path $src)) {
        Write-Warning "Skip $prefix (no _i)"
        continue
    }
    foreach ($tier in @('i', 'ii', 'iii', 'iv', 'v')) {
        $suffix = if ($tier -eq 'i') { '_i' } elseif ($tier -eq 'ii') { '_ii' } elseif ($tier -eq 'iii') { '_iii' } elseif ($tier -eq 'iv') { '_iv' } else { '_v' }
        Tint-Image $src (Join-Path $IconsDir "$prefix$suffix.png") $tier
    }
    Write-Host "Tinted $prefix I-V"
}

$chef = @(
    @{ src = 'local_chef.png'; dst = 'local_chef.png'; tier = 'i' },
    @{ src = 'local_chef.png'; dst = 'master_chef.png'; tier = 'ii' },
    @{ src = 'local_chef.png'; dst = 'sous_chef_iii.png'; tier = 'iii' },
    @{ src = 'local_chef.png'; dst = 'head_chef_iv.png'; tier = 'iv' },
    @{ src = 'local_chef.png'; dst = 'executive_chef_v.png'; tier = 'v' }
)
$chefBase = Join-Path $IconsDir 'local_chef.png'
if (Test-Path $chefBase) {
    foreach ($entry in $chef) {
        Tint-Image $chefBase (Join-Path $IconsDir $entry.dst) $entry.tier
    }
    Write-Host 'Tinted chef chain I-V'
}

Write-Host 'Done'
