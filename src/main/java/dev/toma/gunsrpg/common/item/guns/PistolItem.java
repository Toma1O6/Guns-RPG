package dev.toma.gunsrpg.common.item.guns;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.AnimationProcessor;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.client.animation.impl.ImprovedAimAnimation;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.init.GRPGSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.IWeaponConfiguration;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

public class PistolItem extends GunItem {

    public PistolItem(String name) {
        super(name, GunType.PISTOL);
    }

    @Override
    public IWeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weaponConfig.m1911;
    }

    @Override
    public void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data) {
        data.put(AmmoMaterial.WOOD, 0);
        data.put(AmmoMaterial.STONE, 1);
        data.put(AmmoMaterial.IRON, 3);
        data.put(AmmoMaterial.GOLD, 4);
        data.put(AmmoMaterial.DIAMOND, 6);
        data.put(AmmoMaterial.EMERALD, 8);
        data.put(AmmoMaterial.AMETHYST, 11);
    }

    @Override
    public boolean isSilenced(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_SUPPRESSOR);
    }

    @Override
    public void onHitEntity(EntityBullet bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (shooter instanceof PlayerEntity && PlayerDataFactory.hasActiveSkill((PlayerEntity) shooter, Skills.PISTOL_HEAVY_BULLETS) && random.nextDouble() <= 0.35) {
            victim.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 1, false, false));
            victim.addEffect(new EffectInstance(Effects.WEAKNESS, 100, 0, false, false));
        }
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return this.isSilenced(entity) ? GRPGSounds.P1911_SILENT : GRPGSounds.P1911;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return GRPGSounds.P92;
    }

    @Override
    public SoundEvent getReloadSound(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_QUICKDRAW) ? GRPGSounds.P1911_RELOAD_SHORT : GRPGSounds.P1911_RELOAD;
    }

    @Override
    public int getMaxAmmo(PlayerEntity player) {
        boolean extended = PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_EXTENDED);
        return PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_DUAL_WIELD) ? extended ? 26 : 14 : extended ? 13 : 7;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        IWeaponConfiguration config = getWeaponConfig();
        return PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_TOUGH_SPRING) ? config.getUpgradedFirerate() : config.getFirerate();
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        boolean quickdraw = PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_QUICKDRAW);
        int time = PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_DUAL_WIELD) ? quickdraw ? 50 : 70 : quickdraw ? 25 : 35;
        return (int) (time * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public float getVerticalRecoil(PlayerEntity player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_CARBON_BARREL) ? GRPGConfig.weaponConfig.general.carbonBarrel.floatValue() : 1.0F;
        return mod * f;
    }

    @Override
    public float getHorizontalRecoil(PlayerEntity player) {
        float f = super.getHorizontalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_CARBON_BARREL) ? GRPGConfig.weaponConfig.general.carbonBarrel.floatValue() : 1.0F;
        return mod * f;
    }

    @Override
    public boolean switchFiremode(ItemStack stack, PlayerEntity player) {
        Firemode firemode = this.getFiremode(stack);
        stack.getTag().putInt("firemode", firemode == Firemode.SINGLE ? 1 : 0);
        return true;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.PISTOL_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AimingAnimation createAimAnimation() {
        AnimationProcessor processor = ClientSideManager.instance().processor();
        return this.isDualWieldActive() ? new ImprovedAimAnimation(-0.4F, 0.06F, 0.0F).animateItem((stack, f) -> {
            if (processor.isRenderingDualWield()) {
                stack.translate(0.0F, -0.33F * f, 0.0F);
                stack.mulPose(Vector3f.ZP.rotationDegrees(-30f * f));
            } else {
                stack.translate(0.0F, -0.33F * f, 0.0F);
                stack.mulPose(Vector3f.ZP.rotationDegrees(30 * f));
            }
        }).animateRight((stack, f) -> {
            stack.translate(-0.13F * f, -0.12F * f, 0.1F * f);
            stack.mulPose(Vector3f.ZP.rotationDegrees(30 * f));
            stack.mulPose(Vector3f.YP.rotationDegrees(16 * f));
        }).animateLeft((stack, f) -> {
            stack.translate(0.13F * f, -0.12F * f, 0.1F * f);
            stack.mulPose(Vector3f.ZP.rotationDegrees(-30 * f));
            stack.mulPose(Vector3f.YP.rotationDegrees(-16 * f));
        }) : new AimingAnimation(-0.54F, 0.06F, 0.0F).animateRight((stack, f) -> {
            stack.translate(-0.18F * f, 0.0F, 0.1F * f);
            stack.mulPose(Vector3f.XP.rotationDegrees(3 * f));
            stack.mulPose(Vector3f.YP.rotationDegrees(20 * f));
        }).animateLeft((stack, f) -> {
            stack.translate(-0.2F * f, 0.0F, 0.0F);
            stack.mulPose(Vector3f.XP.rotationDegrees(3 * f));
            stack.mulPose(Vector3f.YP.rotationDegrees(15 * f));
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IAnimation createReloadAnimation(PlayerEntity player) {
        return this.isDualWieldActive() ? new Animations.ReloadDual(this.getReloadTime(player)) : new MultiStepAnimation.Configurable(this.getReloadTime(player), "pistol_reload");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformRightArm(MatrixStack matrix) {
        matrix.translate(-0.05F, -0.02F, 0.0F);
        matrix.mulPose(Vector3f.XP.rotationDegrees(5));
        matrix.mulPose(Vector3f.YP.rotationDegrees(-10));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformLeftArm(MatrixStack matrix) {
        if (this.isDualWieldActive()) {
            matrix.translate(-0.05F, -0.05F, -0.15F);
            matrix.mulPose(Vector3f.XP.rotationDegrees(5.0f));
            matrix.mulPose(Vector3f.YP.rotationDegrees(10));
        } else {
            matrix.translate(0.35F, -0.08F, 0.05F);
            matrix.mulPose(Vector3f.XP.rotationDegrees(5));
            matrix.mulPose(Vector3f.YP.rotationDegrees(-30));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private boolean isDualWieldActive() {
        return PlayerDataFactory.hasActiveSkill(Minecraft.getInstance().player, Skills.PISTOL_DUAL_WIELD);
    }
}
