package com.wf.firearms.bloodmoon;

import com.wf.firearms.GunsRpg;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BloodmoonSleepHandler {
    private BloodmoonSleepHandler() {}

    @SubscribeEvent
    public static void onSleep(PlayerSleepInBedEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (event.getEntity().level().isDay()) {
            return;
        }
        Level level = event.getEntity().level();
        if (level.dimension() != Level.OVERWORLD || !BloodmoonService.isBloodMoon(level)) {
            return;
        }
        BlockPos pos = event.getPos();
        if (event.getEntity() instanceof ServerPlayer server) {
            server.setRespawnPosition(level.dimension(), pos.above(), server.getYRot(), true, false);
        }
        event.setResult(Player.BedSleepingProblem.NOT_POSSIBLE_NOW);
    }
}
