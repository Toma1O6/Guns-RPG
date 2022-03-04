package dev.toma.gunsrpg.common.block;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;

import java.util.List;

public interface ICustomizableDrops {

    List<ItemStack> getCustomDrops(LootTable table, LootContext context);
}
