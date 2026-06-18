package com.wf.firearms.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.combat.AmmoCaliber;
import com.wf.firearms.data.PortPaths;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** {@code config/gunsrpg/weapon_caliber_overrides.json} — tooltip 与装配口径的单一真相源。 */
public final class WeaponCaliberOverrides {
    private static final Map<String, AmmoCaliber> PRIMARY = new HashMap<>();
    private static final Map<String, String> TACZ_AMMO = new HashMap<>();

    private WeaponCaliberOverrides() {}

    public static void reload() {
        PRIMARY.clear();
        TACZ_AMMO.clear();
        var file = PortPaths.configRoot().resolve("weapon_caliber_overrides.json");
        if (!Files.isRegularFile(file)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (!root.has("weapons")) {
                return;
            }
            JsonObject weapons = root.getAsJsonObject("weapons");
            for (var entry : weapons.entrySet()) {
                if (entry.getKey().startsWith("_")) {
                    continue;
                }
                JsonObject row = entry.getValue().getAsJsonObject();
                if (row.has("primary")) {
                    AmmoCaliber c = parseCaliber(row.get("primary").getAsString());
                    if (c != AmmoCaliber.UNKNOWN) {
                        PRIMARY.put(entry.getKey(), c);
                    }
                }
                if (row.has("tacz_ammo")) {
                    TACZ_AMMO.put(entry.getKey(), row.get("tacz_ammo").getAsString());
                }
            }
            GunsRpg.LOGGER.info("[gunsrpg] 已加载枪械口径覆盖 {} 条", PRIMARY.size());
        } catch (IOException ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] 读取 weapon_caliber_overrides.json 失败", ex);
        }
    }

    public static Optional<AmmoCaliber> primaryCaliber(String weaponKey) {
        return Optional.ofNullable(PRIMARY.get(weaponKey));
    }

    public static Map<String, AmmoCaliber> allPrimary() {
        return Collections.unmodifiableMap(PRIMARY);
    }

    public static Optional<String> taczAmmoId(String weaponKey) {
        return Optional.ofNullable(TACZ_AMMO.get(weaponKey));
    }

    private static AmmoCaliber parseCaliber(String raw) {
        if (raw == null || raw.isEmpty()) {
            return AmmoCaliber.UNKNOWN;
        }
        return switch (raw) {
            case "9mm" -> AmmoCaliber.MM_9;
            case "45acp" -> AmmoCaliber.ACP_45;
            case "556mm" -> AmmoCaliber.MM_556;
            case "762mm" -> AmmoCaliber.MM_762;
            case "magnum" -> AmmoCaliber.MAGNUM;
            case "12g" -> AmmoCaliber.G_12;
            default -> AmmoCaliber.UNKNOWN;
        };
    }
}
