package dev.toma.gunsrpg.common.quests.condition;

import net.minecraft.nbt.CompoundNBT;

public interface IQuestConditionProvider<C extends IQuestCondition> {

    C makeConditionInstance();

    QuestConditionProviderType<?> getType();

    default void saveInternalData(CompoundNBT nbt) {}
}
