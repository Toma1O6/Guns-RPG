package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.tileentity.ISkilledCrafting;
import dev.toma.gunsrpg.common.tileentity.VanillaInventoryTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;

public class SkilledWorkbenchContainer<T extends VanillaInventoryTileEntity & ISkilledCrafting> extends AbstractModContainer<T> {

    protected SkilledWorkbenchContainer(ContainerType<? extends SkilledWorkbenchContainer<T>> type, int windowId, PlayerInventory inventory, T workbench) {
        super(type, windowId, inventory, workbench);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                addSlot(new Slot(tileEntity, x + y * 3, 8 + x * 18, 8 + y * 18));
            }
        }
        addPlayerInventory(inventory, 90);
        addSlotListener(new SlotListener<>(tileEntity));
    }

    protected SkilledWorkbenchContainer(ContainerType<? extends SkilledWorkbenchContainer<T>> type, int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(type, windowId, inventory, readTileEntity(buffer, inventory));
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = getSlot(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemStack = stack.copy();
            if (index >= 0 && index <= 8) {
                if (!moveItemStackTo(stack, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemStack);
            } else if (index >= 9 && index <= 44) {
                if (!moveItemStackTo(stack, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemStack);
            }
        }
        return itemStack;
    }

    private static class SlotListener<T extends VanillaInventoryTileEntity & ISkilledCrafting> implements IContainerListener {

        private final T tile;

        public SlotListener(T tile) {
            this.tile = tile;
        }

        @Override
        public void refreshContainer(Container container, NonNullList<ItemStack> nonNullList) {
        }

        @Override
        public void slotChanged(Container container, int i, ItemStack itemStack) {
            if (i < 9)
                tile.onSynch();
        }

        @Override
        public void setContainerData(Container container, int i, int i1) {
        }
    }
}
