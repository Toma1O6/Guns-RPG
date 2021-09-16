package dev.toma.gunsrpg.common.quests.condition;

import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Predicate;

public interface IQuestCondition extends Predicate<PlayerEntity> {

    int getRewardTierModifier();

    default boolean isEmpty() {
        return false;
    }
}
