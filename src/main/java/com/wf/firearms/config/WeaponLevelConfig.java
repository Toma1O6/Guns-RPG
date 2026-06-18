package com.wf.firearms.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PortPaths;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** {@code config/gunsrpg/weapon_levels.json} — 枪械 assembly 等级，用于 mob 配装上限。 */
public final class WeaponLevelConfig {
    private static Map<String, Integer> levels = Map.of();

    private WeaponLevelConfig() {}

    public static void reload() {
        levels = Map.of();
        var file = PortPaths.configRoot().resolve("weapon_levels.json");
        if (!Files.isRegularFile(file)) {
            GunsRpg.LOGGER.warn("[gunsrpg] 未找到 weapon_levels.json");
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (!root.has("guns")) {
                return;
            }
            Map<String, Integer> map = new HashMap<>();
            for (Map.Entry<String, JsonElement> e : root.getAsJsonObject("guns").entrySet()) {
                map.put(e.getKey(), e.getValue().getAsInt());
            }
            levels = Collections.unmodifiableMap(map);
            GunsRpg.LOGGER.info("[gunsrpg] 已加载 weapon_levels {} 条", levels.size());
        } catch (IOException ex) {
            GunsRpg.LOGGER.error("[gunsrpg] 读取 weapon_levels.json 失败", ex);
        }
    }

    public static int levelForGun(String gunKey) {
        if (gunKey == null || gunKey.isEmpty()) {
            return 999;
        }
        return levels.getOrDefault(gunKey, 999);
    }

    public static Map<String, Integer> all() {
        return levels;
    }
}
