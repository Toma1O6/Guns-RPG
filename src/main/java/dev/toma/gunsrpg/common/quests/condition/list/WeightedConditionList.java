package dev.toma.gunsrpg.common.quests.condition.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.NoConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionLoader;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Set;

public class WeightedConditionList {

    public static WeightedConditionList EMPTY_LIST = singletonList(NoConditionProvider.NO_CONDITION);
    private final WeightedRandom<WeightedProvider> randomProviderSelector;

    public WeightedConditionList(WeightedProvider[] providers) {
        this.randomProviderSelector = new WeightedRandom<>(WeightedProvider::getWeight, providers);
    }

    public static WeightedConditionList singletonList(IQuestConditionProvider provider) {
        return new WeightedConditionList(new WeightedProvider[]{ new WeightedProvider(1, GunsRPG.makeResource("no_name"), provider) });
    }

    public WeightedConditionList filter(Set<ResourceLocation> ignoredTypes) {
        return new WeightedConditionList(
                Arrays.stream(randomProviderSelector.getValues())
                        .filter(provider -> !ignoredTypes.contains(provider.getId()))
                        .toArray(WeightedProvider[]::new)
        );
    }

    public IQuestConditionProvider getProvider() {
        return randomProviderSelector.getRandom().getProvider();
    }

    public static WeightedConditionList resolve(JsonElement element, QuestConditionLoader manager) {
        JsonArray array = JsonHelper.asJsonArray(element);
        WeightedProvider[] providers = JsonHelper.deserializeInto(array, WeightedProvider[]::new, json -> WeightedProvider.resolve(json, manager));
        return new WeightedConditionList(providers);
    }
}
