package dev.toma.gunsrpg.resource.blasting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.common.init.ModRecipeSerializers;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.common.tileentity.BlastFurnaceTileEntity;
import dev.toma.gunsrpg.resource.cooking.IBurningRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class BlastingRecipe implements IRecipe<BlastFurnaceTileEntity>, IBurningRecipe {

    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float experience;
    private final int cookingTime;

    public BlastingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(BlastFurnaceTileEntity tile, World world) {
        return ingredient.test(tile.getItem(0));
    }

    @Override
    public ItemStack assemble(BlastFurnaceTileEntity tile) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public ItemStack[] getInputStacks() {
        return ingredient.getItems();
    }

    @Override
    public float getExperience() {
        return experience;
    }

    @Override
    public int getCookTime() {
        return cookingTime;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.BLASTING_RECIPE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeTypes.BLASTING_RECIPE_TYPE;
    }

    public static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BlastingRecipe> {

        @Override
        public BlastingRecipe fromJson(ResourceLocation id, JsonObject object) {
            JsonElement jsonelement = JSONUtils.isArrayNode(object, "ingredient") ? JSONUtils.getAsJsonArray(object, "ingredient") : JSONUtils.getAsJsonObject(object, "ingredient");
            Ingredient ingredient = Ingredient.fromJson(jsonelement);
            if (!object.has("result"))
                throw new JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack itemstack;
            if (object.get("result").isJsonObject())
                itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(object, "result"));
            else {
                String s1 = JSONUtils.getAsString(object, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + s1 + " does not exist")));
            }
            float exp = JSONUtils.getAsFloat(object, "experience", 0.0F);
            int time = JSONUtils.getAsInt(object, "cookingtime", 800);
            return new BlastingRecipe(id, ingredient, itemstack, exp, time);
        }

        @Nullable
        @Override
        public BlastingRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack itemstack = buffer.readItem();
            float exp = buffer.readFloat();
            int time = buffer.readVarInt();
            return new BlastingRecipe(id, ingredient, itemstack, exp, time);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, BlastingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookingTime);
        }
    }
}
