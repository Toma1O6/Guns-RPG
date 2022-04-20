package dev.toma.gunsrpg.common.quests.reward;

import com.google.gson.*;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public interface IQuestRewardResolver {

    IQuestItemProvider resolve(JsonObject object, RewardProviderType type, int count, IAssemblyFunction function);

    class SingleItem implements IQuestRewardResolver {

        @Override
        public IQuestItemProvider resolve(JsonObject object, RewardProviderType type, int count, IAssemblyFunction function) {
            if (!object.has("item")) throw new JsonSyntaxException("Missing 'item' property");
            Item item = resolveItem(object.get("item"));
            return new IQuestItemProvider.Impl(() -> item, count, function);
        }

        public static Item resolveItem(JsonElement element) throws JsonParseException {
            ResourceLocation itemId = new ResourceLocation(element.getAsString());
            Item item = ForgeRegistries.ITEMS.getValue(itemId);
            if (item == null || item == Items.AIR) throw new JsonSyntaxException("Unknown item: " + itemId);
            return item;
        }
    }

    class ItemList implements IQuestRewardResolver {

        @Override
        public IQuestItemProvider resolve(JsonObject object, RewardProviderType type, int count, IAssemblyFunction function) {
            JsonArray array = JSONUtils.getAsJsonArray(object, "list");
            Item[] items = JsonHelper.deserializeInto(array, Item[]::new, SingleItem::resolveItem);
            Random random = new Random();
            return new IQuestItemProvider.Impl(() -> items[random.nextInt(items.length)], count, function);
        }
    }
}
