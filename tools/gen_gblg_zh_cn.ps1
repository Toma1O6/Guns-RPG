# Generate zh_cn.json for greenboys_legendary_guns from en_us.json
$ErrorActionPreference = 'Stop'
$enPath = Join-Path $PSScriptRoot '_extract_gblg/out/assets/greenboys_legendary_guns/lang/en_us.json'
$outPath = 'd:\minecraft\.minecraft\versions\1.20.1-Forge_47.4.20\config\paxi\resourcepacks\无暇赴死汉化包\assets\greenboys_legendary_guns\lang\zh_cn.json'
$en = Get-Content $enPath -Raw -Encoding UTF8 | ConvertFrom-Json

# Exact English value -> Simplified Chinese (219 unique values, many repeated)
$byValue = @{
    'Scar-L' = 'SCAR-L'
    'D-Eagle' = '沙漠之鹰'
    'Craft' = '制作'
    'AKM' = 'AKM'
    'Grip' = '握把'
    'M249' = 'M249'
    'Pistol Ammo' = '手枪弹药'
    'P90' = 'P90'
    'Vector' = 'Vector 冲锋枪'
    'Shoot' = '射击'
    'Ak-74' = 'AK-74'
    'Guns' = '枪械'
    'M16' = 'M16'
    '%1$s Was Eliminated By %2$s' = '%1$s 被 %2$s 击毙'
    'silencer' = '消音器'
    '%1$s Eliminated' = '%1$s 阵亡'
    'OBJECTIVES:' = '任务目标：'
    'MP5' = 'MP5'
    'AUG' = 'AUG'
    '/' = '/'
    'Desert Eagle' = '沙漠之鹰'
    'Bullets' = '子弹'
    'Scar-H' = 'SCAR-H'
    'Take Back The Secret ' = '夺回机密资料 '
    'Rifle Ammo' = '步枪弹药'
    'AK74' = 'AK-74'
    'Greenboy Legendary Guns' = 'Greenboy 传奇枪械'
    'AMMO' = '弹药'
    'SKS' = 'SKS'
    'Ak-47' = 'AK-47'
    'MiniGun' = '加特林'
    'Silencer' = '消音器'
    'Arena Finder' = '竞技场定位器'
    'SG Ammo' = '霰弹弹药'
    'Reload' = '换弹'
    'Iron Tower' = '钢铁塔楼'
    'RPG' = 'RPG'
    'Attachements' = '配件'
    'sgt. Frost' = '弗罗斯特中士'
    'Thompson' = '汤姆逊冲锋枪'
    'Minigun' = '加特林'
    'greenboy_legendary_guns' = 'Greenboy 传奇枪械'
    'Pump Shotgun' = '泵动霰弹枪'
    'Gun Crafter' = '枪械合成台'
    'Pistol Bullet' = '手枪弹'
    'RPG-7' = 'RPG-7'
    'Revolver' = '左轮手枪'
    '> Kill The Enemies In City' = '> 肃清城区敌人'
    'M1014 Shotgun' = 'M1014 霰弹枪'
    'Inspect Gun' = '检视枪械'
    'M1014-Shotgun' = 'M1014 霰弹枪'
    'Mossberg 500-shotgun' = '莫斯伯格 500 霰弹枪'
    'Gun' = '枪械'
    'Double USP' = '双持 USP'
    'M4A1' = 'M4A1'
    'scope' = '瞄准镜'
    'cpt. Cost' = '科斯特上尉'
    'Detach' = '拆卸'
    'Glock 18' = '格洛克 18'
    'Sniper Bullets' = '狙击弹'
    'Machinegun & Minigun Ammo' = '机枪与加特林弹药'
    'UI' = '界面'
    'AK47' = 'AK-47'
    '> YOU ARE CAPTURED!' = '> 你已被俘！'
    '> Rescue SGT. Frost From' = '> 从建筑中营救弗罗斯特中士'
    'Attachments' = '配件'
    'Rifle Bullet' = '步枪弹'
    'Level 2' = '第二关'
    'AWM' = 'AWM 狙击步枪'
    '> Follow Captain Cost' = '> 跟随科斯特上尉'
    'The Building' = '目标建筑'
    '> Mission Completed!' = '> 任务完成！'
    'Submachine Gun Bullet' = '冲锋枪弹'
    '> Open The Iron Door And' = '> 打开铁门并前进'
    'RPG Ammo' = 'RPG 弹药'
    'G18' = '格洛克 18'
    'SMG Ammo' = '冲锋枪弹药'
    'ShotGun Bullets' = '霰弹'
    '> Break Into The Iron Tower' = '> 攻入钢铁塔楼'
    'Scope' = '瞄准镜'
    'Level 3' = '第三关'
    'MP40' = 'MP40'
    'The Rescue' = '营救行动'
    'grip' = '握把'
    'Machinegun Ammo' = '机枪弹药'
    'USP-2' = '双持 USP'
    '> Clear The Enemies' = '> 肃清敌人'
}

$zh = [ordered]@{}
foreach ($prop in $en.PSObject.Properties) {
    $key = $prop.Name
    $val = [string]$prop.Value
    if ($val -eq '') {
        $zh[$key] = ''
        continue
    }
    if ($byValue.ContainsKey($val)) {
        $zh[$key] = $byValue[$val]
    }
    else {
        Write-Warning "Untranslated: [$key] = $val"
        $zh[$key] = $val
    }
}

$dir = Split-Path $outPath -Parent
New-Item -ItemType Directory -Force -Path $dir | Out-Null
$zh | ConvertTo-Json -Depth 3 | Set-Content $outPath -Encoding UTF8
Write-Host "Wrote $($zh.Count) keys to $outPath"
