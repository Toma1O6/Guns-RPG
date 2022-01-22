package dev.toma.gunsrpg.resource.cooking;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.common.init.ModRecipeSerializers;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.common.tileentity.CookerTileEntity;
import dev.toma.gunsrpg.resource.util.ResourceUtils;
import dev.toma.gunsrpg.resource.util.conditions.ConditionType;
import dev.toma.gunsrpg.resource.util.conditions.IRecipeCondition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CookingRecipe implements IRecipe<CookerTileEntity> {

    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack result;
    private final float experience;
    private final int cookTime;
    private final List<IRecipeCondition> conditions;

    public CookingRecipe(ResourceLocation id, Ingredient input, ItemStack result, float experience, int cookTime, List<IRecipeCondition> conditions) {
        this.id = id;
        this.input = input;
        this.result = result;
        this.experience = experience;
        this.cookTime = cookTime;
        this.conditions = conditions;
    }

    public int getCookTime() {
        return cookTime;
    }

    public float getExperience() {
        return experience;
    }

    @Override
    public boolean matches(CookerTileEntity cooker, World level) {
        return input.test(cooker.getItem(CookerTileEntity.SLOT_INPUT));
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
            JsonElement element = JSONUtils.getAsJsonObject(object, "input");
            Ingredient in = Ingredient.fromJson(element);
            JsonObject outJson = JSONUtils.getAsJsonObject(object, "output");
            ItemStack result = itemFromJson(outJson);
            int cookTime = JSONUtils.getAsInt(object, "cookTime", 200);
            List<IRecipeCondition> conditions = object.has("requirements") ? ResourceUtils.getConditionsFromJson(JSONUtils.getAsJsonArray(object, "requirements")) : Collections.emptyList();
            float exp = JSONUtils.getAsFloat(object, "exp", 0.0F);
            return new CookingRecipe(id, in, result, exp, cookTime, conditions);
        }

        @Nullable
        @Override
        public CookingRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
            Ingredient in = Ingredient.fromNetwork(buffer);
            ItemStack out = buffer.readItem();
            int cooktime = buffer.readInt();
            float exp = buffer.readFloat();
            byte limit = buffer.readByte();
            List<IRecipeCondition> list;
            if (limit == 0) {
                list = Collections.emptyList();
            } else {
                list = new ArrayList<>();
                for (int i = 0; i < limit; i++) {
                    list.add(ConditionType.fromNetwork(buffer));
                }
            }
            return new CookingRecipe(id, in, out, exp, cooktime, list);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, CookingRecipe recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.cookTime);
            buffer.writeFloat(recipe.experience);
            buffer.writeByte(recipe.conditions.size());
            for (IRecipeCondition condition : recipe.conditions) {
                ConditionType.toNetwork(buffer, condition);
            }
        }

        private ItemStack itemFromJson(JsonObject object) {
            String itemId = JSONUtils.getAsString(object, "item");
            ResourceLocation id = new ResourceLocation(itemId);
            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item == Items.AIR) {
                throw new JsonSyntaxException("Unknown item: " + itemId);
            }
            int count = JSONUtils.getAsInt(object, "count", 1);
            if (count < 1 || count > 64) {
                throw new JsonSyntaxException("Invalid item count! Count must be from <1; 64> interval");
            }
            return new ItemStack(item, count);
        }
    }
}
