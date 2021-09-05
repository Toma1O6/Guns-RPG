package dev.toma.gunsrpg.common.quests.condition;

public final class EmptyCondition implements IQuestCondition {

    private static final EmptyCondition INSTANCE = new EmptyCondition();

    private EmptyCondition() {}

    public static IQuestCondition empty() {
        return INSTANCE;
    }

    @Override
    public int getRewardTierModifier() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
