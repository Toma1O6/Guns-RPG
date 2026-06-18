# 1.16 medstation -> 1.20.1 data/gunsrpg/recipes/medical_station（skill 字段、跳过未实装物品）
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$srcDir = Join-Path $root "..\backups\gunsrpg-1.16.5-1.1.0\data\gunsrpg\recipes\medstation"
$dstDir = Join-Path $root "src\main\resources\data\gunsrpg\recipes\medical_station"
$skipItems = @('kodiak', 'propital', 'ufak', 'calcium_shot', 'vitamins')
New-Item -ItemType Directory -Force -Path $dstDir | Out-Null

Get-ChildItem $srcDir -Filter *.json | ForEach-Object {
    $name = $_.BaseName
    if ($skipItems -contains $name) {
        Write-Host "Skip $name (item not in 1.20.1 pack)"
        return
    }
    $j = Get-Content $_.FullName -Raw | ConvertFrom-Json
    $skill = ""
    if ($j.requirements) {
        foreach ($req in $j.requirements) {
            if ($req.predicate.skill) {
                $skill = $req.predicate.skill -replace '^gunsrpg:', ''
            }
        }
    }
    $out = [ordered]@{
        type    = "gunsrpg:medical_station"
        pattern = $j.pattern
        key     = $j.key
        result  = $j.result
    }
    if ($skill) { $out.skill = $skill }
    $json = $out | ConvertTo-Json -Depth 10
    $json = $json -replace '"gunsrpg:amethyst"', '"minecraft:amethyst_shard"'
    $path = Join-Path $dstDir "$name.json"
    [System.IO.File]::WriteAllText($path, $json + "`n", [System.Text.UTF8Encoding]::new($false))
    Write-Host "Wrote $name.json (skill=$skill)"
}
Write-Host "Medstation recipes ported."
