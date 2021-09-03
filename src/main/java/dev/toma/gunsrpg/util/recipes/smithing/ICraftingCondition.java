package dev.toma.gunsrpg.util.recipes.smithing;

import net.minecraft.entity.player.PlayerEntity;

public interface ICraftingCondition {

    boolean canCraft(PlayerEntity player);

    ConditionType<?> getType();
}
