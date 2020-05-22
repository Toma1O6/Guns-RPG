package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

import java.util.Map;

public class PistolItem extends GunItem {

    public PistolItem(String name) {
        super(name, GunType.PISTOL);
    }

    @Override
    public WeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weapon.pistol;
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
    public boolean isSilenced(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.PISTOL_SUPPRESSOR);
    }

    @Override
    public void onHitEntity(EntityBullet bullet, EntityLivingBase victim, ItemStack stack, EntityLivingBase shooter) {
        if(shooter instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) shooter, Ability.PISTOL_HEAVY_BULLETS) && random.nextDouble() <= 0.35) {
            victim.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 1, false, false));
            victim.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100, 0, false, false));
        }
    }

    @Override
    public int getMaxAmmo(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.PISTOL_EXTENDED) ? 13 : 7;
    }

    @Override
    public int getFirerate(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.PISTOL_TOUGH_SPRING) ? 1 : 2;
    }

    @Override
    public int getReloadTime(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.PISTOL_QUICKDRAW) ? 35 : 60;
    }
}
