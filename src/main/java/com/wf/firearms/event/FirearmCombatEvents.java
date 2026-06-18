package com.wf.firearms.event;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.combat.FirearmCombat;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FirearmCombatEvents {
    private FirearmCombatEvents() {}

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }
        FirearmCombat.tickPlayer(event.player);
        if (event.player.tickCount % 20 == 0) {
            var gun = FirearmCombat.getHeldGun(event.player);
            if (!gun.isEmpty()) {
                FirearmCombat.syncState(event.player, gun);
            }
        }
    }
}
