package com.wf.firearms.combat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PortPaths;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 弹药材质数据：伤害倍率 + 口径属性表（移植自 Guns RPG {@code MaterialDataList}）。
 * 后续可在 {@code config/gunsrpg/ammo_materials.json} 覆盖。
 */
public final class AmmoStatsRegistry {
    private static final Map<AmmoCaliber, Map<AmmoMaterial, AmmoStatProfile>> PROFILES = new EnumMap<>(AmmoCaliber.class);
    private static final Map<AmmoMaterial, Float> DAMAGE_MULT = new EnumMap<>(AmmoMaterial.class);
    private static final Map<String, AmmoMaterial> CUSTOM_PREFIX = new HashMap<>();

    private AmmoStatsRegistry() {}

    public static void reload() {
        PROFILES.clear();
        DAMAGE_MULT.clear();
        CUSTOM_PREFIX.clear();
        applyGunsRpgDefaults();
        for (AmmoMaterial material : AmmoMaterial.values()) {
            if (material != AmmoMaterial.UNKNOWN) {
                DAMAGE_MULT.putIfAbsent(material, material.damageMultiplier());
            }
        }
        DAMAGE_MULT.put(AmmoMaterial.UNKNOWN, 0.5f);
        loadConfigOverrides();
    }

    private static final float MAGNUM_DAMAGE_FACTOR = 1.25f;

    public static float damageMultiplier(AmmoMaterial material) {
        return DAMAGE_MULT.getOrDefault(material, 0.5f);
    }

    public static float damageMultiplier(AmmoMaterial material, AmmoCaliber caliber) {
        float base = damageMultiplier(material);
        if (caliber == AmmoCaliber.MAGNUM) {
            return base * MAGNUM_DAMAGE_FACTOR;
        }
        return base;
    }

    public static int damagePercent(AmmoMaterial material, AmmoCaliber caliber) {
        return Math.round(damageMultiplier(material, caliber) * 100f);
    }

    public static AmmoStatProfile profile(AmmoCaliber caliber, AmmoMaterial material) {
        return PROFILES.getOrDefault(caliber, Map.of()).getOrDefault(material, AmmoStatProfile.EMPTY);
    }

    public static Optional<AmmoMaterial> materialFromCustomId(String itemPath) {
        for (var e : CUSTOM_PREFIX.entrySet()) {
            if (itemPath.startsWith(e.getKey() + "_")) {
                return Optional.of(e.getValue());
            }
        }
        return Optional.empty();
    }

    private static void applyGunsRpgDefaults() {
        apply9mm();
        apply45Acp();
        apply556();
        apply762();
        applyMagnum();
        apply12g();
    }

    private static void apply9mm() {
        put(AmmoCaliber.MM_9, AmmoMaterial.WOOD, -0.1f, 0.2f, 0.0f);
        put(AmmoCaliber.MM_9, AmmoMaterial.STONE, 0.1f, 0.0f, 0.15f);
        put(AmmoCaliber.MM_9, AmmoMaterial.IRON, 0.0f, 0.0f, -0.15f);
        put(AmmoCaliber.MM_9, AmmoMaterial.LAPIS, -0.15f, -0.15f, 0.0f);
        put(AmmoCaliber.MM_9, AmmoMaterial.GOLD, 0.15f, 0.1f, 0.05f);
        put(AmmoCaliber.MM_9, AmmoMaterial.REDSTONE, 0.0f, 0.25f, 0.05f);
        put(AmmoCaliber.MM_9, AmmoMaterial.EMERALD, -0.05f, -0.15f, 0.0f);
        put(AmmoCaliber.MM_9, AmmoMaterial.QUARTZ, 0.0f, 0.0f, 0.2f);
        put(AmmoCaliber.MM_9, AmmoMaterial.DIAMOND, 0.0f, -0.1f, -0.05f);
        put(AmmoCaliber.MM_9, AmmoMaterial.NETHERITE, 0.05f, -0.25f, 0.0f);
    }

