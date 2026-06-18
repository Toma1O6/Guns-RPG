package com.wf.firearms.client;

import com.wf.firearms.combat.FirearmCombat;
import com.wf.firearms.item.FirearmItem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** 客户端：拦截原版攻击键（挥臂 / 挖方块），开火仍走 {@link ClientFirearmInput}。 */
@Mod.EventBusSubscriber(modid = com.wf.firearms.GunsRpg.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FirearmClientInteractionEvents {
    private FirearmClientInteractionEvents() {}

    private static boolean holdingFirearm(Minecraft mc) {
        if (mc.player == null) {
            return false;
        }
        var gun = FirearmCombat.getHeldGun(mc.player);
        return !gun.isEmpty() && gun.getItem() instanceof FirearmItem;
    }

    @SubscribeEvent
    public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        if (!holdingFirearm(Minecraft.getInstance())) {
            return;
        }
        if (event.isAttack()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (!holdingFirearm(mc) || mc.player == null) {
            return;
        }
        if (mc.player.swinging) {
            mc.player.swinging = false;
            mc.player.swingTime = 0;
        }
    }
}
