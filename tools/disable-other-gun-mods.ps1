# 暂时禁用除 CGM + gunsrpg 以外的枪械 mod，避免按键/伤害/进度冲突
# 恢复：Rename-Item *.jar.disabled *.jar（去掉 .disabled 后缀）
$ErrorActionPreference = "Stop"
$mods = Join-Path (Split-Path (Split-Path $PSScriptRoot)) "mods"

# 子串匹配（小写）；留空表示仅按 Keep 白名单外禁用
$DisablePatterns = @(
    "greenboys_legendary_guns",
    "chromaticarsenal",
    "wf_firearms"
)

# 始终保留启用（文件名子串，小写）
$KeepPatterns = @(
    "cgm-unofficial",
    "gunsrpg-",
    "configuration-forge"
)

function Test-KeepMod([string]$name) {
    $lower = $name.ToLowerInvariant()
    foreach ($k in $KeepPatterns) {
        if ($lower.Contains($k.ToLowerInvariant())) { return $true }
    }
    return $false
}

function Test-DisableMod([string]$name) {
    if ($name.EndsWith(".disabled")) { return $false }
    if (-not $name.EndsWith(".jar")) { return $false }
    if (Test-KeepMod $name) { return $false }
    $lower = $name.ToLowerInvariant()
    foreach ($p in $DisablePatterns) {
        if ($lower.Contains($p.ToLowerInvariant())) { return $true }
    }
    return $false
}

$disabled = @()
Get-ChildItem $mods -File | ForEach-Object {
    if (-not (Test-DisableMod $_.Name)) { return }
    $dest = $_.FullName + ".disabled"
    if (Test-Path $dest) { Remove-Item $dest -Force }
    Rename-Item $_.FullName $dest
    $disabled += $_.Name
    Write-Host "Disabled: $($_.Name)"
}

if ($disabled.Count -eq 0) {
    Write-Host "No matching gun mods to disable (already off?)."
} else {
    Write-Host ""
    Write-Host "Active gun stack: CGM-Unofficial + gunsrpg + configuration"
    Write-Host "Restart Minecraft completely."
}
