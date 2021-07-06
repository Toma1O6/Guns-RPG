package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.client.gui.skills.GuiPlayerSkills;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import net.minecraft.client.Minecraft;

public class ClientFallbacks {

    public static void onCapDataUpdate() {
        Minecraft mc = Minecraft.getInstance();
        if(mc.screen instanceof GuiPlayerSkills) {
            mc.screen.init(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
        }
        if(!PlayerDataFactory.get(mc.player).getAimInfo().aiming) {
            ClientEventHandler.preAimFov.ifPresent(value -> mc.options.fov = value);
            ClientEventHandler.preAimSens.ifPresent(value -> mc.options.sensitivity = value);
        }
    }
}
