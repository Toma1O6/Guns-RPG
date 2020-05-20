package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.common.item.GRPGItem;

public class ItemAmmo extends GRPGItem implements IAmmoProvider {

    private final AmmoType ammoType;

    public ItemAmmo(String name, AmmoType ammoType) {
        super(name);
        this.ammoType = ammoType;
    }

    @Override
    public AmmoType getAmmoType() {
        return ammoType;
    }
}
