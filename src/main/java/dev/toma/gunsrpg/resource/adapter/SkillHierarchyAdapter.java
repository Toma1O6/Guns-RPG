package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillHierarchy;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
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

        JsonObject childStructure = getChildObject(object);
        boolean childOverrides = childStructure != null && useOnlyChildAsOverride(childStructure);
        boolean isContainer = childStructure != null && isContainerForChildSkills(childStructure);
        SkillType<?>[] children = childStructure != null ? deserializeChildren(childStructure) : null;

        SkillType<?> override;
        if (childOverrides) {
            if (children.length != 1) {
                throw new JsonSyntaxException("Cannot setup children array as automatic override because exactly 1 child is required. Got " + children.length);
            }
            override = children[0];
        } else {
            override = deserializeOverride(object);
        }
        validateSkillOverride(children, override);
        return new SkillHierarchy<>(category, parent, children, override, isContainer);
    }

    private static void validateSkillOverride(SkillType<?>[] children, SkillType<?> override) throws JsonParseException {
        if (override == null)
            return;
        if ((children == null && override != null) || !ModUtils.contains(override, children)) {
            throw new JsonSyntaxException("Skill cannot be overriden by non-child skill type!");
        }
    }

    private static SkillType<?> deserializeOverride(JsonObject object) throws JsonParseException {
        return parseSkillFromObject(object, "override");
    }

    private static boolean useOnlyChildAsOverride(JsonObject object) throws JsonParseException {
        return JSONUtils.getAsBoolean(object, "overrides", false);
    }

    private static boolean isContainerForChildSkills(JsonObject object) throws JsonParseException {
        return JSONUtils.getAsBoolean(object, "container", false);
    }

    private static SkillType<?>[] deserializeChildren(JsonObject object) throws JsonParseException {
        JsonArray array = JSONUtils.getAsJsonArray(object, "values");
        SkillType<?>[] children = new SkillType[array.size()];
        int index = 0;
        for (JsonElement element : array) {
            children[index++] = parseSkillByKey(element.getAsString());
        }
        return children;
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
