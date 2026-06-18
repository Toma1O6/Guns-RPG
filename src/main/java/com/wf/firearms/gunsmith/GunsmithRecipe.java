package com.wf.firearms.gunsmith;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.WeaponMapping;
import com.wf.firearms.gameplay.AmmoCraftBonusService;
import com.wf.firearms.gameplay.BoneGrinderCraftHandler;
import com.wf.firearms.gameplay.MiningSkillService;
import com.wf.firearms.registry.ModRecipeSerializers;
import com.wf.firearms.registry.ModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/** 枪械台专属配方（3×3 + 技能门控，对标 Guns RPG smithing_table）。 */
public class GunsmithRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final int width;
    private final int height;
    private final ItemStack baseResult;
    private final String requiredSkill;
    private final boolean boneMealRecipe;
    private final boolean shapeless;

    public GunsmithRecipe(
            ResourceLocation id,
            NonNullList<Ingredient> ingredients,
            int width,
            int height,
            ItemStack baseResult,
            String requiredSkill,
            boolean shapeless) {
        this.id = id;
        this.ingredients = ingredients;
        this.width = width;
        this.height = height;
        this.baseResult = baseResult;
        this.requiredSkill = requiredSkill == null ? "" : requiredSkill;
        this.boneMealRecipe = baseResult.is(net.minecraft.world.item.Items.BONE_MEAL);
        this.shapeless = shapeless;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        if (shapeless) {
            return matchesShapeless(inv);
        }
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

    public boolean isShapeless() {
        return shapeless;
    }

    private boolean matchesShapeless(Container inv) {
        boolean[] used = new boolean[Math.min(9, inv.getContainerSize())];
        for (Ingredient required : ingredients) {
            if (required.isEmpty()) {
                continue;
            }
            boolean found = false;
            for (int slot = 0; slot < used.length; slot++) {
                if (used[slot]) {
                    continue;
                }
                ItemStack stack = inv.getItem(slot);
                if (!stack.isEmpty() && required.test(stack)) {
                    used[slot] = true;
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private void consumeShapeless(Container inv) {
        for (Ingredient required : ingredients) {
            if (required.isEmpty()) {
                continue;
            }
            for (int slot = 0; slot < Math.min(9, inv.getContainerSize()); slot++) {
                ItemStack stack = inv.getItem(slot);
                if (!stack.isEmpty() && required.test(stack)) {
                    stack.shrink(1);
                    inv.setItem(slot, stack.isEmpty() ? ItemStack.EMPTY : stack);
                    break;
                }
            }
        }
    }

    private boolean matchesAt(Container inv, int offsetX, int offsetY, boolean mirror) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int recipeCol = col - offsetX;
                int recipeRow = row - offsetY;
                Ingredient required = Ingredient.EMPTY;
                if (recipeCol >= 0 && recipeCol < width && recipeRow >= 0 && recipeRow < height) {
                    int idx = mirror ? (height - 1 - recipeRow) * width + (width - 1 - recipeCol) : recipeRow * width + recipeCol;
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
        if (shapeless) {
            consumeShapeless(inv);
            return;
        }
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
                int idx = mirror ? (height - 1 - recipeRow) * width + (width - 1 - recipeCol) : recipeRow * width + recipeCol;
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
        return mapResultStack(baseResult.copy());
    }

    public ItemStack resultFor(@Nullable Player player) {
        if (player == null) {
            return mapResultStack(baseResult.copy());
        }
        if (boneMealRecipe) {
            int perUnit = BoneGrinderCraftHandler.boneMealYield(player);
            if (perUnit <= 0) {
                return ItemStack.EMPTY;
            }
            int total = perUnit * Math.max(1, baseResult.getCount());
            return new ItemStack(baseResult.getItem(), total);
        }
        if (!requiredSkill.isEmpty() && !PlayerFirearmsData.isUnlocked(player, requiredSkill)) {
            return ItemStack.EMPTY;
        }
        if (baseResult.is(Items.GUNPOWDER)) {
            int yield = MiningSkillService.gunpowderYield(player);
            if (yield <= 0) {
                return ItemStack.EMPTY;
            }
            return new ItemStack(Items.GUNPOWDER, yield);
        }
        ItemStack out = mapResultStack(baseResult.copy());
        int bonus = AmmoCraftBonusService.bonusOutput(player, out);
        if (bonus > 0) {
            out.grow(bonus);
        }
        return out;
    }

    /** CGM 模式下将 {@code gunsrpg:akm} 等产出映射为 TaCZ / 自研物品。 */
    private static ItemStack mapResultStack(ItemStack stack) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id == null || !GunsRpg.MOD_ID.equals(id.getNamespace())) {
            return stack;
        }
        return WeaponMapping.itemIdForWeapon(id.getPath())
                .filter(mapped -> !mapped.equals(id.toString()))
                .map(mapped -> {
                    ItemStack out = WeaponMapping.stackForWeapon(id.getPath());
                    out.setCount(stack.getCount());
                    return out;
                })
                .orElse(stack);
    }

    public int getPatternWidth() {
        return width;
    }

    public int getPatternHeight() {
        return height;
    }

    public String getRequiredSkill() {
        return requiredSkill;
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
        return ModRecipeSerializers.GUNSMITH.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.GUNSMITH.get();
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

    public static class Serializer implements RecipeSerializer<GunsmithRecipe> {
        @Override
        public GunsmithRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack result = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("result"));
            String skill = json.has("skill") ? json.get("skill").getAsString() : "";
            if (json.has("ingredients")) {
                JsonArray arr = json.getAsJsonArray("ingredients");
                NonNullList<Ingredient> shapelessIngs = NonNullList.create();
                for (JsonElement el : arr) {
                    shapelessIngs.add(Ingredient.fromJson(el));
                }
                return new GunsmithRecipe(id, shapelessIngs, 1, 1, result, skill, true);
            }
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
            return new GunsmithRecipe(id, ings, w, h, result, skill, false);
        }

        @Override
        public GunsmithRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int w = buf.readVarInt();
            int h = buf.readVarInt();
            int count = buf.readVarInt();
            NonNullList<Ingredient> ings = NonNullList.withSize(count, Ingredient.EMPTY);
            for (int i = 0; i < count; i++) {
                ings.set(i, Ingredient.fromNetwork(buf));
            }
            ItemStack result = buf.readItem();
            String skill = buf.readUtf();
            boolean shapeless = buf.readBoolean();
            return new GunsmithRecipe(id, ings, w, h, result, skill, shapeless);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GunsmithRecipe recipe) {
            buf.writeVarInt(recipe.width);
            buf.writeVarInt(recipe.height);
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ing : recipe.ingredients) {
                ing.toNetwork(buf);
            }
            buf.writeItem(recipe.baseResult);
            buf.writeUtf(recipe.requiredSkill);
            buf.writeBoolean(recipe.shapeless);
        }
    }
}
