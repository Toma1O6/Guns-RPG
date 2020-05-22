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

public class SGItem extends GunItem {

    public SGItem(String name) {
        super(name, GunType.SG);
    }

    @Override
    public WeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weapon.shotgun;
    }

    @Override
    public void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data) {
        data.put(AmmoMaterial.WOOD, 0);
        data.put(AmmoMaterial.STONE, 1);
        data.put(AmmoMaterial.IRON, 2);
        data.put(AmmoMaterial.GOLD, 3);
        data.put(AmmoMaterial.DIAMOND, 5);
        data.put(AmmoMaterial.EMERALD, 6);
        data.put(AmmoMaterial.AMETHYST, 8);
    }

    @Override
    public int getReloadTime(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.BULLET_LOOPS) ? 20 : 30;
    }

    @Override
    public int getMaxAmmo(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.SG_EXTENDED) ? 8 : 5;
    }

    @Override
    public int getFirerate(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.PUMP_IN_ACTION) ? 15 : 25;
    }

    @Override
    public void onKillEntity(EntityBullet bullet, EntityLivingBase victim, ItemStack stack, EntityLivingBase shooter) {
        if(!shooter.world.isRemote && shooter instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) shooter, Ability.NEVER_GIVE_UP)) {
            shooter.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 0, false, false));
        }
    }
}
