package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.resource.skill.SkillPropertyLoader;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.util.Arrays;

public class SkillPropertyAdapter implements JsonDeserializer<SkillPropertyLoader.Properties> {

    @Override
    public SkillPropertyLoader.Properties deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = JsonHelper.asJsonObject(json);
        int level = JSONUtils.getAsInt(obj, "level", 1);
        int price = JSONUtils.getAsInt(obj, "price", 1);
        String categoryKey = JSONUtils.getAsString(obj, "category");
        SkillCategory category;
        try {
            category = SkillCategory.get(categoryKey);
        } catch (Exception e) {
            String[] categories = Arrays.stream(SkillCategory.values()).map(Enum::name).map(String::toUpperCase).toArray(String[]::new);
            throw new JsonSyntaxException("Unknown category: " + categoryKey + ". Valid categories [" + String.join(", ", categories) + "]");
        }
        JsonArray childrenJson = JSONUtils.getAsJsonArray(obj, "children");
        SkillType<?>[] children = mapChildren(childrenJson);
        return new SkillPropertyLoader.Properties(level, price, category, children);
    }

    private SkillType<?>[] mapChildren(JsonArray array) {
        SkillType<?>[] children = new SkillType[array.size()];
        int index = 0;
        try {
            for (JsonElement element : array) {
                String key = element.getAsString();
                ResourceLocation resourceKey = new ResourceLocation(key);
                SkillType<?> type = ModRegistries.SKILLS.getValue(resourceKey);
                if (type == null) {
                    throw new JsonSyntaxException("Unknown skill type in children list: " + resourceKey);
                }
                children[index++] = type;
            }
        } catch (IllegalStateException | UnsupportedOperationException e) {
            throw new JsonParseException(e);
        }

        return children;
    }
}
