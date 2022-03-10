package dev.toma.gunsrpg.resource.cooking;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.common.init.ModRecipeSerializers;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.common.tileentity.CookerTileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class CookingRecipe implements IRecipe<CookerTileEntity>, IBurningRecipe {

    private final ResourceLocation id;
    private final ItemStack input;
    private final ItemStack result;
    private final float experience;
    private final int cookTime;

    public CookingRecipe(ResourceLocation id, Item input, ItemStack result, float experience, int cookTime) {
        this.id = id;
        this.input = new ItemStack(input);
        this.result = result;
        this.experience = experience;
        this.cookTime = cookTime;
    }

    @Override
    public ItemStack[] getInputStacks() {
        return new ItemStack[] {input};
    }

    @Override
    public int getCookTime() {
        return cookTime;
    }

    @Override
    public float getExperience() {
        return experience;
    }

    @Override
    public boolean matches(CookerTileEntity cooker, World level) {
        ItemStack candidate = cooker.getItem(CookerTileEntity.SLOT_INPUT);
        return input.sameItem(candidate);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack assemble(CookerTileEntity cooker) {
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
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.COOKING_RECIPE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeTypes.COOKING_RECIPE_TYPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CookingRecipe> {

        @Override
        public CookingRecipe fromJson(ResourceLocation id, JsonObject object) {
            Item in = getItem(JSONUtils.getAsString(object, "input"));
            JsonObject outJson = JSONUtils.getAsJsonObject(object, "output");
            ItemStack result = itemFromJson(outJson);
            int cookTime = JSONUtils.getAsInt(object, "cookTime", 200);
            float exp = JSONUtils.getAsFloat(object, "exp", 0.0F);
            return new CookingRecipe(id, in, result, exp, cookTime);
        }

        @Nullable
        @Override
        public CookingRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
            ResourceLocation inId = buffer.readResourceLocation();
            Item in = ForgeRegistries.ITEMS.getValue(inId);
            ItemStack out = buffer.readItem();
            int cooktime = buffer.readInt();
            float exp = buffer.readFloat();
            return new CookingRecipe(id, in.getItem(), out, exp, cooktime);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, CookingRecipe recipe) {
            buffer.writeResourceLocation(recipe.input.getItem().getRegistryName());
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.cookTime);
            buffer.writeFloat(recipe.experience);
        }

        private ItemStack itemFromJson(JsonObject object) {
            String itemId = JSONUtils.getAsString(object, "item");
            Item item = getItem(itemId);
            int count = JSONUtils.getAsInt(object, "count", 1);
            if (count < 1 || count > 64) {
                throw new JsonSyntaxException("Invalid item count! Count must be from <1; 64> interval");
            }
            return new ItemStack(item, count);
        }

        private Item getItem(String itemId) {
            ResourceLocation location = new ResourceLocation(itemId);
            Item item = ForgeRegistries.ITEMS.getValue(location);
            if (item == Items.AIR) {
                throw new JsonSyntaxException("Unknown item: " + itemId);
            }
            return item;
        }
    }
}
