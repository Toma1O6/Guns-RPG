$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$files = Get-ChildItem $root -Recurse -File | Where-Object {
    $_.FullName -notmatch '\\build\\|\\\.gradle\\|\\run\\' -and
    $_.Extension -match '\.(java|json|toml|properties|md|ps1|mcmeta)$'
}
foreach ($f in $files) {
    $text = [IO.File]::ReadAllText($f.FullName)
    $orig = $text
    $text = $text.Replace('gunsrpg', 'gunsrpg')
    $text = $text.Replace('GunsRpg', 'GunsRpg')
    $text = $text.Replace('Guns RPG', 'Guns RPG')
    if ($text -ne $orig) { [IO.File]::WriteAllText($f.FullName, $text) }
}
$main = Join-Path $root "src\main\java\com\wf\firearms\GunsRpg.java"
if (Test-Path $main) { Rename-Item $main "GunsRpg.java" -Force }
Write-Host "Rebrand done (mod id gunsrpg, package unchanged)."
