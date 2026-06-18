package com.wf.firearms.airdrop;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** 绑定到某一玩家空投格子的容器视图（供 {@link AirdropMenu} 使用）。 */
public final class PlayerAirdropContainer implements Container {
    private final NonNullList<ItemStack> items;
    private final AirdropBlockEntity blockEntity;

    public PlayerAirdropContainer(AirdropBlockEntity blockEntity, NonNullList<ItemStack> items) {
        this.blockEntity = blockEntity;
        this.items = items;
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack result = ContainerHelper.removeItem(items, slot, count);
        if (!result.isEmpty()) {
            blockEntity.setChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        blockEntity.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    @Override
    public void clearContent() {
        items.clear();
        blockEntity.setChanged();
    }

    @Override
    public void setChanged() {
        blockEntity.setChanged();
    }
}
