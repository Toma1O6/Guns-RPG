package dev.toma.gunsrpg.common.quests.reward;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

@FunctionalInterface
public interface IQuestItemProvider {

    ItemStack assembleItem(PlayerEntity player);

    class Impl implements IQuestItemProvider {

        private final Supplier<Item> itemSupplier;
        private final int count;
        private final IAssemblyFunction[] functions;

        public Impl(Supplier<Item> itemSupplier, int count, IAssemblyFunction[] functions) {
            this.itemSupplier = itemSupplier;
            this.count = count;
            this.functions = functions;
        }

        @Override
        public ItemStack assembleItem(PlayerEntity player) {
            ItemStack stack = new ItemStack(itemSupplier.get(), count);
            if (functions != null) {
                for (IAssemblyFunction function : functions) {
                    function.onAssembly(stack, player);
                }
            }
            return stack;
        }
    }
}
