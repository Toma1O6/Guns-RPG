package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.ModBlockEntities;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.stream.IntStream;

public class SmithingTableTileEntity extends VanillaInventoryTileEntity {

    private static final int OUTPUT = 0;
    private static final int[] INPUTS = IntStream.range(1, 11).toArray();

    public SmithingTableTileEntity() {
        this(ModBlockEntities.SMITHING_TABLE.get());
    }

    protected SmithingTableTileEntity(TileEntityType<? extends SmithingTableTileEntity> type) {
        super(type);
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(9);
    }
}
