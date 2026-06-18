package com.wf.firearms.debuff;

import com.wf.firearms.GunsRpg;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/** 骨折：移速与攻击速度惩罚（属性修正，比纯药水更稳定）。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FractureModifiers {
    private static final UUID MOVE_UUID =
            UUID.fromString("c3d4e5f6-a7b8-9012-cdef-345678901234");
    private static final UUID ATTACK_UUID =
            UUID.fromString("d4e5f6a7-b8c9-0123-def0-456789012345");

    private FractureModifiers() {}

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            sync(player);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            sync(player);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        if (player.level().getGameTime() % 20 == 0) {
            sync(player);
        }
    }

    public static void sync(ServerPlayer player) {
        if (!PlayerDebuffData.hasFracture(player)) {
            clear(player);
            return;
        }
        var rules = DebuffConfig.fracture();
        applyPenalty(player, Attributes.MOVEMENT_SPEED, MOVE_UUID, rules.movementSpeedPenalty());
        applyPenalty(player, Attributes.ATTACK_SPEED, ATTACK_UUID, rules.attackSpeedPenalty());
    }

    public static void clear(ServerPlayer player) {
        remove(player, Attributes.MOVEMENT_SPEED, MOVE_UUID);
        remove(player, Attributes.ATTACK_SPEED, ATTACK_UUID);
    }

    private static void applyPenalty(
            ServerPlayer player,
            net.minecraft.world.entity.ai.attributes.Attribute attribute,
            UUID id,
            double penalty) {
        AttributeInstance inst = player.getAttribute(attribute);
        if (inst == null || penalty <= 0) {
            return;
        }
        inst.removeModifier(id);
        double amount = -Math.min(0.95, penalty);
        inst.addTransientModifier(
                new AttributeModifier(id, "wf_fracture", amount, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    private static void remove(
            ServerPlayer player,
            net.minecraft.world.entity.ai.attributes.Attribute attribute,
            UUID id) {
        AttributeInstance inst = player.getAttribute(attribute);
        if (inst != null) {
            inst.removeModifier(id);
        }
    }
}
