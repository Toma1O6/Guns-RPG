package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;

public class SRItem extends GunItem {

    public SRItem(String name) {
        super(name, GunType.SR);
    }

    @Override
    public WeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weapon.sr;
    }

    @Override
    public void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data) {
        data.put(AmmoMaterial.WOOD, 0);
        data.put(AmmoMaterial.STONE, 4);
        data.put(AmmoMaterial.IRON, 9);
        data.put(AmmoMaterial.GOLD, 13);
        data.put(AmmoMaterial.DIAMOND, 17);
        data.put(AmmoMaterial.EMERALD, 20);
        data.put(AmmoMaterial.AMETHYST, 25);
    }

    @Override
    public int getMaxAmmo(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.SR_EXTENDED) ? 10 : 5;
    }

    @Override
    public int getFirerate(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.FAST_HANDS) ? 30 : 17;
    }

    @Override
    public int getReloadTime(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.FAST_HANDS) ? 35 : 65;
    }

    @Override
    public boolean isSilenced(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.SR_SUPPRESSOR);
    }
}
