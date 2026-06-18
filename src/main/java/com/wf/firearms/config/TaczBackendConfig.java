package com.wf.firearms.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.compat.tacz.TaczWeaponCatalog;
import com.wf.firearms.data.PortPaths;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

/** {@code config/gunsrpg/tacz_backend.json} — TaCZ 射击与 wf 枪映射。 */
public final class TaczBackendConfig {
    private static boolean useTaczShooting;
    private static boolean disableTaczNativeCrafting = true;
    private static final Map<String, String> WEAPON_MAP = new LinkedHashMap<>();

    private TaczBackendConfig() {}

    public static void reload() {
        useTaczShooting = false;
        disableTaczNativeCrafting = true;
        WEAPON_MAP.clear();
        WEAPON_MAP.putAll(TaczWeaponCatalog.builtinWeaponToGunMap());

        var file = PortPaths.configRoot().resolve("tacz_backend.json");
        if (!Files.isRegularFile(file)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (root.has("use_tacz_as_shooting_backend")) {
                useTaczShooting = root.get("use_tacz_as_shooting_backend").getAsBoolean();
            }
            if (root.has("disable_tacz_native_crafting")) {
                disableTaczNativeCrafting = root.get("disable_tacz_native_crafting").getAsBoolean();
            }
            if (root.has("weapon_map")) {
                JsonObject map = root.getAsJsonObject("weapon_map");
                for (var entry : map.entrySet()) {
                    if (!entry.getKey().startsWith("_")) {
                        WEAPON_MAP.put(entry.getKey(), entry.getValue().getAsString());
                    }
                }
            }
        } catch (IOException ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] 读取 tacz_backend.json 失败", ex);
        }
        GunsRpg.LOGGER.info(
                "[gunsrpg] TaCZ 射击={}（mod 已装={}），禁用 TaCZ 原版合成台={}，枪映射 {} 条",
                useTaczShooting,
                TaczCompat.isTaczLoaded(),
                disableTaczNativeCrafting,
                WEAPON_MAP.size());
    }

    public static boolean useTaczShooting() {
        return useTaczShooting && TaczCompat.isTaczLoaded();
    }

    public static boolean disableTaczNativeCrafting() {
        return disableTaczNativeCrafting;
    }

    public static Map<String, String> weaponMap() {
        return Map.copyOf(WEAPON_MAP);
    }
}
