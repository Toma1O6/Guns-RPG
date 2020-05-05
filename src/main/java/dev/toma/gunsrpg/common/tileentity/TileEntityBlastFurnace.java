package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.block.BlockBlastFurnace;
import dev.toma.gunsrpg.util.recipes.BlastFurnaceRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TileEntityBlastFurnace extends IInventoryFactory implements ITickable {

    public static final int SMELT_TIME = 800;
    public static final int FUEL_VALUE_LIMIT = 1600;
    public int timeCooking;
    public int fuelValue;
    private boolean isBurning;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public String getName() {
        return "container.blast_furnace";
    }

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public void write(NBTTagCompound nbt) {
        nbt.setInteger("time", timeCooking);
        nbt.setInteger("fuel", fuelValue);
        nbt.setBoolean("burning", isBurning);
    }

    @Override
    public void read(NBTTagCompound nbt) {
        timeCooking = nbt.getInteger("time");
        fuelValue = nbt.getInteger("fuel");
        isBurning = nbt.getBoolean("burning");
    }

    @Override
    public void update() {
        if(BlastFurnaceRecipe.hasRecipeFor(getInput()) && canSmelt()) {
            if(fuelValue > 0) {
                setBurningState(true);
            } else if(hasFuelInSlot()) {
                this.fuelValue = FUEL_VALUE_LIMIT;
                this.decrStackSize(2, 1);
                setBurningState(true);
            } else setBurningState(false);
            if(isBurning && ++timeCooking >= SMELT_TIME) {
                timeCooking = 0;
                ItemStack res = BlastFurnaceRecipe.getResult(getInput());
                ItemStack out = getOutput();
                setInventorySlotContents(1, out.isEmpty() ? res : new ItemStack(out.getItem(), out.getCount() + 2));
                decrStackSize(0, 1);
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
            BlockBlastFurnace.updateBurnState(pos, world, state);
            this.timeCooking = 0;
        }
        this.isBurning = state;
    }

    private ItemStack getInput() {
        return this.getStackInSlot(0);
    }

    private ItemStack getOutput() {
        return this.getStackInSlot(1);
    }

    private boolean hasFuelInSlot() {
        return this.getStackInSlot(2).getItem() == Items.COAL;
    }
}
