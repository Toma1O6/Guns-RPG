package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.common.item.BaseItem;

public class AmmoItem extends BaseItem implements IAmmoProvider {

    private final AmmoType ammoType;
    private final IAmmoMaterial material;

    public AmmoItem(String name, AmmoType ammoType, IAmmoMaterial material) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
        this.material = material;
        this.ammoType = ammoType;
    }

    @Override
    public AmmoType getAmmoType() {
        return ammoType;
    }

    @Override
    public IAmmoMaterial getMaterial() {
        return material;
    }
}
