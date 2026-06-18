# Guns RPG (1.20.1 Forge Port)

**English** | [简体中文](README_zh-CN.md)

A Minecraft **1.20.1 Forge** port of [Guns RPG](https://github.com/Toma1O6/Guns-RPG) (originally **1.16.5** by Toma1O6).  
This branch adds a native firearm system, survival progression, optional TaCZ integration, and modpack-oriented features such as gun-wielding mobs and Bloodmoon events.

| | |
|---|---|
| **Mod ID** | `gunsrpg` |
| **Version** | `1.20.1-0.1.0-port` |
| **Minecraft** | 1.20.1 |
| **Forge** | 47.x (tested on 47.4.x) |
| **Upstream** | [Toma1O6/Guns-RPG](https://github.com/Toma1O6/Guns-RPG) (`1.16.5`) |

> This is an **early port**. Gameplay is largely playable, but some perks, mob visuals, and edge-case balance are still being finished. Contributions and PRs back to upstream are welcome.

---

## Features

### Progression & skill tree

- **Skill tree UI** — default key **O** (`SkillTreeScreen`): skills, weapon extensions, and perks
- Skill data loaded from `config/gunsrpg/port_from_gunsrpg/` (300+ nodes, 50+ perks)
- Bilingual names and descriptions in `assets/gunsrpg/lang/*_skills.json`
- **Account level** and **weapon level** from firearm kills (`leveling_strategy.json`)
- **Starter kit** on first join (`config/gunsrpg/starter_kit.json`)
- Active skills (e.g. emergency airdrop on **G**)

### Firearms & crafting

- **Native `FirearmItem` system** with custom BEWLR models, reload, jamming, fire modes, attachments, and durability
- **Gunsmith table** (`gunsrpg:gunsmith_table`) — weapon parts, ammo tiers, bone meal, and assembly recipes
- **Repair station**, **medical station**, and **culinary table**
- Grenades, grenade launcher, and rocket launcher support
- Optional **TaCZ** shooting backend (`config/gunsrpg/tacz_backend.json`) for selected weapons when TaCZ is installed
- **JEI** recipe integration (optional)

### Survival systems

- **Debuffs**: bleeding, fracture, poison, infection (`debuff_config.json`) with HUD overlay and resistance from skills/perks
- **Airdrop** supply drops (configurable loot tables)
- **Bloodmoon** world event with custom mobs and client effects
- **Gunshot alert** — nearby mobs react to gunfire (configurable)

### Hostile mobs

- **Zombie Gunner** — ranged zombie with firearm AI
- **Explosive Skeleton** — grenade-throwing skeleton
- **Bloodmoon Golem** and **Rocket Angel** — Bloodmoon bosses
- Natural spawn replacement and scaling via `mob_spawn.json`, `gunner_loadout.json`, `weapon_levels.json` (includes optional L2 Hostility scaling)

### Compatibility

- **Configuration** library (required) — config UI and sync framework used by the original mod
- **TaCZ** (optional) — alternate shooting backend and weapon mapping
- **Sophisticated Backpacks** (optional) — ammo pulled from backpacks when reloading
- **JEI** (optional)

---

## Requirements

| Dependency | Required | Notes |
|------------|----------|-------|
| Minecraft 1.20.1 | Yes | |
| Forge 47+ | Yes | |
| [Configuration](https://www.curseforge.com/minecraft/mc-mods/configuration) | Yes | CurseMaven id `444699:4608425` in `build.gradle` |
| JDK 17 | Yes | For building |
| TaCZ | No | Enable in `tacz_backend.json` |
| JEI | No | Recipe viewer |

---

## Building

```powershell
git clone https://github.com/Saitoseason/Guns-RPG.git
cd Guns-RPG
git checkout 1.20.1

.\gradlew.bat build
```

Output JAR: `build/libs/gunsrpg-1.20.1-0.1.0-port.jar`

Copy the JAR into your instance `mods/` folder together with **Configuration**.

### Modpack development (WF Pack)

If you work inside the *极限挑战 · 无暇赴死* pack layout:

```powershell
cd gunsrpg-1201
.\gradlew.bat build
.\tools\deploy.ps1          # copies the latest jar into the pack mods/ folder
.\tools\enable-mod.ps1      # re-enable a .disabled jar, or build+deploy if missing
.\tools\disable-mod.ps1     # temporarily disable without deleting
```

Runtime configs for a running instance live under `<instance>/config/gunsrpg/`.  
The pack ships a full skill dataset in `config/gunsrpg/port_from_gunsrpg/`.

---

## Configuration

All runtime configs are under **`config/gunsrpg/`**:

| File / folder | Purpose |
|---------------|---------|
| `port_from_gunsrpg/` | Skill nodes, perks, leveling strategy, weapon mapping |
| `starter_kit.json` | First-join starter items and skill points |
| `debuff_config.json` | Survival debuff tuning |
| `mob_spawn.json` | Gun mob spawn rules and scaling |
| `gunner_loadout.json` | Loadouts for zombie gunners / grenadiers |
| `weapon_levels.json` | Mob weapon level tables |
| `tacz_backend.json` | TaCZ backend toggle and weapon map |
| `airdrop.json` | Airdrop timing and loot |
| `world.json` | Bloodmoon and gunshot-alert settings |
| `ammo_materials.json` | Ammo crafting material tiers |
| `weapon_caliber_overrides.json` | Per-weapon caliber overrides |

Reload most configs in-game (OP): `/gunsrpg reload`

---

## Controls

| Key | Action |
|-----|--------|
| **O** | Open skill tree |
| **R** | Reload |
| **U** | Unjam |
| **B** | Toggle fire mode |
| **N** / **M** | Sight color / sight type |
| **G** | Emergency airdrop (requires perk) |

Rebind under **Options → Controls → Guns RPG**.

---

## Commands

All commands are under `/gunsrpg` (OP level noted where restricted).

| Command | Description |
|---------|-------------|
| `/gunsrpg status` | Skill DB load status |
| `/gunsrpg reload` | Reload configs (OP 2) |
| `/gunsrpg bootstrap` | Re-grant starter kit (OP 2) |
| `/gunsrpg points <n>` | Add skill points (OP 2) |
| `/gunsrpg level <n>` | Set account firearm level (OP 2) |
| `/gunsrpg unlock <skill_id>` | Unlock a skill (OP 2) |
| `/gunsrpg give gun <weapon_key>` | Give a weapon (OP 2) |
| `/gunsrpg give spawn_egg <mob>` | Give spawn egg: `zombie_gunner`, `explosive_skeleton`, `bloodmoon_golem`, `rocket_angel` |
| `/gunsrpg airdrop spawn` | Force an airdrop (OP 2, overworld) |
| `/gunsrpg bloodmoon start\|stop` | Force Bloodmoon (OP 2) |
| `/gunsrpg debuff status\|clear\|apply ...` | Debuff debug (OP 2) |

---

## Quick in-game test

```mcfunction
/gunsrpg give spawn_egg zombie_gunner
/gunsrpg give spawn_egg explosive_skeleton
/gunsrpg give gun m1911
/gunsrpg bloodmoon start
```

After rebuilding the mod, **fully restart** the game client — hot-swapping the JAR is not supported.

---

## Project layout

```
gunsrpg-1201/
├── src/main/java/com/wf/firearms/   # mod source
├── src/main/resources/assets/gunsrpg/
├── tools/                           # deploy, icon import, data scripts
├── reference/                       # read-only reference assets (e.g. TaCZ AK)
└── build/libs/                      # built JAR
```

Skill tree grid layout is ported from upstream `Tree.java` / `SkillTrees.java` into `client/gui/layout/GunsRpgTree.java` and `GunsRpgSkillTrees.java`.

---

## Contributing & upstream

1. Fork [Toma1O6/Guns-RPG](https://github.com/Toma1O6/Guns-RPG)
2. Work on branch **`1.20.1`**
3. Open a PR against `Toma1O6/Guns-RPG:1.20.1` with build steps and test notes

Please keep commit messages clear (Conventional Commits style is fine).  
Original author Toma1O6 has expressed interest in community ports and PRs for new Minecraft versions.

---

## Credits

- **Guns RPG (1.16.5)** — [Toma1O6](https://github.com/Toma1O6/Guns-RPG)
- **1.20.1 port** — Saitoseason / 极限挑战 · 无暇赴死 pack team
- **Configuration** — Toma1O6
- Skill tree UI algorithms and design — original Guns RPG codebase
- Optional **TaCZ** integration — [Timeless and Classics Zero](https://www.curseforge.com/minecraft/mc-mods/timeless-and-classics-zero)

## License

Follows the upstream Guns RPG project terms. See `gradle.properties` (`mod_license`) and the original repository for details.
