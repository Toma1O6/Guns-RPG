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
        removeFromInventory(player, Items.BONE_MEAL, craftedCount);
        refundBones(player, event.getInventory());
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

    private static void refundBones(ServerPlayer player, Container inv) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(Items.BONE) || stack.is(Items.BONE_BLOCK)) {
                player.getInventory().add(stack.copy());
            }
        }
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
