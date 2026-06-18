package com.wf.firearms.client;

import com.wf.firearms.combat.FirearmCombat;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.FirearmStackState;
import com.wf.firearms.item.FirearmItem;
import com.wf.firearms.network.FirearmActionPacket;
import com.wf.firearms.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/** 客户端：左键开火、右键切换瞄准、功能键换弹/排障/模式/准星。 */
public final class ClientFirearmInput {
    private static boolean attackWasDown;

    private ClientFirearmInput() {}

    public static void tick(Minecraft mc) {
        if (mc.player == null || mc.screen != null) {
            attackWasDown = false;
            return;
        }

        if (FirearmCombat.tickHotbarAimReset(mc.player)) {
            ModNetwork.sendFirearmAction(
                    new FirearmActionPacket(FirearmActionPacket.Action.AIM_STOP, false));
        }

        ItemStack gun = FirearmCombat.getHeldGun(mc.player);
        if (gun.isEmpty() || !(gun.getItem() instanceof FirearmItem firearm)) {
            attackWasDown = mc.options.keyAttack.isDown();
            return;
        }

        FirearmSpec spec = firearm.getSpec();
        boolean aiming = FirearmStackState.isAiming(gun);

        boolean attackDown = mc.options.keyAttack.isDown();
        boolean auto = FirearmCombat.isAutomaticMode(gun, spec);
        if (attackDown) {
            if (auto || !attackWasDown) {
                ModNetwork.sendFirearmAction(
                        new FirearmActionPacket(FirearmActionPacket.Action.FIRE, aiming));
            }
        }
        attackWasDown = attackDown;

        while (KeyBindings.RELOAD.consumeClick()) {
            ModNetwork.sendFirearmAction(new FirearmActionPacket(FirearmActionPacket.Action.RELOAD, aiming));
        }
        while (KeyBindings.UNJAM.consumeClick()) {
            ModNetwork.sendFirearmAction(new FirearmActionPacket(FirearmActionPacket.Action.UNJAM, aiming));
        }
        while (KeyBindings.FIREMODE.consumeClick()) {
            ModNetwork.sendFirearmAction(new FirearmActionPacket(FirearmActionPacket.Action.FIREMODE, aiming));
        }
        while (KeyBindings.SIGHT_COLOR.consumeClick()) {
            FirearmCrosshairStyle.cycleColor();
            mc.player.displayClientMessage(
                    Component.translatable(
                            "gunsrpg.gun.sight_color",
                            String.valueOf((FirearmCrosshairStyle.colorArgb() >>> 16) & 0xFF)),
                    true);
        }
        while (KeyBindings.SIGHT_TYPE.consumeClick()) {
            FirearmCrosshairStyle.cycleType();
            mc.player.displayClientMessage(
                    Component.translatable(
                            "gunsrpg.gun.sight_type",
                            Component.translatable(
                                    "gunsrpg.gun.sight." + FirearmCrosshairStyle.typeKey())),
                    true);
        }
    }
}
