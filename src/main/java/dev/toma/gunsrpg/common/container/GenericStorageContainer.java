package dev.toma.gunsrpg.common.container;

import com.google.common.annotations.VisibleForTesting;
import dev.toma.gunsrpg.common.item.StorageItem;
import dev.toma.gunsrpg.util.math.IDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public abstract class GenericStorageContainer extends AbstractContainer {

    private final ItemStack stack;
    private final IInventory inventory;
    private final int slotCount;

    public GenericStorageContainer(ContainerType<? extends GenericStorageContainer> type, PlayerInventory inventory, int windowId) {
        super(type, windowId);
        this.stack = inventory.player.getMainHandItem();
        StorageItem item = (StorageItem) stack.getItem(); // quite unsafe
        StorageItem.IInputFilter filter = item.getInputFilter();
        IDimensions dimensions = item.getDimensions();
        this.inventory = item.getInventory(stack, this::onChangedCallback);
        int left = 8;
        int top = 18;
        int width = dimensions.getWidth();
        int height = dimensions.getHeight();
        slotCount = width * height;
        double offset = getOffsetModifier(width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int xPosition = getOffset(x, offset);
                addSlot(new RestrictedSlot(this.inventory, filter, y * width + x, left + xPosition, top + y * 18));
            }
        }
        addPlayerInventory(inventory, height * 18 + 38);
    }

    @Override
    public void removed(PlayerEntity player) {
        super.removed(player);
        this.onChangedCallback();
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = getSlot(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index < slotCount) {
                if (!moveItemStackTo(slotStack, slotCount, slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!moveItemStackTo(slotStack, 0, slotCount, false))
                return ItemStack.EMPTY;
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return player.isAlive() && stack == player.getMainHandItem();
    }

    private void onChangedCallback() {
        if (inventory != null)
            StorageItem.saveInventoryContents(stack, inventory);
    }

    @VisibleForTesting
    static double getOffsetModifier(int width) {
        return (9 - width) * 0.5;
    }

    @VisibleForTesting
    static int getOffset(int x, double modifier) {
        double left = modifier * 18;
        return (int) (left + x * 18);
    }

    public static class RestrictedSlot extends Slot {

        final StorageItem.IInputFilter filter;

        public RestrictedSlot(IInventory inventory, StorageItem.IInputFilter filter, int id, int x, int y) {
            super(inventory, id, x, y);
            this.filter = filter;
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return filter.isValidInput(stack);
        }
    }

    public static class SavingInventory extends Inventory {

        private final Runnable callback;

        public SavingInventory(int size, Runnable callback) {
            super(size);
            this.callback = callback;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            callback.run();
        }
    }
}
