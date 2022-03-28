package dev.toma.gunsrpg.config.client;

import net.minecraft.item.ItemStack;

public interface IHeldLayerConfig {

    Mode getRenderingMode();

    ItemStack getRenderItem();

    enum Mode {
        DEFAULT,
        STATIC,
        NONE
    }
}
