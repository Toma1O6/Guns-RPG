package com.wf.firearms.compat.jei;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.culinary.CulinaryRecipe;
import com.wf.firearms.registry.ModBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class CulinaryRecipeCategory implements IRecipeCategory<CulinaryRecipe> {
    public static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "culinary");

    public static final RecipeType<CulinaryRecipe> RECIPE_TYPE =
            RecipeType.create(GunsRpg.MOD_ID, "culinary", CulinaryRecipe.class);

    private static final int GRID_X = 1;
    private static final int GRID_Y = 1;
    private static final int SLOT = 18;
    private static final int OUTPUT_X = 95;
    private static final int OUTPUT_Y = 19;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;

    public CulinaryRecipeCategory(IJeiHelpers helpers) {
        IGuiHelper gui = helpers.getGuiHelper();
        this.background = gui.createDrawable(
                ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/container/crafting_table.png"),
                29, 16, 116, 54);
        this.icon = gui.createDrawableItemStack(new ItemStack(ModBlocks.CULINARY_TABLE_ITEM.get()));
        this.title = Component.translatable("jei.gunsrpg.culinary");
    }

    @Override
    public RecipeType<CulinaryRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    @SuppressWarnings("removal")
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CulinaryRecipe recipe, IFocusGroup focuses) {
        int w = recipe.getPatternWidth();
        int h = recipe.getPatternHeight();
        int offsetX = Math.max(0, (3 - w) / 2);
        int offsetY = Math.max(0, (3 - h) / 2);
        int idx = 0;
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Ingredient ing = recipe.getIngredients().get(idx++);
                if (!ing.isEmpty()) {
                    builder.addSlot(
                                    RecipeIngredientRole.INPUT,
                                    GRID_X + (col + offsetX) * SLOT,
                                    GRID_Y + (row + offsetY) * SLOT)
                            .addIngredients(ing);
                }
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
    }
}
