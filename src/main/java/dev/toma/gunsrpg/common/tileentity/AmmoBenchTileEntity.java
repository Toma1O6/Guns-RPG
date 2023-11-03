package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.ModBlockEntities;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class AmmoBenchTileEntity extends InventoryTileEntity implements ITickableTileEntity, ISynchronizable {

    public static final int SLOT_OUTPUT = 6;
    public static final int[] SLOT_INPUTS = { 0, 1, 2, 3, 4, 5 };

    private boolean crafting;
    private int recipeCraftTime;
    private int timeCrafting;
    // TODO Active Recipe

    public AmmoBenchTileEntity() {
        super(ModBlockEntities.AMMO_BENCH.get());
    }

    @Override
    public void tick() {

    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(7);
    }

    @Override
    public void onSynch() {

    }
}
