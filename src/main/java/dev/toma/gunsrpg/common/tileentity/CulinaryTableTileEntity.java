package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.ModBlockEntities;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.S2C_SynchBlockEntityPacket;
import dev.toma.gunsrpg.resource.crafting.CulinaryRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

public class CulinaryTableTileEntity extends VanillaInventoryTileEntity implements ISkilledCrafting {

    private IGridChanged gridChanged;

    protected CulinaryTableTileEntity(TileEntityType<? extends CulinaryTableTileEntity> type) {
        super(type);
    }

    public CulinaryTableTileEntity() {
        this(ModBlockEntities.CULINARY_TABLE.get());
    }

    @Override
    public IRecipeType<?> getRecipeType() {
        return ModRecipeTypes.CULINARY_RECIPE_TYPE;
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(9);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        onSynch();
    }

    @Override
    public void attachCallback(IGridChanged gridChanged) {
        this.gridChanged = gridChanged;
    }

    @Override
    public void detachCallback() {
        attachCallback(null);
    }

    @Override
    public void onSynch() {
        if (level.isClientSide) {
            if (gridChanged != null) {
                RecipeManager manager = level.getRecipeManager();
                Optional<CulinaryRecipe> optional = manager.getRecipeFor(ModRecipeTypes.CULINARY_RECIPE_TYPE, this, level);
                gridChanged.onChange(optional.orElse(null));
            }
        } else {
            NetworkManager.sendWorldPacket(level, new S2C_SynchBlockEntityPacket(worldPosition));
        }
    }
}
