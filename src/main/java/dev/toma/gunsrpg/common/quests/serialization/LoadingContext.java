package dev.toma.gunsrpg.common.quests.serialization;

import dev.toma.gunsrpg.common.quests.condition.ConditionListManager;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionManager;

public final class LoadingContext {

    private final QuestConditionManager conditionManager;
    private final ConditionListManager listManager;

    private LoadingContext(QuestConditionManager conditionManager, ConditionListManager listManager) {
        this.conditionManager = conditionManager;
        this.listManager = listManager;
    }

    public static LoadingContext of(QuestConditionManager conditionManager, ConditionListManager listManager) {
        return new LoadingContext(conditionManager, listManager);
    }

    public QuestConditionManager getConditionManager() {
        return conditionManager;
    }

    public ConditionListManager getListManager() {
        return listManager;
    }
}
