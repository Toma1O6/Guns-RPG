# Generate ammo recipes, tinted textures, lang (UTF-8 data in ammo_materials.json)
$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Drawing
$root = Split-Path -Parent $PSScriptRoot
$config = Get-Content (Join-Path $PSScriptRoot "ammo_materials.json") -Raw -Encoding UTF8 | ConvertFrom-Json
$baseUrl = "https://raw.githubusercontent.com/Toma1O6/Guns-RPG/1.16.5/src/main/resources/assets/gunsrpg"
$modelDir = Join-Path $root "src\main\resources\assets\gunsrpg\models\item"
$recipeDir = Join-Path $root "src\main\resources\data\gunsrpg\recipes\gunsmith"
$texDir = Join-Path $root "src\main\resources\assets\gunsrpg\textures\item"
$langDir = Join-Path $root "src\main\resources\assets\gunsrpg\lang"
New-Item -ItemType Directory -Force -Path $modelDir, $recipeDir, $texDir, $langDir | Out-Null

$calibers = @("9mm", "45acp", "556mm", "762mm", "magnum", "12g")
$caliberBase = @{
    "9mm" = "iron_9mm"; "45acp" = "iron_45acp"; "556mm" = "iron_556mm"
    "762mm" = "iron_762mm"; "magnum" = "iron_762mm"; "12g" = "iron_12g"
}

function IngredientJson($item) {
    if ($item -is [pscustomobject]) {
        if ($item.tag) { return "{ `"tag`": `"$($item.tag)`" }" }
        if ($item.item) { return "{ `"item`": `"$($item.item)`" }" }
    }
    return "{ `"item`": `"$item`" }"
}

function Get-BasePng($name) {
    $local = Join-Path $texDir "$name.png"
    if ((Test-Path $local) -and ((Get-Item $local).Length -gt 80)) { return $local }
    $url = "$baseUrl/textures/item/$name.png"
    Write-Host "GET $url"
    try {
        Invoke-WebRequest -Uri $url -OutFile $local -UseBasicParsing -TimeoutSec 45
        if ((Get-Item $local).Length -gt 80) { return $local }
    } catch {}
    return $null
}

function Test-BrassCasingPixel($p) {
    $sum = $p.R + $p.G + $p.B
    if ($sum -lt 80) { return $false }
    return ($p.R -gt 155 -and $p.G -gt 120 -and $p.B -lt 145 -and ($p.R - $p.B) -gt 35)
}

function Test-TipTintPixel($p, [int]$y, [int]$imgH, [double]$tipFraction) {
    if ($p.A -lt 8) { return $false }
    if (Test-BrassCasingPixel $p) { return $false }
    $chroma = [Math]::Abs($p.R - $p.G) + [Math]::Abs($p.G - $p.B)
    $sum = $p.R + $p.G + $p.B
    if ($sum -lt 70) { return $false }
    if ($y -ge [int]($imgH * $tipFraction)) { return $false }
    if ($chroma -gt 15 -and $sum -gt 70) { return $true }
    if ($chroma -lt 42 -and $sum -gt 110) { return $true }
    return $false
}

function Write-TintedBullet($basePath, $dstPath, [string]$hexColor, [double]$tipFraction = 0.46) {
    if (-not (Test-Path $basePath)) { return }
    try {
        $fs = [System.IO.File]::OpenRead($basePath)
        $img = [System.Drawing.Bitmap]::FromStream($fs)
        $fs.Close()
        $r = [Convert]::ToInt32($hexColor.Substring(0, 2), 16)
        $g = [Convert]::ToInt32($hexColor.Substring(2, 2), 16)
        $b = [Convert]::ToInt32($hexColor.Substring(4, 2), 16)
        $tint = [System.Drawing.Color]::FromArgb(255, $r, $g, $b)
        $out = New-Object System.Drawing.Bitmap $img.Width, $img.Height
        for ($y = 0; $y -lt $img.Height; $y++) {
            for ($x = 0; $x -lt $img.Width; $x++) {
                $p = $img.GetPixel($x, $y)
                if (-not (Test-TipTintPixel $p $y $img.Height $tipFraction)) {
                    $out.SetPixel($x, $y, $p)
                    continue
                }
                $chroma = [Math]::Abs($p.R - $p.G) + [Math]::Abs($p.G - $p.B)
                $mix = if ($chroma -lt 30) { 0.86 } else { 0.74 }
                $nr = [int]($p.R * (1 - $mix) + $tint.R * $mix)
                $ng = [int]($p.G * (1 - $mix) + $tint.G * $mix)
                $nb = [int]($p.B * (1 - $mix) + $tint.B * $mix)
                $out.SetPixel($x, $y, [System.Drawing.Color]::FromArgb($p.A, $nr, $ng, $nb))
            }
        }
        $img.Dispose()
        $ms = New-Object System.IO.MemoryStream
        $out.Save($ms, [System.Drawing.Imaging.ImageFormat]::Png)
        $out.Dispose()
        [System.IO.File]::WriteAllBytes($dstPath, $ms.ToArray())
        $ms.Dispose()
    } catch {
        Write-Host "texture skip $dstPath : $_"
    }
}

