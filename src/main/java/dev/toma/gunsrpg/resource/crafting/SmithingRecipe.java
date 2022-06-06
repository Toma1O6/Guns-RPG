package dev.toma.gunsrpg.resource.crafting;

import dev.toma.gunsrpg.common.init.ModRecipeSerializers;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import dev.toma.gunsrpg.resource.util.conditions.IRecipeCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class SmithingRecipe extends SkilledRecipe<SmithingTableTileEntity> {

    public SmithingRecipe(ResourceLocation id, int width, int height, NonNullList<Ingredient> ingredientList, ItemStack output, ItemStack returningItem, OutputModifier outputModifier, List<IRecipeCondition> conditions) {
        super(id, width, height, ingredientList, output, returningItem, outputModifier, conditions);
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeTypes.SMITHING_RECIPE_TYPE;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.SMITHING_RECIPE_SERIALIZER.get();
    }

    public static final class Serializer extends SkilledRecipeSerializer<SmithingRecipe> {

        @Override
        public SmithingRecipe createRecipeInstance(ResourceLocation id, int width, int height, NonNullList<Ingredient> ingredients, ItemStack output, ItemStack returningItem, OutputModifier modifier, List<IRecipeCondition> conditionList) {
            return new SmithingRecipe(id, width, height, ingredients, output, returningItem, modifier, conditionList);
        }
    }
}
