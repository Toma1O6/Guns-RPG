package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.blasting.BlastingRecipe;
import dev.toma.gunsrpg.resource.cooking.CookingRecipe;
import dev.toma.gunsrpg.resource.crafting.CulinaryRecipe;
import dev.toma.gunsrpg.resource.crafting.MedRecipe;
import dev.toma.gunsrpg.resource.crafting.SmithingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;

public final class ModRecipeTypes {

    public static IRecipeType<SmithingRecipe> SMITHING_RECIPE_TYPE;
    public static IRecipeType<CookingRecipe> COOKING_RECIPE_TYPE;
    public static IRecipeType<CulinaryRecipe> CULINARY_RECIPE_TYPE;
    public static IRecipeType<MedRecipe> MED_RECIPE_TYPE;
    public static IRecipeType<BlastingRecipe> BLASTING_RECIPE_TYPE;

    public static void register() {
        SMITHING_RECIPE_TYPE = registerType("smithing");
        COOKING_RECIPE_TYPE = registerType("cooking");
        CULINARY_RECIPE_TYPE = registerType("culinary");
        MED_RECIPE_TYPE = registerType("medstation");
        BLASTING_RECIPE_TYPE = registerType("blasting");
    }

    private static <R extends IRecipe<?>> IRecipeType<R> registerType(String id) {
        return Registry.register(Registry.RECIPE_TYPE, GunsRPG.makeResource(id), new IRecipeType<R>() {});
    }
}
