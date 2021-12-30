package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillHierarchy;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Type;

public class SkillHierarchyAdapter implements JsonDeserializer<ISkillHierarchy<?>> {

    @Override
    public ISkillHierarchy<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        String categoryKey = JSONUtils.getAsString(object, "category");
        SkillCategory category = deserializeCategory(categoryKey);
        SkillType<?> parent = deserializeParent(object);
        SkillType<?> override = deserializeOverride(object);
        SkillType<?>[] children = deserializeChildren(object);
        SkillType<?>[] extensions = deserializeExtensions(object);
        return new SkillHierarchy<>(category, parent, override, children, extensions);
    }

    private static SkillType<?> deserializeOverride(JsonObject object) throws JsonParseException {
        if (!object.has("override")) {
            return null;
        }
        JsonElement overrideJson = object.get("override");
        if (overrideJson.isJsonNull())
            return null;
        return parseSkillByKey(overrideJson.getAsString());
    }

    private static SkillType<?>[] deserializeExtensions(JsonObject object) throws JsonParseException {
        if (!object.has("extensions")) {
            return null;
        }
        JsonArray array = JSONUtils.getAsJsonArray(object, "extensions");
        SkillType<?>[] ext = new SkillType[array.size()];
        int index = 0;
        for (JsonElement element : array) {
            ext[index++] = parseSkillByKey(element.getAsString());
        }
        return ext;
    }

    private static SkillType<?>[] deserializeChildren(JsonObject object) throws JsonParseException {
        if (object.has("children")) {
            JsonArray array = JSONUtils.getAsJsonArray(object, "children");
            SkillType<?>[] children = new SkillType[array.size()];
            int index = 0;
            for (JsonElement element : array) {
                children[index++] = parseSkillByKey(element.getAsString());
            }
            return children;
        }
        return null;
    }

    private static SkillType<?> deserializeParent(JsonObject object) throws JsonParseException {
        return parseSkillFromObject(object, "parent");
    }

    private static SkillCategory deserializeCategory(String categoryKey) throws JsonParseException {
        try {
            return SkillCategory.get(categoryKey);
        } catch (Exception e) {
            throw new JsonSyntaxException("Invalid skill category key: " + categoryKey);
        }
    }

    private static SkillType<?> parseSkillFromObject(JsonObject object, String key) throws JsonParseException {
        if (object.has(key)) {
            JsonElement element = object.get(key);
            if (element.isJsonNull())
                return null;
            return parseSkillByKey(element.getAsString());
        }
        return null;
    }

    private static SkillType<?> parseSkillByKey(String key) throws JsonParseException {
        ResourceLocation location = new ResourceLocation(key);
        IForgeRegistry<SkillType<?>> registry = ModRegistries.SKILLS;
        SkillType<?> type = registry.getValue(location);
        if (type == null) {
            throw new JsonSyntaxException("Unknown skill: " + key);
        }
        return type;
    }

    private static JsonObject getChildObject(JsonObject object) {
        if (object.has("children")) {
            JsonElement value = object.get("children");
            if (value.isJsonNull())
                return null;
            return JsonHelper.asJsonObject(value);
        }
        return null;
    }
}
