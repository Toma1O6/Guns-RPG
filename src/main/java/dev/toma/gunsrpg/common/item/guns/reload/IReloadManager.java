package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetReloading;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IReloadManager {

    default void startReloading(PlayerEntity player, int time, ItemStack stack) {
        if(player.level.isClientSide) {
            NetworkManager.sendServerPacket(new SPacketSetReloading(true, time));
        } else {
            PlayerData data = PlayerDataFactory.get(player);
            data.getReloadInfo().startReloading(player.inventory.selected, time);
        }
    }

    void finishReload(PlayerEntity player, GunItem item, ItemStack stack);

    boolean canBeInterrupted(GunItem gun, ItemStack stack);
}
