package dev.toma.gunsrpg.common.item.guns;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.config.gun.IWeaponConfiguration;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

public class SksItem extends GunItem {

    public SksItem(String name) {
        super(name, GunType.AR);
    }

    @Override
    public IWeaponConfiguration getWeaponConfig() {
        return ModConfig.weaponConfig.sks;
    }

    @Override
    public void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data) {
        data.put(AmmoMaterial.WOOD, 0);
        data.put(AmmoMaterial.STONE, 2);
        data.put(AmmoMaterial.IRON, 4);
        data.put(AmmoMaterial.GOLD, 6);
        data.put(AmmoMaterial.DIAMOND, 9);
        data.put(AmmoMaterial.EMERALD, 11);
        data.put(AmmoMaterial.AMETHYST, 14);
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return this.isSilenced(entity) ? ModSounds.SKS_SILENT : ModSounds.SKS;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.SLR;
    }

    @Override
    public SoundEvent getReloadSound(PlayerEntity player) {
        return ModSounds.AR_RELOAD;
    }

    @Override
    public boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.AR_SUPPRESSOR);
    }

    @Override
    public int getMaxAmmo(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.AR_EXTENDED) ? 20 : 10;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        IWeaponConfiguration cfg = getWeaponConfig();
        int firerate = PlayerData.hasActiveSkill(player, Skills.AR_TOUGH_SPRING) ? cfg.getUpgradedFirerate() : cfg.getFirerate();
        if (PlayerData.hasActiveSkill(player, Skills.AR_ADAPTIVE_CHAMBERING)) {
            firerate -= 2;
        }
        return Math.max(firerate, 1);
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        return (int) (32 * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public float getVerticalRecoil(PlayerEntity player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerData.hasActiveSkill(player, Skills.AR_VERTICAL_GRIP) ? ModConfig.weaponConfig.general.verticalGrip.floatValue() : 1.0F;
        float mod2 = PlayerData.hasActiveSkill(player, Skills.AR_CHEEKPAD) ? ModConfig.weaponConfig.general.cheekpad.floatValue() : 1.0F;
        return mod * mod2 * f;
    }

    @Override
    public float getHorizontalRecoil(PlayerEntity player) {
        float f = super.getHorizontalRecoil(player);
        float mod = PlayerData.hasActiveSkill(player, Skills.AR_CHEEKPAD) ? ModConfig.weaponConfig.general.cheekpad.floatValue() : 1.0F;
        return mod * f;
    }

    @Override
    public boolean switchFiremode(ItemStack stack, PlayerEntity player) {
        Firemode firemode = this.getFiremode(stack);
        int newMode = 0;
        if (firemode == Firemode.SINGLE && PlayerData.hasActiveSkill(player, Skills.AR_ADAPTIVE_CHAMBERING)) {
            newMode = 2;
        }
        stack.getTag().putInt("firemode", newMode);
        return firemode.ordinal() != newMode;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.ASSAULT_RIFLE_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformRightArm(MatrixStack matrix) {
        matrix.translate(-0.1F, -0.05F, 0.6F);
        matrix.mulPose(Vector3f.XP.rotationDegrees(5));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformLeftArm(MatrixStack matrix) {
        matrix.translate(0.32F, -0.1F, -0.1F);
        matrix.mulPose(Vector3f.XP.rotationDegrees(5.0F));
        matrix.mulPose(Vector3f.YP.rotationDegrees(-20.0F));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AimingAnimation createAimAnimation() {
        return new AimingAnimation(-0.265F, 0.07F, 0.2F).animateRight((stack, f) -> {
            stack.translate(-0.2F * f, 0.0F, 0.0F);
            stack.mulPose(Vector3f.YP.rotationDegrees(20 * f));
        }).animateLeft((stack, f) -> {
            stack.translate(-0.33F * f, 0.1F * f, 0.2F * f);
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IAnimation createReloadAnimation(PlayerEntity player) {
        return new Animations.ArReload(this.getReloadTime(player));
    }
}
