package com.wf.firearms.compat.tacz;

import com.wf.firearms.client.sound.ReloadSoundClip;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.gameplay.PerkEffectService;
import com.wf.firearms.network.ReloadSoundPacket;
import com.wf.firearms.network.TaczUnjamSyncPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** TaCZ 整合包枪：{@link TaczPerkBridge} 卡弹后的 U 键排障。 */
@Mod.EventBusSubscriber(modid = com.wf.firearms.GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class TaczUnjamBridge {
    /** 基础排障时长：3 秒（40 tick/s × 3）。天赋 {@code unjamming_speed} 可缩短。 */
    public static final int BASE_UNJAM_TICKS = 60;

    private static final Map<UUID, UnjamSession> SESSIONS = new ConcurrentHashMap<>();

    private TaczUnjamBridge() {}

    public static boolean isUnjamming(Player player) {
        return player != null && SESSIONS.containsKey(player.getUUID());
    }

    public static boolean startUnjam(Player player, ItemStack gun) {
        if (!TaczBackendConfig.useTaczShooting() || player == null || player.level().isClientSide) {
            return false;
        }
        if (gun == null || gun.isEmpty() || !TaczBridge.isTaczGun(gun)) {
            return false;
        }
        if (!TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(gun)) {
            return false;
        }
        if (!TaczPerkBridge.isJammed(gun)) {
            return false;
        }
        if (SESSIONS.containsKey(player.getUUID())) {
            return false;
        }
        Optional<String> weaponKey = TaczGunsrpgAmmoBridge.resolveWeaponKey(gun);
        if (weaponKey.isEmpty()) {
            return false;
        }
        float speedMult = (float) PerkEffectService.unjammingSpeedMultiplier(player);
        int ticks = Math.max(5, Math.round(BASE_UNJAM_TICKS / speedMult));
        long start = player.level().getGameTime();
        long end = start + ticks;
        SESSIONS.put(player.getUUID(), new UnjamSession(weaponKey.get(), start, end));
        if (player instanceof ServerPlayer server) {
            TaczUnjamSyncPacket.sendStart(server, start, end);
            ReloadSoundPacket.send(server, weaponKey.get(), ReloadSoundClip.UNJAM, ticks);
        }
        return true;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != LogicalSide.SERVER || event.phase != TickEvent.Phase.END) {
            return;
        }
        tickUnjam(event.player);
    }

    private static void tickUnjam(Player player) {
        UnjamSession session = SESSIONS.get(player.getUUID());
        if (session == null) {
            return;
        }
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty()
                || !TaczBridge.isTaczGun(held)
                || !TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(held)) {
            cancelUnjam(player);
            return;
        }
        if (player.level().getGameTime() < session.endTick) {
            return;
        }
        TaczPerkBridge.clearJam(held);
        player.displayClientMessage(Component.translatable("gunsrpg.gun.unjam_done"), true);
        finishUnjam(player);
    }

    private static void cancelUnjam(Player player) {
        if (!SESSIONS.containsKey(player.getUUID())) {
            return;
        }
        SESSIONS.remove(player.getUUID());
        if (player instanceof ServerPlayer server) {
            TaczUnjamSyncPacket.sendStop(server);
        }
    }

    private static void finishUnjam(Player player) {
        SESSIONS.remove(player.getUUID());
        if (player instanceof ServerPlayer server) {
            TaczUnjamSyncPacket.sendStop(server);
        }
    }

    private record UnjamSession(String weaponKey, long startTick, long endTick) {}
}
