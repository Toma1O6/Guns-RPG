package dev.toma.gunsrpg.util.object;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IReloadInfo;
import dev.toma.gunsrpg.client.ClientEventHandler;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.util.MaterialContainer;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetReloading;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ShootingManager {

    public static boolean canShoot(PlayerEntity player, ItemStack stack) {
        IPlayerData data = PlayerData.get(player).orElseThrow(NullPointerException::new);
        IReloadInfo reloadInfo = data.getReloadInfo();
        PlayerSkills skills = data.getSkills();
        GunItem item = (GunItem) stack.getItem();
        if (!player.isSprinting() && ClientEventHandler.shootDelay == 0) {
            IAmmoMaterial material = item.getMaterialFromNBT(stack);
            if (material == null) return false;
            if (reloadInfo.isReloading()) {
                reloadInfo.enqueueCancel();
                if (!player.level.isClientSide) {
                    data.sync();
                } else {
                    NetworkManager.sendServerPacket(new SPacketSetReloading(false, 0));
                }
                return false;
            }
            MaterialContainer container = item.getContainer();
            int weaponLevel = skills.getGunData(item).getLevel();
            return item.hasAmmo(stack) && weaponLevel >= container.getRequiredLevel(material) + 1;
        }
        return false;
    }

    @Nullable
    public static GunItem getGunFrom(PlayerEntity player) {
        return player.getMainHandItem().getItem() instanceof GunItem ? (GunItem) player.getMainHandItem().getItem() : null;
    }
}
