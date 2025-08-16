package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.animation.RecoilAnimation;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.IMaterialData;
import dev.toma.gunsrpg.common.item.guns.ammo.IMaterialDataContainer;
import dev.toma.gunsrpg.common.item.guns.util.IAdditionalShootData;
import dev.toma.gunsrpg.common.item.guns.util.ScopeDataRegistry;
import dev.toma.gunsrpg.config.gun.RecoilConfiguration;
import dev.toma.gunsrpg.config.gun.WeaponConfig;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_ShootPacket;
import dev.toma.gunsrpg.util.math.Vec2f;
import dev.toma.gunsrpg.util.object.OptionalObject;
import dev.toma.gunsrpg.util.object.ShootingManager;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.GameSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ClientShootingManager {

    private static final OptionalObject<Double> fov = OptionalObject.empty();
    private static final OptionalObject<Double> sensitivity = OptionalObject.empty();
    private static boolean burstActive;
    private static float weaponPositionZ;
    private static float weaponPositionOldZ;
    private static final Vec2f weaponRotation = new Vec2f();
    private static final Vec2f weaponRotationOld = new Vec2f();
    private static float recoilDecay = 0.75F;

    public static void update() {
        weaponPositionOldZ = weaponPositionZ;
        weaponRotationOld.copyFrom(weaponRotation);

        weaponPositionZ *= recoilDecay;
        weaponRotation.scale(recoilDecay);
    }

    public static float getActiveWeaponKickOffset(float renderDelta) {
        return AnimationUtils.linearInterpolate(weaponPositionZ, weaponPositionOldZ, renderDelta);
    }

    public static float getActiveWeaponPitchOffset(float renderDelta) {
        return weaponRotation.lerpX(weaponRotationOld.x, renderDelta);
    }

    public static float getActiveWeaponYawOffset(float renderDelta) {
        return weaponRotation.lerpY(weaponRotationOld.y, renderDelta);
    }

    private static void addWeaponRecoil(float x, float y, float maxX, float maxY) {
        float rx = MathHelper.clamp(weaponRotation.x + x, -maxX, maxX);
        float ry = MathHelper.clamp(weaponRotation.y + y, -maxY, maxY); // allow negative y too for some specific weapons
        weaponRotation.add(rx, ry);
    }

    public static void shoot(PlayerEntity player, ItemStack stack, IPlayerData dataProvider) {
        GunItem gun = (GunItem) stack.getItem();
        IAttributeProvider attributeProvider = dataProvider.getAttributes();
        AmmoType type = gun.getAmmoType();
        IAmmoMaterial material = gun.getMaterialFromNBT(stack);
        IMaterialDataContainer materialDataContainer = type.getContainer();
        WeaponConfig config = GunsRPG.config.weapon;
        float recoilModifier = config.globalWeaponRecoilMultiplier;
        if (materialDataContainer != null) {
            IMaterialData materialData = materialDataContainer.getMaterialData(material);
            recoilModifier += materialData.getAddedRecoil();
        }
        if (player.isCrouching()) {
            recoilModifier *= config.crouchWeaponRecoilMultiplier;
        }
        float xRot = gun.getVerticalRecoil(attributeProvider) * recoilModifier;
        float yRot = gun.getHorizontalRecoil(attributeProvider) * recoilModifier;
        boolean hasRecoil = xRot != 0.0F || yRot != 0.0F;
        float angleX = 0;
        float angleY = 0;
        Random random = player.getRandom();
        boolean invertDirection = random.nextBoolean();
        if (hasRecoil) {
            float mz = 0.1F * xRot * yRot;
            IWeaponConfig weaponConfig = gun.getWeaponConfig();
            RecoilConfiguration recoil = weaponConfig.recoil();
            angleX = weaponRotation.x;
            angleY = weaponRotation.y;
            weaponPositionZ = MathHelper.clamp(weaponPositionZ + mz, 0, recoil.maxKick);
            addWeaponRecoil(xRot, invertDirection ? -yRot : yRot, recoil.maxRecoilPitch, recoil.maxRecoilYaw);
            recoilDecay = recoil.recoilDecay;
            IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
            pipeline.insertIfMissing(ModAnimations.RECOIL, RecoilAnimation::new);
        }
        NetworkManager.sendServerPacket(new C2S_ShootPacket(context -> {
            if (gun instanceof IAdditionalShootData) {
                ((IAdditionalShootData) gun).addExtraData(context, player, stack, material);
            }
        }, angleX, angleY));
        WeaponConfig weaponConfig = GunsRPG.config.weapon;
        float cameraX = weaponConfig.globalCameraRecoilScale * xRot;
        float cameraY = weaponConfig.globalCameraRecoilScale * yRot;
        if (invertDirection)
            cameraY = -cameraY;
        player.xRot -= cameraX;
        player.yRot -= cameraY;
        gun.onShoot(player, stack);
        ShootingManager.setCooldown(gun.getFirerate(attributeProvider));
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

    public static void forceShootDelay(int delay) {
        ShootingManager.setCooldown(delay);
    }
}
