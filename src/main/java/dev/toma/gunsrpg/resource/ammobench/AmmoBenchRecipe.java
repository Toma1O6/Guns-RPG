package dev.toma.gunsrpg.resource.ammobench;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.common.init.ModRecipeSerializers;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.common.tileentity.AmmoBenchTileEntity;
import dev.toma.gunsrpg.resource.MultiIngredient;
import dev.toma.gunsrpg.resource.util.ResourceUtils;
import dev.toma.gunsrpg.resource.util.conditions.ConditionType;
import dev.toma.gunsrpg.resource.util.conditions.IRecipeCondition;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AmmoBenchRecipe implements IRecipe<AmmoBenchTileEntity> {

    private final ResourceLocation recipeId;
    private final List<MultiIngredient> inputs;
    private final List<AmmoBenchOutput> outputs;
    private final int craftingTimer;
    private final List<IRecipeCondition> craftingConditions;

    public AmmoBenchRecipe(ResourceLocation recipeId, List<MultiIngredient> inputs, List<AmmoBenchOutput> outputs, int time, List<IRecipeCondition> craftingConditions) {
        this.recipeId = recipeId;
        this.inputs = inputs;
        this.outputs = outputs;
        this.craftingTimer = time;
        this.craftingConditions = craftingConditions;
    }

    @Override
    public boolean matches(AmmoBenchTileEntity tile, World world) {
        return MultiIngredient.test(tile, AmmoBenchTileEntity.SLOT_INPUTS, inputs);
    }

    @Override
    public ItemStack assemble(AmmoBenchTileEntity tile) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public boolean canPlayerCraft(PlayerEntity player) {
        for (IRecipeCondition condition : craftingConditions) {
            if (!condition.canCraft(player))
                return false;
        }
        return true;
    }

    public int getCraftingTimer() {
        return craftingTimer;
    }

    public List<MultiIngredient> getInputs() {
        return inputs;
    }

    public List<AmmoBenchOutput> getOutputs() {
        return outputs;
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeTypes.AMMO_BENCH_RECIPE_TYPE;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.AMMO_BENCH_RECIPE_SERIALIZER.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AmmoBenchRecipe> {

        @Override
        public AmmoBenchRecipe fromJson(ResourceLocation recipeId, JsonObject data) {
            JsonArray inputsJson = JSONUtils.getAsJsonArray(data, "inputs");
            if (inputsJson.size() > AmmoBenchTileEntity.SLOT_INPUTS.length) {
                throw new JsonSyntaxException(String.format("Too many recipe inputs for ammo bench recipe %s. Got %d inputs, limit is %d", recipeId, inputsJson.size(), AmmoBenchTileEntity.SLOT_INPUTS.length));
            }
            JsonArray outputsJson = JSONUtils.getAsJsonArray(data, "outputs");
            if (outputsJson.size() > AmmoBenchTileEntity.SLOT_OUTPUTS.length) {
                throw new JsonSyntaxException(String.format("Too many recipe outputs for ammo bench recipe %s. Got %d outputs, limit is %d", recipeId, outputsJson.size(), AmmoBenchTileEntity.SLOT_OUTPUTS.length));
            }
            JsonArray conditions = JSONUtils.getAsJsonArray(data, "requirements", new JsonArray());
            int timer = Math.max(1, JSONUtils.getAsInt(data, "craftingTimer", 100));
            List<MultiIngredient> ingredients = JsonHelper.deserialize(inputsJson, arr -> new ArrayList<>(arr.size()), MultiIngredient::parseJson, List::add);
            List<AmmoBenchOutput> outputs = JsonHelper.deserialize(outputsJson, arr -> new ArrayList<>(arr.size()), el -> AmmoBenchOutput.fromJson(el.getAsJsonObject()), List::add);
            List<IRecipeCondition> conditionList = ResourceUtils.getConditionsFromJson(conditions);
            return new AmmoBenchRecipe(recipeId, ingredients, outputs, timer, conditionList);
        }

        @Nullable
        @Override
        public AmmoBenchRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            int limit = buffer.readVarInt();
            List<MultiIngredient> inputs = new ArrayList<>();
            for (int i = 0; i < limit; i++) {
                inputs.add(MultiIngredient.decode(buffer));
            }
            limit = buffer.readVarInt();
            List<AmmoBenchOutput> outputs = new ArrayList<>();
            for (int i = 0; i < limit; i++) {
                outputs.add(AmmoBenchOutput.decode(buffer));
            }
            int timer = buffer.readInt();
            limit = buffer.readVarInt();
            List<IRecipeCondition> conditionList = limit != 0 ? new ArrayList<>() : Collections.emptyList();
            for (int i = 0; i < limit; i++) {
                conditionList.add(ConditionType.fromNetwork(buffer));
            }
            return new AmmoBenchRecipe(recipeId, inputs, outputs, timer, conditionList);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, AmmoBenchRecipe recipe) {
            buffer.writeVarInt(recipe.inputs.size());
            recipe.inputs.forEach(inp -> inp.encode(buffer));
            buffer.writeVarInt(recipe.outputs.size());
            recipe.outputs.forEach(out -> out.encode(buffer));
            buffer.writeInt(recipe.craftingTimer);
            buffer.writeVarInt(recipe.craftingConditions.size());
            recipe.craftingConditions.forEach(condition -> ConditionType.toNetwork(buffer, condition));
        }
    }

    public static final class AmmoBenchOutput {

        private final ItemStack baseResult;
        private final List<AmmoBenchOutputModifier> outputModifiers;

        public AmmoBenchOutput(ItemStack baseResult, List<AmmoBenchOutputModifier> outputModifiers) {
            this.baseResult = baseResult;
            this.outputModifiers = outputModifiers;
        }

        public ItemStack getItemStack() {
            return baseResult;
        }

        public List<AmmoBenchOutputModifier> getOutputModifiers() {
            return outputModifiers;
        }

        public static AmmoBenchOutput fromJson(JsonObject json) throws JsonParseException {
            ItemStack item = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "result"), true);
            JsonArray modifiersJson = JSONUtils.getAsJsonArray(json, "modifiers", new JsonArray());
            List<AmmoBenchOutputModifier> modifiers = JsonHelper.deserialize(modifiersJson, arr -> new ArrayList<>(arr.size()), AmmoBenchOutputModifier::parseJson, List::add);
            return new AmmoBenchOutput(item, modifiers);
        }

        public void encode(PacketBuffer buffer) {
            buffer.writeItem(baseResult);
            buffer.writeVarInt(outputModifiers.size());
            outputModifiers.forEach(mod -> mod.encode(buffer));
        }

        public static AmmoBenchOutput decode(PacketBuffer buffer) {
            ItemStack itemStack = buffer.readItem();
            int limit = buffer.readVarInt();
            List<AmmoBenchOutputModifier> modifiers = new ArrayList<>();
            for (int i = 0; i < limit; i++) {
                modifiers.add(AmmoBenchOutputModifier.decode(buffer));
            }
            return new AmmoBenchOutput(itemStack, modifiers);
        }
    }
}
