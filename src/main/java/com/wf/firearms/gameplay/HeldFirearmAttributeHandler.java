package com.wf.firearms.gameplay;

import com.wf.firearms.GunsRpg;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/** 持枪移速修正（手枪加速、狙击/重机枪减速）。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class HeldFirearmAttributeHandler {
    private static final UUID HELD_MOVE_UUID = UUID.fromString("b2c3d4e5-f6a7-8901-bcde-f12345678901");

    private HeldFirearmAttributeHandler() {}

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            applyHeldMoveSpeed(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            applyHeldMoveSpeed(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        if (player.level().getGameTime() % 5 == 0) {
            applyHeldMoveSpeed(player);
        }
    }

    public static void applyHeldMoveSpeed(Player player) {
        AttributeInstance inst = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (inst == null) {
            return;
        }
        inst.removeModifier(HELD_MOVE_UUID);
        String key = GunKillHandler.heldFirearmKey(player);
        if (key == null) {
            return;
        }
        float bonus = heldMoveSpeedBonus(key);
        if (player != null) {
            bonus += com.wf.firearms.gameplay.WeaponExtensionCombat.heldMoveSpeedBonus(player, key);
        }
        if (Math.abs(bonus) > 0.0001f) {
            inst.addTransientModifier(
                    new AttributeModifier(HELD_MOVE_UUID, "wf_held_firearm", bonus, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    /** 内联 {@link com.wf.firearms.combat.WeaponBalance#heldMoveSpeedBonus}，避免部分环境下该类无法加载导致玩家 tick 崩溃。 */
    private static float heldMoveSpeedBonus(String weaponKey) {
        return switch (weaponKey) {
            case "m1911" -> 0.20f;
            case "r45" -> 0.15f;
            case "desert_eagle" -> 0.10f;
            case "awm" -> -0.20f;
            case "pkm" -> -0.07f;
            case "m249" -> -0.12f;
            case "gatling", "minigun" -> -0.70f;
            default -> 0f;
        };
    }
}
