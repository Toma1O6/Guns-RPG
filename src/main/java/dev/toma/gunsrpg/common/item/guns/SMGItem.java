package dev.toma.gunsrpg.common.item.guns;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
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

public class SMGItem extends GunItem {

    public SMGItem(String name) {
        super(name, GunType.SMG);
    }

    @Override
    public IWeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weaponConfig.ump;
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
        return this.isSilenced(entity) ? GRPGSounds.UMP45_SILENT : GRPGSounds.UMP45;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return GRPGSounds.MP5;
    }

    @Override
    public SoundEvent getReloadSound(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.SMG_QUICKDRAW) ? GRPGSounds.SMG_RELOAD_SHORT : GRPGSounds.SMG_RELOAD;
    }

    @Override
    public int getMaxAmmo(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.SMG_EXTENDED) ? 40 : 25;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        IWeaponConfiguration cfg = getWeaponConfig();
        return PlayerDataFactory.hasActiveSkill(player, Skills.SMG_TOUGH_SPRING) ? cfg.getUpgradedFirerate() : cfg.getFirerate();
    }

    @Override
    public boolean isSilenced(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.SMG_SUPPRESSOR);
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        int time = PlayerDataFactory.hasActiveSkill(player, Skills.SMG_QUICKDRAW) ? 40 : 52;
        return (int) (time * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public void onKillEntity(EntityBullet bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if(!shooter.level.isClientSide && shooter instanceof PlayerEntity && PlayerDataFactory.hasActiveSkill((PlayerEntity) shooter, Skills.SMG_COMMANDO)) {
            shooter.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 100, 1, false, false));
            shooter.addEffect(new EffectInstance(Effects.REGENERATION, 60, 2, false, false));
        }
    }

    @Override
    public float getVerticalRecoil(PlayerEntity player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, Skills.SMG_VERTICAL_GRIP) ? GRPGConfig.weaponConfig.general.verticalGrip.floatValue() : 1.0F;
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
        boolean rds = PlayerDataFactory.hasActiveSkill(Minecraft.getInstance().player, Skills.SMG_RED_DOT);
        return new AimingAnimation(-0.57F, rds ? 0.144F : 0.2F, 0.2F).animateRight((stack, f) -> {
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
        return new MultiStepAnimation.Configurable(this.getReloadTime(player), "smg_reload");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformRightArm(MatrixStack matrix) {
        matrix.translate(0.15F, -0.3F, 0.25F);
        matrix.mulPose(Vector3f.XP.rotationDegrees(10));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformLeftArm(MatrixStack matrix) {
        matrix.translate(0.4F, -0.25F, -0.2F);
        matrix.mulPose(Vector3f.XP.rotationDegrees(10));
        matrix.mulPose(Vector3f.YP.rotationDegrees(-30));
    }
}
