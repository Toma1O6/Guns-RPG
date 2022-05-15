package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

public interface IQuestCondition {

    boolean isValid(PlayerEntity player, IPropertyReader reader);

    ITextComponent getDescriptor();
}
