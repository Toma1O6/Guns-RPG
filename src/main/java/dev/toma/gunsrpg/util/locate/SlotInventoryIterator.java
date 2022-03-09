package dev.toma.gunsrpg.util.locate;

import net.minecraft.item.ItemStack;

import java.util.function.BiFunction;

public class SlotInventoryIterator<I> implements IContextIterator<ItemStack> {

    private final int[] slots;
    private final I inventory;
    private final BiFunction<I, Integer, ItemStack> fetcher;
    private int index;

    public SlotInventoryIterator(int[] slots, I inventory, BiFunction<I, Integer, ItemStack> fetcher) {
        this.slots = slots;
        this.inventory = inventory;
        this.fetcher = fetcher;
    }

    @Override
    public ItemStack next() {
        int slot = slots[index++];
        return fetcher.apply(inventory, slot);
    }

    @Override
    public boolean hasNext() {
        return index < slots.length;
    }
}