function Write-Model($id) {
    $json = "{`n  `"parent`": `"minecraft:item/generated`",`n  `"textures`": { `"layer0`": `"gunsrpg:item/$id`" }`n}`n"
    [IO.File]::WriteAllText((Join-Path $modelDir "$id.json"), $json)
}

function Write-PistolRecipe($mat, $cal) {
    $id = "$($mat.prefix)_$cal"
    $ing = IngredientJson $mat.item
    @"
{
  "type": "gunsrpg:gunsmith",
  "pattern": ["m", "g", "s"],
  "key": {
    "g": { "item": "minecraft:gunpowder" },
    "s": { "item": "gunsrpg:small_bullet_casing" },
    "m": $ing
  },
  "result": { "count": 1, "item": "gunsrpg:$id" },
  "skill": "$($mat.skill)"
}
"@ | Set-Content (Join-Path $recipeDir "$id.json") -Encoding UTF8
}

function Write-Rifle556Recipe($mat) {
    $id = "$($mat.prefix)_556mm"
    $ing = IngredientJson $mat.item
    @"
{
  "type": "gunsrpg:gunsmith",
  "pattern": ["m ", "gg", "l "],
  "key": {
    "g": { "item": "minecraft:gunpowder" },
    "l": { "item": "gunsrpg:large_bullet_casing" },
    "m": $ing
  },
  "result": { "count": 1, "item": "gunsrpg:$id" },
  "skill": "$($mat.skill)"
}
"@ | Set-Content (Join-Path $recipeDir "$id.json") -Encoding UTF8
}

function Write-Rifle762Recipe($mat) {
    $id = "$($mat.prefix)_762mm"
    $ing = IngredientJson $mat.item
    @"
{
  "type": "gunsrpg:gunsmith",
  "pattern": ["m  ", "ggg", "l  "],
  "key": {
    "g": { "item": "minecraft:gunpowder" },
    "l": { "item": "gunsrpg:large_bullet_casing" },
    "m": $ing
  },
  "result": { "count": 1, "item": "gunsrpg:$id" },
  "skill": "$($mat.skill)"
}
"@ | Set-Content (Join-Path $recipeDir "$id.json") -Encoding UTF8
}

function Write-ShotgunRecipe($mat) {
    $id = "$($mat.prefix)_12g"
    $ing = IngredientJson $mat.item
    @"
{
  "type": "gunsrpg:gunsmith",
  "pattern": ["m  ", "ggg", "s  "],
  "key": {
    "g": { "item": "minecraft:gunpowder" },
    "s": { "item": "gunsrpg:shotgun_shell" },
    "m": $ing
  },
  "result": { "count": 1, "item": "gunsrpg:$id" },
  "skill": "$($mat.skill)"
}
"@ | Set-Content (Join-Path $recipeDir "$id.json") -Encoding UTF8
}

function Write-MagnumRecipe($mat) {
    if ($mat.prefix -in @("wood", "stone")) { return }
    $id = "$($mat.prefix)_magnum"
    $ing = IngredientJson $mat.item
    @"
{
  "type": "gunsrpg:gunsmith",
  "pattern": ["MMM", "GGG", "LLL"],
  "key": {
    "L": { "item": "gunsrpg:large_bullet_casing" },
    "G": { "item": "minecraft:gunpowder" },
    "M": $ing
  },
  "result": { "count": 1, "item": "gunsrpg:$id" },
  "skill": "$($mat.skill)"
}
"@ | Set-Content (Join-Path $recipeDir "$id.json") -Encoding UTF8
}

foreach ($c in $caliberBase.Values | Select-Object -Unique) { Get-BasePng $c | Out-Null }

$langZh = [ordered]@{ "gunsrpg.caliber.magnum" = $config.caliber_label_zh.magnum }
$langEn = [ordered]@{ "gunsrpg.caliber.magnum" = $config.caliber_label_en.magnum }

foreach ($mat in $config.materials) {
    $langZh["gunsrpg.ammo.material.$($mat.prefix)"] = $mat.zh
    $langEn["gunsrpg.ammo.material.$($mat.prefix)"] = $mat.en
    foreach ($cal in $calibers) {
        if ($cal -eq "magnum" -and $mat.prefix -eq "stone") { continue }
        $id = "$($mat.prefix)_$cal"
        $basePath = Join-Path $texDir "$($caliberBase[$cal]).png"
        $dst = Join-Path $texDir "$id.png"
        if (Test-Path $basePath) {
            $tipFrac = if ($cal -eq "magnum") { 0.50 } else { 0.40 }
            Write-TintedBullet $basePath $dst $mat.color $tipFrac
        }
        Write-Model $id
        switch ($cal) {
            "9mm" { Write-PistolRecipe $mat $cal }
            "45acp" { Write-PistolRecipe $mat $cal }
            "556mm" { Write-Rifle556Recipe $mat }
            "762mm" { Write-Rifle762Recipe $mat }
            "12g" { Write-ShotgunRecipe $mat }
            "magnum" { Write-MagnumRecipe $mat }
        }
        $langZh["item.gunsrpg.$id"] = ($mat.zh + " " + $config.caliber_label_zh.$cal + " " + $config.suffix_zh)
        $langEn["item.gunsrpg.$id"] = ($mat.en + " " + $config.caliber_label_en.$cal + " " + $config.suffix_en)
        Write-Host "ok $id"
    }
}

[System.IO.File]::WriteAllText((Join-Path $langDir "zh_cn_ammo.json"), ($langZh | ConvertTo-Json -Depth 4) + "`n", [System.Text.UTF8Encoding]::new($true))
[System.IO.File]::WriteAllText((Join-Path $langDir "en_us_ammo.json"), ($langEn | ConvertTo-Json -Depth 4) + "`n", [System.Text.UTF8Encoding]::new($true))
Write-Host "All ammo generated."
