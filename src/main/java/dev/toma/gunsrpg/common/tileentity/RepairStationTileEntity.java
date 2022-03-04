package dev.toma.gunsrpg.common.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class RepairStationTileEntity extends InventoryTileEntity {

    protected RepairStationTileEntity(TileEntityType<? extends RepairStationTileEntity> type) {
        super(type);
    }

    public RepairStationTileEntity() {
        this(null); // TODO
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(5);
    }
}
