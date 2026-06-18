package com.wf.firearms.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PortPaths;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** {@code config/gunsrpg/mob_spawn.json} */
public final class MobSpawnConfig {
    public record DimensionSpawn(
            int weight,
            int minCount,
            int maxCount,
            boolean mainIslandOnly,
            int maxHorizontalDistanceFromOrigin,
            int minY) {
        public DimensionSpawn(int weight, int minCount, int maxCount) {
            this(weight, minCount, maxCount, false, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }
    }

    public record MobEntry(
            boolean enabled,
            Map<String, DimensionSpawn> dimensions,
            int spawnStartDay,
            Set<String> excludeBiomeCategories,
            EndIslandBoost endBoost,
            MobCombat combat) {}

    public record EndIslandBoost(
            boolean enabled, float healthMultiplier, float attackDamageMultiplier, float followRangeBonus) {}

    /** 单种 mob 的战斗参数（火箭天使齐射、掷弹手穿墙等）。 */
    public record MobCombat(
            int rocketsPerSalvo,
            int salvoReloadTicks,
            float rocketSpread,
            boolean bloodmoonIgnoreLineOfSight) {
        public static MobCombat defaults() {
            return new MobCombat(4, 100, 0.12f, false);
        }
    }

    public record Global(
            int spawnStartDayOverworld,
            int spawnStartDayNether,
            int spawnStartDayEnd,
            boolean timeScalingEnabled,
            int daysAfterStartPerStep,
            float spawnChanceMultiplierPerStep,
            float maxSpawnChanceMultiplier,
            int gunLevelBonusPerStep,
            int maxGunLevelBonus,
            boolean regionalScalingEnabled,
            float spawnChancePerEffectiveDifficulty,
            float gunLevelPerEffectiveDifficulty,
            boolean l2Enabled,
            float l2SpawnChancePer100,
            float l2MaxSpawnChanceMultiplier,
            int l2GunLevelPer50,
            int l2MaxGunLevelBonus,
            boolean infiniteAmmo,
            boolean skipReload,
            float fireIntervalMultiplier,
            Map<String, Integer> dimensionGunLevelCap) {}

    private static Global global = defaultGlobal();
    private static Map<String, MobEntry> mobs = Map.of();
    private static int rocketAngelBloodmoonWeight = 2;

    private MobSpawnConfig() {}

    private static Global defaultGlobal() {
        Map<String, Integer> caps = new HashMap<>();
        caps.put("minecraft:overworld", 30);
        caps.put("minecraft:the_nether", 65);
        caps.put("minecraft:the_end", 100);
        return new Global(
                30,
                0,
                0,
                true,
                10,
                0.12f,
                2.5f,
                1,
                12,
                true,
                0.08f,
                2f,
                true,
                0.18f,
                2.0f,
                3,
                15,
                true,
                true,
                1.35f,
                caps);
    }

    public static void reload() {
        global = defaultGlobal();
        mobs = Map.of();
        rocketAngelBloodmoonWeight = 2;
        var file = PortPaths.configRoot().resolve("mob_spawn.json");
        if (!Files.isRegularFile(file)) {
            GunsRpg.LOGGER.warn("[gunsrpg] 未找到 mob_spawn.json，持枪怪刷怪未启用");
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (root.has("global")) {
                global = parseGlobal(root.getAsJsonObject("global"));
            }
            Map<String, MobEntry> map = new HashMap<>();
            if (root.has("mobs")) {
                for (Map.Entry<String, JsonElement> e : root.getAsJsonObject("mobs").entrySet()) {
                    MobEntry entry = parseMob(e.getKey(), e.getValue().getAsJsonObject());
                    map.put(e.getKey(), entry);
                    if ("gunsrpg:rocket_angel".equals(e.getKey())
                            && e.getValue().getAsJsonObject().has("bloodmoon_spider_replace_weight")) {
                        rocketAngelBloodmoonWeight =
                                Math.max(1, e.getValue().getAsJsonObject().get("bloodmoon_spider_replace_weight").getAsInt());
                    }
                }
            }
            mobs = Collections.unmodifiableMap(map);
            GunsRpg.LOGGER.info("[gunsrpg] 已加载 mob_spawn {} 种怪物规则", mobs.size());
        } catch (IOException ex) {
            GunsRpg.LOGGER.error("[gunsrpg] 读取 mob_spawn.json 失败", ex);
        }
    }

    private static Global parseGlobal(JsonObject g) {
        Global d = defaultGlobal();
        int ow = g.has("spawn_start_day_overworld") ? g.get("spawn_start_day_overworld").getAsInt() : d.spawnStartDayOverworld();
        int ne = g.has("spawn_start_day_nether") ? g.get("spawn_start_day_nether").getAsInt() : d.spawnStartDayNether();
        int en = g.has("spawn_start_day_end") ? g.get("spawn_start_day_end").getAsInt() : d.spawnStartDayEnd();
        boolean timeOn = d.timeScalingEnabled();
        int dayStep = d.daysAfterStartPerStep();
        float chanceStep = d.spawnChanceMultiplierPerStep();
        float maxChance = d.maxSpawnChanceMultiplier();
        int gunStep = d.gunLevelBonusPerStep();
        int maxGunBonus = d.maxGunLevelBonus();
        if (g.has("time_scaling")) {
            JsonObject ts = g.getAsJsonObject("time_scaling");
            timeOn = !ts.has("enabled") || ts.get("enabled").getAsBoolean();
            if (ts.has("days_after_start_per_step")) {
                dayStep = ts.get("days_after_start_per_step").getAsInt();
            }
            if (ts.has("spawn_chance_multiplier_per_step")) {
                chanceStep = ts.get("spawn_chance_multiplier_per_step").getAsFloat();
            }
            if (ts.has("max_spawn_chance_multiplier")) {
                maxChance = ts.get("max_spawn_chance_multiplier").getAsFloat();
            }
            if (ts.has("gun_level_bonus_per_step")) {
                gunStep = ts.get("gun_level_bonus_per_step").getAsInt();
            }
            if (ts.has("max_gun_level_bonus")) {
                maxGunBonus = ts.get("max_gun_level_bonus").getAsInt();
            }
        }
        boolean regOn = d.regionalScalingEnabled();
        float regChance = d.spawnChancePerEffectiveDifficulty();
        float regGun = d.gunLevelPerEffectiveDifficulty();
        if (g.has("regional_difficulty_scaling")) {
            JsonObject rs = g.getAsJsonObject("regional_difficulty_scaling");
            regOn = !rs.has("enabled") || rs.get("enabled").getAsBoolean();
            if (rs.has("spawn_chance_per_effective_difficulty")) {
                regChance = rs.get("spawn_chance_per_effective_difficulty").getAsFloat();
            }
            if (rs.has("gun_level_per_effective_difficulty")) {
                regGun = rs.get("gun_level_per_effective_difficulty").getAsFloat();
            }
        }
        boolean l2On = d.l2Enabled();
        float l2Chance = d.l2SpawnChancePer100();
        float l2Max = d.l2MaxSpawnChanceMultiplier();
        int l2Gun = d.l2GunLevelPer50();
        int l2MaxGun = d.l2MaxGunLevelBonus();
        if (g.has("l2_hostility_scaling")) {
            JsonObject l2 = g.getAsJsonObject("l2_hostility_scaling");
            l2On = !l2.has("enabled") || l2.get("enabled").getAsBoolean();
            if (l2.has("spawn_chance_per_100_level")) {
                l2Chance = l2.get("spawn_chance_per_100_level").getAsFloat();
            }
            if (l2.has("max_spawn_chance_multiplier")) {
                l2Max = l2.get("max_spawn_chance_multiplier").getAsFloat();
            }
            if (l2.has("gun_level_per_50_level")) {
                l2Gun = l2.get("gun_level_per_50_level").getAsInt();
            }
            if (l2.has("max_gun_level_bonus")) {
                l2MaxGun = l2.get("max_gun_level_bonus").getAsInt();
            }
        }
        boolean infAmmo = d.infiniteAmmo();
        boolean skipReload = d.skipReload();
        float fireMul = d.fireIntervalMultiplier();
        if (g.has("mob_combat")) {
            JsonObject mc = g.getAsJsonObject("mob_combat");
            if (mc.has("infinite_ammo")) {
                infAmmo = mc.get("infinite_ammo").getAsBoolean();
            }
            if (mc.has("skip_reload")) {
                skipReload = mc.get("skip_reload").getAsBoolean();
            }
            if (mc.has("fire_interval_multiplier")) {
                fireMul = mc.get("fire_interval_multiplier").getAsFloat();
            }
        }
        Map<String, Integer> caps = new HashMap<>(d.dimensionGunLevelCap());
        if (g.has("dimension_gun_level_cap")) {
            for (Map.Entry<String, JsonElement> e : g.getAsJsonObject("dimension_gun_level_cap").entrySet()) {
                caps.put(e.getKey(), e.getValue().getAsInt());
            }
        }
        return new Global(
                ow,
                ne,
                en,
                timeOn,
                dayStep,
                chanceStep,
                maxChance,
                gunStep,
                maxGunBonus,
                regOn,
                regChance,
                regGun,
                l2On,
                l2Chance,
                l2Max,
                l2Gun,
                l2MaxGun,
                infAmmo,
                skipReload,
                fireMul,
                caps);
    }

    private static MobEntry parseMob(String id, JsonObject o) {
        boolean enabled = !o.has("enabled") || o.get("enabled").getAsBoolean();
        Map<String, DimensionSpawn> dims = new HashMap<>();
        if (o.has("dimensions")) {
            for (Map.Entry<String, JsonElement> e : o.getAsJsonObject("dimensions").entrySet()) {
                JsonObject d = e.getValue().getAsJsonObject();
                int weight = d.has("weight") ? d.get("weight").getAsInt() : 0;
                int min = d.has("min_count") ? d.get("min_count").getAsInt() : 1;
                int max = d.has("max_count") ? d.get("max_count").getAsInt() : min;
                boolean mainIsland = d.has("main_island_only") && d.get("main_island_only").getAsBoolean();
                int maxDist = d.has("max_horizontal_distance_from_origin")
                        ? d.get("max_horizontal_distance_from_origin").getAsInt()
                        : Integer.MAX_VALUE;
                int minY = d.has("min_y") ? d.get("min_y").getAsInt() : Integer.MIN_VALUE;
                dims.put(e.getKey(), new DimensionSpawn(weight, min, max, mainIsland, maxDist, minY));
            }
        }
        int startDay = o.has("spawn_start_day") ? o.get("spawn_start_day").getAsInt() : global.spawnStartDayOverworld();
        Set<String> exclude = new HashSet<>();
        if (o.has("exclude_biome_categories")) {
            o.getAsJsonArray("exclude_biome_categories").forEach(el -> exclude.add(el.getAsString()));
        }
        EndIslandBoost boost = new EndIslandBoost(false, 1f, 1f, 0f);
        if (o.has("end_main_island_boost")) {
            JsonObject b = o.getAsJsonObject("end_main_island_boost");
            boost =
                    new EndIslandBoost(
                            !b.has("enabled") || b.get("enabled").getAsBoolean(),
                            b.has("health_multiplier") ? b.get("health_multiplier").getAsFloat() : 1f,
                            b.has("attack_damage_multiplier") ? b.get("attack_damage_multiplier").getAsFloat() : 1f,
                            b.has("follow_range_bonus") ? b.get("follow_range_bonus").getAsFloat() : 0f);
        }
        MobCombat combat = MobCombat.defaults();
        if (o.has("combat")) {
            JsonObject c = o.getAsJsonObject("combat");
            combat =
                    new MobCombat(
                            c.has("rockets_per_salvo") ? c.get("rockets_per_salvo").getAsInt() : 4,
                            c.has("salvo_reload_ticks") ? c.get("salvo_reload_ticks").getAsInt() : 100,
                            c.has("rocket_spread") ? c.get("rocket_spread").getAsFloat() : 0.12f,
                            c.has("bloodmoon_ignore_line_of_sight")
                                    && c.get("bloodmoon_ignore_line_of_sight").getAsBoolean());
        } else if (o.has("bloodmoon_ignore_line_of_sight")
                && o.get("bloodmoon_ignore_line_of_sight").getAsBoolean()) {
            combat = new MobCombat(4, 100, 0.12f, true);
        }
        return new MobEntry(enabled, dims, startDay, exclude, boost, combat);
    }

    public static MobCombat rocketAngelCombat() {
        MobEntry entry = mobs.get("gunsrpg:rocket_angel");
        return entry != null ? entry.combat() : MobCombat.defaults();
    }

    public static Global global() {
        return global;
    }

    public static MobEntry mob(String id) {
        return mobs.get(id);
    }

    public static int gunLevelCapForDimension(ResourceLocation dim) {
        return global.dimensionGunLevelCap().getOrDefault(dim.toString(), 100);
    }

    public static int rocketAngelBloodmoonWeight() {
        return rocketAngelBloodmoonWeight;
    }

    public static int globalStartDayForDimension(ResourceLocation dim) {
        if ("minecraft:the_nether".equals(dim.toString())) {
            return global.spawnStartDayNether();
        }
        if ("minecraft:the_end".equals(dim.toString())) {
            return global.spawnStartDayEnd();
        }
        return global.spawnStartDayOverworld();
    }
}
