package dev.toma.gunsrpg.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class MultiIngredient {

    private final Ingredient ingredient;
    private final int count;

    public MultiIngredient(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    public static boolean test(IInventory inventory, int[] slots, List<MultiIngredient> ingredients) {
        for (int slot : slots) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty())
                continue;
            boolean validItemStack = false;
            for (MultiIngredient ingredient : ingredients) {
                if (ingredient.ingredient.test(stack)) {
                    validItemStack = true;
                }
            }
            if (!validItemStack) {
                return false;
            }
        }
        for (MultiIngredient ingredient : ingredients) {
            if (!ingredient.test(inventory, slots)) {
                return false;
            }
        }
        return true;
    }

    public static MultiIngredient parseJson(JsonElement element) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(element);
        JsonObject ingredientJson = JSONUtils.getAsJsonObject(object, "ingredient");
        Ingredient ingredient = Ingredient.fromJson(ingredientJson);
        int count = JSONUtils.getAsInt(object, "count", 1);
        return new MultiIngredient(ingredient, count);
    }

    public void encode(PacketBuffer buffer) {
        ingredient.toNetwork(buffer);
        buffer.writeInt(count);
    }

    public static MultiIngredient decode(PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        int count = buffer.readInt();
        return new MultiIngredient(ingredient, count);
    }

    public boolean test(IInventory container, int[] slots) {
        return test(container, slots, false, IInventory::getItem);
    }

    public boolean test(IItemHandler itemHandler, int[] slots) {
        return test(itemHandler, slots, false, IItemHandler::getStackInSlot);
    }

    private <C> boolean test(C container, int[] slots, boolean ignoreCount, BiFunction<C, Integer, ItemStack> itemProvider) {
        int remaining = count;
        for (int slot : slots) {
            ItemStack stack = itemProvider.apply(container, slot);
            if (ingredient.test(stack)) {
                if (ignoreCount)
                    return true;
                remaining -= stack.getCount();
                if (remaining <= 0)
                    break;
            }
        }
        return remaining <= 0;
    }

    public void consume(IInventory inventory, int[] slots) {
        consume(inventory, slots, IInventory::getItem);
    }

    public void consume(IItemHandler itemHandler, int[] slots) {
        consume(itemHandler, slots, IItemHandler::getStackInSlot);
    }

    private <C> void consume(C container, int[] slots, BiFunction<C, Integer, ItemStack> itemProvider) {
        int remaining = count;
        for (int slot : slots) {
            ItemStack stack = itemProvider.apply(container, slot);
            if (ingredient.test(stack)) {
                int toConsume = Math.min(remaining, stack.getCount());
                stack.shrink(toConsume);
                remaining -= toConsume;
                if (remaining <= 0) {
                    break;
                }
            }
        }
    }

    public void consume(List<ItemStack> list) {
        int remaining = count;
        for (ItemStack stack : list) {
            if (ingredient.test(stack)) {
                int toConsume = Math.min(remaining, stack.getCount());
                stack.shrink(toConsume);
                remaining -= toConsume;
                if (remaining <= 0) {
                    break;
                }
            }
        }
    }

    public List<ItemStack> getItemStackList() {
        List<ItemStack> list = new ArrayList<>();
        for (ItemStack stack : ingredient.getItems()) {
            ItemStack itemStack = stack.copy();
            itemStack.setCount(count);
            list.add(itemStack);
        }
        return list;
    }
}
