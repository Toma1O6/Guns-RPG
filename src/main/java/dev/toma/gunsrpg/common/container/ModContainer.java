package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.tileentity.IInventoryFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ModContainer extends Container {

    protected final IInventoryFactory factory;

    public ModContainer(IInventoryFactory factory) {
        this.factory = factory;
    }

    public void addSlots(IInventory inventory, int rows, int cols, int posY) {
        this.addSlots(inventory, rows, cols, 8, posY);
    }

    public void addSlots(IInventory inventory, int rows, int cols, int posX, int posY) {
        this.addSlots(inventory, rows, cols, posX, posY, 0);
    }

    public void addSlots(IInventory inventory, int rows, int cols, int posX, int posY, int idAdd) {
        this.addSlots(inventory, rows, cols, posX, posY, idAdd, this::makeDefaultSlot);
    }

    public void addSlots(IInventory inventory, int rows, int cols, int posX, int posY, int idAdd, SlotFactory factory) {
        for(int y = 0; y < rows; y++) {
            for(int x = 0; x < cols; x++) {
                this.addSlotToContainer(factory.createSlot(inventory, y * cols + x + idAdd, posX + x * 18, posY + y * 18));
            }
        }
    }

    public void addPlayerInventory(InventoryPlayer inv, int ys) {
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(inv, 9 + y * 9 + x, 8 + x * 18, ys + y * 18));
            }
        }
        for(int x = 0; x < 9; x++) {
            addSlotToContainer(new Slot(inv, x, 8 + x * 18, ys + 58));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.factory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.factory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.factory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    private Slot makeDefaultSlot(IInventory inventory, int id, int posX, int posY) {
        return new Slot(inventory, id, posX, posY);
    }

    public interface SlotFactory {
        Slot createSlot(IInventory inventory, int id, int posX, int posY);
    }
}
