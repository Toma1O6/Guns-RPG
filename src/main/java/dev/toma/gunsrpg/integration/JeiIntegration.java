package dev.toma.gunsrpg.integration;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.SmithingTableContainer;
import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.resource.smithing.SmithingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.*;

@JeiPlugin
public class JeiIntegration implements IModPlugin {

    public static final ResourceLocation PLUGIN_UID = GunsRPG.makeResource("jei");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new SmithingRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeFetcher fetcher = new RecipeFetcher();
        registration.addRecipes(fetcher.getSmithingRecipes(), SmithingRecipeCategory.CATEGORY_UID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(SmithingTableContainer.class, SmithingRecipeCategory.CATEGORY_UID, 0, 9, 9, 36);
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

        private static final int OUTPUT = 0;
        private static final int INPUT_1 = 1;

        private static final ResourceLocation CATEGORY_UID = GunsRPG.makeResource("smithing");
        private static final ResourceLocation BACKGROUND_TEXTURE = GunsRPG.makeResource("textures/gui/jei_smithing.png");
        private final String title;
        private final IDrawable background;
        private final IDrawable icon;
        private final ICraftingGridHelper gridHelper;

        SmithingRecipeCategory(IGuiHelper helper) {
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

    public static class RecipeFetcher {

        private final ClientWorld level;

        public RecipeFetcher() {
            level = Objects.requireNonNull(Minecraft.getInstance().level);
        }

        public List<SmithingRecipe> getSmithingRecipes() {
            return getRecipesByType(level.getRecipeManager(), ModRecipeTypes.SMITHING_RECIPE_TYPE);
        }

        @SuppressWarnings("unchecked")
        public <I extends IInventory, T extends IRecipe<I>> List<T> getRecipesByType(RecipeManager manager, IRecipeType<T> type) {
            Map<ResourceLocation, IRecipe<I>> recipeMap = manager.byType(type);
            return new ArrayList<>((Collection<T>) recipeMap.values());
        }
    }
}
