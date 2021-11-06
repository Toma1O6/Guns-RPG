package dev.toma.gunsrpg.common.item.guns.setup;

import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;

public enum WeaponCategory {

    PISTOL(AmmoType._9MM),
    SMG(AmmoType._45ACP),
    AR(AmmoType._556MM),
    SR(AmmoType._762MM),
    SG(AmmoType._12G),
    CROSSBOW(AmmoType.CROSSBOW),
    GRENADE_LAUNCHER(AmmoType.GRENADE),
    ROCKET_LAUNCHER(AmmoType.ROCKET);

    private final AmmoType defaultAmmoType;

    WeaponCategory(AmmoType ammoType) {
        this.defaultAmmoType = ammoType;
    }

    public AmmoType getDefaultAmmoType() {
        return defaultAmmoType;
    }
}
