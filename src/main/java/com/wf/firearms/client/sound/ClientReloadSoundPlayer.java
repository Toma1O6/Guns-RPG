package com.wf.firearms.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.wf.firearms.GunsRpg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/** 客户端：按 animation json 时间点播放换弹/排障音效。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, value = Dist.CLIENT)
public final class ClientReloadSoundPlayer {
    private static final List<PendingCue> PENDING = new ArrayList<>();

    private ClientReloadSoundPlayer() {}

    public static void schedule(Player player, String weaponKey, ReloadSoundClip clip, int durationTicks) {
        if (player == null || durationTicks <= 0) {
            return;
        }
        List<ReloadAnimationSounds.SoundCue> cues = ReloadAnimationSounds.cuesFor(weaponKey, clip);
        if (cues.isEmpty()) {
            return;
        }
        long start = player.level().getGameTime();
        UUID id = player.getUUID();
        for (ReloadAnimationSounds.SoundCue cue : cues) {
            long trigger = start + Math.max(0L, Math.round(cue.normalizedTime() * durationTicks));
            PENDING.add(new PendingCue(id, trigger, cue));
        }
    }

    public static void cancel(Player player) {
        if (player == null) {
            return;
        }
        UUID id = player.getUUID();
        PENDING.removeIf(c -> c.playerId.equals(id));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || PENDING.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            PENDING.clear();
            return;
        }
        long now = mc.level.getGameTime();
        Iterator<PendingCue> it = PENDING.iterator();
        while (it.hasNext()) {
            PendingCue pending = it.next();
            if (now < pending.triggerTick) {
                continue;
            }
            it.remove();
            if (!pending.playerId.equals(mc.player.getUUID())) {
                continue;
            }
            play(mc, pending.cue);
        }
    }

    private static void play(Minecraft mc, ReloadAnimationSounds.SoundCue cue) {
        SoundEvent sound = SoundEvent.createVariableRangeEvent(cue.sound());
        mc.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(sound, cue.volume(), cue.pitch()));
    }

    private record PendingCue(UUID playerId, long triggerTick, ReloadAnimationSounds.SoundCue cue) {}
}
