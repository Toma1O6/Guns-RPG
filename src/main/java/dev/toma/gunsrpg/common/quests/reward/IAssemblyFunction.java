package dev.toma.gunsrpg.common.quests.reward;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IAssemblyFunction {

    /**
     * Modifies the input item according to implementation logic
     * @param stack Original itemstack
     * @param player Player who receives the item
     * @param output Built output content
     * @return Whether the original item should be part of the output too
     */
    boolean onAssembly(ItemStack stack, PlayerEntity player, List<ItemStack> output);
}
