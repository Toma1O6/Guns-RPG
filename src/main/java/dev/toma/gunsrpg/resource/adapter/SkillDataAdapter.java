package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.resource.skill.SkillPropertyLoader;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class SkillDataAdapter implements JsonDeserializer<SkillPropertyLoader.ILoadResult<?>> {

    @Override
    public SkillPropertyLoader.ILoadResult<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        ISkillHierarchy<?> hierarchy = context.deserialize(JSONUtils.getAsJsonObject(object, "hierarchy"), ISkillHierarchy.class);
        ISkillProperties properties = context.deserialize(JSONUtils.getAsJsonObject(object, "properties"), ISkillProperties.class);
        boolean disabled = JSONUtils.getAsBoolean(object, "disabled", false);
        return new SkillPropertyLoader.Result<>(hierarchy, properties, disabled);
    }
}
