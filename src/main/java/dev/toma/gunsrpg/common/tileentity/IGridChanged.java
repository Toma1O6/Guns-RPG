package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.resource.crafting.SkilledRecipe;

public interface IGridChanged {
    void onChange(SkilledRecipe<?> recipe);
}
