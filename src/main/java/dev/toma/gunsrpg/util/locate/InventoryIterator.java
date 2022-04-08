package dev.toma.gunsrpg.util.locate;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.function.BiFunction;
import java.util.function.Function;

public class InventoryIterator<I> implements IContextIterator<ItemStack> {

    private final I inventory;
    private final BiFunction<I, Integer, ItemStack> accessor;
    private final int size;
    private int index;

    public InventoryIterator(I inventory, BiFunction<I, Integer, ItemStack> accessor, Function<I, Integer> size) {
        this.inventory = inventory;
        this.accessor = accessor;
        this.size = size.apply(inventory);
    }

    public static InventoryIterator<IInventory> vanilla(IInventory inventory) {
        return new InventoryIterator<>(inventory, IInventory::getItem, IInventory::getContainerSize);
    }

    @Override
    public ItemStack next() {
        return accessor.apply(inventory, index++);
    }

    @Override
    public boolean hasNext() {
        return index < size;
    }
}
