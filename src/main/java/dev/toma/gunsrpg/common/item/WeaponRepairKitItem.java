package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;

public class WeaponRepairKitItem extends BaseItem {

    public WeaponRepairKitItem(String name) {
        super(name, new Properties().stacksTo(1).tab(ModTabs.ITEM_TAB));
    }
}
