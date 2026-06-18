#!/usr/bin/env python3
"""从 Guns RPG 备份生成 wf_firearms 枪械台配方 data 包。"""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[ 2]
SRC = ROOT / "backups" / "gunsrpg-1.16.5-1.1.0" / "data" / "gunsrpg" / "recipes" / "smithing"
OUT = ROOT / "wf_firearms" / "src" / "main" / "resources" / "data" / "wf_firearms" / "recipes" / "gunsmith"
MAPPING = json.loads((ROOT / "config" / "wf_firearms" / "item_mapping.json").read_text(encoding="utf-8"))
ITEM_MAP = MAPPING["items"]
SKILL_MAP = MAPPING.get("skills", {})


def map_item(obj):
    if "item" in obj:
        gid = obj["item"]
        if gid in ITEM_MAP:
            return {"item": ITEM_MAP[gid]}
        if gid.startswith("gunsrpg:"):
            return {"item": "minecraft:barrier"}
        return obj
    if "tag" in obj:
        return obj
    return obj


def map_skill(requirements):
    for req in requirements or []:
        if req.get("type") == "gunsrpg:skill":
            sk = req.get("predicate", {}).get("skill", "")
            sk = sk.replace("gunsrpg:", "")
            return SKILL_MAP.get("gunsrpg:" + sk, sk)
    return ""


def convert(recipe_id, data):
    pattern = data.get("pattern")
    if not pattern:
        return None
    key = {k: map_item(v) for k, v in data.get("key", {}).items()}
    result = data.get("result", {})
    ritem = result.get("item", "")
    if ritem in ITEM_MAP:
        result = {"item": ITEM_MAP[ritem], "count": result.get("count", 1)}
    elif ritem.startswith("gunsrpg:"):
        return None
    skill = map_skill(data.get("requirements"))
    out = {
        "type": "wf_firearms:gunsmith",
        "pattern": pattern,
        "key": key,
        "result": result,
    }
    if skill:
        out["skill"] = skill
    return out


def main():
    OUT.mkdir(parents=True, exist_ok=True)
    count = 0
    skip = 0
    for f in sorted(SRC.glob("*.json")):
        data = json.loads(f.read_text(encoding="utf-8"))
        if data.get("type") != "gunsrpg:smithing_table":
            continue
        conv = convert(f.stem, data)
        if conv is None:
            skip += 1
            continue
        out_path = OUT / f"{f.stem}.json"
        out_path.write_text(json.dumps(conv, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
        count += 1
    print(f"Generated {count} recipes, skipped {skip}")


if __name__ == "__main__":
    main()
