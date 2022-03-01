package dev.toma.gunsrpg.common.item.guns.setup;

import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;

public enum WeaponCategory {

    PISTOL(AmmoType.AMMO_9MM, Attribs.PISTOL_DAMAGE),
    SMG(AmmoType.AMMO_45ACP, Attribs.SMG_DAMAGE),
    AR(AmmoType.AMMO_556MM, Attribs.AR_DAMAGE),
    DMR(AmmoType.AMMO_762MM, Attribs.DMR_DAMAGE),
    SR(AmmoType.AMMO_762MM, Attribs.SR_DAMAGE),
    SG(AmmoType.AMMO_12G, Attribs.SHOTGUN_DAMAGE),
    CROSSBOW(AmmoType.BOLT, Attribs.BOW_DAMAGE),
    GRENADE_LAUNCHER(AmmoType.GRENADE),
    ROCKET_LAUNCHER(AmmoType.ROCKET);

    private final AmmoType defaultAmmoType;
    private final IAttributeId bonusDamageAttribute;

    WeaponCategory(AmmoType ammoType) {
        this(ammoType, null);
    }

    WeaponCategory(AmmoType ammoType, IAttributeId id) {
        this.defaultAmmoType = ammoType;
        this.bonusDamageAttribute = id;
    }

    public AmmoType getDefaultAmmoType() {
        return defaultAmmoType;
    }

    public IAttributeId getBonusDamageAttribute() {
        return bonusDamageAttribute;
    }

    public boolean hasBonusDamage() {
        return bonusDamageAttribute != null;
    }
}
