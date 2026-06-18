package com.wf.firearms.grenade;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PortPaths;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 手雷金属材质表。默认铁/金；可在 {@code config/gunsrpg/grenade_materials.json} 追加 MMT 合金。
 * 仅加载 {@code metal: true}（默认）的条目。
 */
public final class GrenadeMaterialRegistry {
    private static final Map<String, GrenadeMaterial> MATERIALS = new LinkedHashMap<>();

    private GrenadeMaterialRegistry() {}

    public static void reload() {
        MATERIALS.clear();
        register(GrenadeMaterial.ironDefaults());
        register(GrenadeMaterial.goldDefaults());
        register(GrenadeMaterial.netheriteDefaults());
        loadConfigOverrides();
    }

    public static List<GrenadeMaterial> all() {
        return List.copyOf(MATERIALS.values());
    }

    public static Optional<GrenadeMaterial> byId(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(MATERIALS.get(id));
    }

    public static Optional<GrenadeStats> statsForItemId(String path) {
        for (GrenadeKind kind : GrenadeKind.values()) {
            String legacy = kind.legacyItemId();
            if (legacy.equals(path)) {
                return byId("iron").map(m -> GrenadeStats.of(m, kind));
            }
            for (GrenadeMaterial mat : all()) {
                if (kind.itemId(mat.id()).equals(path)) {
                    return Optional.of(GrenadeStats.of(mat, kind));
                }
            }
        }
        return Optional.empty();
    }

    private static void register(GrenadeMaterial material) {
        MATERIALS.put(material.id(), material);
    }

    private static void loadConfigOverrides() {
        Path path = PortPaths.configRoot().resolve("grenade_materials.json");
        if (!Files.isRegularFile(path)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (!root.has("materials")) {
                return;
            }
            JsonArray arr = root.getAsJsonArray("materials");
            for (JsonElement el : arr) {
                if (!el.isJsonObject()) {
                    continue;
                }
                parseMaterial(el.getAsJsonObject()).ifPresent(GrenadeMaterialRegistry::register);
            }
        } catch (Exception e) {
            GunsRpg.LOGGER.warn("[gunsrpg] 无法加载 grenade_materials.json: {}", e.toString());
        }
    }

    private static Optional<GrenadeMaterial> parseMaterial(JsonObject obj) {
        if (obj.has("enabled") && !obj.get("enabled").getAsBoolean()) {
            return Optional.empty();
        }
        if (obj.has("metal") && !obj.get("metal").getAsBoolean()) {
            return Optional.empty();
        }
        String id = obj.get("id").getAsString();
        float radius = obj.has("base_blast_radius") ? obj.get("base_blast_radius").getAsFloat() : 2.5f;
        float damage = obj.has("base_damage") ? obj.get("base_damage").getAsFloat() : 20f;
        Item ingot = item(obj, "ingot").orElse(null);
        Item plate = item(obj, "plate").orElse(ingot);
        Item nugget = item(obj, "nugget").orElse(null);
        if (ingot == null || nugget == null) {
            GunsRpg.LOGGER.warn("[gunsrpg] 手雷材质 {} 缺少 ingot/nugget，已跳过", id);
            return Optional.empty();
        }
        if (plate == null) {
            plate = ingot;
        }
        return Optional.of(new GrenadeMaterial(id, radius, damage, ingot, plate, nugget));
    }

    private static Optional<Item> item(JsonObject obj, String key) {
        if (!obj.has(key)) {
            return Optional.empty();
        }
        ResourceLocation loc = ResourceLocation.tryParse(obj.get(key).getAsString());
        if (loc == null) {
            return Optional.empty();
        }
        Item item = ForgeRegistries.ITEMS.getValue(loc);
        return item == null ? Optional.empty() : Optional.of(item);
    }

    /** 供生成脚本读取：导出为不可变列表。 */
    public static List<GrenadeMaterial> snapshot() {
        return Collections.unmodifiableList(new ArrayList<>(MATERIALS.values()));
    }
}
