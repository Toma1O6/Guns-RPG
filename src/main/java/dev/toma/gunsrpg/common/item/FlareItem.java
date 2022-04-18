package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;

public class FlareItem extends BaseItem {

    public FlareItem(String name) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
    }
}