    private static void apply45Acp() {
        put(AmmoCaliber.ACP_45, AmmoMaterial.WOOD, 0.0f, 0.15f, -0.05f);
        put(AmmoCaliber.ACP_45, AmmoMaterial.STONE, 0.0f, -0.05f, 0.0f);
        put(AmmoCaliber.ACP_45, AmmoMaterial.IRON, 0.0f, 0.0f, -0.1f);
        put(AmmoCaliber.ACP_45, AmmoMaterial.LAPIS, 0.0f, 0.20f, 0.15f);
        put(AmmoCaliber.ACP_45, AmmoMaterial.GOLD, 0.15f, 0.05f, 0.0f);
        put(AmmoCaliber.ACP_45, AmmoMaterial.REDSTONE, -0.2f, -0.05f, 0.0f);
        put(AmmoCaliber.ACP_45, AmmoMaterial.EMERALD, -0.1f, 0.0f, 0.2f);
        put(AmmoCaliber.ACP_45, AmmoMaterial.QUARTZ, -0.05f, 0.15f, 0.0f);
        put(AmmoCaliber.ACP_45, AmmoMaterial.DIAMOND, 0.0f, -0.2f, -0.1f);
        put(AmmoCaliber.ACP_45, AmmoMaterial.NETHERITE, 0.1f, -0.45f, 0.0f);
    }

    private static void apply556() {
        put(AmmoCaliber.MM_556, AmmoMaterial.WOOD, -0.1f, 0.15f, 0.1f);
        put(AmmoCaliber.MM_556, AmmoMaterial.STONE, 0.0f, 0.0f, 0.1f);
        put(AmmoCaliber.MM_556, AmmoMaterial.IRON, 0.0f, -0.15f, 0.0f);
        put(AmmoCaliber.MM_556, AmmoMaterial.LAPIS, 0.1f, 0.2f, -0.05f);
        put(AmmoCaliber.MM_556, AmmoMaterial.GOLD, 0.0f, 0.0f, -0.15f);
        put(AmmoCaliber.MM_556, AmmoMaterial.REDSTONE, 0.0f, 0.15f, 0.0f);
        put(AmmoCaliber.MM_556, AmmoMaterial.EMERALD, -0.1f, 0.0f, 0.2f);
        put(AmmoCaliber.MM_556, AmmoMaterial.QUARTZ, 0.1f, 0.3f, 0.0f);
        put(AmmoCaliber.MM_556, AmmoMaterial.DIAMOND, 0.05f, 0.0f, 0.0f);
        put(AmmoCaliber.MM_556, AmmoMaterial.NETHERITE, 0.25f, -0.2f, -0.1f);
    }

    private static void apply762() {
        put(AmmoCaliber.MM_762, AmmoMaterial.WOOD, -0.25f, 0.0f, -0.2f);
        put(AmmoCaliber.MM_762, AmmoMaterial.STONE, 0.0f, 0.0f, 0.25f);
        put(AmmoCaliber.MM_762, AmmoMaterial.IRON, 0.3f, 0.0f, -0.25f);
        put(AmmoCaliber.MM_762, AmmoMaterial.LAPIS, 0.15f, 0.25f, -0.05f);
        put(AmmoCaliber.MM_762, AmmoMaterial.REDSTONE, -0.2f, -0.05f, 0.05f);
        put(AmmoCaliber.MM_762, AmmoMaterial.EMERALD, -0.05f, 0.05f, 0.0f);
        put(AmmoCaliber.MM_762, AmmoMaterial.QUARTZ, 0.05f, 0.35f, 0.15f);
        put(AmmoCaliber.MM_762, AmmoMaterial.DIAMOND, -0.1f, -0.15f, 0.0f);
        put(AmmoCaliber.MM_762, AmmoMaterial.NETHERITE, 0.25f, -0.3f, -0.05f);
    }

