$PackRoot = (Resolve-Path (Join-Path $PSScriptRoot "..\..")).Path
$Src = Join-Path $PackRoot "backups\gunsrpg-1.16.5-1.1.0\data\gunsrpg\recipes\smithing"
$Out = Join-Path $PackRoot "gunsrpg\src\main\resources\data\gunsrpg\recipes\gunsmith"
$map = Get-Content (Join-Path $PackRoot "config\gunsrpg\item_mapping.json") -Encoding UTF8 -Raw | ConvertFrom-Json
$wmap = Get-Content (Join-Path $PackRoot "config\gunsrpg\weapon_mapping.json") -Encoding UTF8 -Raw | ConvertFrom-Json
$items = @{}
$map.items.PSObject.Properties | ForEach-Object { $items[$_.Name] = $_.Value }
if ($wmap.weapons) {
    $wmap.weapons.PSObject.Properties | ForEach-Object {
        $gid = "gunsrpg:" + $_.Name
        if (-not $items.ContainsKey($gid)) { $items[$gid] = $_.Value }
    }
}
# common parts proxies
@{
    "gunsrpg:magazine" = "minecraft:iron_nugget"
    "gunsrpg:small_iron_stock" = "minecraft:stick"
    "gunsrpg:long_barrel" = "minecraft:iron_ingot"
    "gunsrpg:short_barrel" = "minecraft:iron_ingot"
    "gunsrpg:wooden_hammer" = "minecraft:wooden_pickaxe"
    "gunsrpg:stone_hammer" = "minecraft:stone_pickaxe"
    "gunsrpg:iron_hammer" = "minecraft:iron_pickaxe"
    "gunsrpg:trigger" = "minecraft:flint"
    "gunsrpg:spring" = "minecraft:iron_nugget"
    "gunsrpg:receiver" = "minecraft:iron_block"
}.GetEnumerator() | ForEach-Object { if (-not $items.ContainsKey($_.Key)) { $items[$_.Key] = $_.Value } }
$skills = @{}
if ($map.skills) { $map.skills.PSObject.Properties | ForEach-Object { $skills[$_.Name] = $_.Value } }

function Map-Item($obj) {
    if ($obj.item) {
        $gid = [string]$obj.item
        if ($items.ContainsKey($gid)) { return @{ item = $items[$gid] } }
        if ($gid.StartsWith("gunsrpg:")) { return $null }
        return @{ item = $gid }
    }
    if ($obj.tag) { return @{ tag = [string]$obj.tag } }
    return $null
}

function Map-Skill($reqs) {
    if (-not $reqs) { return "" }
    foreach ($r in $reqs) {
        if ($r.type -eq "gunsrpg:skill") {
            $sk = [string]$r.predicate.skill -replace "^gunsrpg:", ""
            $full = "gunsrpg:$sk"
            if ($skills.ContainsKey($full)) { return $skills[$full] }
            return $sk.Replace("gunsrpg:", "")
        }
    }
    return ""
}

New-Item -ItemType Directory -Force -Path $Out | Out-Null
$count = 0; $skip = 0
Get-ChildItem $Src -Filter *.json | ForEach-Object {
    $data = Get-Content $_.FullName -Encoding UTF8 -Raw | ConvertFrom-Json
    if ($data.type -ne "gunsrpg:smithing_table") { return }
    if (-not $data.pattern) { $skip++; return }
    $key = @{}
    $ok = $true
    $data.key.PSObject.Properties | ForEach-Object {
        $m = Map-Item $_.Value
        if ($null -eq $m) { $ok = $false }
        else { $key[$_.Name] = $m }
    }
    if (-not $ok) { $skip++; return }
    $ritem = [string]$data.result.item
    if ($items.ContainsKey($ritem)) {
        $cnt = 1
        if ($data.result.count) { $cnt = [int]$data.result.count }
        $result = @{ item = $items[$ritem]; count = $cnt }
    } elseif ($ritem.StartsWith("gunsrpg:")) { $skip++; return }
    else {
        $cnt = 1
        if ($data.result.count) { $cnt = [int]$data.result.count }
        $result = @{ item = $ritem; count = $cnt }
    }
    $outObj = [ordered]@{
        type = "gunsrpg:gunsmith"
        pattern = @($data.pattern)
        key = $key
        result = $result
    }
    $sk = Map-Skill $data.requirements
    if ($sk) { $outObj.skill = $sk }
    $json = $outObj | ConvertTo-Json -Depth 10 -Compress:$false
    [System.IO.File]::WriteAllText((Join-Path $Out "$($_.BaseName).json"), $json, [System.Text.UTF8Encoding]::new($false))
    $count++
}
Write-Host "Generated $count recipes, skipped $skip at $Out"
