package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.client.animation.AimingAnimation;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundEvent;

import java.util.Map;

public class SMGItem extends GunItem {

    public SMGItem(String name) {
        super(name, GunType.SMG);
    }

    @Override
    public WeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weapon.smg;
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
    public SoundEvent getShootSound(EntityLivingBase entity) {
        return entity instanceof EntityPlayer && this.isSilenced((EntityPlayer) entity) ? ModRegistry.GRPGSounds.UMP45_SILENT : ModRegistry.GRPGSounds.UMP45;
    }

    @Override
    public int getMaxAmmo(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.SMG_EXTENDED) ? 40 : 25;
    }

    @Override
    public int getFirerate(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.SMG_TOUGH_SPRING) ? 2 : 3;
    }

    @Override
    public boolean isSilenced(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.SMG_SUPPRESSOR);
    }

    @Override
    public int getReloadTime(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.SMG_QUICKDRAW) ? 30 : 44;
    }

    @Override
    public void onKillEntity(EntityBullet bullet, EntityLivingBase victim, ItemStack stack, EntityLivingBase shooter) {
        if(!shooter.world.isRemote && shooter instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) shooter, Ability.COMMANDO)) {
            shooter.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 0, false, false));
            shooter.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 60, 2, false, false));
        }
    }

    @Override
    public float getVerticalRecoil(EntityPlayer player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, Ability.SMG_VERTICAL_GRIP) ? GRPGConfig.weapon.general.verticalGrip : 1.0F;
        return mod * f;
    }

    @Override
    public AimingAnimation createAimAnimation() {
        return new AimingAnimation(-0.57F, 0.2F, 0.2F).animateRight(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.25F * f, 0.2F * f, 0.2F * f);
            GlStateManager.rotate(20.0F * f, 0.0F, 1.0F, 0.0F);
        }).animateLeft(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.32F * f, 0.2F * f, 0.35F * f);
            GlStateManager.rotate(10.0F * f, 0.0F, 1.0F, 0.0F);
        });
    }

    @Override
    public void renderRightArm() {
        GlStateManager.translate(0.15F, -0.3F, 0.25F);
        GlStateManager.rotate(10.0F, 1.0F, 0.0F, 0.0F);
        renderArm(EnumHandSide.RIGHT);
    }

    @Override
    public void renderLeftArm() {
        GlStateManager.translate(0.4F, -0.25F, -0.2F);
        GlStateManager.rotate(10.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
        renderArm(EnumHandSide.LEFT);
    }

    @Override
    public Firemode getFiremode(EntityPlayer player) {
        return Firemode.FULL_AUTO;
    }
}
