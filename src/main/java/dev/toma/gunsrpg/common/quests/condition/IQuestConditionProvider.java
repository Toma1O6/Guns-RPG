package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.util.properties.IPropertyWriter;
import net.minecraft.entity.player.PlayerEntity;

public interface IQuestConditionProvider {

    IQuestCondition getCondition();

    QuestConditionProviderType<?> getType();

    default void setInitialStates(PlayerEntity player, IPropertyWriter writer) {}
}
