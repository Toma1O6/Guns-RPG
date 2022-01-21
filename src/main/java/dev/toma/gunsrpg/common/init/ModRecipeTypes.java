package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.cooking.CookingRecipe;
import dev.toma.gunsrpg.resource.smithing.SmithingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;

public final class ModRecipeTypes {

    public static IRecipeType<SmithingRecipe> SMITHING_RECIPE_TYPE;
    public static IRecipeType<CookingRecipe> COOKING_RECIPE_TYPE;

    public static void register() {
        SMITHING_RECIPE_TYPE = registerType("smithing");
        COOKING_RECIPE_TYPE = registerType("cooking");
    }

    private static <R extends IRecipe<?>> IRecipeType<R> registerType(String id) {
        return Registry.register(Registry.RECIPE_TYPE, GunsRPG.makeResource(id), new IRecipeType<R>() {});
    }
}
