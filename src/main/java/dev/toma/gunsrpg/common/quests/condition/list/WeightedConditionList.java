package dev.toma.gunsrpg.common.quests.condition.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.NoConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionManager;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.math.WeightedRandom;

public class WeightedConditionList {

    public static WeightedConditionList EMPTY_LIST = new WeightedConditionList(new WeightedProvider[] { new WeightedProvider(1, NoConditionProvider.NO_CONDITION) });
    private final WeightedRandom<WeightedProvider> randomProviderSelector;

    public WeightedConditionList(WeightedProvider[] providers) {
        this.randomProviderSelector = new WeightedRandom<>(WeightedProvider::getWeight, providers);
    }

    public IQuestConditionProvider getProvider() {
        return randomProviderSelector.getRandom().getProvider();
    }

    public static WeightedConditionList resolve(JsonElement element, QuestConditionManager manager) {
        JsonArray array = JsonHelper.asJsonArray(element);
        WeightedProvider[] providers = JsonHelper.deserializeInto(array, WeightedProvider[]::new, json -> WeightedProvider.resolve(json, manager));
        return new WeightedConditionList(providers);
    }
}
