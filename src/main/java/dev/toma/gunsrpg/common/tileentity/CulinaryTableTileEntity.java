package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.ModBlockEntities;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.resource.crafting.SkilledRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class CulinaryTableTileEntity extends SkilledWorkbenchTileEntity {

    protected CulinaryTableTileEntity(TileEntityType<? extends CulinaryTableTileEntity> type) {
        super(type);
    }

    public CulinaryTableTileEntity() {
        this(ModBlockEntities.CULINARY_TABLE.get());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends SkilledRecipe<?>> IRecipeType<R> getRecipeType() {
        return (IRecipeType<R>) ModRecipeTypes.CULINARY_RECIPE_TYPE;
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(9);
    }
}
