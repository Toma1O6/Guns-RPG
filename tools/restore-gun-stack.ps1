# 从 backup-gun-stack.ps1 生成的目录恢复 CGM + gunsrpg 栈
param(
    [Parameter(Mandatory = $true)]
    [string]$BackupDir
)
$ErrorActionPreference = "Stop"
if (-not (Test-Path $BackupDir)) { throw "Backup not found: $BackupDir" }
$packRoot = Split-Path (Split-Path $PSScriptRoot)
$mods = Join-Path $packRoot "mods"

Write-Host "Restore from: $BackupDir"

# 先禁用可能冲突的枪械 mod（与 disable-other-gun-mods 一致）
& (Join-Path $PSScriptRoot "disable-other-gun-mods.ps1")

$srcMods = Join-Path $BackupDir "mods"
if (Test-Path $srcMods) {
    Get-ChildItem $srcMods -File | ForEach-Object {
        $target = Join-Path $mods $_.Name
        if (Test-Path ($target + ".disabled")) { Remove-Item ($target + ".disabled") -Force }
        Copy-Item $_.FullName $target -Force
        Write-Host "Restored mod: $($_.Name)"
    }
}

function Restore-Tree($rel) {
    $src = Join-Path $BackupDir $rel
    $dst = Join-Path $packRoot $rel
    if (-not (Test-Path $src)) { return }
    if (Test-Path $dst) { Remove-Item $dst -Recurse -Force }
    $parent = Split-Path $dst -Parent
    if (-not (Test-Path $parent)) { New-Item -ItemType Directory -Force -Path $parent | Out-Null }
    Copy-Item $src $dst -Recurse -Force
    Write-Host "Restored: $rel"
}

Restore-Tree "config\gunsrpg"
Restore-Tree "config\wf_gun"
Restore-Tree "kubejs\data\cgm"
$ss = Join-Path $BackupDir "kubejs\server_scripts"
if (Test-Path $ss) {
    Get-ChildItem $ss -File | ForEach-Object {
        Copy-Item $_.FullName (Join-Path $packRoot "kubejs\server_scripts\$($_.Name)") -Force
        Write-Host "Restored script: $($_.Name)"
    }
}

Write-Host ""
Write-Host "Restore complete. Rebuild gunsrpg if you changed source: cd gunsrpg-1201; .\gradlew.bat build; .\tools\deploy.ps1"
Write-Host "Then restart Minecraft."
