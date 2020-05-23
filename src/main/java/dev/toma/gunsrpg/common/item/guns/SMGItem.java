package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.EntityBullet;
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
    public int getMaxAmmo(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.SMG_EXTENDED) ? 30 : 20;
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
}
