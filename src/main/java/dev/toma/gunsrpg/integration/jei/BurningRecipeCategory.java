package dev.toma.gunsrpg.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.cooking.IBurningRecipe;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;

public class BurningRecipeCategory<R extends IRecipe<?> & IBurningRecipe> implements IRecipeCategory<R> {

    private static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation(ModIds.JEI_ID, "textures/gui/gui_vanilla.png");
    private static final int INPUT = 0;
    private static final int OUTPUT = 2;
    private final ResourceLocation categoryId;
    private final IDrawableAnimated flameAnimated;
    private final IDrawable background;
    private final IDrawable icon;
    private final String title;
    private final int regularCookTime;
    private final Class<R> recipeClass;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public BurningRecipeCategory(IGuiHelper helper, String category, Block iconBlock, int regularCookTime, Class<R> recipeClass) {
        this.categoryId = GunsRPG.makeResource(category);
        this.title = category.substring(0, 1).toUpperCase() + category.substring(1);
        IDrawableStatic flameStatic = helper.createDrawable(RECIPE_GUI_VANILLA, 82, 114, 14, 14);
        this.flameAnimated = helper.createAnimatedDrawable(flameStatic, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.background = helper.createDrawable(RECIPE_GUI_VANILLA, 0, 114, 82, 54);
        this.icon = helper.createDrawableIngredient(new ItemStack(iconBlock));
        this.regularCookTime = regularCookTime;
        this.recipeClass = recipeClass;
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<Integer, IDrawableAnimated>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return helper.drawableBuilder(RECIPE_GUI_VANILLA, 82, 128, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    @Override
    public ResourceLocation getUid() {
        return categoryId;
    }

    @Override
    public Class<R> getRecipeClass() {
        return recipeClass;
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
    public String getTitle() {
        return title;
    }

    @Override
    public void setIngredients(R recipe, IIngredients ingredients) {
        ItemStack[] stacks = recipe.getInputStacks();
        ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(stacks));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void draw(R recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        flameAnimated.draw(matrixStack, 1, 20);
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(matrixStack, 24, 18);
        drawExperience(recipe, matrixStack, 0);
        drawCookTime(recipe, matrixStack, 45);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, R recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(INPUT, true, 0, 0);
        guiItemStacks.init(OUTPUT, false, 60, 18);
        guiItemStacks.set(ingredients);
    }

    private void drawExperience(R recipe, MatrixStack matrixStack, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            TranslationTextComponent experienceString = new TranslationTextComponent("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            fontRenderer.draw(matrixStack, experienceString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    private void drawCookTime(R recipe, MatrixStack matrixStack, int y) {
        int cookTime = recipe.getCookTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            TranslationTextComponent timeString = new TranslationTextComponent("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(matrixStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    private IDrawableAnimated getArrow(R recipe) {
        int cookTime = recipe.getCookTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }
}
