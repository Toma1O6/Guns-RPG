param([string]$IconsDir = (Join-Path $PSScriptRoot '..\src\main\resources\assets\gunsrpg\textures\icons'))
# 直接调用 Tint-Image（gen_survival_colors 未导出函数，内联复制核心链）
Add-Type -AssemblyName System.Drawing

function Tint-ImageLocal($srcPath, $dstPath, [string]$tier) {
    if (-not (Test-Path $srcPath)) { return }
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
                'i' { $r = [Math]::Min(255, [int]($c.R * 1.05 + 28)); $g = [Math]::Min(255, [int]($c.G * 0.75 + 12)); $b = [Math]::Max(0, [int]($c.B * 0.45)) }
                'ii' { $r = [Math]::Min(255, [int]($c.R * 0.75 + 55)); $g = [Math]::Min(255, [int]($c.G * 0.78 + 58)); $b = [Math]::Min(255, [int]($c.B * 0.82 + 62)) }
                'iii' { $r = [Math]::Max(0, [int]($c.R * 0.55 + 18)); $g = [Math]::Min(255, [int]($c.G * 1.15 + 35)); $b = [Math]::Max(0, [int]($c.B * 0.65 + 12)) }
                'iv' { $r = [Math]::Min(255, [int]($c.R * 1.12 + 38)); $g = [Math]::Min(255, [int]($c.G * 1.02 + 28)); $b = [Math]::Max(0, [int]($c.B * 0.5)) }
                default { $r = [Math]::Min(255, [int]($c.R * 0.82 + 55)); $g = [Math]::Max(0, [int]($c.G * 0.48)); $b = [Math]::Min(255, [int]($c.B * 1.05 + 72)) }
            }
            $out.SetPixel($x, $y, [System.Drawing.Color]::FromArgb($c.A, $r, $g, $b))
        }
    }
    $tmp = [System.IO.Path]::Combine([System.IO.Path]::GetTempPath(), [System.IO.Path]::GetRandomFileName() + ".png")
    $out.Save($tmp, [System.Drawing.Imaging.ImageFormat]::Png)
    $bmp.Dispose(); $out.Dispose()
    Copy-Item -LiteralPath $tmp -Destination $dstPath -Force
    Remove-Item -LiteralPath $tmp -Force
}

$chains = @(
    @{ prefix = 'bleeding_resistance'; tiers = @('i','ii','iii') },
    @{ prefix = 'fracture_resistance'; tiers = @('i','ii','iii') },
    @{ prefix = 'poison_resistance'; tiers = @('i','ii','iii') },
    @{ prefix = 'infection_resistance'; tiers = @('i','ii','iii') },
    @{ prefix = 'acrobatics'; tiers = @('i','ii','iii') },
    @{ prefix = 'pharmacist'; tiers = @('i','ii','iii','iv','v') }
)
foreach ($ch in $chains) {
    $src = Join-Path $IconsDir "$($ch.prefix)_i.png"
    if (-not (Test-Path $src)) { Write-Warning "No $($ch.prefix)_i"; continue }
    foreach ($t in $ch.tiers) {
        $suffix = switch ($t) { 'i' { '_i' } 'ii' { '_ii' } 'iii' { '_iii' } 'iv' { '_iv' } default { '_v' } }
        Tint-ImageLocal $src (Join-Path $IconsDir "$($ch.prefix)$suffix.png") $t
    }
}
$medSrc = Join-Path $IconsDir 'medic.png'
if (Test-Path $medSrc) {
    Tint-ImageLocal $medSrc (Join-Path $IconsDir 'medic.png') 'i'
    Tint-ImageLocal $medSrc (Join-Path $IconsDir 'doctor.png') 'ii'
    Tint-ImageLocal $medSrc (Join-Path $IconsDir 'medical_station.png') 'iii'
}
Write-Host 'Resistance tier tints done.'
