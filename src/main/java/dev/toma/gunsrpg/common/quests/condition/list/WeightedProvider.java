package dev.toma.gunsrpg.common.quests.condition.list;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionLoader;
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

    public static WeightedProvider resolve(JsonElement element, QuestConditionLoader manager) {
        JsonObject object = JsonHelper.asJsonObject(element);
        int weight = JSONUtils.getAsInt(object, "weight");
        JsonObject condition = JSONUtils.getAsJsonObject(object, "condition");
        IQuestConditionProvider provider = GunsRPG.getModLifecycle().quests().getConditionLoader().loadCondition(condition);
        return new WeightedProvider(weight, provider);
    }
}
