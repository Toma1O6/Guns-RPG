package com.wf.firearms.gameplay;

import com.wf.firearms.GunsRpg;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PerkMiscHandler {
    private PerkMiscHandler() {}

    @SubscribeEvent
    public static void onMeleeHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        DamageSource src = event.getSource();
        if (!(src.getEntity() instanceof Player player)) {
            return;
        }
        if (GunKillHandler.isFirearmBulletDamage(src)) {
            return;
        }
        if (src.getDirectEntity() != player) {
            return;
        }
        double mult = PerkEffectService.meleeDamageMultiplier(player);
        if (mult != 1.0) {
            event.setAmount((float) (event.getAmount() * mult));
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.level().isClientSide()) {
            return;
        }
        double mult = PerkEffectService.motherlodeMultiplier(player);
        if (mult <= 1.0 || player.getRandom().nextFloat() > (mult - 1.0)) {
            return;
        }
        if (!event.getState().isAir()) {
            event.setExpToDrop(event.getExpToDrop() + 1);
        }
    }

}
