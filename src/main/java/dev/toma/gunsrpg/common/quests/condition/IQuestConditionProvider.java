package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.quest.IQuestFactory;
import net.minecraft.nbt.CompoundNBT;

public interface IQuestConditionProvider<C extends IQuestCondition> {

    C createDefaultInstance();

    default C createWithContext(IQuestFactory.InstanceContext<?, ?> context) {
        return this.createDefaultInstance();
    }

    QuestConditionProviderType<?> getType();

    default void saveInternalData(CompoundNBT nbt) {}
}
