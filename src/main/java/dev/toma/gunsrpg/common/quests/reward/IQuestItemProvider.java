package dev.toma.gunsrpg.common.quests.reward;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public interface IQuestItemProvider {

    int getWeight();

    ItemStack[] assembleItem(PlayerEntity player);

    class Impl implements IQuestItemProvider {

        private final Supplier<Item> itemSupplier;
        private final int count;
        private final int weight;
        private final IAssemblyFunction[] functions;

        public Impl(Supplier<Item> itemSupplier, int count, int weight, IAssemblyFunction[] functions) {
            this.itemSupplier = itemSupplier;
            this.count = count;
            this.weight = weight;
            this.functions = functions;
        }

        @Override
        public int getWeight() {
            return weight;
        }

        @Override
        public ItemStack[] assembleItem(PlayerEntity player) {
            ItemStack stack = new ItemStack(itemSupplier.get(), count);
            List<ItemStack> items = new ArrayList<>();
            if (functions != null) {
                for (IAssemblyFunction function : functions) {
                    items.addAll(Arrays.asList(function.onAssembly(stack, player)));
                }
            }
            if (items.isEmpty()) {
                items.add(stack);
            }
            return items.toArray(new ItemStack[0]);
        }
    }
}
