"""生成生存栏 IV/V 技能 JSON，并更新 III 级 children；从 III 级图标生成 IV/V。"""
import json
from pathlib import Path

try:
    from PIL import Image
except ImportError:
    Image = None

PORT = Path(r"d:\minecraft\.minecraft\versions\1.20.1-Forge_47.4.20\config\gunsrpg\port_from_gunsrpg\skill_properties")
EXTRA = Path(r"d:\minecraft\.minecraft\versions\1.20.1-Forge_47.4.20\config\gunsrpg\extra_skills")
INDEX = Path(r"d:\minecraft\.minecraft\versions\1.20.1-Forge_47.4.20\config\gunsrpg\port_from_gunsrpg\skill_index.json")
ICONS = Path(r"d:\minecraft\.minecraft\versions\1.20.1-Forge_47.4.20\gunsrpg-1201\src\main\resources\assets\gunsrpg\textures\icons")

CHAINS = {
    "agility": (50, 5, 65, 7),
    "adrenaline_rush": (55, 6, 70, 8),
    "like_a_cat": (58, 5, 72, 7),
    "second_chance": (95, 11, 100, 12),
    "well_fed": (68, 6, 80, 8),
    "skull_crusher": (75, 7, 88, 9),
    "strong_muscles": (52, 6, 65, 8),
}

CHEF = [
    ("sous_chef_iii", "master_chef", 60, 4, "head_chef_iv"),
    ("head_chef_iv", "sous_chef_iii", 75, 6, "executive_chef_v"),
    ("executive_chef_v", "head_chef_iv", 90, 8, None),
]


def write_node(path: Path, node_id: str, parent: str | None, child: str | None, level: int, price: int):
    hierarchy = {"category": "survival"}
    if parent:
        hierarchy["parent"] = f"gunsrpg:{parent}"
    if child:
        hierarchy["children"] = [f"gunsrpg:{child}"]
        hierarchy["override"] = f"gunsrpg:{child}"
    data = {
        "hierarchy": hierarchy,
        "properties": {
            "level": level,
            "price": price,
            "transactionValidator": {"type": "gunsrpg:level"},
        },
    }
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def update_iii(prefix: str, child: str):
    path = PORT / f"{prefix}_iii.json"
    if not path.exists():
        print("missing", path)
        return
    data = json.loads(path.read_text(encoding="utf-8"))
    data.setdefault("hierarchy", {})["children"] = [f"gunsrpg:{child}"]
    data["hierarchy"]["override"] = f"gunsrpg:{child}"
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def gen_chain(prefix: str, iv_level: int, iv_price: int, v_level: int, v_price: int):
    iv_id = f"{prefix}_iv"
    v_id = f"{prefix}_v"
    update_iii(prefix, iv_id)
    write_node(PORT / f"{iv_id}.json", iv_id, f"{prefix}_iii", v_id, iv_level, iv_price)
    write_node(PORT / f"{v_id}.json", v_id, iv_id, None, v_level, v_price)


def gen_chef():
    master = PORT / "master_chef.json"
    data = json.loads(master.read_text(encoding="utf-8"))
    data.setdefault("hierarchy", {})["children"] = ["gunsrpg:sous_chef_iii"]
    data["hierarchy"]["override"] = "gunsrpg:sous_chef_iii"
    master.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    EXTRA.mkdir(parents=True, exist_ok=True)
    for node_id, parent, level, price, child in CHEF:
        write_node(EXTRA / f"{node_id}.json", node_id, parent, child, level, price)


def update_index():
    data = json.loads(INDEX.read_text(encoding="utf-8"))
    roots = data["roots_by_category"]["survival"]
    data["roots_by_category"]["survival"] = [
        r for r in roots if r not in ("treasure_hunter_i", "traps_i")
    ]
    INDEX.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def tint_gold(img: Image.Image) -> Image.Image:
    out = img.copy()
    px = []
    for r, g, b, a in img.getdata():
        if a == 0:
            px.append((r, g, b, a))
        else:
            px.append((min(255, int(r * 1.15 + 40)), min(255, int(g * 1.05 + 30)), max(0, int(b * 0.55)), a))
    out.putdata(px)
    return out


def tint_purple(img: Image.Image) -> Image.Image:
    out = img.copy()
    px = []
    for r, g, b, a in img.getdata():
        if a == 0:
            px.append((r, g, b, a))
        else:
            px.append((min(255, int(r * 0.85 + 50)), max(0, int(g * 0.55)), min(255, int(b * 1.1 + 60)), a))
    out.putdata(px)
    return out


def gen_icons():
    if Image is None:
        print("skip icons: pillow missing")
        return
    ICONS.mkdir(parents=True, exist_ok=True)
    prefixes = list(CHAINS.keys()) + ["like_a_cat"]
    for prefix in prefixes:
        src = ICONS / f"{prefix}_iii.png"
        if not src.exists():
            print("skip icon", src)
            continue
        img = Image.open(src).convert("RGBA")
        tint_gold(img).save(ICONS / f"{prefix}_iv.png")
        tint_purple(img).save(ICONS / f"{prefix}_v.png")
        print("icons", prefix)
    chef_src = ICONS / "master_chef.png"
    if chef_src.exists():
        img = Image.open(chef_src).convert("RGBA")
        tint_gold(img).save(ICONS / "sous_chef_iii.png")
        tint_gold(tint_gold(img)).save(ICONS / "head_chef_iv.png")
        tint_purple(img).save(ICONS / "executive_chef_v.png")
        print("icons chef")


def main():
    EXTRA.mkdir(parents=True, exist_ok=True)
    update_index()
    for prefix, tpl in CHAINS.items():
        gen_chain(prefix, *tpl)
    gen_chef()
    gen_icons()
    print("done")


if __name__ == "__main__":
    main()
