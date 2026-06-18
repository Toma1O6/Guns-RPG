package com.wf.firearms.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.combat.WeaponClass;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** 读取 {@code config/wf_gun/combat.json}（弹道衰减与测试伤害）。 */
public final class CombatConfig {
    private static boolean loaded;
    private static boolean useScriptDamage = true;
    private static boolean disableCrit = true;
    private static int testDamageBonus;
    private static int defaultBulletBase = 5;

    private static final java.util.Map<String, FalloffCurve> FALLBACK = new java.util.HashMap<>();
    private static final java.util.Map<String, GunClassInfo> GUN_CLASSES = new java.util.HashMap<>();

    static {
        FALLBACK.put("pistol", new FalloffCurve(8, 28, 0.35f));
        FALLBACK.put("smg", new FalloffCurve(10, 32, 0.4f));
        FALLBACK.put("rifle_556", new FalloffCurve(40, 80, 0.85f));
        FALLBACK.put("rifle_762", new FalloffCurve(48, 96, 0.88f));
        FALLBACK.put("sniper", new FalloffCurve(64, 128, 0.92f));
        FALLBACK.put("shotgun", new FalloffCurve(6, 18, 0.25f));
        FALLBACK.put("shotgun_burst", new FalloffCurve(10, 22, 0.42f));
        FALLBACK.put("awm", new FalloffCurve(72, 160, 0.94f));
    }

    private CombatConfig() {}

    public static void reload() {
        loaded = false;
        useScriptDamage = true;
        disableCrit = true;
        testDamageBonus = 0;
        defaultBulletBase = 5;
        GUN_CLASSES.clear();

        Path file = Path.of("config", "wf_gun", "combat.json");
        if (!Files.isRegularFile(file)) {
            loaded = true;
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (root.has("use_script_damage")) {
                useScriptDamage = root.get("use_script_damage").getAsBoolean();
            }
            if (root.has("disable_crit_for_guns")) {
                disableCrit = root.get("disable_crit_for_guns").getAsBoolean();
            }
            if (root.has("test_damage_bonus")) {
                testDamageBonus = root.get("test_damage_bonus").getAsInt();
            }
            if (root.has("default_bullet_base")) {
                defaultBulletBase = root.get("default_bullet_base").getAsInt();
            }
            if (root.has("gun_classes")) {
                JsonObject guns = root.getAsJsonObject("gun_classes");
                for (var e : guns.entrySet()) {
                    JsonObject o = e.getValue().getAsJsonObject();
                    String cls = o.get("class").getAsString();
                    float mult = o.has("multiplier") ? o.get("multiplier").getAsFloat() : 1f;
                    GUN_CLASSES.put(e.getKey(), new GunClassInfo(cls, mult));
                }
            }
            if (root.has("falloff")) {
                JsonObject fall = root.getAsJsonObject("falloff");
                for (var e : fall.entrySet()) {
                    JsonObject o = e.getValue().getAsJsonObject();
                    FALLBACK.put(
                            e.getKey(),
                            new FalloffCurve(
                                    o.get("full_damage_blocks").getAsInt(),
                                    o.get("min_damage_blocks").getAsInt(),
                                    o.get("min_multiplier").getAsFloat()));
                }
            }
            loaded = true;
            GunsRpg.LOGGER.info("[gunsrpg] 已加载 combat.json");
        } catch (IOException ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] 读取 combat.json 失败", ex);
            loaded = true;
        }
    }

    public static boolean useJavaDamage() {
        return loaded && useScriptDamage;
    }

    public static boolean disableCrit() {
        return disableCrit;
    }

    public static int testDamageBonus() {
        return testDamageBonus;
    }

    public static int defaultBulletBase() {
        return defaultBulletBase;
    }

    public static GunClassInfo gunClass(String itemId) {
        return GUN_CLASSES.getOrDefault(itemId, new GunClassInfo("pistol", 1f));
    }

    public static float falloffMultiplier(String curveId, double distance) {
        FalloffCurve curve = FALLBACK.get(curveId);
        if (curve == null) {
            return 1f;
        }
        if (distance <= curve.fullBlocks) {
            return 1f;
        }
        if (distance >= curve.minBlocks) {
            return curve.minMult;
        }
        float t = (float) ((distance - curve.fullBlocks) / (curve.minBlocks - curve.fullBlocks));
        return 1f - t * (1f - curve.minMult);
    }

    public static String curveForWeaponClass(WeaponClass wc) {
        return switch (wc) {
            case PISTOL -> "pistol";
            case SMG -> "smg";
            case SHOTGUN -> "shotgun";
            case DMR -> "rifle_762";
            case SNIPER -> "sniper";
            case HEAVY -> "smg";
            default -> "rifle_556";
        };
    }

    public static String curveForWeapon(String weaponKey, WeaponClass wc) {
        if ("s686".equals(weaponKey)) {
            return "shotgun_burst";
        }
        if ("awm".equals(weaponKey)) {
            return "awm";
        }
        return curveForWeaponClass(wc);
    }

    public record FalloffCurve(int fullBlocks, int minBlocks, float minMult) {}

    public record GunClassInfo(String curveId, float multiplier) {}
}
