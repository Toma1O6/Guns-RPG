package com.wf.firearms.bloodmoon;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.config.BloodmoonConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** 血月期间玩家承伤倍率。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BloodmoonDamageHandler {
    private BloodmoonDamageHandler() {}

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (!BloodmoonService.isBloodMoon(player.level())) {
            return;
        }
        float mult = BloodmoonConfig.playerDamageTakenMultiplier();
        if (mult <= 1.0f) {
            return;
        }
        event.setAmount(event.getAmount() * mult);
    }
}
