#!/usr/bin/env python3
"""从 Guns RPG 1.16 备份同步枪械台配方，并应用本包分级材料补丁。"""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
SRC = ROOT / "backups" / "gunsrpg-1.16.5-1.1.0" / "data" / "gunsrpg" / "recipes" / "smithing"
OUT = Path(__file__).resolve().parents[1] / "src" / "main" / "resources" / "data" / "gunsrpg" / "recipes" / "gunsmith"

ITEM_ALIAS = {
    "gunsrpg:iron_stock": "gunsrpg:small_iron_stock",
}

# 威力越高，额外「高级材料」越多（在备份合理零件配方基础上追加）
GUN_PATCHES = {
    "m1911": {},
    "r45": {
        "extra_key": {"c": {"item": "minecraft:copper_ingot"}},
        "pattern_replace": [("bgs", "bgc")],
    },
    "desert_eagle": {
        "extra_key": {"o": {"item": "minecraft:gold_ingot"}},
        "pattern_replace": [("bgs", "bgo"), (" gm", " og")],
    },
    "thompson": {
        "extra_key": {"i": {"item": "minecraft:iron_ingot"}},
        "pattern_replace": [("bgw", "big"), (" m ", " i ")],
    },
    "vector": {
        "extra_key": {"i": {"item": "minecraft:iron_ingot"}},
        "pattern_replace": [(" mg", "mig")],
    },
    "akm": {
        "extra_key": {"i": {"item": "minecraft:iron_ingot"}},
        "pattern_replace": [("m w", "miw")],
    },
    "vss": {
        "extra_key": {"i": {"item": "minecraft:iron_ingot"}, "d": {"item": "minecraft:diamond"}},
        "pattern_replace": [(" m ", "mdi")],
    },
    "winchester": {},
    "kar98k": {
        "extra_key": {"i": {"item": "minecraft:iron_ingot"}},
        "pattern_replace": [("lgw", "lgi")],
    },
    "awm": {
        "extra_key": {
            "n": {"item": "minecraft:netherite_block"},
            "i": {"item": "gunsrpg:small_iron_stock"},
        },
        "pattern": ["nlg", "ngmi", " ng"],
    },
    "s1897": {
        "pattern": ["lgw", " w ", "   "],
        "drop_keys": ["i"],
    },
    "s686": {},
    "s12k": {
        "extra_key": {"n": {"item": "minecraft:netherite_ingot"}},
        "pattern_replace": [(" g ", " n ")],
    },
    "minigun": {
        "pattern": ["lgl", "gmi", "nin"],
        "key": {
            "l": {"item": "gunsrpg:long_barrel"},
            "g": {"item": "gunsrpg:gun_parts"},
            "m": {"item": "gunsrpg:magazine"},
            "i": {"item": "gunsrpg:small_iron_stock"},
            "n": {"item": "minecraft:netherite_ingot"},
        },
        "skill": "minigun_assembly",
    },
}


def map_item(obj):
    if "item" not in obj:
        return obj
    gid = obj["item"]
    return {"item": ITEM_ALIAS.get(gid, gid)}


def map_skill(requirements):
    for req in requirements or []:
        if req.get("type") == "gunsrpg:skill":
            sk = req.get("predicate", {}).get("skill", "")
            return sk.replace("gunsrpg:", "")
    return ""


def convert(recipe_id, data):
    pattern = data.get("pattern")
    if not pattern:
        return None
    key = {k: map_item(v) for k, v in data.get("key", {}).items()}
    result = data.get("result", {})
    if not result.get("item", "").startswith("gunsrpg:"):
        return None
    skill = map_skill(data.get("requirements"))
    out = {
        "type": "gunsrpg:gunsmith",
        "pattern": pattern,
        "key": key,
        "result": {"item": result["item"], "count": result.get("count", 1)},
    }
    if skill:
        out["skill"] = skill
    return out


def apply_patch(out, patch):
    if not patch:
        return out
    if "pattern" in patch:
        out["pattern"] = patch["pattern"]
    key = out.get("key", {})
    for drop in patch.get("drop_keys", []):
        key.pop(drop, None)
    for k, v in patch.get("key", {}).items():
        key[k] = v
    for k, v in patch.get("extra_key", {}).items():
        key[k] = v
    for old, new in patch.get("pattern_replace", []):
        out["pattern"] = [line.replace(old, new) for line in out["pattern"]]
    if patch.get("skill"):
        out["skill"] = patch["skill"]
    out["key"] = key
    return out


def main():
    OUT.mkdir(parents=True, exist_ok=True)
    guns = set(GUN_PATCHES.keys())
    written = 0
    for gun in sorted(guns):
        src_file = SRC / f"{gun}.json"
        if gun == "minigun" or not src_file.is_file():
            patch = GUN_PATCHES[gun]
            if "pattern" not in patch:
                print(f"skip {gun}: no source")
                continue
            out = {
                "type": "gunsrpg:gunsmith",
                "pattern": patch["pattern"],
                "key": patch["key"],
                "result": {"item": f"gunsrpg:{gun}", "count": 1},
            }
            if patch.get("skill"):
                out["skill"] = patch["skill"]
        else:
            data = json.loads(src_file.read_text(encoding="utf-8"))
            out = convert(gun, data)
            if out is None:
                print(f"skip {gun}: convert failed")
                continue
            out = apply_patch(out, GUN_PATCHES.get(gun, {}))
        out_path = OUT / f"{gun}.json"
        out_path.write_text(json.dumps(out, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
        written += 1
        print("wrote", out_path.name)
    print(f"done: {written} gun recipes")


if __name__ == "__main__":
    main()
