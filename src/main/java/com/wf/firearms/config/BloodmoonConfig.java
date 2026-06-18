package com.wf.firearms.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PortPaths;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/** {@code config/gunsrpg/world.json} — 血月周期与仇恨距离等。 */
public final class BloodmoonConfig {
    private static int bloodmoonCycle = 15;
    private static int bloodMoonMobAgroRange = 64;
    private static float health2xChance = 0.45f;
    private static float health3xChance = 0.70f;
    private static float health4xChance = 0.85f;
    private static int rocketAngelSpawnWeight = 2;
    private static float playerDamageTakenMultiplier = 1.3f;
    /** 火箭爆炸强度 = ATTACK_DAMAGE × 该系数 × 变体系数 × 区域难度。 */
    private static float rocketExplosionMeleeRatio = 0.2f;

    private BloodmoonConfig() {}

    public static void reload() {
        bloodmoonCycle = 15;
        bloodMoonMobAgroRange = 64;
        health2xChance = 0.45f;
        health3xChance = 0.70f;
        health4xChance = 0.85f;
        rocketAngelSpawnWeight = 2;
        playerDamageTakenMultiplier = 1.3f;
        rocketExplosionMeleeRatio = 0.2f;
        var file = PortPaths.configRoot().resolve("world.json");
        if (!Files.isRegularFile(file)) {
            GunsRpg.LOGGER.info("[gunsrpg] 未找到 world.json，血月默认每 {} 游戏日", bloodmoonCycle);
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (root.has("bloodmoon_cycle")) {
                bloodmoonCycle = root.get("bloodmoon_cycle").getAsInt();
            }
            if (root.has("blood_moon_mob_agro_range")) {
                bloodMoonMobAgroRange = Math.max(8, root.get("blood_moon_mob_agro_range").getAsInt());
            }
            if (root.has("health_2x_chance")) {
                health2xChance = root.get("health_2x_chance").getAsFloat();
            }
            if (root.has("health_3x_chance")) {
                health3xChance = root.get("health_3x_chance").getAsFloat();
            }
            if (root.has("health_4x_chance")) {
                health4xChance = root.get("health_4x_chance").getAsFloat();
            }
            if (root.has("rocket_angel_spawn_weight")) {
                rocketAngelSpawnWeight = Math.max(1, root.get("rocket_angel_spawn_weight").getAsInt());
            }
            if (root.has("player_damage_taken_multiplier")) {
                playerDamageTakenMultiplier =
                        Math.max(1.0f, root.get("player_damage_taken_multiplier").getAsFloat());
            }
            if (root.has("rocket_explosion_melee_ratio")) {
                rocketExplosionMeleeRatio =
                        Math.max(0.05f, root.get("rocket_explosion_melee_ratio").getAsFloat());
            }
        } catch (IOException ex) {
            GunsRpg.LOGGER.error("[gunsrpg] 读取 world.json 失败", ex);
        }
    }

    public static int bloodmoonCycle() {
        return bloodmoonCycle;
    }

    public static int bloodMoonMobAgroRange() {
        return bloodMoonMobAgroRange;
    }

    public static float health2xChance() {
        return health2xChance;
    }

    public static float health3xChance() {
        return health3xChance;
    }

    public static float health4xChance() {
        return health4xChance;
    }

    public static int rocketAngelSpawnWeight() {
        return rocketAngelSpawnWeight;
    }

    /** 血月期间玩家受到的伤害倍率（1.3 = +30%）。 */
    public static float playerDamageTakenMultiplier() {
        return playerDamageTakenMultiplier;
    }

    public static float rocketExplosionMeleeRatio() {
        return rocketExplosionMeleeRatio;
    }
}
