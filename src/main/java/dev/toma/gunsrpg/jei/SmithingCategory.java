package dev.toma.gunsrpg.jei;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.GRPGBlocks;
import dev.toma.gunsrpg.util.recipes.SmithingTableRecipes;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class SmithingCategory implements IRecipeCategory<SmithingRecipeWrapper> {

    static final ResourceLocation BACKGROUND = new ResourceLocation("gunsrpg:textures/gui/jei_smithing.png");
    final IDrawable background;
    final IDrawable icon;

    public SmithingCategory(IGuiHelper helper) {
        background = helper.drawableBuilder(BACKGROUND, 0, 0, 116, 54).build();
        icon = helper.createDrawableIngredient(new ItemStack(GRPGBlocks.SMITHING_TABLE));
    }

    @Override
    public String getUid() {
        return ModPluginImpl.SMITHING_UID;
    }

    @Override
    public String getTitle() {
        return "Smithing Table";
    }

    @Override
    public String getModName() {
        return GunsRPG.MODID;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SmithingRecipeWrapper recipeWrapper, IIngredients ingredients) {
        int i = 0;
        SmithingTableRecipes.SmithingRecipe recipe = recipeWrapper.getRecipe();
        for (List<ItemStack> list : ingredients.getInputs(VanillaTypes.ITEM)) {
            int n = recipe.getIngredients()[i].getIndex();
            int x = (n % 3) * 18;
            int y = (n / 3) * 18;
            recipeLayout.getItemStacks().init(i, true, x, y);
            recipeLayout.getItemStacks().set(i++, list);
        }
        recipeLayout.getItemStacks().init(++i, false, 94, 18);
        recipeLayout.getItemStacks().set(i, recipeWrapper.getRecipe().getOutputForDisplay());
    }
}
