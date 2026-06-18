package com.wf.firearms.event;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.combat.FirearmCombat;
import com.wf.firearms.item.FirearmItem;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** 持枪时左键只用于开火，不触发近战攻击实体。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FirearmAttackEvents {
    private FirearmAttackEvents() {}

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        var gun = FirearmCombat.getHeldGun(event.getEntity());
        if (!gun.isEmpty() && gun.getItem() instanceof FirearmItem) {
            event.setCanceled(true);
        }
    }
}
