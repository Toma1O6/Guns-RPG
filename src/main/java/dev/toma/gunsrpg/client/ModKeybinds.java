package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.client.gui.GuiSkillTree;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketRequestDataUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class ModKeybinds {

    private static List<ModKeyBind> keyBinds = new ArrayList<>(2);

    public static void registerKeybinds() {
        register("reload", Keyboard.KEY_R, ModKeybinds::reloadPressed);
        register("class_list", Keyboard.KEY_L, ModKeybinds::showClassesPressed);
    }

    private static void reloadPressed() {

    }

    private static void showClassesPressed() {
        Minecraft mc = Minecraft.getMinecraft();
        NetworkManager.toServer(new SPacketRequestDataUpdate(mc.player.getUniqueID()));
        mc.displayGuiScreen(new GuiSkillTree());
    }

    private static void register(String name, int key, Runnable onPress) {
        ModKeyBind bind = new ModKeyBind(String.format("gunsrpg.key.%s", name), key, "gunsrpg.category.keys", onPress);
        ClientRegistry.registerKeyBinding(bind);
        keyBinds.add(bind);
    }

    @SubscribeEvent
    public void onInput(InputEvent.KeyInputEvent event) {
        for(ModKeyBind bind : keyBinds) {
            if(bind.isPressed()) {
                bind.onPress.run();
                break;
            }
        }
    }

    private static class ModKeyBind extends KeyBinding {

        private final Runnable onPress;

        private ModKeyBind(String name, int key, String category, Runnable onPress) {
            super(name, key, category);
            this.onPress = onPress;
        }
    }
}
