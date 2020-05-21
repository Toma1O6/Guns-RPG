package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;

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

    }
}
