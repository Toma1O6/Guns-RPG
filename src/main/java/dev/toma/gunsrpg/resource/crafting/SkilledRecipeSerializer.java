package dev.toma.gunsrpg.resource.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.resource.util.ResourceUtils;
import dev.toma.gunsrpg.resource.util.conditions.ConditionType;
import dev.toma.gunsrpg.resource.util.conditions.IRecipeCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.*;

public abstract class SkilledRecipeSerializer<R extends SkilledRecipe<?>> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<R> {

    public abstract R createRecipeInstance(ResourceLocation id, int width, int height, NonNullList<Ingredient> ingredients, ItemStack output, OutputModifier modifier, List<IRecipeCondition> conditionList);

    @Override
    public final R fromJson(ResourceLocation id, JsonObject data) {
        Map<String, Ingredient> ingredientKeys = parseIngredientKeys(JSONUtils.getAsJsonObject(data, "key"));
        String[] optimizedPattern = minimize(parsePattern(JSONUtils.getAsJsonArray(data, "pattern")));
        int width = optimizedPattern[0].length();
        int height = optimizedPattern.length;
        NonNullList<Ingredient> ingredientList = processPatternAndMatch(optimizedPattern, ingredientKeys, width, height);
        ItemStack output = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(data, "result"), true);
        List<IRecipeCondition> required = data.has("requirements") ? ResourceUtils.getConditionsFromJson(JSONUtils.getAsJsonArray(data, "requirements")) : Collections.emptyList();
        OutputModifier modifier = null;
        if (data.has("outputModifier")) {
            modifier = OutputModifier.fromJson(JSONUtils.getAsJsonObject(data, "outputModifier"));
        }
        return createRecipeInstance(id, width, height, ingredientList, output, modifier, required);
    }

    @Nullable
    @Override
    public final R fromNetwork(ResourceLocation id, PacketBuffer buffer) {
        int width = buffer.readVarInt();
        int height = buffer.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.set(i, Ingredient.fromNetwork(buffer));
        }
        ItemStack out = buffer.readItem();
        boolean outputModifier = buffer.readBoolean();
        OutputModifier modifier = null;
        if (outputModifier)
            modifier = OutputModifier.decode(buffer);
        int conditionCount = buffer.readVarInt();
        List<IRecipeCondition> conditions;
        if (conditionCount == 0) {
            conditions = Collections.emptyList();
        } else {
            conditions = new ArrayList<>(conditionCount);
            for (int i = 0; i < conditionCount; i++) {
                conditions.add(ConditionType.fromNetwork(buffer));
            }
        }
        return createRecipeInstance(id, width, height, ingredients, out, modifier, conditions);
    }

    @Override
    public final void toNetwork(PacketBuffer buffer, R recipe) {
        buffer.writeVarInt(recipe.getWidth());
        buffer.writeVarInt(recipe.getHeight());
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buffer);
        }
        buffer.writeItem(recipe.getResultItem());
        boolean outputModifier = recipe.getOutputModifier() != null;
        buffer.writeBoolean(outputModifier);
        if (outputModifier)
            recipe.getOutputModifier().encode(buffer);
        buffer.writeVarInt(recipe.listConditions().size());
        for (IRecipeCondition craftingCondition : recipe.listConditions()) {
            ConditionType.toNetwork(buffer, craftingCondition);
        }
    }

    private static NonNullList<Ingredient> processPatternAndMatch(String[] pattern, Map<String, Ingredient> mappings, int width, int height) {
        NonNullList<Ingredient> list = NonNullList.withSize(width * height, Ingredient.EMPTY);
        Set<String> keySet = new HashSet<>(mappings.keySet());
        keySet.remove(" ");

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String key = pattern[y].substring(x, x + 1);
                Ingredient ingredient = mappings.get(key);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + key + "' but it's not defined in the file.");
                }
                keySet.remove(key);
                list.set(x + width * y, ingredient);
            }
        }
        if (!keySet.isEmpty()) {
            throw new JsonSyntaxException("Found unused key references: " + keySet);
        } else {
            return list;
        }
    }

    private static Map<String, Ingredient> parseIngredientKeys(JsonObject keyObj) {
        Map<String, Ingredient> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : keyObj.entrySet()) {
            String key = entry.getKey();
            if (key.length() != 1) {
                throw new JsonSyntaxException("Invalid key: '" + key + "' is an invalid syntax (only single character keys are allowed).");
            }
            if (key.equals(" ")) {
                throw new JsonSyntaxException("Invalid key: whitespace is a reserved symbol.");
            }
            map.put(key, Ingredient.fromJson(entry.getValue()));
        }
        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    private static String[] parsePattern(JsonArray array) {
        String[] pattern = new String[array.size()];
        if (pattern.length > SkilledRecipe.GRID_SIZE) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + SkilledRecipe.GRID_SIZE + " is maximum.");
        } else if (pattern.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed.");
        } else {
            for (int i = 0; i < pattern.length; i++) {
                String patternLine = JSONUtils.convertToString(array.get(i), "pattern[" + i + "]");
                if (patternLine.length() > SkilledRecipe.GRID_SIZE) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + SkilledRecipe.GRID_SIZE + " is maximum.");
                }
                if (i > 0 && pattern[0].length() != patternLine.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }
                pattern[i] = patternLine;
            }
            return pattern;
        }
    }

    private static String[] minimize(String... lines) {
        int leftTrim = Integer.MAX_VALUE;
        int rightTrim = 0;
        int k = 0;
        int l = 0;
        for (int index = 0; index < lines.length; ++index) {
            String line = lines[index];
            leftTrim = Math.min(leftTrim, firstNonWhitespaceChar(line));
            int lastNonWhitespace = lastNonWhitespaceChar(line);
            rightTrim = Math.max(rightTrim, lastNonWhitespace);
            if (lastNonWhitespace < 0) {
                if (k == index) {
                    ++k;
                }
                ++l;
            } else {
                l = 0;
            }
        }
        if (lines.length == l) {
            return new String[0];
        }
        String[] newLines = new String[lines.length - l - k];
        for (int i = 0; i < newLines.length; ++i) {
            newLines[i] = lines[i + k].substring(leftTrim, rightTrim + 1);
        }
        return newLines;
    }

    private static int firstNonWhitespaceChar(String string) {
        int pos = 0;
        while (pos < string.length() && string.charAt(pos) == ' ') {
            ++pos;
        }
        return pos;
    }

    private static int lastNonWhitespaceChar(String string) {
        int pos;
        for (pos = string.length() - 1; pos >= 0 && string.charAt(pos) == ' '; --pos) {}
        return pos;
    }
}
