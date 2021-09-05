package dev.toma.gunsrpg.integration;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.util.recipes.smithing.SmithingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class JeiIntegration implements IModPlugin {

    public static final ResourceLocation PLUGIN_UID = GunsRPG.makeResource("jei");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SmithingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(SmithingTableRecipes.getRecipes(), SmithingRecipeCategory.CATEGORY_UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SMITHING_TABLE), SmithingRecipeCategory.CATEGORY_UID);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    private static class SmithingRecipeCategory implements IRecipeCategory<SmithingRecipe> {

        private static final ResourceLocation CATEGORY_UID = GunsRPG.makeResource("smithing");
        private static final ResourceLocation BACKGROUND_TEXTURE = GunsRPG.makeResource("textures/gui/jei_smithing.png");
        private final String title;
        private final IDrawable background;
        private final IDrawable icon;

        SmithingRecipeCategory(IGuiHelper helper) {
            title = "Smithing";
            background = helper.createDrawable(BACKGROUND_TEXTURE, 0, 0, 116, 54);
            icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.SMITHING_TABLE));
        }

        @Override
        public ResourceLocation getUid() {
            return CATEGORY_UID;
        }

        @Override
        public Class<? extends SmithingRecipe> getRecipeClass() {
            return SmithingRecipe.class;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public IDrawable getBackground() {
            return background;
        }

        @Override
        public IDrawable getIcon() {
            return icon;
        }

        @Override
        public void setIngredients(SmithingRecipe recipe, IIngredients ingredients) {
            List<List<ItemStack>> inputs = new ArrayList<>();
            for (SmithingTableRecipes.SmithingIngredient ingredient : recipe.getIngredients()) {
                inputs.add(Arrays.stream(ingredient.getItems()).map(ItemStack::new).collect(Collectors.toList()));
            }
            ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutputForDisplay());
            ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        }

        @Override
        public void setRecipe(IRecipeLayout recipeLayout, SmithingRecipe recipe, IIngredients ingredients) {
            int index = 0;
            IGuiItemStackGroup group = recipeLayout.getItemStacks();
            for (List<ItemStack> inputs : ingredients.getInputs(VanillaTypes.ITEM)) {
                int ingredientIndex = recipe.getIngredients()[index].getIndex();
                int x = (ingredientIndex % 3) * 18;
                int y = (ingredientIndex / 3) * 18;
                group.init(index, true, x, y);
                group.set(index++, inputs);
            }

            group.init(++index, false, 94, 18);
            group.set(index, recipe.getOutputForDisplay());
        }
    }
}
