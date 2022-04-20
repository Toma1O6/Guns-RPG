package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

public interface IQuestConditionProviderSerializer<Q extends IQuestConditionProvider> {

    Q deserialize(QuestConditionProviderType<Q> conditionType, JsonDeserializationContext context, JsonElement data);
}
