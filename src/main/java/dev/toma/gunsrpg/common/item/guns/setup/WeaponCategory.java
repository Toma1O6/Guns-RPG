package dev.toma.gunsrpg.common.item.guns.setup;

import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;

public enum WeaponCategory {

    PISTOL(AmmoType.AMMO_9MM),
    SMG(AmmoType.AMMO_45ACP),
    AR(AmmoType.AMMO_556MM),
    DMR(AmmoType.AMMO_762MM),
    SR(AmmoType.AMMO_762MM),
    SG(AmmoType.AMMO_12G),
    CROSSBOW(AmmoType.BOLT),
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
