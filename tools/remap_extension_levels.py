#!/usr/bin/env python3
"""Remap weapon extension talent levels to smoother 0/1/3/5/7 progression."""
import json
from pathlib import Path

PROPS = Path(__file__).resolve().parents[2] / "config" / "gunsrpg" / "port_from_gunsrpg" / "skill_properties"

LEVEL_0_SUFFIXES = (
    "_quickdraw",
    "_vertical",
    "_vertical_grip",
    "_bullet_loops",
)

LEVEL_3_SUFFIXES = ("_suppressor", "_quick_shift", "_shrapnel")

LEVEL_1_SUFFIXES = ("_reliable", "_cheekpad", "_precision_care", "_power_knockback")

LEVEL_7_SUFFIXES = (
    "_overloaded",
    "_extended_mk2",
    "_every_bullet_counts",
    "_soul_taker",
    "_dead_eye",
    "_extended_barrel",
    "_adaptive_chambering",
    "_killing_spree",
    "_finisher",
    "_cruel",
    "_ace_of_hearts",
    "_demolition_expert",
    "_scalding_hot",
    "_brutal_cannon",
    "_hot_hands",
    "_back_three",
    "_heavy_rounds",
    "_harvester",
    "_veteran_hunter",
    "_atrocity",
    "_glory_kill",
    "_close_quarters",
    "_armor_bane",
    "_unarmored_loot",
)

LEVEL_5_SUFFIXES = (
    "_red_dot",
    "_compensator",
    "_heavy_bullets",
    "_heavy_bolts",
    "_tough_spring",
    "_penetrator",
    "_fast_hands",
    "_carbon_barrel",
    "_pump_in_action",
    "_never_give_up",
    "_quick_mover",
    "_choke",
    "_light_trigger",
    "_hunter",
    "_repeater",
    "_better_cartridge",
    "_rapid_fire",
    "_blazing_pellets",
    "_cannon_blast",
    "_deft_reload",
    "_gear_grinder",
    "_quiver",
    "_tough_bowstring",
    "_poisoned_bolts",
    "_cold_blooded",
    "_commando",
)

SPECIAL = {
    "akm_extended": 3,
    "s12k_never_give_up": 3,
    "s12k_red_dot": 1,
    "s686_hunter": 1,
}


def ends_with_any(name: str, suffixes: tuple[str, ...]) -> bool:
    return any(name.endswith(s) for s in suffixes)


def map_level(skill_id: str, old: int) -> int:
    if skill_id in SPECIAL:
        return SPECIAL[skill_id]
    if ends_with_any(skill_id, LEVEL_0_SUFFIXES):
        return 0
    if skill_id.endswith("_scope") and old == 0:
        return 0
    if skill_id.endswith("_extended") and old == 0:
        return 0
    if ends_with_any(skill_id, LEVEL_1_SUFFIXES):
        return 1
    if ends_with_any(skill_id, LEVEL_3_SUFFIXES):
        return 3
    if skill_id.endswith("_extended") and old == 5:
        return 3
    if ends_with_any(skill_id, LEVEL_7_SUFFIXES):
        return 7
    if ends_with_any(skill_id, LEVEL_5_SUFFIXES):
        return 5
    if old in (0, 1, 3, 5, 7):
        return old
    if old == 5:
        return 5
    if old == 7:
        return 7
    return old


def main() -> None:
    changed = 0
    for path in sorted(PROPS.glob("*.json")):
        data = json.loads(path.read_text(encoding="utf-8"))
        props = data.get("properties")
        if not props:
            continue
        validator = props.get("transactionValidator", {})
        if validator.get("type") != "gunsrpg:weapon":
            continue
        skill_id = path.stem
        old = props.get("level", 0)
        new = map_level(skill_id, old)
        if new != old:
            props["level"] = new
            path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
            changed += 1
            print(f"{skill_id}: {old} -> {new}")
    print(f"Updated {changed} files")


if __name__ == "__main__":
    main()
