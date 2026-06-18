#!/usr/bin/env python3
"""把模板枪扩展天赋的 lang 条目复制到克隆枪（与 clone_weapon_extensions.py 一致）。"""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
LANG_DIR = ROOT / "src" / "main" / "resources" / "assets" / "gunsrpg" / "lang"
SKILLS_SUFFIX = ("zh_cn_skills.json", "en_us_skills.json")

CLONES: list[tuple[str, str]] = [
    ("glock", "m1911"),
    ("p90", "vector"),
    ("uzi", "vector"),
    ("gatling", "vector"),
    ("type_81", "hk416"),
    ("pkm", "hk416"),
    ("m249", "hk416"),
]

PREFIX = "skill.gunsrpg."


def clone_keys(data: dict, weapon: str, template: str) -> int:
    added = 0
    pat = re.compile(rf"^{re.escape(PREFIX)}{template}_(\w+)\.(.+)$")
    new_items: dict[str, str] = {}
    for key, val in data.items():
        m = pat.match(key)
        if not m:
            continue
        suffix, rest = m.group(1), m.group(2)
        dst = f"{PREFIX}{weapon}_{suffix}.{rest}"
        if dst not in data:
            new_items[dst] = val
    for k, v in new_items.items():
        data[k] = v
        added += 1
    return added


def main() -> None:
    for fname in SKILLS_SUFFIX:
        path = LANG_DIR / fname
        if not path.exists():
            print(f"skip {path}")
            continue
        data = json.loads(path.read_text(encoding="utf-8"))
        total = 0
        for weapon, template in CLONES:
            total += clone_keys(data, weapon, template)
        path.write_text(json.dumps(data, indent=4, ensure_ascii=False) + "\n", encoding="utf-8")
        print(f"{fname}: +{total} keys")


if __name__ == "__main__":
    main()
