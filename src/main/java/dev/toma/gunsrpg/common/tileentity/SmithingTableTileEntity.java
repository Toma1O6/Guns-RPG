package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.GRPGTileEntities;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class SmithingTableTileEntity extends InventoryTileEntity {

    public SmithingTableTileEntity() {
        this(GRPGTileEntities.SMITHING_TABLE.get());
    }

    protected SmithingTableTileEntity(TileEntityType<? extends SmithingTableTileEntity> type) {
        super(type);
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(9);
    }
}