    private static void applyMagnum() {
        for (AmmoMaterial mat : AmmoMaterial.values()) {
            if (mat == AmmoMaterial.UNKNOWN || mat == AmmoMaterial.WOOD || mat == AmmoMaterial.STONE) {
                continue;
            }
            float scale = mat.damageMultiplier();
            put(AmmoCaliber.MAGNUM, mat, -0.05f + scale * 0.05f, -0.1f, -0.15f);
        }
    }

    private static void apply12g() {
        put(AmmoCaliber.G_12, AmmoMaterial.WOOD, 0.0f, 0.5f, 0.0f);
        put(AmmoCaliber.G_12, AmmoMaterial.STONE, 0.2f, -0.05f, 0.0f);
        put(AmmoCaliber.G_12, AmmoMaterial.IRON, 0.0f, 0.0f, -0.1f);
        put(AmmoCaliber.G_12, AmmoMaterial.LAPIS, -0.05f, 0.1f, -0.05f);
        put(AmmoCaliber.G_12, AmmoMaterial.GOLD, -0.3f, 0.0f, 0.2f);
        put(AmmoCaliber.G_12, AmmoMaterial.REDSTONE, 0.0f, 0.35f, 0.0f);
        put(AmmoCaliber.G_12, AmmoMaterial.QUARTZ, -0.5f, 0.0f, 0.0f);
        put(AmmoCaliber.G_12, AmmoMaterial.DIAMOND, -0.2f, -0.25f, 0.0f);
        put(AmmoCaliber.G_12, AmmoMaterial.NETHERITE, 0.3f, -0.35f, 0.1f);
    }

    private static void put(AmmoCaliber caliber, AmmoMaterial mat, float r, float d, float j) {
        PROFILES.computeIfAbsent(caliber, k -> new EnumMap<>(AmmoMaterial.class))
                .put(mat, new AmmoStatProfile(r, d, j));
    }

    private static void loadConfigOverrides() {
        Path file = PortPaths.configRoot().resolve("ammo_materials.json");
        if (!Files.isRegularFile(file)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (root.has("damage_multiplier")) {
                JsonObject dm = root.getAsJsonObject("damage_multiplier");
                for (AmmoMaterial m : AmmoMaterial.values()) {
                    if (m == AmmoMaterial.UNKNOWN) {
                        continue;
                    }
                    if (dm.has(m.getId())) {
                        DAMAGE_MULT.put(m, dm.get(m.getId()).getAsFloat());
                    }
                }
            }
            if (root.has("profiles")) {
                JsonObject profiles = root.getAsJsonObject("profiles");
                for (String caliberKey : profiles.keySet()) {
                    AmmoCaliber caliber = parseCaliber(caliberKey);
                    if (caliber == AmmoCaliber.UNKNOWN) {
                        continue;
                    }
                    JsonObject byMat = profiles.getAsJsonObject(caliberKey);
                    for (String matKey : byMat.keySet()) {
                        AmmoMaterial mat = AmmoMaterial.fromId(matKey);
                        JsonObject o = byMat.getAsJsonObject(matKey);
                        put(
                                caliber,
                                mat,
                                o.get("recoil").getAsFloat(),
                                o.get("durability").getAsFloat(),
                                o.get("jam_chance").getAsFloat());
                    }
                }
            }
            GunsRpg.LOGGER.info("[gunsrpg] 已加载 ammo_materials.json 覆盖");
        } catch (Exception ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] ammo_materials.json 解析失败，使用内置默认", ex);
        }
    }

    private static AmmoCaliber parseCaliber(String key) {
        return switch (key.toLowerCase()) {
            case "9mm" -> AmmoCaliber.MM_9;
            case "45acp", ".45acp" -> AmmoCaliber.ACP_45;
            case "556mm", "5.56mm" -> AmmoCaliber.MM_556;
            case "762mm", "7.62mm" -> AmmoCaliber.MM_762;
            case "magnum", "338mag", ".338magnum" -> AmmoCaliber.MAGNUM;
            case "12g" -> AmmoCaliber.G_12;
            default -> AmmoCaliber.UNKNOWN;
        };
    }
}
