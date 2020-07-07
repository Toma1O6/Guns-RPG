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
            ClientEventHandler.preAimFov.ifPresent(value -> mc.gameSettings.fovSetting = value);
            ClientEventHandler.preAimSens.ifPresent(value -> mc.gameSettings.mouseSensitivity = value);
        }
    }
}
