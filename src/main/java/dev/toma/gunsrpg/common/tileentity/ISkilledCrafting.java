package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.entity.ISynchronizable;
import net.minecraft.item.crafting.IRecipeType;

public interface ISkilledCrafting extends ISynchronizable {

    void attachCallback(IGridChanged gridChanged);

    void detachCallback();

    IRecipeType<?> getRecipeType();
}
