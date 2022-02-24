package dev.toma.gunsrpg.util.object;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.data.IHandState;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IProgressData;
import dev.toma.gunsrpg.api.common.data.IReloadInfo;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.animation.RecoilAnimation;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.IMaterialData;
import dev.toma.gunsrpg.common.item.guns.ammo.IMaterialDataContainer;
import dev.toma.gunsrpg.common.item.guns.setup.MaterialContainer;
import dev.toma.gunsrpg.common.item.guns.util.ScopeDataRegistry;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_SetReloadingPacket;
import dev.toma.gunsrpg.network.packet.C2S_ShootPacket;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.GameSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class ShootingManager {

    private static int shootingDelay;

    public static boolean canShoot(PlayerEntity player, ItemStack stack) {
        IPlayerData data = PlayerData.get(player).orElseThrow(NullPointerException::new);
        IReloadInfo reloadInfo = data.getReloadInfo();
        IProgressData levelData = data.getProgressData();
        GunItem item = (GunItem) stack.getItem();
        if (!player.isSprinting() && isShootingReady()) {
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

    @OnlyIn(Dist.CLIENT)
    public static class Client {

        private static final OptionalObject<Double> fov = OptionalObject.empty();
        private static final OptionalObject<Double> sensitivity = OptionalObject.empty();
        private static boolean burstActive;

        public static void shoot(PlayerEntity player, ItemStack stack, IPlayerData dataProvider) {
            GunItem gun = (GunItem) stack.getItem();
            IAttributeProvider attributeProvider = dataProvider.getAttributes();
            AmmoType type = gun.getAmmoType();
            IAmmoMaterial material = gun.getMaterialFromNBT(stack);
            IMaterialDataContainer materialDataContainer = type.getContainer();
            float recoilModifier = 1.0F;
            if (materialDataContainer != null) {
                IMaterialData materialData = materialDataContainer.getMaterialData(material);
                recoilModifier += materialData.getAddedRecoil();
            }
            float xRot = gun.getVerticalRecoil(attributeProvider) * recoilModifier;
            float yRot = gun.getHorizontalRecoil(attributeProvider) * recoilModifier;
            if (player.getRandom().nextBoolean())
                yRot = -yRot;
            player.xRot -= xRot;
            player.yRot -= yRot;
            NetworkManager.sendServerPacket(new C2S_ShootPacket());
            gun.onShoot(player, stack);
            shootingDelay = gun.getFirerate(attributeProvider);
            IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
            pipeline.insert(ModAnimations.RECOIL, new RecoilAnimation(xRot, yRot, gun, stack, dataProvider));
        }

        public static void saveSettings(GameSettings settings) {
            fov.map(settings.fov);
            sensitivity.map(settings.sensitivity);
        }

        public static void loadSettings(GameSettings settings) {
            fov.ifPresent(value -> settings.fov = value);
            sensitivity.ifPresent(value -> settings.sensitivity = value);
        }

        public static void applySettings(GameSettings settings, ScopeDataRegistry.Entry entry) {
            settings.fov = entry.getFov();
            settings.sensitivity = sensitivity.get() * entry.getSensitivityMultiplier();
        }

        public static void setBurstActive(boolean state) {
            burstActive = state;
        }

        public static boolean isBurstModeActive() {
            return burstActive;
        }
    }
}
