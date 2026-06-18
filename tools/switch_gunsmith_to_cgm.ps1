$ErrorActionPreference = "Stop"
$recipeDir = Join-Path (Split-Path -Parent $PSScriptRoot) "src\main\resources\data\gunsrpg\recipes\gunsmith"

$gunResults = @{
    "m1911.json" = "cgm:pistol"
    "r45.json" = "cgm:pistol"
    "desert_eagle.json" = "cgm:pistol"
    "thompson.json" = "cgm:machine_pistol"
    "vector.json" = "cgm:machine_pistol"
    "akm.json" = "cgm:assault_rifle"
    "vss.json" = "cgm:rifle"
    "kar98k.json" = "cgm:rifle"
    "winchester.json" = "cgm:heavy_rifle"
    "s686.json" = "cgm:shotgun"
    "minigun.json" = "cgm:mini_gun"
}

foreach ($entry in $gunResults.GetEnumerator()) {
    $path = Join-Path $recipeDir $entry.Key
    if (-not (Test-Path $path)) { continue }
    $text = [IO.File]::ReadAllText($path)
    $id = $entry.Value
    $text = $text -replace '("result"\s*:\s*\{[^}]*"item"\s*:\s*")[^"]+(")', "`${1}$id`${2}"
    [IO.File]::WriteAllText($path, $text)
    Write-Host "  $($entry.Key) -> $id"
}
Write-Host "Gunsmith recipes now output CGM guns."
