package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.function.Consumer;

public class ItemModdedFood extends GRPGItem {

    private Consumer<PlayerEntity> onEaten;

    public ItemModdedFood(String name, Food food) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB).food(Objects.requireNonNull(food)));
    }

    public ItemModdedFood buff(Consumer<PlayerEntity> consumer) {
        this.onEaten = consumer;
        return this;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (isEdible()) {
            if (onEaten != null && entity instanceof PlayerEntity && !world.isClientSide)
                onEaten.accept((PlayerEntity) entity);
            return entity.eat(world, stack);
        }
        return stack;
    }
}
