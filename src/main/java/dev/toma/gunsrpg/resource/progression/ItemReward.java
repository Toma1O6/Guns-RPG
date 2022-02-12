package dev.toma.gunsrpg.resource.progression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.resource.util.functions.RangedFunction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemReward implements ILevelReward {

    private final ItemStack stack;

    public ItemReward(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void applyTo(PlayerEntity player, IKillData data) {
        player.addItem(stack.copy());
    }

    public static class Adapter implements ILevelRewardAdapter<ItemReward> {

        @Override
        public ItemReward resolveFromJson(JsonObject object) throws JsonParseException {
            ResourceLocation location = new ResourceLocation(JSONUtils.getAsString(object, "item"));
            Item item = ForgeRegistries.ITEMS.getValue(location);
            if (item == Items.AIR) {
                throw new JsonSyntaxException("Unknown item: " + location);
            }
            int count = JSONUtils.getAsInt(object, "count", 1);
            if (!RangedFunction.BETWEEN_INCLUSIVE.isWithinRange(count, 1, 64)) {
                throw new JsonSyntaxException("Item count must be within <1;64> interval");
            }
            return new ItemReward(new ItemStack(item, count));
        }
    }
}
