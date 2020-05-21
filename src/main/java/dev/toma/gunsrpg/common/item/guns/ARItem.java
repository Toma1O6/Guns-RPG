package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;

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

    }
}
