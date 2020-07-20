package dev.toma.gunsrpg.util.recipes;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.tileentity.TileEntitySmithingTable;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SmithingTableRecipes {

    private static final List<SmithingRecipe> RECIPES = new ArrayList<>();

    /*
    0 1 2
    3 4 5
    6 7 8
     */
    public static void register() {
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.SHOTGUN_SHELL).withIngredient(0, Items.DYE, 1).withIngredient(1, ModRegistry.GRPGItems.LARGE_BULLET_CASING).withIngredient(2, Items.PAPER).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.LARGE_BULLET_CASING).withIngredient(new int[]{0, 1}, ModRegistry.GRPGItems.SMALL_BULLET_CASING).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.BOLT_FLETCHING).withIngredient(0, Items.IRON_NUGGET).withIngredient(3, Items.FEATHER).withIngredient(6, Items.GOLD_NUGGET).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.SMALL_BULLET_CASING, 16).withIngredient(0, Items.COAL, 0).withIngredient(1, Items.IRON_INGOT).withIngredient(2, Items.GOLD_INGOT).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.PISTOL).withIngredient(0, ModRegistry.GRPGItems.BARREL).withIngredient(1, ModRegistry.GRPGItems.GUN_PARTS).withIngredient(2, ModRegistry.GRPGItems.SMALL_IRON_STOCK).requires(ModRegistry.Skills.PISTOL_ASSEMBLY).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.SMG).withIngredient(0, ModRegistry.GRPGItems.BARREL).withIngredient(1, ModRegistry.GRPGItems.GUN_PARTS).withIngredient(2, ModRegistry.GRPGItems.IRON_STOCK).withIngredient(4, ModRegistry.GRPGItems.MAGAZINE).requires(ModRegistry.Skills.SMG_ASSEMBLY).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.CROSSBOW).withIngredient(3, Blocks.TRIPWIRE_HOOK).withIngredient(4, ModRegistry.GRPGItems.GUN_PARTS).withIngredient(5, ModRegistry.GRPGItems.WOODEN_STOCK).withIngredient(new int[] {1, 7}, Items.BOW).requires(ModRegistry.Skills.CROSSBOW_ASSEMBLY).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.SHOTGUN).withIngredient(0, ModRegistry.GRPGItems.LONG_BARREL).withIngredient(1, ModRegistry.GRPGItems.GUN_PARTS).withIngredient(2, ModRegistry.GRPGItems.WOODEN_STOCK).withIngredient(4, ModRegistry.GRPGItems.MAGAZINE).requires(ModRegistry.Skills.SHOTGUN_ASSEMBLY).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.ASSAULT_RIFLE).withIngredient(0, ModRegistry.GRPGItems.LONG_BARREL).withIngredient(1, ModRegistry.GRPGItems.GUN_PARTS).withIngredient(2, ModRegistry.GRPGItems.IRON_STOCK).withIngredient(4, ModRegistry.GRPGItems.MAGAZINE).requires(ModRegistry.Skills.ASSAULT_RIFLE_ASSEMBLY).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.SNIPER_RIFLE).withIngredient(0, ModRegistry.GRPGItems.LONG_BARREL).withIngredient(1, ModRegistry.GRPGItems.GUN_PARTS).withIngredient(2, ModRegistry.GRPGItems.WOODEN_STOCK).requires(ModRegistry.Skills.SNIPER_RIFLE_ASSEMBLY).asRecipe());
        register(new RecipeBuilder().outputs(Items.GUNPOWDER, SkillUtil::getGunpowderCraftAmount).withIngredient(new int[] {0, 4}, 1, Items.COAL).withIngredient(1, Items.DYE, 15).withIngredient(3, Items.SUGAR).requires(ModRegistry.Skills.GUNPOWDER_NOVICE).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.AMETHYST).withIngredient(1, Items.DIAMOND).withIngredient(new int[]{3, 5}, Items.QUARTZ).withIngredient(4, Items.BLAZE_POWDER).withIngredient(7, Items.EMERALD).requires(ModRegistry.Skills.MINERALOGIST).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.BARREL).withIngredient(new int[]{0, 1, 2, 6, 7, 8}, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.GUN_PARTS).withIngredient(new int[]{0, 2, 6, 8}, Blocks.IRON_BARS).withIngredient(new int[]{1, 7}, Blocks.IRON_TRAPDOOR).withIngredient(new int[]{3, 5}, Items.IRON_INGOT).withIngredient(4, Items.IRON_PICKAXE).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.IRON_STOCK).withIngredient(new int[]{0, 1}, Blocks.IRON_BLOCK).withIngredient(new int[]{2, 4}, Items.IRON_INGOT).withIngredient(5, Items.FLINT_AND_STEEL).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.LONG_BARREL).withIngredient(new int[]{1, 2}, ModRegistry.GRPGItems.BARREL).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.MAGAZINE).withIngredient(new int[]{1, 2, 4, 5, 6, 7}, Items.IRON_INGOT).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.SMALL_IRON_STOCK).withIngredient(0, Blocks.IRON_BLOCK).withIngredient(new int[]{1, 3}, Items.IRON_INGOT).withIngredient(4, Items.FLINT_AND_STEEL).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.WOODEN_STOCK).withIngredient(new int[]{0, 1}, Blocks.LOG, Blocks.LOG2).withIngredient(2, Items.IRON_INGOT).withIngredient(4, Blocks.WOODEN_SLAB).withIngredient(5, Items.FLINT_AND_STEEL).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
    }

    public static SmithingRecipe findRecipe(TileEntitySmithingTable table) {
        for (SmithingRecipe recipe : RECIPES) {
            boolean valid = true;
            for (SmithingIngredient ingredient : recipe.ingredients) {
                ItemStack item = table.getStackInSlot(ingredient.id);
                if (!ModUtils.contains(item, ingredient.items, ItemStack::isItemEqual)) {
                    valid = false;
                    break;
                }
            }
            if (valid) return recipe;
        }
        return null;
    }

    private static void register(SmithingRecipe recipe) {
        RECIPES.add(recipe);
    }

    public static List<SmithingRecipe> getAvailableRecipes(EntityPlayer player) {
        PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
        return RECIPES.stream().filter(recipe -> recipe.requiredType == null || skills.hasSkill(recipe.requiredType)).collect(Collectors.toList());
    }

    public static class SmithingRecipe {
        private final SmithingIngredient[] ingredients;
        private final SmithingRecipeOutput output;
        private final SkillType<?> requiredType;

        public SmithingRecipe(RecipeBuilder builder) {
            this.ingredients = builder.ingredientList.toArray(new SmithingIngredient[0]);
            this.output = builder.output;
            this.requiredType = builder.requiredType;
        }

        public ItemStack getOutput(EntityPlayer player) {
            return output.getResult(player);
        }

        public SkillType<?> getRequiredType() {
            return requiredType;
        }

        public SmithingIngredient[] getIngredients() {
            return ingredients;
        }
    }

    public static class RecipeBuilder {
        private final List<SmithingIngredient> ingredientList = new ArrayList<>();
        private SmithingRecipeOutput output;
        private SkillType<?> requiredType;

        public RecipeBuilder withIngredient(int id, Block... blocks) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, Arrays.stream(blocks).map(ItemStack::new).toArray(ItemStack[]::new)));
            return this;
        }

        public RecipeBuilder withIngredient(int id, Block block) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, Item.getItemFromBlock(block)));
            return this;
        }

        public RecipeBuilder withIngredient(int id, Item item) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, item));
            return this;
        }

        public RecipeBuilder withIngredient(int id, Item item, int meta) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, item, meta));
            return this;
        }

        public RecipeBuilder withIngredient(int id, Item... items) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, 0, items));
            return this;
        }

        public RecipeBuilder withIngredient(int[] ints, Item item, int meta) {
            for (int id : ints) {
                if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
                ingredientList.add(new SmithingIngredient(id, item, meta));
            }
            return this;
        }

        public RecipeBuilder withIngredient(int[] ints, Item... items) {
            for (int id : ints) {
                if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
                ingredientList.add(new SmithingIngredient(id, 0, items));
            }
            return this;
        }

        public RecipeBuilder withIngredient(int[] ints, int meta, Item... items) {
            for (int id : ints) {
                if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
                ingredientList.add(new SmithingIngredient(id, meta, items));
            }
            return this;
        }

        public RecipeBuilder withIngredient(int[] ints, Block block) {
            return this.withIngredient(ints, Item.getItemFromBlock(block));
        }

        public RecipeBuilder withIngredient(int[] ints, Block... blocks) {
            return this.withIngredient(ints, Arrays.stream(blocks).map(Item::getItemFromBlock).toArray(Item[]::new));
        }

        public RecipeBuilder outputs(Item item) {
            this.output = new SmithingRecipeOutput(item, p -> 1);
            return this;
        }

        public RecipeBuilder outputs(Item item, int amount) {
            this.output = new SmithingRecipeOutput(item, p -> amount);
            return this;
        }

        public RecipeBuilder outputs(Item item, Function<EntityPlayer, Integer> amountFunction) {
            this.output = new SmithingRecipeOutput(item, amountFunction);
            return this;
        }

        public RecipeBuilder requires(SkillType<?> type) {
            this.requiredType = type;
            return this;
        }

        public SmithingRecipe asRecipe() {
            return new SmithingRecipe(this);
        }
    }

    public static class SmithingIngredient {

        private final int id;
        private final ItemStack[] items;

        private SmithingIngredient(int id, Item item, int meta) {
            this(id, new ItemStack(item, 1, meta));
        }

        private SmithingIngredient(int id, Item item) {
            this(id, new ItemStack(item));
        }

        private SmithingIngredient(int id, int meta, Item... items) {
            this(id, Arrays.stream(items).map(item -> new ItemStack(item, 1, meta)).toArray(ItemStack[]::new));
        }

        private SmithingIngredient(int id, ItemStack item) {
            this(id, new ItemStack[]{item});
        }

        private SmithingIngredient(int id, ItemStack[] item) {
            this.id = id;
            this.items = item;
        }

        public int getIndex() {
            return id;
        }

        public ItemStack getFirstItem() {
            return items[0].copy();
        }
    }

    public static class SmithingRecipeOutput {

        private final Function<EntityPlayer, Integer> amount;
        private final Item item;

        private SmithingRecipeOutput(Item item, Function<EntityPlayer, Integer> amount) {
            this.item = item;
            this.amount = amount;
        }

        public ItemStack getResult(EntityPlayer player) {
            return new ItemStack(item, amount.apply(player));
        }
    }
}
