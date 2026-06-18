"""从 III 级图标生成 IV/V 级（色调对齐 sharp_axe 等现有技能）。"""
from pathlib import Path

try:
    from PIL import Image
except ImportError:
    raise SystemExit("pip install pillow")

ROOT = Path(__file__).resolve().parent.parent / "src/main/resources/assets/gunsrpg/textures/icons"
REF = Path(__file__).resolve().parent / "icon-ref"


def load(name: str) -> Image.Image:
    p = ROOT / f"{name}.png"
    if not p.exists():
        p = REF / f"{name}.png"
    return Image.open(p).convert("RGBA")


def palette_map(src: Image.Image, ref_iii: Image.Image, ref_iv: Image.Image, ref_v: Image.Image) -> tuple[Image.Image, Image.Image]:
    """按参考图 III→IV→V 的 RGB 比例映射主图标色调。"""
    s_px = list(ref_iii.getdata())
    iv_px = list(ref_iv.getdata())
    v_px = list(ref_v.getdata())
    ratios_iv = []
    ratios_v = []
    for a, b, c in zip(s_px, iv_px, v_px):
        if a[3] == 0:
            ratios_iv.append(None)
            ratios_v.append(None)
            continue
        ratios_iv.append(tuple((b[i] + 1) / (a[i] + 1) for i in range(3)))
        ratios_v.append(tuple((c[i] + 1) / (a[i] + 1) for i in range(3)))

    def remap(ref_ratios):
        out = src.copy()
        sp = list(src.getdata())
        dp = []
        ri = 0
        for p in sp:
            if p[3] == 0:
                dp.append(p)
                continue
            r = ref_ratios[ri]
            ri += 1
            if r is None:
                dp.append(p)
                continue
            dp.append(
                (
                    min(255, int(p[0] * r[0])),
                    min(255, int(p[1] * r[1])),
                    min(255, int(p[2] * r[2])),
                    p[3],
                )
            )
        out.putdata(dp)
        return out

    # 对齐非透明像素数量：用 sharp_axe 的前 N 个有色像素比例
    src_colored = [p for p in src.getdata() if p[3] > 0]
    ref_colored_iii = [p for p in ref_iii.getdata() if p[3] > 0]
    if len(src_colored) != len(ref_colored_iii):
        # 简单逐像素：只对 src 自身做 IV/V 色调（金/紫）
        return tint_gold(src), tint_purple(src)

    return remap(ratios_iv), remap(ratios_v)


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


def main():
    ROOT.mkdir(parents=True, exist_ok=True)
    ref_iii = load("sharp_axe_iii")
    ref_iv = load("sharp_axe_iv")
    ref_v = load("sharp_axe_v")

    for prefix in ("careful_gunner", "lucky_shooter"):
        src = load(f"{prefix}_iii")
        iv, v = palette_map(src, ref_iii, ref_iv, ref_v)
        iv.save(ROOT / f"{prefix}_iv.png")
        v.save(ROOT / f"{prefix}_v.png")
        print("wrote", prefix, "iv/v")


if __name__ == "__main__":
    main()
