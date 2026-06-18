package com.wf.firearms.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.combat.WeaponClass;
import com.wf.firearms.data.PortPaths;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** {@code config/gunsrpg/world.json} 中的枪声吸引僵尸参数。 */
public final class GunshotAlertConfig {
    private static double baseAggroRange = 28.0;
    private static double minAggroRange = 6.0;
    private static double maxAggroRange = 96.0;
    private static double directAggroDistance = 16.0;
    private static boolean zombiesOnly = true;

    private static final Map<WeaponClass, Double> CLASS_NOISE = new EnumMap<>(WeaponClass.class);
    private static final Map<String, Double> WEAPON_NOISE = new HashMap<>();

    static {
        resetDefaults();
    }

    private GunshotAlertConfig() {}

    public static void reload() {
        resetDefaults();
        var file = PortPaths.configRoot().resolve("world.json");
        if (!Files.isRegularFile(file)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (root.has("shooting_mob_aggro_range")) {
                baseAggroRange = Math.max(1.0, root.get("shooting_mob_aggro_range").getAsDouble());
            }
            if (root.has("gunshot_min_aggro_range")) {
                minAggroRange = Math.max(0.0, root.get("gunshot_min_aggro_range").getAsDouble());
            }
            if (root.has("gunshot_max_aggro_range")) {
                maxAggroRange = Math.max(minAggroRange, root.get("gunshot_max_aggro_range").getAsDouble());
            }
            if (root.has("gunshot_direct_aggro_distance")) {
                directAggroDistance =
                        Math.max(1.0, root.get("gunshot_direct_aggro_distance").getAsDouble());
            }
            if (root.has("gunshot_zombies_only")) {
                zombiesOnly = root.get("gunshot_zombies_only").getAsBoolean();
            }
            if (root.has("gunshot_class_noise") && root.get("gunshot_class_noise").isJsonObject()) {
                JsonObject byClass = root.getAsJsonObject("gunshot_class_noise");
                for (WeaponClass wc : WeaponClass.values()) {
                    String key = wc.name().toLowerCase(Locale.ROOT);
                    if (byClass.has(key)) {
                        CLASS_NOISE.put(wc, Math.max(0.0, byClass.get(key).getAsDouble()));
                    }
                }
            }
            if (root.has("gunshot_weapon_noise") && root.get("gunshot_weapon_noise").isJsonObject()) {
                JsonObject byWeapon = root.getAsJsonObject("gunshot_weapon_noise");
                for (String key : byWeapon.keySet()) {
                    WEAPON_NOISE.put(key, Math.max(0.0, byWeapon.get(key).getAsDouble()));
                }
            }
        } catch (IOException ex) {
            GunsRpg.LOGGER.error("[gunsrpg] 读取 world.json 枪声参数失败", ex);
        }
    }

    private static void resetDefaults() {
        baseAggroRange = 28.0;
        minAggroRange = 6.0;
        maxAggroRange = 96.0;
        directAggroDistance = 16.0;
        zombiesOnly = true;
        CLASS_NOISE.clear();
        CLASS_NOISE.put(WeaponClass.PISTOL, 0.50);
        CLASS_NOISE.put(WeaponClass.SMG, 0.65);
        CLASS_NOISE.put(WeaponClass.RIFLE, 0.80);
        CLASS_NOISE.put(WeaponClass.DMR, 0.95);
        CLASS_NOISE.put(WeaponClass.SNIPER, 1.15);
        CLASS_NOISE.put(WeaponClass.SHOTGUN, 1.05);
        CLASS_NOISE.put(WeaponClass.HEAVY, 1.25);
        CLASS_NOISE.put(WeaponClass.BOW, 0.40);
        CLASS_NOISE.put(WeaponClass.OTHER, 0.70);
        WEAPON_NOISE.clear();
        WEAPON_NOISE.put("vss", 0.12);
    }

    public static double baseAggroRange() {
        return baseAggroRange;
    }

    public static double minAggroRange() {
        return minAggroRange;
    }

    public static double maxAggroRange() {
        return maxAggroRange;
    }

    public static double directAggroDistance() {
        return directAggroDistance;
    }

    public static boolean zombiesOnly() {
        return zombiesOnly;
    }

    public static double classNoise(WeaponClass weaponClass) {
        if (weaponClass == null) {
            return CLASS_NOISE.getOrDefault(WeaponClass.OTHER, 0.70);
        }
        return CLASS_NOISE.getOrDefault(weaponClass, CLASS_NOISE.getOrDefault(WeaponClass.OTHER, 0.70));
    }

    public static double weaponNoiseOverride(String weaponKey) {
        if (weaponKey == null || weaponKey.isEmpty()) {
            return 1.0;
        }
        return WEAPON_NOISE.getOrDefault(weaponKey, 1.0);
    }
}
