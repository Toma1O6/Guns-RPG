package dev.toma.gunsrpg.util.object;

import dev.toma.gunsrpg.client.ClientEventHandler;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.capability.object.ReloadInfo;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.reload.IReloadManager;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetReloading;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ShootingManager {

    public static boolean canShoot(PlayerEntity player, ItemStack stack) {
        IPlayerData data = PlayerData.get(player).orElseThrow(NullPointerException::new);
        ReloadInfo reloadInfo = data.getReloadInfo();
        PlayerSkills skills = data.getSkills();
        GunItem item = (GunItem) stack.getItem();
        IReloadManager reloadManager = item.getReloadManager();
        if (!player.isSprinting() && ClientEventHandler.shootDelay == 0) {
            AmmoMaterial material = item.getMaterialFromNBT(stack);
            if (material == null) return false;
            if (reloadInfo.isReloading()) {
                if (reloadManager.canBeInterrupted(item, stack)) {
                    reloadInfo.cancelReload();
                    if (!player.level.isClientSide) {
                        data.sync();
                    } else {
                        NetworkManager.sendServerPacket(new SPacketSetReloading(false, 0));
                    }
                    return item.hasAmmo(stack) && skills.getGunData(item).getLevel() >= material.ordinal() + 1;
                }
                return false;
            }
            return item.hasAmmo(stack) && skills.getGunData(item).getLevel() >= material.ordinal() + 1;
        }
        return false;
    }

    @Nullable
    public static GunItem getGunFrom(PlayerEntity player) {
        return player.getMainHandItem().getItem() instanceof GunItem ? (GunItem) player.getMainHandItem().getItem() : null;
    }
}
