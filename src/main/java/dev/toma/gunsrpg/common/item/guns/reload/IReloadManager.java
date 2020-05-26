package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetReloading;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IReloadManager {

    default void startReloading(EntityPlayer player, int time, ItemStack stack) {
        if(player.world.isRemote) {
            NetworkManager.toServer(new SPacketSetReloading(true, time));
        } else {
            PlayerData data = PlayerDataFactory.get(player);
            data.getReloadInfo().startReloading(player.inventory.currentItem, time);
        }
    }

    boolean finishReload(EntityPlayer player, GunItem item, ItemStack stack);
}
