package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.item.BaseItem;

public class AmmoItem extends BaseItem implements IAmmoProvider {

    private final AmmoType ammoType;
    private final AmmoMaterial material;

    public AmmoItem(String name, AmmoType ammoType, AmmoMaterial material) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
        this.material = material;
        this.ammoType = ammoType;
    }

    @Override
    public AmmoType getAmmoType() {
        return ammoType;
    }

    @Override
    public AmmoMaterial getMaterial() {
        return material;
    }
}
