package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.client.render.item.Ump45Renderer;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.BulletEntity;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.config.gun.IWeaponConfig;
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

public class Ump45Item extends GunItem {

    public Ump45Item(String name) {
        super(name, GunType.SMG, new Properties().setISTER(() -> Ump45Renderer::new));
    }

    @Override
    public IWeaponConfig getWeaponConfig() {
        return ModConfig.weaponConfig.ump;
    }

    @Override
    public void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data) {
        data.put(AmmoMaterial.WOOD, 0);
        data.put(AmmoMaterial.STONE, 1);
        data.put(AmmoMaterial.IRON, 3);
        data.put(AmmoMaterial.GOLD, 4);
        data.put(AmmoMaterial.DIAMOND, 6);
        data.put(AmmoMaterial.EMERALD, 7);
        data.put(AmmoMaterial.AMETHYST, 9);
    }

    @Override
    public SoundEvent getShootSound(PlayerEntity entity) {
        return this.isSilenced(entity) ? ModSounds.UMP45_SILENT : ModSounds.UMP45;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.MP5;
    }

    @Override
    public SoundEvent getReloadSound(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.SMG_QUICKDRAW) ? ModSounds.SMG_RELOAD_SHORT : ModSounds.SMG_RELOAD;
    }

    @Override
    public int getMaxAmmo(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.SMG_EXTENDED) ? 40 : 25;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        IWeaponConfig cfg = getWeaponConfig();
        return PlayerData.hasActiveSkill(player, Skills.SMG_TOUGH_SPRING) ? cfg.getUpgradedFirerate() : cfg.getFirerate();
    }

    @Override
    public boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.SMG_SUPPRESSOR);
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        int time = PlayerData.hasActiveSkill(player, Skills.SMG_QUICKDRAW) ? 40 : 52;
        return (int) (time * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public void onKillEntity(BulletEntity bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (!shooter.level.isClientSide && shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.SMG_COMMANDO)) {
            shooter.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 100, 1, false, false));
            shooter.addEffect(new EffectInstance(Effects.REGENERATION, 60, 2, false, false));
        }
    }

    @Override
    public float getVerticalRecoil(PlayerEntity player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerData.hasActiveSkill(player, Skills.SMG_VERTICAL_GRIP) ? ModConfig.weaponConfig.general.verticalGrip.floatValue() : 1.0F;
        return mod * f;
    }

    @Override
    public boolean switchFiremode(ItemStack stack, PlayerEntity player) {
        Firemode mode = this.getFiremode(stack);
        stack.getTag().putInt("firemode", mode == Firemode.SINGLE ? 2 : 0);
        return true;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.SMG_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AimingAnimation createAimAnimation() {
        boolean rds = PlayerData.hasActiveSkill(Minecraft.getInstance().player, Skills.SMG_RED_DOT);
        return new AimingAnimation(-0.57F, rds ? 0.08F : 0.2F, 0.2F).animateRight((stack, f) -> {
            stack.translate(-0.25F * f, 0.2F * f, 0.2F * f);
            stack.mulPose(Vector3f.YP.rotationDegrees(20 * f));
        }).animateLeft((stack, f) -> {
            stack.translate(-0.32F * f, 0.2F * f, 0.35F * f);
            stack.mulPose(Vector3f.YP.rotationDegrees(10 * f));
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IAnimation createReloadAnimation(PlayerEntity player) {
        return new Animations.Ump45Reload(this.getReloadTime(player));
    }
}