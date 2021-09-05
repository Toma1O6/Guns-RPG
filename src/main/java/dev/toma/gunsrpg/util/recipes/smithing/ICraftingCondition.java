package dev.toma.gunsrpg.util.recipes.smithing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

public interface ICraftingCondition {

    boolean canCraft(PlayerEntity player);

    ITextComponent getDisplayInfo();

    ConditionType<?> getType();
}
