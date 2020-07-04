package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.client.gui.GuiSkillTree;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import net.minecraft.client.Minecraft;

public class ClientFallbacks {

    public static void onCapDataUpdate() {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.currentScreen instanceof GuiSkillTree) {
            ((GuiSkillTree) mc.currentScreen).onDataUpdate();
        }
        if(!PlayerDataFactory.get(mc.player).getAimInfo().aiming) {
            mc.gameSettings.fovSetting = ClientEventHandler.preAimFov;
            if(ClientEventHandler.preAimSens >= 0) mc.gameSettings.mouseSensitivity = ClientEventHandler.preAimSens;
        }
    }
}
