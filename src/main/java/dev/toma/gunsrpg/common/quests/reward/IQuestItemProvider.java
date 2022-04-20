package dev.toma.gunsrpg.common.quests.reward;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

@FunctionalInterface
public interface IQuestItemProvider {

    ItemStack assembleItem();

    class Impl implements IQuestItemProvider {

        private final Supplier<Item> itemSupplier;
        private final int count;
        private final IAssemblyFunction function;

        public Impl(Supplier<Item> itemSupplier, int count, IAssemblyFunction function) {
            this.itemSupplier = itemSupplier;
            this.count = count;
            this.function = function;
        }

        @Override
        public ItemStack assembleItem() {
            ItemStack stack = new ItemStack(itemSupplier.get(), count);
            if (function != null) {
                function.onAssembly(stack);
            }
            return stack;
        }
    }
}
