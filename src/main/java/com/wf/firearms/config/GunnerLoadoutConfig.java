package com.wf.firearms.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PortPaths;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** {@code config/gunsrpg/gunner_loadout.json} */
public final class GunnerLoadoutConfig {
    public record LoadoutEntry(int weight, String gun, String ammo, float minDifficulty) {}

    private static float damageMultiplier = 0.55f;
    private static float inaccuracy = 0.12f;
    private static float difficultyDamageScale = 0.08f;
    private static List<LoadoutEntry> loadouts = List.of();

    private GunnerLoadoutConfig() {}

    public static void reload() {
        damageMultiplier = 0.55f;
        inaccuracy = 0.12f;
        difficultyDamageScale = 0.08f;
        loadouts = List.of();
        var file = PortPaths.configRoot().resolve("gunner_loadout.json");
        if (!Files.isRegularFile(file)) {
            GunsRpg.LOGGER.warn("[gunsrpg] 未找到 gunner_loadout.json");
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (root.has("global_props")) {
                JsonObject gp = root.getAsJsonObject("global_props");
                if (gp.has("damage_multiplier")) {
                    damageMultiplier = gp.get("damage_multiplier").getAsFloat();
                }
                if (gp.has("inaccuracy")) {
                    inaccuracy = gp.get("inaccuracy").getAsFloat();
                }
                if (gp.has("difficulty_damage_scale")) {
                    difficultyDamageScale = gp.get("difficulty_damage_scale").getAsFloat();
                }
            }
            List<LoadoutEntry> list = new ArrayList<>();
            if (root.has("loadouts")) {
                JsonArray arr = root.getAsJsonArray("loadouts");
                for (JsonElement el : arr) {
                    JsonObject o = el.getAsJsonObject();
                    list.add(
                            new LoadoutEntry(
                                    o.has("weight") ? o.get("weight").getAsInt() : 1,
                                    o.get("gun").getAsString(),
                                    o.has("ammo") ? o.get("ammo").getAsString() : "iron",
                                    o.has("min_difficulty") ? o.get("min_difficulty").getAsFloat() : 0f));
                }
            }
            loadouts = Collections.unmodifiableList(list);
            GunsRpg.LOGGER.info("[gunsrpg] 已加载 gunner_loadout {} 条", loadouts.size());
        } catch (IOException ex) {
            GunsRpg.LOGGER.error("[gunsrpg] 读取 gunner_loadout.json 失败", ex);
        }
    }

    public static float damageMultiplier() {
        return damageMultiplier;
    }

    public static float inaccuracy() {
        return inaccuracy;
    }

    public static float difficultyDamageScale() {
        return difficultyDamageScale;
    }

    public static List<LoadoutEntry> loadouts() {
        return loadouts;
    }
}
