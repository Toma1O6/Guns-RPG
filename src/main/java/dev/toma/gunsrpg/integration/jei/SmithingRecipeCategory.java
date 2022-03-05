package dev.toma.gunsrpg.integration.jei;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.resource.crafting.SmithingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class SmithingRecipeCategory implements IRecipeCategory<SmithingRecipe> {

    private static final int OUTPUT = 0;
    private static final int INPUT_1 = 1;

    public static final ResourceLocation CATEGORY_UID = GunsRPG.makeResource("smithing");
    private static final ResourceLocation BACKGROUND_TEXTURE = GunsRPG.makeResource("textures/screen/jei_smithing.png");
    private final String title;
    private final IDrawable background;
    private final IDrawable icon;
    private final ICraftingGridHelper gridHelper;

    public SmithingRecipeCategory(IGuiHelper helper) {
        title = "Smithing";
        background = helper.createDrawable(BACKGROUND_TEXTURE, 0, 0, 116, 54);
        icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.SMITHING_TABLE));
        gridHelper = helper.createCraftingGridHelper(INPUT_1);
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
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SmithingRecipe recipe, IIngredients ingredients) {
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
