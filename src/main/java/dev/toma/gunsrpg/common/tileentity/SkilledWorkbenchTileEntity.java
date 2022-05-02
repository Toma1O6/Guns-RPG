package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.S2C_SynchBlockEntityPacket;
import dev.toma.gunsrpg.resource.crafting.SkilledRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tileentity.TileEntityType;

import java.util.Optional;

public abstract class SkilledWorkbenchTileEntity extends VanillaInventoryTileEntity implements ISkilledCrafting {

    private IGridChanged gridChanged;

    protected SkilledWorkbenchTileEntity(TileEntityType<? extends SkilledWorkbenchTileEntity> type) {
        super(type);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            onSynch();
        }
    }

    @Override
    public void attachCallback(IGridChanged gridChanged) {
        this.gridChanged = gridChanged;
        onSynch();
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
                updateGridListener(manager);
            }
        } else {
            NetworkManager.sendWorldPacket(level, new S2C_SynchBlockEntityPacket(worldPosition));
        }
    }

    @SuppressWarnings("unchecked")
    private <C extends SkilledWorkbenchTileEntity, T extends SkilledRecipe<C>> void updateGridListener(RecipeManager manager) {
        IRecipeType<T> type = this.getRecipeType();
        Optional<T> optional = manager.getRecipeFor(type, (C) this, level);
        gridChanged.onChange(optional.orElse(null));
    }
}
