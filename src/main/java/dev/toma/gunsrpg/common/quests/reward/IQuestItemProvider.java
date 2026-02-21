package dev.toma.gunsrpg.common.quests.reward;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
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
            List<ItemStack> items = new ArrayList<>();
            Item item = this.itemSupplier.get();
            int remainderAmount = this.count;
            while (remainderAmount > 0) {
                int take = Math.min(remainderAmount, item.getMaxStackSize());
                ItemStack itemStack = new ItemStack(item, take);
                boolean addToOutput = true;
                if (this.functions != null) {
                    for (IAssemblyFunction function : this.functions) {
                        if (!function.onAssembly(itemStack, player, items)) {
                            addToOutput = false;
                        }
                    }
                }
                if (addToOutput) {
                    items.add(0, itemStack);
                }
                remainderAmount -= take;
            }
            if (items.isEmpty()) {
                items.add(item.getDefaultInstance());
            }
            return items.toArray(new ItemStack[0]);
        }

        public int getCount() {
            return count;
        }

        public Item getItem() {
            return itemSupplier.get();
        }

        public int getFunctionCount() {
            return functions != null ? functions.length : 0;
        }
    }
}
