package dev.toma.gunsrpg.resource.crafting;

import dev.toma.gunsrpg.common.init.ModRecipeSerializers;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.common.tileentity.MedstationTileEntity;
import dev.toma.gunsrpg.resource.util.conditions.IRecipeCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class MedRecipe extends SkilledRecipe<MedstationTileEntity> {

    public MedRecipe(ResourceLocation id, int width, int height, NonNullList<Ingredient> ingredients, ItemStack output, OutputModifier modifier, List<IRecipeCondition> conditions) {
        super(id, width, height, ingredients, output, modifier, conditions);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.MED_RECIPE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeTypes.MED_RECIPE_TYPE;
    }

    public static final class Serializer extends SkilledRecipeSerializer<MedRecipe> {

        @Override
        public MedRecipe createRecipeInstance(ResourceLocation id, int width, int height, NonNullList<Ingredient> ingredients, ItemStack output, OutputModifier modifier, List<IRecipeCondition> conditionList) {
            return new MedRecipe(id, width, height, ingredients, output, modifier, conditionList);
        }
    }
}
