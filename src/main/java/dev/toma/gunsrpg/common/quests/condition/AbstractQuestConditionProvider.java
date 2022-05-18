package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.util.ModUtils;

public abstract class AbstractQuestConditionProvider<C extends IQuestCondition> implements IQuestConditionProvider<C> {

    private final QuestConditionProviderType<?> type;

    protected AbstractQuestConditionProvider(QuestConditionProviderType<?> type) {
        this.type = type;
    }

    public String getLocalizationString() {
        return "quest.condition." + ModUtils.convertToLocalization(type.getId());
    }

    @Override
    public QuestConditionProviderType<?> getType() {
        return type;
    }
}
