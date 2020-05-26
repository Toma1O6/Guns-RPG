package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.common.skilltree.Ability;

public interface IAmmoProvider {

    AmmoType getAmmoType();

    AmmoMaterial getMaterial();

    Ability.Type getRequiredProperty();
}
