package com.wf.firearms.client;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.client.debuff.ClientDebuffState;
import com.wf.firearms.data.PlayerFirearmsData;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** 客户端：进世界时丢弃上一存档残留在 LocalPlayer 上的火器进度。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientProgressHandler {
    private ClientProgressHandler() {}

    @SubscribeEvent
    public static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        PlayerFirearmsData.clearClientProgress(event.getPlayer());
        ClientDebuffState.clear();
        Minecraft mc = Minecraft.getInstance();
        if (mc.getSingleplayerServer() == null) {
            return;
        }
        ServerPlayer serverPlayer =
                mc.getSingleplayerServer().getPlayerList().getPlayer(event.getPlayer().getUUID());
        if (serverPlayer != null) {
            PlayerFirearmsData.copyProgressFrom(serverPlayer, event.getPlayer());
        }
    }
}
