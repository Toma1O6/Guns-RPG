#!/usr/bin/env python3
"""从模板枪复制武器扩展技能 JSON 与 assembly.extensions 列表。"""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(r"d:\minecraft\.minecraft\versions\1.20.1-Forge_47.4.20\config\gunsrpg")
SKILLS = ROOT / "port_from_gunsrpg" / "skill_properties"
EXTRA = ROOT / "extra_skills"

# (weapon_key, template_key, extension_suffixes)
CLONES: list[tuple[str, str, list[str]]] = [
    (
        "glock",
        "m1911",
        [
            "carbon_barrel",
            "every_bullet_counts",
            "extended",
            "heavy_bullets",
            "killing_spree",
            "quickdraw",
            "suppressor",
            "tough_spring",
        ],
    ),
    (
        "p90",
        "vector",
        [
            "quickdraw",
            "extended",
            "extended_mk2",
            "overloaded",
            "suppressor",
            "red_dot",
            "vertical",
        ],
    ),
    (
        "uzi",
        "vector",
        [
            "quickdraw",
            "extended",
            "extended_mk2",
            "overloaded",
            "suppressor",
            "red_dot",
            "vertical",
        ],
    ),
    (
        "type_81",
        "hk416",
        [
            "cheekpad",
            "extended",
            "extended_mk2",
            "overloaded",
            "quickdraw",
            "red_dot",
            "suppressor",
            "vertical",
        ],
    ),
    (
        "pkm",
        "hk416",
        [
            "cheekpad",
            "extended",
            "extended_mk2",
            "overloaded",
            "quickdraw",
            "red_dot",
            "suppressor",
            "vertical",
        ],
    ),
    (
        "m249",
        "hk416",
        [
            "cheekpad",
            "extended",
            "extended_mk2",
            "overloaded",
            "quickdraw",
            "red_dot",
            "suppressor",
            "vertical",
        ],
    ),
    (
        "gatling",
        "vector",
        ["quickdraw", "extended", "overloaded", "suppressor"],
    ),
]


def clone_skill(src_name: str, dst_name: str, weapon: str, template: str) -> None:
    src = SKILLS / f"{src_name}.json"
    dst = SKILLS / f"{dst_name}.json"
    if dst.exists():
        return
    if not src.exists():
        print(f"skip missing template {src}")
        return
    text = src.read_text(encoding="utf-8")
    text = text.replace(f"gunsrpg:{template}", f"gunsrpg:{weapon}")
    text = re.sub(rf'"{template}"', f'"{weapon}"', text)
    dst.write_text(text, encoding="utf-8")
    print(f"wrote {dst.name}")


def patch_assembly(weapon: str, extensions: list[str]) -> None:
    asm_name = f"{weapon}_assembly.json"
    path = EXTRA / asm_name
    if not path.exists():
        print(f"no assembly {path}")
        return
    data = json.loads(path.read_text(encoding="utf-8"))
    data.setdefault("hierarchy", {})["extensions"] = [
        f"gunsrpg:{weapon}_{s}" for s in extensions
    ]
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(f"patched {path.name}")


def main() -> None:
    for weapon, template, suffixes in CLONES:
        ext_ids = []
        for suffix in suffixes:
            src_id = f"{template}_{suffix}"
            dst_id = f"{weapon}_{suffix}"
            ext_ids.append(dst_id)
            clone_skill(src_id, dst_id, weapon, template)
        patch_assembly(weapon, suffixes)


if __name__ == "__main__":
    main()
