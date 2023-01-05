package dev.toma.gunsrpg.integration.questing.task.item;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.questing.utils.Codecs;
import dev.toma.questing.utils.Utils;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ListItemProvider implements ItemProvider {

    public static final Codec<ListItemProvider> CODEC = Codecs.SIMPLIFIED_ITEMSTACK.listOf()
            .xmap(ListItemProvider::new, prov -> prov.itemList).fieldOf("items").codec();

    private final List<ItemStack> itemList;

    public ListItemProvider(List<ItemStack> itemList) {
        this.itemList = itemList;
    }

    @Override
    public List<ItemStack> getRequiredItems() {
        return Utils.instantiate(this.itemList, ItemStack::copy);
    }

    @Override
    public ItemProviderType<?> getType() {
        return QuestRegistry.LIST_ITEM_PROVIDER;
    }
}
