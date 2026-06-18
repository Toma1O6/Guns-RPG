# 备份当前「CGM + gunsrpg」枪械栈，便于 TaCZ 试验失败后一键恢复
# 用法: powershell -File tools\backup-gun-stack.ps1 [-Label "before-tacz-ak"]
param([string]$Label = "cgm-gunsrpg")
$ErrorActionPreference = "Stop"
$packRoot = Split-Path (Split-Path $PSScriptRoot)
$mods = Join-Path $packRoot "mods"
$stamp = Get-Date -Format "yyyyMMdd-HHmmss"
$dest = Join-Path $packRoot "backups\gun-stack-cgm\$stamp-$Label"
New-Item -ItemType Directory -Force -Path $dest | Out-Null

function Copy-IfExists($src, $rel) {
    if (-not (Test-Path $src)) {
        Write-Warning "Skip (missing): $rel"
        return
    }
    $out = Join-Path $dest $rel
    $dir = Split-Path $out -Parent
    if ($dir -and -not (Test-Path $dir)) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
    Copy-Item $src $out -Force
    Write-Host "  + $rel"
}

Write-Host "Backup -> $dest"

# mods
Get-ChildItem $mods -File | Where-Object {
    $n = $_.Name.ToLowerInvariant()
    $n -match "cgm-unofficial" -or $n -match "^gunsrpg-" -or $n -match "configuration-forge"
} | ForEach-Object { Copy-IfExists $_.FullName (Join-Path "mods" $_.Name) }
# 兼容 mods 目录里带中文备注前缀的 CGM jar
Get-ChildItem $mods -File | Where-Object { $_.Name -like "*CGM-Unofficial*.jar" } |
    ForEach-Object { Copy-IfExists $_.FullName (Join-Path "mods" $_.Name) }

# config
Copy-IfExists (Join-Path $packRoot "config\gunsrpg") "config\gunsrpg"
Copy-IfExists (Join-Path $packRoot "config\wf_gun") "config\wf_gun"

# kubejs（枪械相关）
$kubejs = Join-Path $packRoot "kubejs"
Copy-IfExists (Join-Path $kubejs "data\cgm") "kubejs\data\cgm"
@(
    "wf_gun_combat.js", "wf_gun_recipes.js", "wf_gun_skills.js", "wf_gun_talents.js",
    "wf_gun_bone_grinder.js", "disable_cgm_workbench.js"
) | ForEach-Object { Copy-IfExists (Join-Path $kubejs "server_scripts\$_") "kubejs\server_scripts\$_" }

# 当前 gunsrpg 源码快照（仅 jar 构建相关关键路径，体积小）
$srcSnap = Join-Path $dest "gunsrpg-1201-snapshot"
New-Item -ItemType Directory -Force -Path $srcSnap | Out-Null
$snapPaths = @(
    "src\main\java\com\wf\firearms\compat",
    "src\main\java\com\wf\firearms\data\WeaponMapping.java",
    "config\gunsrpg\cgm_backend.json"
)
$grRoot = Join-Path $packRoot "gunsrpg-1201"
foreach ($p in $snapPaths) {
    $full = Join-Path $grRoot $p
    if (Test-Path $full) {
        $out = Join-Path $srcSnap $p
        $parent = Split-Path $out -Parent
        if (-not (Test-Path $parent)) { New-Item -ItemType Directory -Force -Path $parent | Out-Null }
        if ((Get-Item $full).PSIsContainer) {
            Copy-Item $full $out -Recurse -Force
        } else {
            Copy-Item $full $out -Force
        }
        Write-Host "  + gunsrpg-1201-snapshot\$p"
    }
}

@"
{
  "created": "$(Get-Date -Format o)",
  "label": "$Label",
  "pack": "$packRoot",
  "note": "CGM + gunsrpg 射击栈。恢复: gunsrpg-1201\\tools\\restore-gun-stack.ps1 -BackupDir \"$dest\""
}
"@ | Set-Content (Join-Path $dest "manifest.json") -Encoding UTF8

Write-Host ""
Write-Host "Done. Latest backup: $dest"
Write-Host "Restore: powershell -File gunsrpg-1201\tools\restore-gun-stack.ps1 -BackupDir `"$dest`""
