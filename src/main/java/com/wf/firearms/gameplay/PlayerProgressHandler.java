package com.wf.firearms.gameplay;

import com.wf.firearms.data.LevelingStrategy;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.debuff.DebuffSyncService;
import com.wf.firearms.debuff.DebuffType;
import com.wf.firearms.debuff.FractureModifiers;
import com.wf.firearms.debuff.PlayerDebuffData;
import com.wf.firearms.network.FirearmsProgressSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.wf.firearms.GunsRpg;

@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlayerProgressHandler {
    private PlayerProgressHandler() {}

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        PlayerFirearmsData.migrateLegacyKills(player);
        LevelingStrategy leveling = LevelingStrategy.load();
        PlayerFirearmsData.setLevelingStrategy(leveling);
        ProgressionService.setLeveling(leveling);

        int kills = PlayerFirearmsData.getTotalKills(player);
        int oldLevel = PlayerFirearmsData.getPlayerLevel(player);
        int level = leveling.playerLevel().levelForKills(kills);
        if (level > oldLevel) {
            PlayerFirearmsData.setPlayerLevel(player, level);
            ProgressionService.grantPlayerLevelRewards(player, oldLevel, level);
        }
        StarterKitService.tryApply(player);
        FirearmsProgressSyncPacket.sendTo(player);
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!(event.getEntity() instanceof ServerPlayer newPlayer)) {
            return;
        }
        PlayerFirearmsData.copyAllProgress(event.getOriginal(), newPlayer);
        if (event.isWasDeath()) {
            for (DebuffType type : DebuffType.values()) {
                PlayerDebuffData.clear(newPlayer, type);
            }
            FractureModifiers.clear(newPlayer);
            DebuffSyncService.syncFull(newPlayer);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        FirearmsProgressSyncPacket.sendTo(player);
    }
}
