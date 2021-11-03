package dev.toma.gunsrpg.resource.startgear;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.Function;

public interface IGear extends Function<Entity, ItemStack[]> {

    ItemStack[] getGearItems(World level);

    @Override
    default ItemStack[] apply(Entity entity) {
        return getGearItems(entity.level);
    }
}
