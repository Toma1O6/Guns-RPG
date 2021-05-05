package dev.toma.gunsrpg.util.object;

import dev.toma.gunsrpg.client.ClientEventHandler;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.capability.object.ReloadInfo;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.reload.IReloadManager;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetReloading;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ShootingManager {

    public static boolean canShoot(EntityPlayer player, ItemStack stack) {
        PlayerData data = PlayerDataFactory.get(player);
        ReloadInfo reloadInfo = data.getReloadInfo();
        PlayerSkills skills = data.getSkills();
        GunItem item = (GunItem) stack.getItem();
        IReloadManager reloadManager = item.getReloadManager();
        if(!player.isSprinting() && ClientEventHandler.shootDelay == 0) {
            AmmoType ammoType = item.getAmmoType();
            AmmoMaterial material = item.getMaterialFromNBT(stack);
            if(material == null) return false;
            if(reloadInfo.isReloading()) {
                if(reloadManager.canBeInterrupted(item, stack)) {
                    reloadInfo.cancelReload();
                    if(!player.world.isRemote) {
                        data.sync();
                    } else {
                        NetworkManager.toServer(new SPacketSetReloading(false, 0));
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
    public static GunItem getGunFrom(EntityPlayer player) {
        return player.getHeldItemMainhand().getItem() instanceof GunItem ? (GunItem) player.getHeldItemMainhand().getItem() : null;
    }
}
