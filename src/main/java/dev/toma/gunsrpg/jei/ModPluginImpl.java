package dev.toma.gunsrpg.jei;

import dev.toma.gunsrpg.common.init.GRPGBlocks;
import dev.toma.gunsrpg.util.recipes.SmithingTableRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class ModPluginImpl implements IModPlugin {

    public static final String SMITHING_UID = "gunsrpg.smithing";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new SmithingCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipes(SmithingTableRecipes.getRecipes(), SMITHING_UID);
        registry.handleRecipes(SmithingTableRecipes.SmithingRecipe.class, SmithingRecipeWrapper::new, SMITHING_UID);
        registry.addRecipeCatalyst(new ItemStack(GRPGBlocks.SMITHING_TABLE), SMITHING_UID);
    }
}
