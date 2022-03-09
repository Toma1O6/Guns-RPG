package dev.toma.gunsrpg.integration.jei;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.crafting.SkilledRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class SkilledRecipeCategory implements IRecipeCategory<SkilledRecipe<?>> {

    private static final int OUTPUT = 0;
    private static final int INPUT_1 = 1;
    private static final ResourceLocation BACKGROUND = GunsRPG.makeResource("textures/screen/jei_skilled_recipe.png");

    private final ResourceLocation id;
    private final Class<? extends SkilledRecipe<?>> recipeClass;
    private final String title;
    private final IDrawable background;
    private final IDrawable icon;
    private final ICraftingGridHelper gridHelper;

    SkilledRecipeCategory(IGuiHelper helper, String id, Block icon, Class<? extends SkilledRecipe<?>> recipeClass) {
        this.id = GunsRPG.makeResource(id);
        this.title = id.substring(0, 1).toUpperCase() + id.substring(1);
        this.background = helper.createDrawable(BACKGROUND, 0, 0, 116, 54);
        this.icon = helper.createDrawableIngredient(new ItemStack(icon));
        this.gridHelper = helper.createCraftingGridHelper(INPUT_1);
        this.recipeClass = recipeClass;
    }

    @Override
    public ResourceLocation getUid() {
        return id;
    }

    @Override
    public Class<? extends SkilledRecipe<?>> getRecipeClass() {
        return recipeClass;
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
    public void setIngredients(SkilledRecipe<?> recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SkilledRecipe<?> recipe, IIngredients ingredients) {
        IGuiItemStackGroup group = recipeLayout.getItemStacks();
        group.init(OUTPUT, false, 94, 18);
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                int slotIndex = x + (y * 3) + INPUT_1;
                group.init(slotIndex, true, x * 18, y * 18);
            }
        }
        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
        gridHelper.setInputs(group, inputs, recipe.getWidth(), recipe.getHeight());
        group.set(OUTPUT, outputs.get(0));
    }
}
