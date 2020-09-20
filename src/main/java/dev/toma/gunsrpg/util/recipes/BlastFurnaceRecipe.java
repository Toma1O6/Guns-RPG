package dev.toma.gunsrpg.util.recipes;

import dev.toma.gunsrpg.common.init.GRPGBlocks;
import dev.toma.gunsrpg.common.init.GRPGItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlastFurnaceRecipe {

    private static final Map<Item, Supplier<ItemStack>> RECIPES = new HashMap<>();

    public static boolean hasRecipeFor(Item item) {
        return RECIPES.containsKey(item);
    }

    public static boolean hasRecipeFor(Block block) {
        return RECIPES.containsKey(Item.getItemFromBlock(block));
    }

    public static boolean hasRecipeFor(ItemStack stack) {
        return RECIPES.containsKey(stack.getItem());
    }

    @Nullable
    public static ItemStack getResult(Item item) {
        return RECIPES.get(item).get();
    }

    @Nullable
    public static ItemStack getResult(Block block) {
        return RECIPES.get(Item.getItemFromBlock(block)).get();
    }

    @Nullable
    public static ItemStack getResult(ItemStack stack) {
        return RECIPES.get(stack.getItem()).get();
    }

    public static void init() {
        register(Blocks.COAL_ORE, () -> new ItemStack(Items.COAL, 2));
        register(Blocks.IRON_ORE, () -> new ItemStack(Items.IRON_INGOT, 2));
        register(Blocks.GOLD_ORE, () -> new ItemStack(Items.GOLD_INGOT, 2));
        register(Blocks.DIAMOND_ORE, () -> new ItemStack(Items.DIAMOND, 2));
        register(GRPGBlocks.AMETHYST_ORE, () -> new ItemStack(GRPGItems.AMETHYST, 2));
        register(GRPGItems.IRON_ORE_CHUNK, () -> new ItemStack(Items.IRON_INGOT, 2));
        register(GRPGItems.GOLD_ORE_CHUNK, () -> new ItemStack(Items.GOLD_INGOT, 2));
    }

    private static void register(Item in, Supplier<ItemStack> out) {
        RECIPES.put(in, out);
    }

    private static void register(Block in, Supplier<ItemStack> out) {
        RECIPES.put(Item.getItemFromBlock(in), out);
    }
}
