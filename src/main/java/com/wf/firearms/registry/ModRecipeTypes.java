package com.wf.firearms.registry;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.culinary.CulinaryRecipe;
import com.wf.firearms.gunsmith.GunsmithRecipe;
import com.wf.firearms.medical.MedicalStationRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, GunsRpg.MOD_ID);

    public static final RegistryObject<RecipeType<GunsmithRecipe>> GUNSMITH =
            RECIPE_TYPES.register(
                    "gunsmith",
                    () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "gunsmith")));

    public static final RegistryObject<RecipeType<CulinaryRecipe>> CULINARY =
            RECIPE_TYPES.register(
                    "culinary",
                    () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "culinary")));

    public static final RegistryObject<RecipeType<MedicalStationRecipe>> MEDICAL_STATION =
            RECIPE_TYPES.register(
                    "medical_station",
                    () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "medical_station")));

    private ModRecipeTypes() {}
}
