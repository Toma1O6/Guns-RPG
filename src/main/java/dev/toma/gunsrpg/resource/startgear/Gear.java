package dev.toma.gunsrpg.resource.startgear;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Gear implements IGear {

    private final ItemStack[] items;

    public Gear(ItemStack[] items) {
        this.items = items;
    }

    @Override
    public ItemStack[] getGearItems(World level) {
        return items;
    }
}
