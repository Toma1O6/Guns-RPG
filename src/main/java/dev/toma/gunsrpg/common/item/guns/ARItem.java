package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import net.minecraft.entity.player.EntityPlayer;

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
    public boolean isSilenced(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.AR_SUPPRESSOR);
    }

    @Override
    public int getMaxAmmo(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.AR_EXTENDED) ? 20 : 10;
    }

    @Override
    public int getFirerate(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.AR_TOUGH_SPRING) ? 2 : 3;
    }
}
