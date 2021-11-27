package dev.toma.gunsrpg.util.object;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.client.ClientEventHandler;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.animation.RecoilAnimation;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.setup.MaterialContainer;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetReloading;
import dev.toma.gunsrpg.network.packet.SPacketShoot;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class ShootingManager {

    public static boolean canShoot(PlayerEntity player, ItemStack stack) {
        IPlayerData data = PlayerData.get(player).orElseThrow(NullPointerException::new);
        IReloadInfo reloadInfo = data.getReloadInfo();
        IData levelData = data.getGenericData();
        GunItem item = (GunItem) stack.getItem();
        if (!player.isSprinting() && ClientEventHandler.shootDelay == 0) {
            IAmmoMaterial material = item.getMaterialFromNBT(stack);
            if (material == null) return false;
            if (reloadInfo.isReloading()) {
                reloadInfo.enqueueCancel();
                if (player.level.isClientSide) {
                    NetworkManager.sendServerPacket(new SPacketSetReloading(false, 0));
                }
                return false;
            }
            MaterialContainer container = item.getContainer();
            int weaponLevel = levelData.getWeaponStats(item).getLevel();
            return item.hasAmmo(stack) && weaponLevel >= container.getRequiredLevel(material) + 1;
        }
        return false;
    }

    @Nullable
    public static GunItem getGunFrom(PlayerEntity player) {
        return player.getMainHandItem().getItem() instanceof GunItem ? (GunItem) player.getMainHandItem().getItem() : null;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Client {

        private static int shootingDelay;

        public static void tickShootingDelay() {
            if (shootingDelay > 0) --shootingDelay;
        }

        public static void shoot(PlayerEntity player, ItemStack stack, IAttributeProvider provider) {
            GunItem gun = (GunItem) stack.getItem();
            float xRot = gun.getVerticalRecoil(provider);
            float yRot = gun.getHorizontalRecoil(provider);
            if (player.getRandom().nextBoolean())
                yRot = -yRot;
            player.xRot -= xRot;
            player.yRot -= yRot;
            NetworkManager.sendServerPacket(new SPacketShoot());
            gun.onShoot(player, stack);
            shootingDelay = gun.getFirerate(provider);
            float recoilAnimationShakeScale = ModConfig.clientConfig.recoilAnimationScale.floatValue();

            IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
            pipeline.insert(ModAnimations.RECOIL, new RecoilAnimation(xRot, yRot, recoilAnimationShakeScale));
        }
    }
}
