package com.wf.firearms.gameplay;

import com.wf.firearms.GunsRpg;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** bow_damage 等未接入 PerkAttributeHandler 的天赋效果。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ExtendedPerkHandler {
    private ExtendedPerkHandler() {}

    @SubscribeEvent
    public static void onBowHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        var source = event.getSource();
        if (!(source.getDirectEntity() instanceof AbstractArrow)) {
            return;
        }
        if (!(source.getEntity() instanceof Player player)) {
            return;
        }
        if (source.is(DamageTypes.ARROW) || source.getDirectEntity() instanceof AbstractArrow) {
            double mult = PerkEffectService.bowDamageMultiplier(player);
            if (mult != 1.0) {
                event.setAmount((float) (event.getAmount() * mult));
            }
        }
    }
}
