package com.wf.firearms.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PortPaths;
import net.minecraft.util.RandomSource;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** {@code config/gunsrpg/airdrop.json} */
public final class AirdropConfig {
    private static int intervalDays = 7;
    private static int testIntervalDays = 1;
    private static int spawnRadius = 128;
    private static boolean testMode;
    /** 补给箱出现一把枪的概率（0~1），默认 25%。 */
    private static float gunDropChance = 0.25f;
    /** 手雷堆叠数量范围（含端点）。 */
    private static int grenadeCountMin = 4;
    private static int grenadeCountMax = 16;
    /** 第 0 天起的手雷出现概率；随天数线性升至 {@link #grenadeDropChanceMax}（约第 120 天封顶）。 */
    private static float grenadeDropChanceMin = 0.06f;
    private static float grenadeDropChanceMax = 0.42f;
    private static final List<WeightedGun> TIER4 = new ArrayList<>();
    private static final List<String> TIER23 = new ArrayList<>();
    private static float tier4WeightSum;

    private AirdropConfig() {}

    public static void reload() {
        intervalDays = 7;
        testIntervalDays = 1;
        spawnRadius = 128;
        testMode = false;
        gunDropChance = 0.25f;
        grenadeCountMin = 4;
        grenadeCountMax = 16;
        grenadeDropChanceMin = 0.06f;
        grenadeDropChanceMax = 0.42f;
        TIER4.clear();
        TIER23.clear();
        tier4WeightSum = 0f;
        loadDefaults();
        var file = PortPaths.configRoot().resolve("airdrop.json");
        if (!Files.isRegularFile(file)) {
            GunsRpg.LOGGER.info("[gunsrpg] 未找到 airdrop.json，使用默认空投枪表");
            recomputeTier4Sum();
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (root.has("interval_days")) {
                intervalDays = Math.max(1, root.get("interval_days").getAsInt());
            }
            if (root.has("test_interval_days")) {
                testIntervalDays = Math.max(1, root.get("test_interval_days").getAsInt());
            }
            if (root.has("spawn_radius")) {
                spawnRadius = Math.max(16, root.get("spawn_radius").getAsInt());
            }
            if (root.has("test_mode")) {
                testMode = root.get("test_mode").getAsBoolean();
            }
            if (root.has("gun_drop_chance")) {
                gunDropChance = Math.max(0f, Math.min(1f, root.get("gun_drop_chance").getAsFloat()));
            }
            if (root.has("grenade_count_min")) {
                grenadeCountMin = Math.max(1, root.get("grenade_count_min").getAsInt());
            }
            if (root.has("grenade_count_max")) {
                grenadeCountMax = Math.max(grenadeCountMin, root.get("grenade_count_max").getAsInt());
            }
            if (root.has("grenade_drop_chance_min")) {
                grenadeDropChanceMin = Math.max(0f, Math.min(1f, root.get("grenade_drop_chance_min").getAsFloat()));
            }
            if (root.has("grenade_drop_chance_max")) {
                grenadeDropChanceMax = Math.max(
                        grenadeDropChanceMin, Math.min(1f, root.get("grenade_drop_chance_max").getAsFloat()));
            }
            if (root.has("tier4_guns")) {
                TIER4.clear();
                JsonObject tier4 = root.getAsJsonObject("tier4_guns");
                for (var entry : tier4.entrySet()) {
                    if (entry.getKey().startsWith("_")) {
                        continue;
                    }
                    float w = Math.max(0f, entry.getValue().getAsFloat());
                    if (w > 0f) {
                        TIER4.add(new WeightedGun(entry.getKey(), w));
                    }
                }
            }
            if (root.has("tier23_guns")) {
                TIER23.clear();
                JsonArray arr = root.getAsJsonArray("tier23_guns");
                for (var el : arr) {
                    String key = el.getAsString().trim();
                    if (!key.isEmpty()) {
                        TIER23.add(key);
                    }
                }
            }
            recomputeTier4Sum();
            GunsRpg.LOGGER.info(
                    "[gunsrpg] 空投枪表：4级 {} 种（权重合计 {}），2–3级 {} 种，出枪概率 {}%",
                    TIER4.size(),
                    Math.round(tier4WeightSum * 100f),
                    TIER23.size(),
                    Math.round(gunDropChance * 100f));
        } catch (IOException ex) {
            GunsRpg.LOGGER.error("[gunsrpg] 读取 airdrop.json 失败", ex);
            recomputeTier4Sum();
        }
    }

    private static void loadDefaults() {
        Map<String, Float> tier4 = new LinkedHashMap<>();
        tier4.put("r45", 0.07f);
        tier4.put("p90", 0.06f);
        tier4.put("s686", 0.06f);
        tier4.put("mk14ebr", 0.06f);
        tier4.put("awm", 0.04f);
        tier4.put("m249", 0.06f);
        for (var e : tier4.entrySet()) {
            TIER4.add(new WeightedGun(e.getKey(), e.getValue()));
        }
        TIER23.addAll(
                List.of(
                        "glock",
                        "m1911",
                        "ump45",
                        "vector",
                        "uzi",
                        "akm",
                        "hk416",
                        "type_81",
                        "fn_fal",
                        "sks",
                        "spr15",
                        "desert_eagle",
                        "kar98k",
                        "winchester",
                        "aug",
                        "s1897",
                        "s12k"));
    }

    private static void recomputeTier4Sum() {
        tier4WeightSum = 0f;
        for (WeightedGun g : TIER4) {
            tier4WeightSum += g.weight();
        }
    }

    public static int activeIntervalDays() {
        return testMode ? testIntervalDays : intervalDays;
    }

    public static int spawnRadius() {
        return spawnRadius;
    }

    public static boolean testMode() {
        return testMode;
    }

    public static void setTestMode(boolean value) {
        testMode = value;
    }

    public static float gunDropChance() {
        return gunDropChance;
    }

    /** 按世界天数插值的手雷出现概率（0~1）。 */
    public static float grenadeDropChance(long worldDay) {
        float day = Math.min(120f, (float) worldDay);
        float t = day / 120f;
        return grenadeDropChanceMin + t * (grenadeDropChanceMax - grenadeDropChanceMin);
    }

    public static int rollGrenadeCount(RandomSource random) {
        if (grenadeCountMin >= grenadeCountMax) {
            return grenadeCountMin;
        }
        return grenadeCountMin + random.nextInt(grenadeCountMax - grenadeCountMin + 1);
    }

    /**
     * 在已判定「本次空投有枪」后调用：先按 tier4 权重（默认合计 35%）抽 4 级枪，否则从 tier23 均匀抽。
     */
    public static Optional<String> rollGunKey(RandomSource random) {
        if (TIER4.isEmpty() && TIER23.isEmpty()) {
            return Optional.empty();
        }
        float roll = random.nextFloat();
        if (!TIER4.isEmpty() && tier4WeightSum > 0f && roll < tier4WeightSum) {
            float acc = 0f;
            for (WeightedGun g : TIER4) {
                acc += g.weight();
                if (roll < acc) {
                    return Optional.of(g.weaponKey());
                }
            }
            return Optional.of(TIER4.get(TIER4.size() - 1).weaponKey());
        }
        if (TIER23.isEmpty()) {
            return TIER4.isEmpty()
                    ? Optional.empty()
                    : Optional.of(TIER4.get(random.nextInt(TIER4.size())).weaponKey());
        }
        return Optional.of(TIER23.get(random.nextInt(TIER23.size())));
    }

    private record WeightedGun(String weaponKey, float weight) {}
}
