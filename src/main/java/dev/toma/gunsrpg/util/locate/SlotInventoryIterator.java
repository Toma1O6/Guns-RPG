package dev.toma.gunsrpg.util.locate;

import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import net.minecraft.item.ItemStack;

import java.util.function.BiFunction;

public class SlotInventoryIterator<I> implements ItemLocator.InventoryIterator<I> {

    private final int[] slots;
    private final BiFunction<I, Integer, ItemStack> fetcher;

    public SlotInventoryIterator(int[] slots, BiFunction<I, Integer, ItemStack> fetcher) {
        this.slots = slots;
        this.fetcher = fetcher;
    }

    @Override
    public int slots(I inventory) {
        return slots.length;
    }

    @Override
    public ItemStack itemAt(I inventory, int index) {
        return index >= 0 && index < slots.length ? fetcher.apply(inventory, slots[index]) : ItemStack.EMPTY;
    }
}
