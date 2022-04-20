package dev.toma.gunsrpg.common.quests.condition.list;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionManager;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public final class WeightedProvider {

    private final int weight;
    private final IQuestConditionProvider provider;

    public WeightedProvider(int weight, IQuestConditionProvider provider) {
        this.weight = weight;
        this.provider = provider;
    }

    public int getWeight() {
        return weight;
    }

    public IQuestConditionProvider getProvider() {
        return provider;
    }

    public static WeightedProvider resolve(JsonElement element, QuestConditionManager manager) {
        JsonObject object = JsonHelper.asJsonObject(element);
        int weight = JSONUtils.getAsInt(object, "weight");
        ResourceLocation conditionId = new ResourceLocation(JSONUtils.getAsString(object, "condition"));
        IQuestConditionProvider provider = manager.getProvider(conditionId);
        return new WeightedProvider(weight, provider);
    }
}
