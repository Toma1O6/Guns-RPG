package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.blasting.BlastingRecipe;
import dev.toma.gunsrpg.resource.cooking.CookingRecipe;
import dev.toma.gunsrpg.resource.crafting.CulinaryRecipe;
import dev.toma.gunsrpg.resource.crafting.MedRecipe;
import dev.toma.gunsrpg.resource.crafting.SmithingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModRecipeSerializers {

    private static final DeferredRegister<IRecipeSerializer<?>> TYPE = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GunsRPG.MODID);

    public static final RegistryObject<IRecipeSerializer<SmithingRecipe>> SMITHING_RECIPE_SERIALIZER = register("smithing_table", SmithingRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<CookingRecipe>> COOKING_RECIPE_SERIALIZER = register("cooking", CookingRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<CulinaryRecipe>> CULINARY_RECIPE_SERIALIZER = register("culinary", CulinaryRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<MedRecipe>> MED_RECIPE_SERIALIZER = register("medical_station", MedRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<BlastingRecipe>> BLASTING_RECIPE_SERIALIZER = register("blasting", BlastingRecipe.Serializer::new);

    public static void subscribe(IEventBus bus) {
        TYPE.register(bus);
    }

    private static <T extends IRecipe<?>> RegistryObject<IRecipeSerializer<T>> register(String name, Supplier<IRecipeSerializer<T>> serializerSupplier) {
        return TYPE.register(name, serializerSupplier);
    }
}
