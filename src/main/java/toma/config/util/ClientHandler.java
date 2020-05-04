package toma.config.util;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toma.config.ui.ModListScreen;

public class ClientHandler {

    @SubscribeEvent
    public void openGuiEvent(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiModList) {
            event.setGui(new ModListScreen());
        }
    }
}
