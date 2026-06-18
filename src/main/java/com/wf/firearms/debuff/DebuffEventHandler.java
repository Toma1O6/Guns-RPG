package com.wf.firearms.debuff;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.combat.ModDamageTypes;
import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.debuff.DebuffSyncService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DebuffEventHandler {
    private DebuffEventHandler() {}

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            DebuffSyncService.syncFull(player);
        }
    }

    /** 原版：玩家死亡时 {@code clearActive()}，重生不带 debuff。 */
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            clearDebuffsOnDeath(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath() && event.getEntity() instanceof ServerPlayer player) {
            clearDebuffsOnDeath(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            DebuffSyncService.syncFull(player);
        }
    }

    private static void clearDebuffsOnDeath(ServerPlayer player) {
        for (DebuffType type : DebuffType.values()) {
            PlayerDebuffData.clear(player, type);
        }
        FractureModifiers.clear(player);
        DebuffSyncService.syncFull(player);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        // 每 tick 推进 debuff 计时（此前每 20 tick 才减 1，导致 DoT/阶段间隔慢 20 倍）
        DebuffService.tickPlayer(player);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (ModDamageSources.isDebuffDamage(event.getSource())) {
            return;
        }
        double takenMult = DebuffModifiers.damageTakenMultiplier(player);
        if (event.getSource().is(DamageTypes.FALL)) {
            double fallReduction = com.wf.firearms.data.PlayerFirearmsData.getPerkMultiplier(player, "fall_damage");
            takenMult *= Math.max(0.0, 1.0 - fallReduction);
        }
        if (takenMult != 1.0) {
            event.setAmount((float) (event.getAmount() * takenMult));
        }
        boolean fall = event.getSource().is(DamageTypes.FALL);
        // 枪伤已在 GunHitEffects 中处理流血/中毒，避免与受伤 proc 叠加
        if (ModDamageTypes.isBullet(event.getSource())
                || (TaczBackendConfig.useTaczShooting() && TaczCompat.isTaczBulletDamage(event.getSource()))) {
            return;
        }
        DebuffService.onHurt(player, event.getAmount(), fall);
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            DebuffService.onPlayerJump(player);
        }
    }
}
