package dev.toma.gunsrpg.common.quests.reward;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IAssemblyFunction {

    void onAssembly(ItemStack stack, PlayerEntity player);
}
