package com.wf.firearms.item;



import com.wf.firearms.data.PlayerFirearmsData;

import com.wf.firearms.entity.GrenadeEntity;

import com.wf.firearms.grenade.GrenadeKind;

import com.wf.firearms.grenade.GrenadeStats;

import com.wf.firearms.registry.ModEntities;

import net.minecraft.ChatFormatting;

import net.minecraft.network.chat.Component;

import net.minecraft.sounds.SoundEvents;

import net.minecraft.sounds.SoundSource;

import net.minecraft.stats.Stats;

import net.minecraft.world.InteractionHand;

import net.minecraft.world.InteractionResultHolder;

import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.Item;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.TooltipFlag;

import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;



import java.util.List;



/** 右键即投掷（与雪球相同），避免拉弓/松手链路导致服务端不生成实体。 */

public class GrenadeItem extends Item {
    /** 比雪球(1.5)更短，模拟手雷偏重。 */
    private static final float BASE_THROW_POWER = 0.72f;
    private static final float GRENADIER_THROW_MULT = 1.4f;
    private static final float THROW_INACCURACY = 1.15f;

    private final GrenadeStats stats;



    public GrenadeItem(Properties props, GrenadeStats stats) {

        super(props);

        this.stats = stats;

    }



    public GrenadeStats stats() {

        return stats;

    }



    @Override

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {

            throwGrenade(stack, level, player);

            player.awardStat(Stats.ITEM_USED.get(this));

        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);

    }



    private void throwGrenade(ItemStack stack, Level level, Player player) {

        float power = BASE_THROW_POWER;

        if (PlayerFirearmsData.isUnlocked(player, "grenadier")) {

            power *= GRENADIER_THROW_MULT;

        }

        GrenadeEntity grenade =

                new GrenadeEntity(

                        ModEntities.GRENADE.get(),

                        level,

                        player,

                        stats.fuseTicks(),

                        stats.blastRadius(),

                        stats.explosionDamage(),

                        stats.explodeOnImpact(),

                        this);

        grenade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, power, THROW_INACCURACY);

        level.addFreshEntity(grenade);

        level.playSound(

                null,

                player.getX(),

                player.getY(),

                player.getZ(),

                SoundEvents.SNOWBALL_THROW,

                SoundSource.PLAYERS,

                0.5f,

                0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));

        if (!player.getAbilities().instabuild) {

            stack.shrink(1);

        }

        player.getCooldowns().addCooldown(this, 5);

    }



    @Override

    public void appendHoverText(

            ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {

        lines.add(

                Component.translatable("gunsrpg.grenade.tooltip.fuse", stats.fuseTicks() / 20)

                        .withStyle(ChatFormatting.DARK_GRAY));

        lines.add(

                Component.translatable("gunsrpg.grenade.tooltip.damage", (int) stats.explosionDamage())

                        .withStyle(ChatFormatting.DARK_GRAY));

        lines.add(

                Component.translatable("gunsrpg.grenade.tooltip.blast", String.format("%.1f", stats.blastRadius()))

                        .withStyle(ChatFormatting.DARK_GRAY));

        if (stats.kind() == GrenadeKind.FRAGMENTATION) {

            lines.add(

                    Component.translatable("gunsrpg.grenade.tooltip.fragmentation")

                            .withStyle(ChatFormatting.GRAY));

        } else if (stats.kind() == GrenadeKind.HEAVY) {

            lines.add(

                    Component.translatable("gunsrpg.grenade.tooltip.heavy").withStyle(ChatFormatting.GRAY));

        }

    }

}

