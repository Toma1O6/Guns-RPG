package dev.toma.gunsrpg.config.client;

import net.minecraft.item.ItemStack;

public interface IHeldLayerSettings {

    Mode getRenderingMode();

    ItemStack getRenderItem();

    enum Mode {
        DEFAULT,
        STATIC,
        NONE
    }
}
