package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.resource.crafting.SkilledRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface ISkilledCrafting extends ISynchronizable {

    void attachCallback(IGridChanged gridChanged);

    void detachCallback();

    <I extends SkilledRecipe<?>> IRecipeType<I> getRecipeType();
}
