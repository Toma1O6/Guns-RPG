package com.wf.firearms.culinary;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.registry.ModRecipeSerializers;
import com.wf.firearms.registry.ModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/** 烹饪台专属配方（3×3 + 技能门控）。 */
public class CulinaryRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final int width;
    private final int height;
    private final ItemStack baseResult;
    private final String requiredSkill;

    public CulinaryRecipe(
            ResourceLocation id,
            NonNullList<Ingredient> ingredients,
            int width,
            int height,
            ItemStack baseResult,
            String requiredSkill) {
        this.id = id;
        this.ingredients = ingredients;
        this.width = width;
        this.height = height;
        this.baseResult = baseResult;
        this.requiredSkill = requiredSkill == null ? "" : requiredSkill;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        if (inv.getContainerSize() < 9) {
            return false;
        }
        for (int offsetX = 0; offsetX <= 3 - width; offsetX++) {
            for (int offsetY = 0; offsetY <= 3 - height; offsetY++) {
                if (matchesAt(inv, offsetX, offsetY, false) || matchesAt(inv, offsetX, offsetY, true)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesAt(Container inv, int offsetX, int offsetY, boolean mirror) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int recipeCol = col - offsetX;
                int recipeRow = row - offsetY;
                Ingredient required = Ingredient.EMPTY;
                if (recipeCol >= 0 && recipeCol < width && recipeRow >= 0 && recipeRow < height) {
                    int idx = mirror
                            ? (height - 1 - recipeRow) * width + (width - 1 - recipeCol)
                            : recipeRow * width + recipeCol;
                    required = ingredients.get(idx);
                }
                ItemStack inSlot = inv.getItem(row * 3 + col);
                if (!required.test(inSlot)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void consumeMatches(Container inv) {
        for (int offsetX = 0; offsetX <= 3 - width; offsetX++) {
            for (int offsetY = 0; offsetY <= 3 - height; offsetY++) {
                if (consumeAt(inv, offsetX, offsetY, false) || consumeAt(inv, offsetX, offsetY, true)) {
                    return;
                }
            }
        }
    }

    private boolean consumeAt(Container inv, int offsetX, int offsetY, boolean mirror) {
        if (!matchesAt(inv, offsetX, offsetY, mirror)) {
            return false;
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int recipeCol = col - offsetX;
                int recipeRow = row - offsetY;
                if (recipeCol < 0 || recipeCol >= width || recipeRow < 0 || recipeRow >= height) {
                    continue;
                }
                int idx = mirror
                        ? (height - 1 - recipeRow) * width + (width - 1 - recipeCol)
                        : recipeRow * width + recipeCol;
                if (!ingredients.get(idx).isEmpty()) {
                    int slot = row * 3 + col;
                    ItemStack stack = inv.getItem(slot);
                    stack.shrink(1);
                    inv.setItem(slot, stack.isEmpty() ? ItemStack.EMPTY : stack);
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(Container inv, RegistryAccess access) {
        return getResultItem(access).copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return baseResult.copy();
    }

    public ItemStack resultFor(@Nullable Player player) {
        if (player != null
                && !requiredSkill.isEmpty()
                && !PlayerFirearmsData.isUnlocked(player, requiredSkill)) {
            return ItemStack.EMPTY;
        }
        return baseResult.copy();
    }

    public String getRequiredSkill() {
        return requiredSkill;
    }

    public int getPatternWidth() {
        return width;
    }

    public int getPatternHeight() {
        return height;
    }

    public boolean canProduce(Player player) {
        return !resultFor(player).isEmpty();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(Container inv) {
        return NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CULINARY.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.CULINARY.get();
    }

    private static Map<Character, Ingredient> parseKey(JsonObject json) {
        Map<Character, Ingredient> map = new HashMap<>();
        for (String k : json.keySet()) {
            map.put(k.charAt(0), Ingredient.fromJson(json.get(k)));
        }
        return map;
    }

    private static NonNullList<Ingredient> patternToIngredients(
            String[] pattern, int width, int height, Map<Character, Ingredient> key) {
        NonNullList<Ingredient> list = NonNullList.withSize(width * height, Ingredient.EMPTY);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                char c = pattern[row].charAt(col);
                list.set(row * width + col, c == ' ' ? Ingredient.EMPTY : key.getOrDefault(c, Ingredient.EMPTY));
            }
        }
        return list;
    }

    private static String normalizeSkill(String raw) {
        if (raw == null || raw.isEmpty()) {
            return "";
        }
        int colon = raw.indexOf(':');
        return colon >= 0 ? raw.substring(colon + 1) : raw;
    }

    private static String skillFromJson(JsonObject json) {
        if (json.has("skill")) {
            return normalizeSkill(json.get("skill").getAsString());
        }
        if (json.has("requirements") && json.get("requirements").isJsonArray()) {
            for (var el : json.getAsJsonArray("requirements")) {
                if (!el.isJsonObject()) {
                    continue;
                }
                JsonObject req = el.getAsJsonObject();
                if (!"gunsrpg:skill".equals(req.has("type") ? req.get("type").getAsString() : "")) {
                    continue;
                }
                if (req.has("predicate") && req.getAsJsonObject("predicate").has("skill")) {
                    return normalizeSkill(req.getAsJsonObject("predicate").get("skill").getAsString());
                }
            }
        }
        return "";
    }

    public static class Serializer implements RecipeSerializer<CulinaryRecipe> {
        @Override
        public CulinaryRecipe fromJson(ResourceLocation id, JsonObject json) {
            NonNullList<Ingredient> ings;
            int w;
            int h;
            if (json.has("pattern")) {
                JsonArray pattern = json.getAsJsonArray("pattern");
                h = pattern.size();
                w = 0;
                for (int i = 0; i < h; i++) {
                    w = Math.max(w, pattern.get(i).getAsString().length());
                }
                Map<Character, Ingredient> key = parseKey(json.getAsJsonObject("key"));
                String[] rows = new String[h];
                for (int i = 0; i < h; i++) {
                    String row = pattern.get(i).getAsString();
                    if (row.length() < w) {
                        row = row + " ".repeat(w - row.length());
                    }
                    rows[i] = row;
                }
                ings = patternToIngredients(rows, w, h, key);
            } else {
                ings = NonNullList.of(Ingredient.EMPTY, Ingredient.fromJson(json.get("ingredient")));
                w = 1;
                h = 1;
            }
            ItemStack result = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("result"));
            return new CulinaryRecipe(id, ings, w, h, result, skillFromJson(json));
        }

        @Override
        public CulinaryRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int w = buf.readVarInt();
            int h = buf.readVarInt();
            int count = buf.readVarInt();
            NonNullList<Ingredient> ings = NonNullList.withSize(count, Ingredient.EMPTY);
            for (int i = 0; i < count; i++) {
                ings.set(i, Ingredient.fromNetwork(buf));
            }
            ItemStack result = buf.readItem();
            String skill = buf.readUtf();
            return new CulinaryRecipe(id, ings, w, h, result, skill);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CulinaryRecipe recipe) {
            buf.writeVarInt(recipe.width);
            buf.writeVarInt(recipe.height);
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ing : recipe.ingredients) {
                ing.toNetwork(buf);
            }
            buf.writeItem(recipe.baseResult);
            buf.writeUtf(recipe.requiredSkill);
        }
    }
}
