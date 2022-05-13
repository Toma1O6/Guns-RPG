package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public final class QuestConditionLoader {

    public IQuestConditionProvider loadCondition(JsonElement data) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(data);
        ResourceLocation type = new ResourceLocation(JSONUtils.getAsString(object, "type"));
        return loadProvider(type, object);
    }

    private <P extends IQuestConditionProvider> P loadProvider(ResourceLocation typeId, JsonObject data) {
        QuestConditionProviderType<P> type = QuestConditions.getByKey(typeId);
        if (type == null) {
            throw new JsonSyntaxException("Unknown condition type: " + typeId);
        }
        IQuestConditionProviderSerializer<P> serializer = type.getSerializer();
        return serializer.deserialize(type, data);
    }
}
