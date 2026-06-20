package com.wf.firearms.gameplay;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PlayerFirearmsData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 磨骨机产出计算；工作台骨头→骨粉一律拦截（骨粉只能在枪械台合成）。
 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BoneGrinderCraftHandler {
    private BoneGrinderCraftHandler() {}

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack crafted = event.getCrafting();
        if (crafted.isEmpty() || !crafted.is(Items.BONE_MEAL)) {
            return;
        }
        if (!craftedFromBoneIngredients(event.getInventory(), crafted.getCount())) {
            return;
        }

        int craftedCount = crafted.getCount();
        removeBoneMeal(player, craftedCount);
        refundConsumedBones(player, craftedCount);
        player.sendSystemMessage(
                Component.literal("§c[火器]§r 骨粉只能在「枪械台」制作，请先在技能树解锁「磨骨机 I」"),
                true);
    }

    public static int boneMealYield(Player player) {
        if (PlayerFirearmsData.isUnlocked(player, "bone_grinder_iii")) {
            return 8;
        }
        if (PlayerFirearmsData.isUnlocked(player, "bone_grinder_ii")) {
            return 5;
        }
        if (PlayerFirearmsData.isUnlocked(player, "bone_grinder_i")) {
            return 3;
        }
        return 0;
    }

    /** 工作台合成已消耗原料，按原版产出数量退回（勿复制合成格内剩余物品，否则会刷物品）。 */
    private static void refundConsumedBones(ServerPlayer player, int craftedCount) {
        ItemStack refund = ItemStack.EMPTY;
        if (craftedCount == 9) {
            refund = new ItemStack(Items.BONE_BLOCK);
        } else if (craftedCount > 0 && craftedCount % 3 == 0) {
            refund = new ItemStack(Items.BONE, craftedCount / 3);
        }
        if (refund.isEmpty()) {
            return;
        }
        if (!player.getInventory().add(refund)) {
            player.drop(refund, false);
        }
    }

    private static void removeBoneMeal(ServerPlayer player, int count) {
        int left = count;
        ItemStack carried = player.containerMenu.getCarried();
        if (!carried.isEmpty() && carried.is(Items.BONE_MEAL)) {
            int take = Math.min(left, carried.getCount());
            carried.shrink(take);
            left -= take;
            if (carried.isEmpty()) {
                player.containerMenu.setCarried(ItemStack.EMPTY);
            }
        }
        removeFromInventory(player, Items.BONE_MEAL, left);
    }

    private static boolean craftedFromBoneIngredients(Container inv, int craftedCount) {
        boolean hasBone = false;
        boolean hasBoneBlock = false;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(Items.BONE)) {
                hasBone = true;
            } else if (stack.is(Items.BONE_BLOCK)) {
                hasBoneBlock = true;
            } else if (!stack.is(Items.BONE_MEAL)) {
                return false;
            }
        }
        if (hasBone || hasBoneBlock) {
            return true;
        }
        return craftedCount == 1 || craftedCount == 3 || craftedCount == 5 || craftedCount == 8 || craftedCount == 9;
    }

    private static void removeFromInventory(ServerPlayer player, net.minecraft.world.item.Item item, int count) {
        int left = count;
        for (int i = 0; i < player.getInventory().getContainerSize() && left > 0; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                int take = Math.min(left, stack.getCount());
                stack.shrink(take);
                left -= take;
            }
        }
    }
}
