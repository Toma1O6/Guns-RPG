package dev.toma.gunsrpg.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ICustomUseDuration {

    int getUseDuration(int defaultDuration, ItemStack stack, PlayerEntity player);
}
