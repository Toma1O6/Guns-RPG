package dev.toma.gunsrpg.common.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;

public abstract class AbstractContainer extends Container {

    public AbstractContainer(ContainerType<? extends AbstractContainer> type, int windowID) {
        super(type, windowID);
    }

    public final void addSlots(IInventory inventory, int rows, int cols, int posY) {
        this.addSlots(inventory, rows, cols, 8, posY);
    }

    public final void addSlots(IInventory inventory, int rows, int cols, int posX, int posY) {
        this.addSlots(inventory, rows, cols, posX, posY, 0);
    }

    public final void addSlots(IInventory inventory, int rows, int cols, int posX, int posY, int idAdd) {
        this.addSlots(inventory, rows, cols, posX, posY, idAdd, this::makeDefaultSlot);
    }

    public final void addSlots(IInventory inventory, int rows, int cols, int posX, int posY, int idAdd, SlotFactory factory) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                this.addSlot(factory.createSlot(inventory, y * cols + x + idAdd, posX + x * 18, posY + y * 18));
            }
        }
    }

    public final void addPlayerInventory(PlayerInventory inv, int yOffset) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(inv, 9 + y * 9 + x, 8 + x * 18, yOffset + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(inv, x, 8 + x * 18, yOffset + 58));
        }
    }

    protected Slot makeDefaultSlot(IInventory inventory, int id, int posX, int posY) {
        return new Slot(inventory, id, posX, posY);
    }

    public interface SlotFactory {
        Slot createSlot(IInventory inventory, int id, int posX, int posY);
    }
}
