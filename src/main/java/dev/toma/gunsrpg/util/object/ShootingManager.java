package dev.toma.gunsrpg.util.object;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.setup.MaterialContainer;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_SetReloadingPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ShootingManager {

    private static int shootingDelay;

    public static boolean canShoot(PlayerEntity player, ItemStack stack) {
        IPlayerData data = PlayerData.get(player).orElseThrow(NullPointerException::new);
        IReloadInfo reloadInfo = data.getReloadInfo();
        IProgressData levelData = data.getProgressData();
        GunItem item = (GunItem) stack.getItem();
        if (!player.isSprinting() && isShootingReady()) {
            ISkillProvider provider = data.getSkillProvider();
            if (!provider.hasSkill(item.getRequiredSkill())) {
                return false;
            }
            IAmmoMaterial material = item.getMaterialFromNBT(stack);
            if (material == null) return false;
            if (reloadInfo.isReloading()) {
                reloadInfo.enqueueCancel();
                if (player.level.isClientSide) {
                    NetworkManager.sendServerPacket(new C2S_SetReloadingPacket(false, 0));
                }
                return false;
            }
            if (data.getHandState().areHandsBusy()) {
                return false;
            }
            MaterialContainer container = item.getContainer();
            int weaponLevel = levelData.getWeaponStats(item).getLevel();
            IHandState state = data.getHandState();
            return item.hasAmmo(stack) && weaponLevel >= container.getRequiredLevel(material) && stack.getDamageValue() < stack.getMaxDamage() && !item.isJammed(stack) && !state.areHandsBusy();
        }
        return false;
    }

    public static void setCooldown(int ticks) {
        shootingDelay = ticks;
    }

    @Nullable
    public static GunItem getGunFrom(PlayerEntity player) {
        return player.getMainHandItem().getItem() instanceof GunItem ? (GunItem) player.getMainHandItem().getItem() : null;
    }

    public static void tickShootingDelay() {
        if (shootingDelay > 0) --shootingDelay;
    }

    public static boolean isShootingReady() {
        return shootingDelay <= 0;
    }

}
