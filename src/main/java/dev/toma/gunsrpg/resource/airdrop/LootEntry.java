package dev.toma.gunsrpg.resource.airdrop;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class LootEntry {

    private final int weight;
    private final Item item;
    private final ICountFunction function;

    public LootEntry(int weight, Item item, ICountFunction function) {
        this.weight = weight;
        this.item = item;
        this.function = function;
    }

    public int getWeight() {
        return weight;
    }

    public ItemStack produceItem() {
        return new ItemStack(item, function.getCount());
    }
}
