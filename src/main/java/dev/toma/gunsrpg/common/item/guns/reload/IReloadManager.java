package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IReloadManager {

    void startReloading(EntityPlayer player, GunItem item, ItemStack stack);

    void finishReload(EntityPlayer player, GunItem item, ItemStack stack);
}
