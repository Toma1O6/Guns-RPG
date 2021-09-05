package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;

public class SmithingTableContainer extends AbstractModContainer<SmithingTableTileEntity> {

    public SmithingTableContainer(int windowID, PlayerInventory inventory, SmithingTableTileEntity tileEntity) {
        super(ModContainers.SMITHING_TABLE.get(), windowID, inventory, tileEntity);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                addSlot(new Slot(tileEntity, x + y * 3, 8 + x * 18, 8 + y * 18));
            }
        }
        addPlayerInventory(inventory, 90);
        addSlotListener(new SlotListener(tileEntity));
    }

    public SmithingTableContainer(int windowID, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowID, inventory, readTileEntity(buffer, inventory));
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

    private static class SlotListener implements IContainerListener {

        private final SmithingTableTileEntity tile;

        public SlotListener(SmithingTableTileEntity tile) {
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
