package dev.toma.gunsrpg.common.item.util;

import dev.toma.gunsrpg.common.item.GRPGItem;

public class ItemAmmoBag extends GRPGItem {

    public ItemAmmoBag(String name) {
        super(name);
        this.setMaxStackSize(1);
    }
}
