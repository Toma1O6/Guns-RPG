package dev.toma.gunsrpg.integration.jei;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.CookerContainer;
import dev.toma.gunsrpg.common.container.CulinaryTableContainer;
import dev.toma.gunsrpg.common.container.MedstationContainer;
import dev.toma.gunsrpg.common.container.SmithingTableContainer;
import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.resource.crafting.CulinaryRecipe;
import dev.toma.gunsrpg.resource.crafting.MedRecipe;
import dev.toma.gunsrpg.resource.crafting.SmithingRecipe;
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
    public static final ResourceLocation SMITHING = GunsRPG.makeResource("smithing");
    public static final ResourceLocation CULINARY = GunsRPG.makeResource("culinary");
    public static final ResourceLocation MEDSTATION = GunsRPG.makeResource("medstation");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new CookingRecipeCategory(helper),
                new SkilledRecipeCategory(helper, SMITHING.getPath(), ModBlocks.SMITHING_TABLE, SmithingRecipe.class),
                new SkilledRecipeCategory(helper, CULINARY.getPath(), ModBlocks.CULINARY_TABLE, CulinaryRecipe.class),
                new SkilledRecipeCategory(helper, MEDSTATION.getPath(), ModBlocks.MEDICAL_STATION, MedRecipe.class)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeFetcher fetcher = new RecipeFetcher();
        registration.addRecipes(fetcher.getRecipes(ModRecipeTypes.COOKING_RECIPE_TYPE), CookingRecipeCategory.CATEGORY_UID);
        registration.addRecipes(fetcher.getRecipes(ModRecipeTypes.SMITHING_RECIPE_TYPE), SMITHING);
        registration.addRecipes(fetcher.getRecipes(ModRecipeTypes.CULINARY_RECIPE_TYPE), CULINARY);
        registration.addRecipes(fetcher.getRecipes(ModRecipeTypes.MED_RECIPE_TYPE), MEDSTATION);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CookerContainer.class, CookingRecipeCategory.CATEGORY_UID, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(SmithingTableContainer.class, SMITHING, 0, 9, 9, 36);
        registration.addRecipeTransferHandler(CulinaryTableContainer.class, CULINARY, 0, 9, 9, 36);
        registration.addRecipeTransferHandler(MedstationContainer.class, MEDSTATION, 0, 9, 9, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.COOKER), CookingRecipeCategory.CATEGORY_UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SMITHING_TABLE), SMITHING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CULINARY_TABLE), CULINARY);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MEDICAL_STATION), MEDSTATION);
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

        public <I extends IInventory, T extends IRecipe<I>> List<T> getRecipes(IRecipeType<T> type) {
            return getRecipesByType(level.getRecipeManager(), type);
        }

        @SuppressWarnings("unchecked")
        public <I extends IInventory, T extends IRecipe<I>> List<T> getRecipesByType(RecipeManager manager, IRecipeType<T> type) {
            Map<ResourceLocation, IRecipe<I>> recipeMap = manager.byType(type);
            return new ArrayList<>((Collection<T>) recipeMap.values());
        }
    }
}
