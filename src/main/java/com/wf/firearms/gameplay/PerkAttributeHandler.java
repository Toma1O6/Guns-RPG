package com.wf.firearms.gameplay;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PlayerFirearmsData;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/** 将天赋 Perk 应用到玩家属性（移速、挖掘等）。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PerkAttributeHandler {
    private static final UUID MOVE_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");

    private PerkAttributeHandler() {}

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            applyAttributes(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            applyAttributes(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        if (player.level().getGameTime() % 40 == 0) {
            applyAttributes(player);
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        double dig = PlayerFirearmsData.getPerkMultiplier(player, "mining_speed")
                + PlayerFirearmsData.getPerkMultiplier(player, "digging_speed")
                + PlayerFirearmsData.getPerkMultiplier(player, "woodcutting_speed");

        var state = event.getState();
        ItemStack tool = event.getEntity().getMainHandItem();
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            dig += MiningSkillService.heavyPickaxeSpeedBonus(MiningSkillService.heavyPickaxeTier(player));
        }
        if (state.is(Blocks.GRAVEL)) {
            dig += MiningSkillService.graveDiggerSpeedBonus(MiningSkillService.graveDiggerTier(player));
        }
        if (state.is(BlockTags.MINEABLE_WITH_AXE) && tool.isCorrectToolForDrops(state)) {
            dig += MiningSkillService.sharpAxeSpeedBonus(MiningSkillService.sharpAxeTier(player));
        }

        if (dig != 0.0) {
            event.setNewSpeed((float) (event.getNewSpeed() * (1.0 + dig)));
        }
    }

    public static void applyAttributes(Player player) {
        applyModifier(player, Attributes.MOVEMENT_SPEED, MOVE_UUID, PlayerFirearmsData.getPerkMultiplier(player, "movement_speed"));
        applyModifier(
                player,
                Attributes.ATTACK_SPEED,
                MELEE_UUID,
                PerkEffectService.meleeCooldownAttackSpeedBonus(player));
    }

    private static final UUID MELEE_UUID = UUID.fromString("d3e4f5a6-b7c8-4901-d234-56789abcdef1");

    private static void applyModifier(Player player, net.minecraft.world.entity.ai.attributes.Attribute attribute, UUID id, double bonus) {
        AttributeInstance inst = player.getAttribute(attribute);
        if (inst == null) {
            return;
        }
        inst.removeModifier(id);
        if (Math.abs(bonus) > 0.0001) {
            inst.addTransientModifier(
                    new AttributeModifier(id, "gunsrpg_perk", bonus, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }
}
