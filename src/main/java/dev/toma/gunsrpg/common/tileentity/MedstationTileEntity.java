package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.ModBlockEntities;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.resource.crafting.SkilledRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class MedstationTileEntity extends SkilledWorkbenchTileEntity {

    protected MedstationTileEntity(TileEntityType<? extends SkilledWorkbenchTileEntity> type) {
        super(type);
    }

    public MedstationTileEntity() {
        this(ModBlockEntities.MEDSTATION.get());
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(9);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends SkilledRecipe<?>> IRecipeType<I> getRecipeType() {
        return (IRecipeType<I>) ModRecipeTypes.MED_RECIPE_TYPE;
    }
}
