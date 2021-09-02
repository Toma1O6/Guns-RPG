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

public abstract class AbstractModContainer<T extends InventoryTileEntity> extends Container {

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
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                this.addSlot(factory.createSlot(inventory, y * cols + x + idAdd, posX + x * 18, posY + y * 18));
            }
        }
    }

    public void addPlayerInventory(PlayerInventory inv, int yOffset) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(inv, 9 + y * 9 + x, 8 + x * 18, yOffset + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(inv, x, 8 + x * 18, yOffset + 58));
        }
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

    private Slot makeDefaultSlot(IInventory inventory, int id, int posX, int posY) {
        return new Slot(inventory, id, posX, posY);
    }

    public interface SlotFactory {
        Slot createSlot(IInventory inventory, int id, int posX, int posY);
    }
}
