package dev.toma.gunsrpg.common.quests.reward;

import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public final class Reward {

    private final Item[] items;
    private final int count;
    @Nullable
    private final CompoundNBT data;

    private Reward(Item[] items, int count, @Nullable CompoundNBT data) {
        this.items = items;
        this.count = count;
        this.data = data;
    }

    public static Reward newAward(Item[] items, int count, @Nullable CompoundNBT data) {
        return new Reward(items, count, data);
    }

    public void awardTo(PlayerEntity player) {
        int toDistribute = count;
        Item item = ModUtils.randomElement(items);
        int max = Math.min(player.inventory.getMaxStackSize(), item.getMaxStackSize());
        while (toDistribute > 0) {
            int award = Math.min(toDistribute, max);
            ItemStack stack = new ItemStack(item, award);
            if (data != null) {
                stack.setTag(data.copy());
            }
            toDistribute -= award;
        }
    }
}
