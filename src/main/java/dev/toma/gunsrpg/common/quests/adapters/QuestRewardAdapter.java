package dev.toma.gunsrpg.common.quests.adapters;

import com.google.gson.*;
import dev.toma.gunsrpg.common.quests.reward.IAssemblyFunction;
import dev.toma.gunsrpg.common.quests.reward.IQuestItemProvider;
import dev.toma.gunsrpg.common.quests.reward.IQuestRewardResolver;
import dev.toma.gunsrpg.common.quests.reward.RewardProviderType;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Type;

public class QuestRewardAdapter implements JsonDeserializer<IQuestItemProvider> {

    @Override
    public IQuestItemProvider deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        ResourceLocation location = new ResourceLocation(JSONUtils.getAsString(object, "type"));
        RewardProviderType type = RewardProviderType.Registry.getById(location);
        if (type == null) {
            throw new JsonSyntaxException("Unknown type: " + location);
        }
        int count = MathHelper.clamp(JSONUtils.getAsInt(object, "count", 1), 1, 64);
        IQuestRewardResolver resolver = type.getResolver();
        IAssemblyFunction function = object.has("function") ? context.deserialize(JSONUtils.getAsJsonObject(object, "function"), IAssemblyFunction.class) : null;
        return resolver.resolve(object, type, count, function);
    }
}
