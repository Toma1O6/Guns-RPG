package com.wf.firearms.item;

import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.network.FirearmsProgressSyncPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** 右键使用给予 1 点进度货币（技能点 / 属性点 / 共享武器扩展点）。 */
public class PointAwardItem extends Item {
    public enum PointKind {
        SKILL((player, amount) -> PlayerFirearmsData.addSkillPoints(player, amount)),
        PERK((player, amount) -> PlayerFirearmsData.addPerkPoints(player, amount)),
        WEAPON((player, amount) -> PlayerFirearmsData.addSharedWeaponPoints(player, amount));

        private final PointApplier applier;

        PointKind(PointApplier applier) {
            this.applier = applier;
        }

        void apply(Player player, int amount) {
            applier.apply(player, amount);
        }

        @FunctionalInterface
        private interface PointApplier {
            void apply(Player player, int amount);
        }
    }

    private final PointKind kind;

    public PointAwardItem(PointKind kind, Properties properties) {
        super(properties);
        this.kind = kind;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            kind.apply(player, 1);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.PLAYER_LEVELUP,
                    SoundSource.PLAYERS,
                    0.75F,
                    1.0F);
            player.displayClientMessage(Component.translatable("point.award.success"), true);
            if (player instanceof ServerPlayer serverPlayer) {
                FirearmsProgressSyncPacket.sendTo(serverPlayer);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
