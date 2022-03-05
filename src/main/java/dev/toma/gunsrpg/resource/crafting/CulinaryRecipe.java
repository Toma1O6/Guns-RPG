package dev.toma.gunsrpg.resource.crafting;

import dev.toma.gunsrpg.common.init.ModRecipeSerializers;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.common.tileentity.CulinaryTableTileEntity;
import dev.toma.gunsrpg.resource.util.conditions.IRecipeCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class CulinaryRecipe extends SkilledRecipe<CulinaryTableTileEntity> {

    public CulinaryRecipe(ResourceLocation id, int width, int height, NonNullList<Ingredient> ingredients, ItemStack output, OutputModifier modifier, List<IRecipeCondition> conditionList) {
        super(id, width, height, ingredients, output, modifier, conditionList);
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeTypes.CULINARY_RECIPE_TYPE;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CULINARY_RECIPE_SERIALIZER.get();
    }

    public static final class Serializer extends SkilledRecipeSerializer<CulinaryRecipe> {

        @Override
        public CulinaryRecipe createRecipeInstance(ResourceLocation id, int width, int height, NonNullList<Ingredient> ingredients, ItemStack output, OutputModifier modifier, List<IRecipeCondition> conditionList) {
            return new CulinaryRecipe(id, width, height, ingredients, output, modifier, conditionList);
        }
    }
}
