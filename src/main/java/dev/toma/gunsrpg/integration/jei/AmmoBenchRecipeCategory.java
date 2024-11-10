package dev.toma.gunsrpg.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.resource.MultiIngredient;
import dev.toma.gunsrpg.resource.ammobench.AmmoBenchRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;

public class AmmoBenchRecipeCategory implements IRecipeCategory<AmmoBenchRecipe> {


    private static final ResourceLocation BACKGROUND_TEXTURE = GunsRPG.makeResource("textures/screen/jei_ammo_bench.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated progress;

    public AmmoBenchRecipeCategory(IGuiHelper helper) {
        this.background = helper.drawableBuilder(BACKGROUND_TEXTURE, 0, 0, 162, 36).setTextureSize(188, 36).build();
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.AMMO_BENCH));
        this.progress = helper.drawableBuilder(BACKGROUND_TEXTURE, 162, 0, 26, 12).setTextureSize(188, 36).buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid() {
        return JeiIntegration.AMMO_BENCH;
    }

    @Override
    public Class<? extends AmmoBenchRecipe> getRecipeClass() {
        return AmmoBenchRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Ammo Bench";
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
    public void setIngredients(AmmoBenchRecipe recipe, IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, recipe.getInputs().stream().map(MultiIngredient::getItemStackList).collect(Collectors.toList()));
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutputs().stream().map(AmmoBenchRecipe.AmmoBenchOutput::getItemStack).collect(Collectors.toList()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AmmoBenchRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup group = recipeLayout.getItemStacks();
        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 3; x++) {
                int inputSlotIndex = (y * 3) + x;
                int outputSlotIndex = inputSlotIndex + 6;
                if (inputSlotIndex < inputs.size()) {
                    group.init(inputSlotIndex, true, x * 18, y * 18);
                    group.set(inputSlotIndex, inputs.get(inputSlotIndex));
                }
                if (inputSlotIndex < outputs.size()) {
                    group.init(outputSlotIndex, false, 108 + x * 18, y * 18);
                    group.set(outputSlotIndex, outputs.get(inputSlotIndex));
                }
            }
        }
    }

    @Override
    public void draw(AmmoBenchRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        progress.draw(matrixStack, 68, 12);
        String label = (recipe.getCraftingTimer() / 20) + "s";
        FontRenderer font = Minecraft.getInstance().font;
        font.draw(matrixStack, label, 81 - font.width(label) / 2.0f, 27, 0x404040);
    }
}
