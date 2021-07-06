package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.block.BlockBlastFurnace;
import dev.toma.gunsrpg.common.init.GRPGTileEntities;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.recipes.BlastFurnaceRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class BlastFurnaceTileEntity extends InventoryTileEntity implements ITickableTileEntity {

    public static final int SMELT_TIME = 800;
    public static final int FUEL_VALUE_LIMIT = 1600;
    public int timeCooking;
    public int fuelValue;
    private boolean isBurning;

    public BlastFurnaceTileEntity() {
        this(GRPGTileEntities.BLAST_FURNACE.get());
    }

    protected BlastFurnaceTileEntity(TileEntityType<? extends BlastFurnaceTileEntity> type) {
        super(type);
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(3);
    }

    @Override
    public void write(CompoundNBT nbt) {
        nbt.putInt("time", timeCooking);
        nbt.putInt("fuel", fuelValue);
        nbt.putBoolean("burning", isBurning);
    }

    @Override
    public void read(CompoundNBT nbt) {
        timeCooking = nbt.getInt("time");
        fuelValue = nbt.getInt("fuel");
        isBurning = nbt.getBoolean("burning");
    }

    @Override
    public void tick() {
        if(BlastFurnaceRecipe.hasRecipeFor(getInput()) && canSmelt()) {
            if(fuelValue > 0) {
                setBurningState(true);
            } else if(hasFuelInSlot()) {
                this.fuelValue = FUEL_VALUE_LIMIT;
                consumeItem(2);
                setBurningState(true);
            } else setBurningState(false);
            if(isBurning && ++timeCooking >= SMELT_TIME) {
                timeCooking = 0;
                ItemStack res = BlastFurnaceRecipe.getResult(getInput());
                ItemStack out = getOutput();
                setItem(1, out.isEmpty() ? res : new ItemStack(out.getItem(), out.getCount() + 2));
                consumeItem(0);
            }
        } else timeCooking = 0;
        if(fuelValue > 0) {
            --fuelValue;
            if(fuelValue == 0) setBurningState(false);
        }
    }

    private boolean canSmelt() {
        ItemStack out = getOutput();
        return (out.isEmpty() || out.getItem() == BlastFurnaceRecipe.getResult(getInput()).getItem()) && out.getCount() < 63;
    }

    private void setBurningState(boolean state) {
        if(state != isBurning) {
            BlockBlastFurnace.updateBurnState(worldPosition, level, state);
            this.timeCooking = 0;
        }
        this.isBurning = state;
    }

    private ItemStack getInput() {
        return ModUtils.extractOptionalOrElse(getInventory(), handler -> handler.getStackInSlot(0), ItemStack.EMPTY);
    }

    private ItemStack getOutput() {
        return ModUtils.extractOptionalOrElse(getInventory(), handler -> handler.getStackInSlot(1), ItemStack.EMPTY);
    }

    private boolean hasFuelInSlot() {
        return ModUtils.extractOptionalOrElse(getInventory(), handler -> handler.getStackInSlot(2).getItem() == Items.COAL, false);
    }
}
