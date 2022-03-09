package dev.toma.gunsrpg.resource.crafting;

import dev.toma.gunsrpg.common.tileentity.SkilledWorkbenchTileEntity;
import dev.toma.gunsrpg.resource.util.conditions.IRecipeCondition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SkilledRecipe<I extends SkilledWorkbenchTileEntity> implements IRecipe<I> {

    public static final int GRID_SIZE = 3;

    private final NonNullList<Ingredient> ingredientList;
    private final List<IRecipeCondition> conditions;
    private final ItemStack output;
    private final ResourceLocation id;
    @Nullable
    private final OutputModifier outputModifier;
    private final int width;
    private final int height;

    protected SkilledRecipe(ResourceLocation id, int width, int height, NonNullList<Ingredient> ingredients, ItemStack output, OutputModifier modifier, List<IRecipeCondition> conditions) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.ingredientList = ingredients;
        this.output = output;
        this.outputModifier = modifier;
        this.conditions = conditions;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public ItemStack getResultItem() {
        return output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredientList;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    @Override
    public boolean matches(I inventory, World world) {
        for (int x = 0; x <= GRID_SIZE - width; x++) {
            for (int y = 0; y <= GRID_SIZE - height; y++) {
                if (matchesGrid(inventory, x, y, true)) {
                    return true;
                }
                if (matchesGrid(inventory, x, y, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(I inventory) {
        return getResultItem().copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Nullable
    public OutputModifier getOutputModifier() {
        return outputModifier;
    }

    public Collection<IRecipeCondition> listConditions() {
        return conditions;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean canCraft(PlayerEntity player) {
        for (IRecipeCondition condition : conditions) {
            if (!condition.canCraft(player))
                return false;
        }
        return true;
    }

    public List<IRecipeCondition> getFailedChecks(PlayerEntity player) {
        return conditions.stream().filter(condition -> !condition.canCraft(player)).collect(Collectors.toList());
    }

    private boolean matchesGrid(I inventory, int right, int top, boolean bool) {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                int xPos = x - right;
                int yPos = y - top;
                Ingredient ingredient = Ingredient.EMPTY;
                if (xPos >= 0 && yPos >= 0 && xPos < width && yPos < height) {
                    if (bool) {
                        ingredient = ingredientList.get(width - xPos - 1 + yPos * width);
                    } else {
                        ingredient = ingredientList.get(xPos + yPos * width);
                    }
                }
                if (!ingredient.test(inventory.getItem(x + y * GRID_SIZE))) {
                    return false;
                }
            }
        }
        return true;
    }
}
