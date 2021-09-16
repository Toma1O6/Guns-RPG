package dev.toma.gunsrpg.common.quests.condition;

import net.minecraft.entity.player.PlayerEntity;

public final class EmptyCondition implements IQuestCondition {

    private static final EmptyCondition INSTANCE = new EmptyCondition();

    private EmptyCondition() {}

    public static IQuestCondition empty() {
        return INSTANCE;
    }

    @Override
    public boolean test(PlayerEntity player) {
        return false;
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
