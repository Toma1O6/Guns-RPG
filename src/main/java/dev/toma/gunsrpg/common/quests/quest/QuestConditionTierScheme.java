package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.*;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.NoConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.list.QuestConditionListManager;
import dev.toma.gunsrpg.common.quests.condition.list.WeightedConditionList;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class QuestConditionTierScheme {

    public static final QuestConditionTierScheme EMPTY_SCHEME = new QuestConditionTierScheme(new TieredList[0], Collections.emptySet());
    private final TieredList[] listProviders;

    private QuestConditionTierScheme(TieredList[] lists, Set<ResourceLocation> excludedConditions) {
        this.listProviders = new TieredList[lists.length];
        for (int i = 0; i < lists.length; i++) {
            this.listProviders[i] = lists[i].filter(excludedConditions);
        }
    }

    public TieredList[] getListProviders() {
        return listProviders;
    }

    public Result getModifiedConditions() {
        int tierModifier = 0;
        IQuestCondition[] conditions = new IQuestCondition[listProviders.length];
        for (int i = 0; i < listProviders.length; i++) {
            TieredList tieredList = listProviders[i];
            IQuestConditionProvider<?> provider = tieredList.getList().getProvider();
            if (provider != NoConditionProvider.NO_CONDITION) {
                tierModifier += tieredList.tier;
            }
            conditions[i] = provider.makeConditionInstance();
        }
        return new Result(conditions, tierModifier);
    }

    public static QuestConditionTierScheme fromJson(JsonElement element) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(element);
        JsonArray tieredConditionsArray = JSONUtils.getAsJsonArray(object, "conditions");
        JsonArray exclusionsArray = JSONUtils.getAsJsonArray(object, "exclusions", new JsonArray());
        Set<ResourceLocation> exclusions = JsonHelper.deserialize(exclusionsArray, arr -> arr.size() == 0 ? Collections.emptySet() : new HashSet<>(), el -> new ResourceLocation(el.getAsString()), Set::add);
        TieredList[] lists = JsonHelper.deserializeInto(tieredConditionsArray, TieredList[]::new, QuestConditionTierScheme::resolveAsList);
        return new QuestConditionTierScheme(lists, exclusions);
    }

    private static TieredList resolveAsList(JsonElement element) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(element);
        int tier = JSONUtils.getAsInt(object, "tier", 1);
        float propability = JSONUtils.getAsFloat(object, "propability", 1.0f);
        JsonElement conditionElement = object.get("condition");
        QuestSystem system = GunsRPG.getModLifecycle().quests();
        WeightedConditionList list;
        if (conditionElement.isJsonPrimitive()) {
            String asString = conditionElement.getAsString();
            if (!asString.startsWith("@")) {
                throw new JsonSyntaxException("List ids must start with @ prefix");
            }
            ResourceLocation location = new ResourceLocation(asString.substring(1));
            QuestConditionListManager manager = system.getConditionListManager();
            list = manager.getList(location);
        } else {
            list = WeightedConditionList.singletonList(system.getConditionLoader().loadCondition(conditionElement));
        }
        return new TieredList(list, tier, propability);
    }

    public static class TieredList {

        private static final Random RANDOM = new Random();
        private final WeightedConditionList list;
        private final int tier;
        private final float propability;

        public TieredList(WeightedConditionList list, int tier, float propability) {
            this.list = list;
            this.tier = tier;
            this.propability = propability;
        }

        public WeightedConditionList getListRaw() {
            return list;
        }

        public float getPropability() {
            return propability;
        }

        public int getTier() {
            return tier;
        }

        public WeightedConditionList getList() {
            return propability < 1.0 && RANDOM.nextFloat() < propability ? WeightedConditionList.EMPTY_LIST : list;
        }

        public TieredList filter(Set<ResourceLocation> ignored) {
            return ignored.isEmpty() ? this : new TieredList(list.filter(ignored), tier, propability);
        }
    }

    public static class Result {

        private final IQuestCondition[] conditions;
        private final int tierModifier;

        Result(IQuestCondition[] conditions, int tierModifier) {
            this.conditions = conditions;
            this.tierModifier = tierModifier;
        }

        public IQuestCondition[] getConditions() {
            return conditions;
        }

        public int getTierModifier() {
            return tierModifier;
        }
    }
}
