package dev.toma.gunsrpg.common.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public abstract class VanillaInventoryTileEntity extends InventoryTileEntity implements IInventory {

    public VanillaInventoryTileEntity(TileEntityType<? extends VanillaInventoryTileEntity> type) {
        super(type);
    }

    @Override
    public int getContainerSize() {
        return itemHandler.getSlots();
    }

    @Override
    public void setItem(int id, ItemStack stack) {
        itemHandler.setStackInSlot(id, stack);
    }

    @Override
    public ItemStack getItem(int index) {
        return itemHandler.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return index >= 0 && index < getContainerSize() && !getItem(index).isEmpty() && count > 0 ? getItem(index).split(count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index >= 0 && index < getContainerSize()) {
            ItemStack old = getItem(index);
            setItem(index, ItemStack.EMPTY);
            return old;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        } else {
            BlockPos pos = worldPosition;
            return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
        }
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}
