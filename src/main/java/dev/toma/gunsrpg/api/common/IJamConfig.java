package dev.toma.gunsrpg.api.common;

import lib.toma.animations.Easings;
import net.minecraft.item.ItemStack;

public interface IJamConfig {

    Easings DEFAULT_EASING = Easings.EASE_IN_CUBIC;

    float getJamChance(float breakProgress);

    float getJamChance(ItemStack stack);
}
