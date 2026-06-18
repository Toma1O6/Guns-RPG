$ErrorActionPreference = "Stop"
$recipeDir = Join-Path (Split-Path -Parent $PSScriptRoot) "src\main\resources\data\gunsrpg\recipes\gunsmith"
$gunResults = @{
    "m1911.json" = "gunsrpg:m1911"
    "r45.json" = "gunsrpg:r45"
    "desert_eagle.json" = "gunsrpg:desert_eagle"
    "thompson.json" = "gunsrpg:thompson"
    "vector.json" = "gunsrpg:vector"
    "akm.json" = "gunsrpg:akm"
    "vss.json" = "gunsrpg:vss"
    "kar98k.json" = "gunsrpg:kar98k"
    "winchester.json" = "gunsrpg:winchester"
    "s686.json" = "gunsrpg:s686"
    "minigun.json" = "gunsrpg:minigun"
}
foreach ($entry in $gunResults.GetEnumerator()) {
    $path = Join-Path $recipeDir $entry.Key
    if (-not (Test-Path $path)) { continue }
    $text = [IO.File]::ReadAllText($path)
    $id = $entry.Value
    $text = $text -replace '("result"\s*:\s*\{[^}]*"item"\s*:\s*")[^"]+(")', "`${1}$id`${2}"
    [IO.File]::WriteAllText($path, $text)
}
Write-Host "Gunsmith recipes now output gunsrpg guns."
