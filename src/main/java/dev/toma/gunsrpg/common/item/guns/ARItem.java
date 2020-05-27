package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundEvent;

import java.util.Map;

public class ARItem extends GunItem {

    public ARItem(String name) {
        super(name, GunType.AR);
    }

    @Override
    public WeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weapon.ar;
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
    public SoundEvent getShootSound(EntityLivingBase entity) {
        return entity instanceof EntityPlayer && this.isSilenced((EntityPlayer) entity) ? ModRegistry.GRPGSounds.SKS_SILENT : ModRegistry.GRPGSounds.SKS;
    }

    @Override
    public boolean isSilenced(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.AR_SUPPRESSOR);
    }

    @Override
    public int getMaxAmmo(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.AR_EXTENDED) ? 20 : 10;
    }

    @Override
    public int getFirerate(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.AR_TOUGH_SPRING) ? 3 : 4;
    }

    @Override
    public float getVerticalRecoil(EntityPlayer player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, Ability.AR_VERTICAL_GRIP) ? GRPGConfig.weapon.general.verticalGrip : 1.0F;
        float mod2 = PlayerDataFactory.hasActiveSkill(player, Ability.AR_CHEEKPAD) ? GRPGConfig.weapon.general.cheekpad : 1.0F;
        return mod * mod2 * f;
    }

    @Override
    public float getHorizontalRecoil(EntityPlayer player) {
        float f = super.getHorizontalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, Ability.AR_CHEEKPAD) ? GRPGConfig.weapon.general.cheekpad : 1.0F;
        return mod * f;
    }

    @Override
    public void renderRightArm() {
        GlStateManager.translate(-0.1F, -0.05F, 0.6F);
        GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F);
        renderArm(EnumHandSide.RIGHT);
    }

    @Override
    public void renderLeftArm() {
        GlStateManager.translate(0.32F, -0.1F, -0.1F);
        GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 1.0F, 0.0F);
        renderArm(EnumHandSide.LEFT);
    }

    @Override
    public AimingAnimation createAimAnimation() {
        return new AimingAnimation(-0.265F, 0.07F, 0.2F).animateRight(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.2F * f, 0.0F, 0.0F);
            GlStateManager.rotate(20.0F * f, 0.0F, 1.0F, 0.0F);
        }).animateLeft(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.33F * f, 0.1F * f, 0.2F * f);
        });
    }

    @Override
    public Firemode getFiremode(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.ADAPTIVE_CHAMBERING) ? Firemode.FULL_AUTO : Firemode.SINGLE;
    }
}
