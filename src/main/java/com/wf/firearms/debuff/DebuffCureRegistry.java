package com.wf.firearms.debuff;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PortPaths;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 物品 ID → 可治愈的 Debuff 列表（来自 debuff_config.json 的 cures）。 */
public final class DebuffCureRegistry {
    public enum CureMode {
        CLEAR,
        /** 阶段型 debuff 降 1 级，骨折则直接清除 */
        REDUCE_ONE
    }

    public record CureEntry(List<DebuffType> types, CureMode mode) {}

    private static final Map<ResourceLocation, CureEntry> CURES = new HashMap<>();

    private DebuffCureRegistry() {}

    public static void reload() {
        CURES.clear();
        loadDefaults();
        var file = PortPaths.configRoot().resolve("debuff_config.json");
        if (!Files.isRegularFile(file)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (root.has("cures")) {
                CURES.clear();
                parseCures(root.getAsJsonObject("cures"));
            }
            GunsRpg.LOGGER.info("[gunsrpg] 已加载 {} 条 Debuff 医疗物品映射", CURES.size());
        } catch (IOException e) {
            GunsRpg.LOGGER.error("[gunsrpg] 读取 cures 配置失败", e);
        }
    }

    private static void loadDefaults() {
        put("scalinghealth:bandages", CureMode.REDUCE_ONE, DebuffType.BLEED);
        put("scalinghealth:medkit", CureMode.CLEAR, DebuffType.FRACTURE);
        put("roughtweaks:medkit_enchanted", CureMode.CLEAR, DebuffType.POISON, DebuffType.INFECTION);
    }

    private static void parseCures(JsonObject cures) {
        for (var entry : cures.entrySet()) {
            if (entry.getKey().startsWith("_")) {
                continue;
            }
            ResourceLocation itemId = ResourceLocation.tryParse(entry.getKey());
            if (itemId == null) {
                continue;
            }
            JsonElement val = entry.getValue();
            CureMode mode = CureMode.CLEAR;
            List<DebuffType> types = new ArrayList<>();
            if (val.isJsonObject()) {
                JsonObject obj = val.getAsJsonObject();
                if (obj.has("mode")) {
                    mode = parseMode(obj.get("mode").getAsString());
                }
                if (obj.has("types")) {
                    addTypes(types, obj.get("types"));
                }
            } else if (val.isJsonArray()) {
                addTypes(types, val);
            } else if (val.isJsonPrimitive()) {
                addTypeToken(types, val.getAsString());
            }
            if (!types.isEmpty()) {
                CURES.put(itemId, new CureEntry(List.copyOf(types), mode));
            }
        }
    }

    private static void addTypes(List<DebuffType> types, JsonElement el) {
        if (el.isJsonArray()) {
            for (JsonElement item : el.getAsJsonArray()) {
                addTypeToken(types, item.getAsString());
            }
        }
    }

    private static void addTypeToken(List<DebuffType> types, String token) {
        if (token == null || token.isEmpty()) {
            return;
        }
        String id = token;
        if (token.contains(":")) {
            String[] parts = token.split(":", 2);
            id = parts[0];
        }
        DebuffType t = DebuffType.fromId(id);
        if (t != null) {
            types.add(t);
        }
    }

    private static CureMode parseMode(String raw) {
        return "reduce_one".equalsIgnoreCase(raw) ? CureMode.REDUCE_ONE : CureMode.CLEAR;
    }

    private static void put(String itemId, CureMode mode, DebuffType... types) {
        ResourceLocation loc = ResourceLocation.tryParse(itemId);
        if (loc != null) {
            CURES.put(loc, new CureEntry(List.of(types), mode));
        }
    }

    public static CureEntry cureEntryFor(ResourceLocation itemId) {
        if (itemId == null) {
            return null;
        }
        return CURES.get(itemId);
    }

    public static Map<ResourceLocation, CureEntry> allCures() {
        return Collections.unmodifiableMap(CURES);
    }
}
