package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IReloadInfo {

    void tick(PlayerEntity player);

    void startReloading(PlayerEntity player, GunItem item, ItemStack stack, int slot);

    void enqueueCancel();

    boolean isReloading();
}
