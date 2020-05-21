package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;

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
        
    }
}
