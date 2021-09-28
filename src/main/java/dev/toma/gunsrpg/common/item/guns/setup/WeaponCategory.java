package dev.toma.gunsrpg.common.item.guns.setup;

import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;

public enum WeaponCategory {

    PISTOL(AmmoType._9MM),
    SMG(AmmoType._45ACP),
    AR(AmmoType._556MM),
    SR(AmmoType._762MM),
    SG(AmmoType._12G),
    CROSSBOW(AmmoType.CROSSBOW);

    private final AmmoType ammoType;

    WeaponCategory(AmmoType ammoType) {
        this.ammoType = ammoType;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }
}
