# PR comment draft (round 2) — paste on GitHub

Hi @Toma1O6,

Thank you again for the detailed review — it’s very helpful for getting this port to release quality.

**Addressed in the latest commits:**

- **Broken gunsmith recipes:** removed `wooden_magnum` (item was never registered); fixed `type_81` to use `wooden_stock` instead of the non-existent `large_iron_stock`.
- **Default skill / perk / leveling data:** bundled `skill_index.json`, `leveling_strategy.json`, all `skill_properties`, and `perks` under `data/gunsrpg/` in the jar; added reload listener support. The mod should now be playable without manual config export.
- **Medical / culinary stations:** restored horizontal `facing` on block classes and aligned blockstates with 1.16.5.
- **TaCZ:** agreed — TaCZ is now a **mandatory** dependency in `mods.toml`, with `compileOnly` TaCZ on the Gradle classpath (CurseMaven). We’ll continue migrating `TaczBridge` from reflection to direct API calls.
- **Configuration / recipes / JEI / MMT conditional recipes:** unchanged from the previous round; still in place.

**Textures & 3D (ammo icons, point books, grenade / launcher projectiles, BEWLR):**

I’m sorry — this part of the port was largely assisted by AI tooling, which is **not good at art assets, item models, or entity rendering**. That’s why several textures look flat or duplicated, and why some 3D / BEWLR behaviour regressed compared to 1.16.5. I don’t want to ship low-quality placeholders as if they were intentional.

**If you have time**, would you be willing to fix or guide the asset / rendering side (restoring upstream textures, distinct medical station art, weapon point book texture, grenade entity models, etc.)? I’m happy to handle the Java / data / recipe side and follow whatever structure you prefer for resources in the repo.

Thanks again for reviewing this — I’m treating it as a learning process toward an official-quality 1.20.1 port.
