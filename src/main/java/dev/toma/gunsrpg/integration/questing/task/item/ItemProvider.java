package dev.toma.gunsrpg.integration.questing.task.item;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface ItemProvider {

    ItemProviderType<?> getType();

    List<ItemStack> getRequiredItems();
}
