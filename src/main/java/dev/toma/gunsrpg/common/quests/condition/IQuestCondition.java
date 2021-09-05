package dev.toma.gunsrpg.common.quests.condition;

public interface IQuestCondition {

    int getRewardTierModifier();

    default boolean isEmpty() {
        return false;
    }
}
