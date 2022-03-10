package dev.toma.gunsrpg.resource.cooking;

import net.minecraft.item.ItemStack;

public interface IBurningRecipe {

    ItemStack[] getInputStacks();

    float getExperience();

    int getCookTime();
}
