package dev.toma.gunsrpg.resource.smithing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.common.init.ModRecipeSerializers;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import dev.toma.gunsrpg.resource.util.ResourceUtils;
import dev.toma.gunsrpg.resource.util.conditions.ConditionType;
import dev.toma.gunsrpg.resource.util.conditions.IRecipeCondition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class SmithingRecipe implements IRecipe<SmithingTableTileEntity> {

    public static final int GRID_SIZE = 3;

    private final NonNullList<Ingredient> ingredientList;
    private final List<IRecipeCondition> conditions;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int width;
    private final int height;

    public SmithingRecipe(ResourceLocation id, int width, int height, NonNullList<Ingredient> ingredientList, ItemStack output, List<IRecipeCondition> conditions) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.ingredientList = ingredientList;
        this.output = output;
        this.conditions = conditions;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.SMITHING_RECIPE_SERIALIZER.get();
    }

    @Override
    public ItemStack getResultItem() {
        return output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredientList;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    @Override
    public boolean matches(SmithingTableTileEntity inventory, World world) {
        for (int x = 0; x <= GRID_SIZE - width; x++) {
            for (int y = 0; y <= GRID_SIZE - height; y++) {
                if (matchesGrid(inventory, x, y, true)) {
                    return true;
                }
                if (matchesGrid(inventory, x, y, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(SmithingTableTileEntity inventory) {
        return getResultItem().copy();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeTypes.SMITHING_RECIPE_TYPE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean canCraft(PlayerEntity player) {
        for (IRecipeCondition condition : conditions) {
            if (!condition.canCraft(player))
                return false;
        }
        return true;
    }

    public List<IRecipeCondition> getFailedChecks(PlayerEntity player) {
        return conditions.stream().filter(condition -> !condition.canCraft(player)).collect(Collectors.toList());
    }

    private boolean matchesGrid(SmithingTableTileEntity inventory, int right, int top, boolean bool) {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                int xPos = x - right;
                int yPos = y - top;
                Ingredient ingredient = Ingredient.EMPTY;
                if (xPos >= 0 && yPos >= 0 && xPos < width && yPos < height) {
                    if (bool) {
                        ingredient = ingredientList.get(width - xPos - 1 + yPos * width);
                    } else {
                        ingredient = ingredientList.get(xPos + yPos * width);
                    }
                }
                if (!ingredient.test(inventory.getItem(x + y * GRID_SIZE))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SmithingRecipe> {

        @Override
        public SmithingRecipe fromJson(ResourceLocation id, JsonObject data) {
            Map<String, Ingredient> ingredientKeys = parseIngredientKeys(JSONUtils.getAsJsonObject(data, "key"));
            String[] optimizedPattern = minimize(parsePattern(JSONUtils.getAsJsonArray(data, "pattern")));
            int width = optimizedPattern[0].length();
            int height = optimizedPattern.length;
            NonNullList<Ingredient> ingredientList = processPatternAndMatch(optimizedPattern, ingredientKeys, width, height);
            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(data, "result"), true);
            List<IRecipeCondition> required = data.has("requirements") ? ResourceUtils.getConditionsFromJson(JSONUtils.getAsJsonArray(data, "requirements")) : Collections.emptyList();
            return new SmithingRecipe(id, width, height, ingredientList, output, required);
        }

        @Nullable
        @Override
        public SmithingRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
            int width = buffer.readVarInt();
            int height = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.fromNetwork(buffer));
            }
            ItemStack out = buffer.readItem();
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
            return new SmithingRecipe(id, width, height, ingredients, out, conditions);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, SmithingRecipe recipe) {
            buffer.writeVarInt(recipe.width);
            buffer.writeVarInt(recipe.height);
            for (Ingredient ingredient : recipe.ingredientList) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(recipe.output);
            buffer.writeVarInt(recipe.conditions.size());
            for (IRecipeCondition craftingCondition : recipe.conditions) {
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
            if (pattern.length > GRID_SIZE) {
                throw new JsonSyntaxException("Invalid pattern: too many rows, " + GRID_SIZE + " is maximum.");
            } else if (pattern.length == 0) {
                throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed.");
            } else {
                for (int i = 0; i < pattern.length; i++) {
                    String patternLine = JSONUtils.convertToString(array.get(i), "pattern[" + i + "]");
                    if (patternLine.length() > GRID_SIZE) {
                        throw new JsonSyntaxException("Invalid pattern: too many columns, " + GRID_SIZE + " is maximum.");
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
}
