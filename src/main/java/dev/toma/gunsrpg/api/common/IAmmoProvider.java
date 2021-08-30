package dev.toma.gunsrpg.api.common;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;

public interface IAmmoProvider {

    AmmoType getAmmoType();

    IAmmoMaterial getMaterial();
}
