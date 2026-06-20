package com.wf.firearms.compat.jei;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.culinary.CulinaryRecipe;
import com.wf.firearms.gunsmith.GunsmithRecipe;
import com.wf.firearms.medical.MedicalStationRecipe;
import com.wf.firearms.registry.ModBlocks;
import com.wf.firearms.registry.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@JeiPlugin
public class GunsRpgJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "jei");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new GunsmithRecipeCategory(registration.getJeiHelpers()),
                new CulinaryRecipeCategory(registration.getJeiHelpers()),
                new MedicalStationRecipeCategory(registration.getJeiHelpers()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        List<GunsmithRecipe> recipes =
                level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.GUNSMITH.get());
        registration.addRecipes(GunsmithRecipeCategory.RECIPE_TYPE, recipes);
        List<CulinaryRecipe> culinary =
                level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.CULINARY.get());
        registration.addRecipes(CulinaryRecipeCategory.RECIPE_TYPE, culinary);
        List<MedicalStationRecipe> medical =
                level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.MEDICAL_STATION.get());
        registration.addRecipes(MedicalStationRecipeCategory.RECIPE_TYPE, medical);
        registration.addItemStackInfo(
                new ItemStack(ModBlocks.GUNSMITH_TABLE_ITEM.get()),
                Component.translatable("jei.gunsrpg.gunsmith_table.info"));
        registration.addItemStackInfo(
                new ItemStack(ModBlocks.CULINARY_TABLE_ITEM.get()),
                Component.translatable("jei.gunsrpg.culinary_table.info"));
        registration.addItemStackInfo(
                new ItemStack(ModBlocks.MEDICAL_STATION_ITEM.get()),
                Component.translatable("jei.gunsrpg.medical_station.info"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.GUNSMITH_TABLE_ITEM.get()),
                GunsmithRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.CULINARY_TABLE_ITEM.get()),
                CulinaryRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.MEDICAL_STATION_ITEM.get()),
                MedicalStationRecipeCategory.RECIPE_TYPE);
    }
}
