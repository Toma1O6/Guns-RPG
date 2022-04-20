package dev.toma.gunsrpg.common.quests.condition;

public abstract class AbstractQuestConditionProvider implements IQuestConditionProvider {

    private final QuestConditionProviderType<?> type;

    protected AbstractQuestConditionProvider(QuestConditionProviderType<?> type) {
        this.type = type;
    }

    @Override
    public QuestConditionProviderType<?> getType() {
        return type;
    }
}
