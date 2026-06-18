import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const enPath = path.join(__dirname, '_extract_gblg/out/assets/greenboys_legendary_guns/lang/en_us.json');
const outPath = path.resolve(__dirname, '../../config/paxi/resourcepacks/\u65e0\u6687\u8d74\u6b7b\u6c49\u5316\u5305/assets/greenboys_legendary_guns/lang/zh_cn.json');

const en = JSON.parse(fs.readFileSync(enPath, 'utf8'));

const byValue = {
  'Scar-L': 'SCAR-L',
  'D-Eagle': '\u6c99\u6f20\u4e4b\u9e70',
  Craft: '\u5236\u4f5c',
  AKM: 'AKM',
  Grip: '\u63e1\u628a',
  M249: 'M249',
  'Pistol Ammo': '\u624b\u67aa\u5f39\u836f',
  P90: 'P90',
  Vector: 'Vector \u51b2\u950b\u67aa',
  Shoot: '\u5c04\u51fb',
  'Ak-74': 'AK-74',
  Guns: '\u67aa\u68b0',
  M16: 'M16',
  '%1$s Was Eliminated By %2$s': '%1$s \u88ab %2$s \u51fb\u6bd9',
  silencer: '\u6d88\u97f3\u5668',
  '%1$s Eliminated': '%1$s \u9635\u4ea1',
  'OBJECTIVES:': '\u4efb\u52a1\u76ee\u6807\uff1a',
  MP5: 'MP5',
  AUG: 'AUG',
  '/': '/',
  'Desert Eagle': '\u6c99\u6f20\u4e4b\u9e70',
  Bullets: '\u5b50\u5f39',
  'Scar-H': 'SCAR-H',
  'Take Back The Secret ': '\u593a\u56de\u673a\u5bc6\u8d44\u6599 ',
  'Rifle Ammo': '\u6b65\u67aa\u5f39\u836f',
  AK74: 'AK-74',
  'Greenboy Legendary Guns': "Greenboy \u4f20\u5947\u67aa\u68b0",
  AMMO: '\u5f39\u836f',
  SKS: 'SKS',
  'Ak-47': 'AK-47',
  MiniGun: '\u52a0\u7279\u6797',
  Silencer: '\u6d88\u97f3\u5668',
  'Arena Finder': '\u7ade\u6280\u573a\u5b9a\u4f4d\u5668',
  'SG Ammo': '\u9730\u5f39\u5f39\u836f',
  Reload: '\u6362\u5f39',
  'Iron Tower': '\u94a2\u94c1\u5854\u697c',
  RPG: 'RPG',
  Attachements: '\u914d\u4ef6',
  'sgt. Frost': '\u5f17\u7f57\u65af\u7279\u4e2d\u58eb',
  Thompson: '\u6c64\u59c6\u900a\u51b2\u950b\u67aa',
  Minigun: '\u52a0\u7279\u6797',
  greenboy_legendary_guns: "Greenboy \u4f20\u5947\u67aa\u68b0",
  'Pump Shotgun': '\u6cf5\u52a8\u9730\u5f39\u67aa',
  'Gun Crafter': '\u67aa\u68b0\u5408\u6210\u53f0',
  'Pistol Bullet': '\u624b\u67aa\u5f39',
  'RPG-7': 'RPG-7',
  Revolver: '\u5de6\u8f6e\u624b\u67aa',
  '> Kill The Enemies In City': '> \u8083\u6e05\u57ce\u533a\u654c\u4eba',
  'M1014 Shotgun': 'M1014 \u9730\u5f39\u67aa',
  'Inspect Gun': '\u68c0\u89c6\u67aa\u68b0',
  'M1014-Shotgun': 'M1014 \u9730\u5f39\u67aa',
  'Mossberg 500-shotgun': '\u83ab\u65af\u4f2f\u683c 500 \u9730\u5f39\u67aa',
  Gun: '\u67aa\u68b0',
  'Double USP': '\u53cc\u6301 USP',
  M4A1: 'M4A1',
  scope: '\u7784\u51c6\u955c',
  'cpt. Cost': '\u79d1\u65af\u7279\u4e0a\u5c09',
  Detach: '\u62c6\u5378',
  'Glock 18': '\u683c\u6d1b\u514b 18',
  'Sniper Bullets': '\u72d9\u51fb\u5f39',
  'Machinegun & Minigun Ammo': '\u673a\u67aa\u4e0e\u52a0\u7279\u6797\u5f39\u836f',
  UI: '\u754c\u9762',
  AK47: 'AK-47',
  '> YOU ARE CAPTURED!': '> \u4f60\u5df2\u88ab\u4ff8\uff01',
  '> Rescue SGT. Frost From': '> \u4ece\u5efa\u7b51\u4e2d\u8425\u6551\u5f17\u7f57\u65af\u7279\u4e2d\u58eb',
  Attachments: '\u914d\u4ef6',
  'Rifle Bullet': '\u6b65\u67aa\u5f39',
  'Level 2': '\u7b2c\u4e8c\u5173',
  AWM: 'AWM \u72d9\u51fb\u67aa',
  '> Follow Captain Cost': '> \u8ddf\u968f\u79d1\u65af\u7279\u4e0a\u5c09',
  'The Building': '\u76ee\u6807\u5efa\u7b51',
  '> Mission Completed!': '> \u4efb\u52a1\u5b8c\u6210\uff01',
  'Submachine Gun Bullet': '\u51b2\u950b\u67aa\u5f39',
  '> Open The Iron Door And': '> \u6253\u5f00\u94c1\u95e8\u5e76\u524d\u8fdb',
  'RPG Ammo': 'RPG \u5f39\u836f',
  G18: '\u683c\u6d1b\u514b 18',
  'SMG Ammo': '\u51b2\u950b\u67aa\u5f39\u836f',
  'ShotGun Bullets': '\u9730\u5f39',
  '> Break Into The Iron Tower': '> \u653b\u5165\u94a2\u94c1\u5854\u697c',
  Scope: '\u7784\u51c6\u955c',
  'Level 3': '\u7b2c\u4e09\u5173',
  MP40: 'MP40',
  'The Rescue': '\u8425\u6551\u884c\u52a8',
  grip: '\u63e1\u628a',
  'Machinegun Ammo': '\u673a\u67aa\u5f39\u836f',
  'USP-2': '\u53cc\u6301 USP',
  '> Clear The Enemies': '> \u8083\u6e05\u654c\u4eba',
};

const zh = {};
let missing = 0;
for (const [key, val] of Object.entries(en)) {
  if (val === '') {
    zh[key] = '';
    continue;
  }
  if (Object.prototype.hasOwnProperty.call(byValue, val)) {
    zh[key] = byValue[val];
  } else {
    console.warn('MISSING:', JSON.stringify(val));
    zh[key] = val;
    missing++;
  }
}

fs.mkdirSync(path.dirname(outPath), { recursive: true });
fs.writeFileSync(outPath, JSON.stringify(zh, null, 2) + '\n', 'utf8');
console.log('keys:', Object.keys(zh).length, 'missing:', missing);
console.log('wrote', outPath);
