package dev.toma.gunsrpg.common.item.guns.ammo;

public interface IAmmoProvider {

    AmmoType getAmmoType();

    IAmmoMaterial getMaterial();
}
