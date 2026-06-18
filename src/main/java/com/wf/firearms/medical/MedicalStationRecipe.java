package com.wf.firearms.medical;

import com.wf.firearms.gunsmith.GunsmithRecipe;
import com.wf.firearms.registry.ModRecipeSerializers;
import com.wf.firearms.registry.ModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

/** 医疗站 3×3 配方（逻辑同 {@link GunsmithRecipe}，独立配方类型）。 */
public class MedicalStationRecipe extends GunsmithRecipe {

    public MedicalStationRecipe(
            ResourceLocation id,
            NonNullList<Ingredient> ingredients,
            int width,
            int height,
            ItemStack baseResult,
            String requiredSkill,
            boolean shapeless) {
        super(id, ingredients, width, height, baseResult, requiredSkill, shapeless);
    }

    static MedicalStationRecipe from(GunsmithRecipe base) {
        return new MedicalStationRecipe(
                base.getId(),
                base.getIngredients(),
                base.getPatternWidth(),
                base.getPatternHeight(),
                base.getResultItem(null),
                base.getRequiredSkill(),
                base.isShapeless());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.MEDICAL_STATION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.MEDICAL_STATION.get();
    }

    public static class Serializer implements RecipeSerializer<MedicalStationRecipe> {
        private final GunsmithRecipe.Serializer delegate = new GunsmithRecipe.Serializer();

        @Override
        public MedicalStationRecipe fromJson(ResourceLocation id, com.google.gson.JsonObject json) {
            return MedicalStationRecipe.from(delegate.fromJson(id, json));
        }

        @Override
        public MedicalStationRecipe fromNetwork(ResourceLocation id, net.minecraft.network.FriendlyByteBuf buf) {
            return MedicalStationRecipe.from(delegate.fromNetwork(id, buf));
        }

        @Override
        public void toNetwork(net.minecraft.network.FriendlyByteBuf buf, MedicalStationRecipe recipe) {
            delegate.toNetwork(buf, recipe);
        }
    }
}
