package dev.toma.gunsrpg.common.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class CrystalStationTileEntity extends InventoryTileEntity {

    protected CrystalStationTileEntity(TileEntityType<? extends CrystalStationTileEntity> type) {
        super(type);
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        this.saveInventory = false;
        return new ItemStackHandler(12);
    }
}
