package com.wf.firearms.registry;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.culinary.CulinaryRecipe;
import com.wf.firearms.gunsmith.GunsmithRecipe;
import com.wf.firearms.medical.MedicalStationRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GunsRpg.MOD_ID);

    public static final RegistryObject<RecipeSerializer<GunsmithRecipe>> GUNSMITH =
            SERIALIZERS.register("gunsmith", GunsmithRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<CulinaryRecipe>> CULINARY =
            SERIALIZERS.register("culinary", CulinaryRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<MedicalStationRecipe>> MEDICAL_STATION =
            SERIALIZERS.register("medical_station", MedicalStationRecipe.Serializer::new);

    private ModRecipeSerializers() {}
}
