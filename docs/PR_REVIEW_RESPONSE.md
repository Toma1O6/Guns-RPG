# PR review response (for upstream)

## Ammo recipes / external mods

**Issue:** MMT (`mmt:*`) ammo recipes were always registered, so they appeared broken when MMT was not installed.

**Fix:** All 78 MMT gunsmith recipes are now wrapped in `forge:conditional` with `forge:mod_loaded` → `mmt`. Vanilla-tier ammo (`minecraft:*` ingots) is unchanged.

**Why it existed:** This port targets a modpack that uses MMT as the mid/late-game material line. The recipes are optional pack content, not a hard dependency of Guns RPG itself.

---

## Configuration library

**Issue:** Settings were loaded with hand-written Gson (`AirdropConfig`, `world.json`, etc.) despite Configuration already being a dependency.

**Fix:** Registered via `Configuration.registerConfig(..., ConfigFormats.json())`:

| Config class | File (generated) |
|--------------|------------------|
| `AirdropSettings` | `config/gunsrpg/airdrop.json` |
| `WorldSettings` | `config/gunsrpg/world.json` |
| `TaczBackendSettings` | `config/gunsrpg/tacz_backend.json` |
| `CombatSettings` | `config/gunsrpg/combat.json` |

Existing facade classes (`AirdropConfig`, `BloodmoonConfig`, …) now read from these instances.

**Still Gson (nested data, follow-up):** `mob_spawn.json`, `gunner_loadout.json`, `weapon_levels.json`, `debuff_config.json`, `ammo_materials.json` — large nested structures; planned as nested `@Configurable` categories in a follow-up commit.

---

## TaCZ dependency

**Updated per review:** TaCZ is now a **mandatory** dependency (`mods.toml` → `mandatory=true`, version `[1.1.0,)`).

- `build.gradle`: `compileOnly` via CurseMaven (`curse.maven:tacz-1028108:…`) so integration code can move off pure reflection.
- Shooting backend no longer checks `ModList.isLoaded("tacz")` — the mod is required to load.
- **Follow-up:** Replace `TaczBridge` reflection calls with direct TaCZ API usage now that the jar is on the compile classpath.

---

## build.gradle dependencies

**Issue:** JEI / Sophisticated Backpacks were resolved from `../mods/*.jar`; JEI was missing `common-api`; SB did not compile on clean CI.

**Fix:**

- JEI: `compileOnly` Maven artifacts `jei-*-common-api` + `jei-*-forge-api`
- Sophisticated Backpacks: **removed** compile dependency; runtime integration via `SophisticatedBackpackBridge` (reflection), gated with `ModList.isLoaded("sophisticatedbackpacks")`
- Removed PowerShell texture tasks from `processResources` dependencies (assets are committed; PS scripts remain optional dev tools on Windows)

---

## JEI

**Fix:** `addIngredientInfo(Item, …)` → `addItemStackInfo(ItemStack, …)` for JEI 15 API.

---

## Optional mod integration pattern

All cross-mod behaviour follows:

1. `ModList.get().isLoaded("modid")` (or dedicated `*Compat.isLoaded()`)
2. No compile-time dependency on optional mods (reflection or conditional recipes)
3. Pack-specific content (MMT ammo, TaCZ shooting backend) disabled when the mod is absent
