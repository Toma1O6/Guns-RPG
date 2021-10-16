package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.tileentity.InventoryTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public abstract class AbstractModContainer<T extends InventoryTileEntity> extends AbstractContainer {

    protected final T tileEntity;
    private final IWorldPosCallable access;
    private int size;

    public AbstractModContainer(ContainerType<? extends AbstractModContainer<?>> type, int windowID, PlayerInventory playerInventory, T tileEntity) {
        super(type, windowID);
        this.access = IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos());
        this.tileEntity = tileEntity;
        tileEntity.getInventory().ifPresent(handler -> size = handler.getSlots());
    }

    @SuppressWarnings("unchecked")
    public static <T extends TileEntity> T readTileEntity(PacketBuffer buffer, PlayerInventory inventory) {
        return (T) inventory.player.level.getBlockEntity(buffer.readBlockPos());
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return stillValid(access, player, player.level.getBlockState(tileEntity.getBlockPos()).getBlock());
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = getSlot(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index < size) {
                if (!moveItemStackTo(slotStack, size, slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!moveItemStackTo(slotStack, 0, size, false))
                return ItemStack.EMPTY;
            if (slotStack.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
        }
        return stack;
    }

    public T getTileEntity() {
        return tileEntity;
    }
}
