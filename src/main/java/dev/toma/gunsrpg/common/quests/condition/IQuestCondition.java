package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public interface IQuestCondition {

    boolean isValid(PlayerEntity player, IPropertyReader reader);

    default ITextComponent getDescriptor() {
        return StringTextComponent.EMPTY;
    };
}
