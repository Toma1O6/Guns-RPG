package com.wf.firearms.compat.tacz;

import net.minecraftforge.eventbus.api.Event;

/** TaCZ 击杀事件桥接（由 {@link TaczBridge#registerKillListener()} 注册）。 */
final class TaczKillBridge {
    private TaczKillBridge() {}

    static void onEntityKillByGun(Event event) {
        TaczGiveGun.onEntityKillByGun(event);
    }
}
