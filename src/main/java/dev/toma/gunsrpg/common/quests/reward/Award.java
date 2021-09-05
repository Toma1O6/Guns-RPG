package dev.toma.gunsrpg.common.quests.reward;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public final class Award {

    private final Item item;
    private final int count;
    @Nullable
    private final CompoundNBT data;

    private Award(Item item, int count, @Nullable CompoundNBT data) {
        this.item = item;
        this.count = count;
        this.data = data;
    }

    public static Award newAward(Item item, int count, @Nullable CompoundNBT data) {
        return new Award(item, count, data);
    }

    public static Award newAward(Item item, int count) {
        return newAward(item, count, null);
    }

    public void awardTo(PlayerEntity player) {
        int toDistribute = count;
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
