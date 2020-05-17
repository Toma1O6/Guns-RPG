package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.client.gui.GuiSkillTree;
import net.minecraft.client.Minecraft;

public class ClientFallbacks {

    public static void onCapDataUpdate() {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.currentScreen instanceof GuiSkillTree) {
            ((GuiSkillTree) mc.currentScreen).onDataUpdate();
        }
    }
}
