package com.wf.firearms.config;

import com.wf.firearms.config.settings.AirdropSettings;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Facade over {@link AirdropSettings} (Configuration library). */
public final class AirdropConfig {
    private AirdropConfig() {}

    public static void reload() {
        // Configuration library hot-reloads; keep for call-site compatibility.
    }

    private static AirdropSettings cfg() {
        return GunsRpgConfigs.AIRDROP;
    }

    public static int activeIntervalDays() {
        return cfg().testMode ? cfg().testIntervalDays : cfg().intervalDays;
    }

    public static int spawnRadius() {
        return cfg().spawnRadius;
    }

    public static boolean testMode() {
        return cfg().testMode;
    }

    public static void setTestMode(boolean value) {
        cfg().testMode = value;
    }

    public static float gunDropChance() {
        return cfg().gunDropChance;
    }

    public static float grenadeDropChance(long worldDay) {
        float day = Math.min(120f, (float) worldDay);
        float t = day / 120f;
        return cfg().grenadeDropChanceMin + t * (cfg().grenadeDropChanceMax - cfg().grenadeDropChanceMin);
    }

    public static int rollGrenadeCount(RandomSource random) {
        if (cfg().grenadeCountMin >= cfg().grenadeCountMax) {
            return cfg().grenadeCountMin;
        }
        return cfg().grenadeCountMin + random.nextInt(cfg().grenadeCountMax - cfg().grenadeCountMin + 1);
    }

    public static Optional<String> rollGunKey(RandomSource random) {
        List<WeightedGun> tier4 = tier4Weighted();
        List<String> tier23 = cfg().tier23Guns != null ? cfg().tier23Guns : List.of();
        if (tier4.isEmpty() && tier23.isEmpty()) {
            return Optional.empty();
        }
        float tier4WeightSum = 0f;
        for (WeightedGun g : tier4) {
            tier4WeightSum += g.weight();
        }
        float roll = random.nextFloat();
        if (!tier4.isEmpty() && tier4WeightSum > 0f && roll < tier4WeightSum) {
            float acc = 0f;
            for (WeightedGun g : tier4) {
                acc += g.weight();
                if (roll < acc) {
                    return Optional.of(g.weaponKey());
                }
            }
            return Optional.of(tier4.get(tier4.size() - 1).weaponKey());
        }
        if (tier23.isEmpty()) {
            return tier4.isEmpty()
                    ? Optional.empty()
                    : Optional.of(tier4.get(random.nextInt(tier4.size())).weaponKey());
        }
        return Optional.of(tier23.get(random.nextInt(tier23.size())));
    }

    private static List<WeightedGun> tier4Weighted() {
        Map<String, Float> map = cfg().tier4Guns;
        if (map == null || map.isEmpty()) {
            return List.of();
        }
        List<WeightedGun> list = new ArrayList<>();
        for (var entry : map.entrySet()) {
            if (entry.getKey().startsWith("_")) {
                continue;
            }
            float w = Math.max(0f, entry.getValue());
            if (w > 0f) {
                list.add(new WeightedGun(entry.getKey(), w));
            }
        }
        return list;
    }

    private record WeightedGun(String weaponKey, float weight) {}
}
