package dev.toma.gunsrpg.resource.util.conditions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

public interface IRecipeCondition {

    boolean canCraft(PlayerEntity player);

    ITextComponent getDisplayInfo();

    ConditionType<?> getType();
}
