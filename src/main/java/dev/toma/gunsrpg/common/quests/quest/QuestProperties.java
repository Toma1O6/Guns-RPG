package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.common.quests.condition.IConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.TieredCondition;

public final class QuestProperties {

    public final QuestType<?> type;
    public final IQuestData data;
    public final IQuestConditionProvider[] conditionProviders;
    public final TieredCondition[] tieredConditions;
    public final int rewardTier;

    public QuestProperties(QuestType<?> type, IQuestData data, IQuestConditionProvider[] conditionProviders, TieredCondition[] tieredConditions, int rewardTier) {
        this.type = type;
        this.data = data;
        this.conditionProviders = conditionProviders;
        this.tieredConditions = tieredConditions;
        this.rewardTier = rewardTier;
    }
}
