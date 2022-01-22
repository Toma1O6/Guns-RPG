package dev.toma.gunsrpg.integration.jei;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.CookerContainer;
import dev.toma.gunsrpg.common.container.SmithingTableContainer;
import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.resource.cooking.CookingRecipe;
import dev.toma.gunsrpg.resource.smithing.SmithingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
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
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new SmithingRecipeCategory(helper),
                new CookingRecipeCategory(helper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeFetcher fetcher = new RecipeFetcher();
        registration.addRecipes(fetcher.getSmithingRecipes(), SmithingRecipeCategory.CATEGORY_UID);
        registration.addRecipes(fetcher.getCookingRecipes(), CookingRecipeCategory.CATEGORY_UID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(SmithingTableContainer.class, SmithingRecipeCategory.CATEGORY_UID, 0, 9, 9, 36);
        registration.addRecipeTransferHandler(CookerContainer.class, CookingRecipeCategory.CATEGORY_UID, 0, 1, 3, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SMITHING_TABLE), SmithingRecipeCategory.CATEGORY_UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.COOKER), CookingRecipeCategory.CATEGORY_UID);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    public static class RecipeFetcher {

        private final ClientWorld level;

        public RecipeFetcher() {
            level = Objects.requireNonNull(Minecraft.getInstance().level);
        }

        public List<SmithingRecipe> getSmithingRecipes() {
            return getRecipesByType(level.getRecipeManager(), ModRecipeTypes.SMITHING_RECIPE_TYPE);
        }

        public List<CookingRecipe> getCookingRecipes() {
            return getRecipesByType(level.getRecipeManager(), ModRecipeTypes.COOKING_RECIPE_TYPE);
        }

        @SuppressWarnings("unchecked")
        public <I extends IInventory, T extends IRecipe<I>> List<T> getRecipesByType(RecipeManager manager, IRecipeType<T> type) {
            Map<ResourceLocation, IRecipe<I>> recipeMap = manager.byType(type);
            return new ArrayList<>((Collection<T>) recipeMap.values());
        }
    }
}
