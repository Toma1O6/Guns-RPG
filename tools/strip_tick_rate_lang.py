#!/usr/bin/env python3
import re
from pathlib import Path

LANG = Path(__file__).resolve().parents[1] / "src/main/resources/assets/gunsrpg/lang"
DROP = re.compile(
    r"\.description\.rate.*(?:%s tick|ticks|%s%% 开火|firerate by %s tick|weapon firerate)",
    re.I,
)

for name in ("zh_cn_skills.json", "en_us_skills.json"):
    path = LANG / name
    lines = path.read_text(encoding="utf-8").splitlines(True)
    kept = [ln for ln in lines if not DROP.search(ln)]
    path.write_text("".join(kept), encoding="utf-8")
    print(path.name, len(lines) - len(kept), "removed")
