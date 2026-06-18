package com.wf.firearms.client.compat.tacz;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.compat.tacz.TaczBridge;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

/** 反射调用 TaCZ 客户端枪械动作（检视 / 换弹）。 */
public final class TaczClientAnim {
    private static final String OPERATOR = "com.tacz.guns.api.client.gameplay.IClientPlayerGunOperator";
    private static final String TIMELESS_API = "com.tacz.guns.api.TimelessAPI";
    private static final String ANIM_CONSTANT = "com.tacz.guns.client.animation.statemachine.GunAnimationConstant";

    private TaczClientAnim() {}

    /** 排障时优先用检视动画（不触发 TaCZ 换弹备弹逻辑）。 */
    public static void playInspect(LocalPlayer player) {
        if (player == null) {
            return;
        }
        ItemStack gun = player.getMainHandItem();
        if (!TaczBridge.isTaczGun(gun)) {
            return;
        }
        if (invokeOperator(player, "inspect")) {
            return;
        }
        triggerStateMachine(gun, "INPUT_INSPECT");
    }

    public static void playReload(LocalPlayer player) {
        if (player == null) {
            return;
        }
        ItemStack gun = player.getMainHandItem();
        if (!TaczBridge.isTaczGun(gun)) {
            return;
        }
        if (invokeOperator(player, "reload")) {
            return;
        }
        triggerStateMachine(gun, "INPUT_RELOAD");
    }

    private static boolean invokeOperator(LocalPlayer player, String method) {
        try {
            Class<?> opClass = Class.forName(OPERATOR);
            Object operator = opClass.getMethod("fromLocalPlayer", LocalPlayer.class).invoke(null, player);
            opClass.getMethod(method).invoke(operator);
            return true;
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] TaCZ 客户端动作 {} 失败", method, ex);
            return false;
        }
    }

    private static void triggerStateMachine(ItemStack gun, String constantField) {
        try {
            Class<?> api = Class.forName(TIMELESS_API);
            Object displayOpt = api.getMethod("getGunDisplay", ItemStack.class).invoke(null, gun);
            if (!(displayOpt instanceof java.util.Optional<?> optional) || optional.isEmpty()) {
                return;
            }
            Object display = optional.get();
            Object stateMachine = display.getClass().getMethod("getAnimationStateMachine").invoke(display);
            if (stateMachine == null) {
                return;
            }
            Object input = Class.forName(ANIM_CONSTANT).getField(constantField).get(null);
            stateMachine.getClass().getMethod("trigger", String.class).invoke(stateMachine, String.valueOf(input));
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] TaCZ 状态机动画 {} 失败", constantField, ex);
        }
    }
}
