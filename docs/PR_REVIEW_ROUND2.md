# PR review round 2 — response draft (for @Toma1O6)

Thanks for the continued review. Below is how we plan to address each point for an official-quality port.

---

## Broken gunsmith recipes

**`wooden_magnum`** — Agreed. The item was intentionally not registered (`ModItems` skips wood/stone magnum calibers). Removed the orphan recipe, item model, and lang entries.

**`type_81` / `large_iron_stock`** — Agreed. `large_iron_stock` was never registered. Recipe now uses `wooden_stock` (same pattern as `akm.json`). `type_81` itself remains a pack-specific gun.

---

## Default skill / perk / leveling data (biggest issue)

**Agreed.** The port should ship playable defaults in the jar.

**Done in this commit:**
- Bundled **453** `skill_properties` + **52** `perks` + `skill_index.json` + `leveling_strategy.json` under `src/main/resources/data/gunsrpg/`
- `PortPaths` now resolves bundled jar data first, then falls back to `config/gunsrpg/port_from_gunsrpg/` for pack overrides
- `GunsRpgSkillDataReloadListener` reloads skill data on datapack reload

**Follow-up:** Migrate loader to `SimpleJsonResourceReloadListener` per-namespace (closer to 1.16.5 `JsonReloadListener`) so datapack overrides merge cleanly without filesystem paths.

---

## Block models (medical / culinary)

**Medical station** — Restored `facing` on `MedicalStationTableBlock` and blockstate (was `""` variant only). Will source **distinct** `medical_station.png` from upstream 1.16.5 (copy script previously duplicated repair texture).

**Culinary table** — Blockstate had `facing` but block class did not; added `HorizontalDirectionalBlock.FACING` + placement rotation (matches repair station).

---

## Textures / 3D downgrade

**Acknowledged.** Repo currently has **no committed PNGs**; dev relied on `tools/copy_gunsrpg_textures.ps1` downloading a subset from upstream. That explains:
- Flat ammo icons (incomplete download list)
- Skill / weapon point books sharing texture (books not in script)
- Grenade / launcher projectiles not using BEWLR / entity models

**Plan:**
1. Expand `copy_gunsrpg_textures.ps1` to pull full upstream `textures/item`, `textures/block`, `textures/gui` sets
2. Commit textures in repo (or CI step) so PR is self-contained
3. Re-verify grenade / rocket **entity** renderers and item `BuiltinModelItem` / BEWLR registration vs 1.16.5

---

## TaCZ: optional + reflection vs mandatory + compile dependency

**Discussion needed.**

This fork targets a **TaCZ-first modpack** (shooting UX, reload, gun IDs). We agree native TaCZ integration is the right end state for an official 1.20.1 port.

**Proposal:**
- Make `tacz` a **mandatory** dependency in `mods.toml` + `compileOnly`/`runtime` in `build.gradle` (CurseMaven)
- Replace `TaczBridge` reflection with direct API calls where stable
- Keep a thin config flag only for *mapping* (which wf guns map to which TaCZ ids), not for “run without TaCZ”

We can drop the “playable without TaCZ” goal if the official port standard is TaCZ-native gunplay.

---

## Sophisticated Backpacks: reflection vs compileOnly

**Agreed in principle.** Reflection was used to keep clean CI without SB jars.

**Proposal:**
- `compileOnly` `sophisticatedbackpacks` + `sophisticatedcore` via CurseMaven
- Replace `SophisticatedBackpackBridge` with a small direct integration class behind `ModList.isLoaded` (no reflection)
- `mods.toml` optionalDependency for runtime

---

## Summary checklist

| Item | Status |
|------|--------|
| Remove/fix broken recipes | Done |
| Bundle default skill data | Done |
| Medical / culinary facing | Done |
| Distinct medical texture | Planned (upstream PNG) |
| Full texture commit / 3D items | Planned |
| SimpleJsonResourceReloadListener | Follow-up |
| TaCZ mandatory + direct API | Planned (needs agreement) |
| SB compileOnly integration | Planned |
