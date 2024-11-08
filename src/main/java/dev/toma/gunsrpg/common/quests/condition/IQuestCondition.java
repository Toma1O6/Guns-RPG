package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

public interface IQuestCondition {

    IQuestConditionProvider<?> getProviderType();

    boolean isValid(QuestingGroup group, IPropertyReader reader);

    ITextComponent getDescriptor(boolean shortDesc);

    default void saveData(CompoundNBT nbt) {}

    default void loadData(CompoundNBT nbt) {}
}
