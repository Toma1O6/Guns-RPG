package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.client.gui.GuiChooseAmmo;
import dev.toma.gunsrpg.client.gui.GuiSkillTree;
import dev.toma.gunsrpg.common.item.guns.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.AmmoType;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.IAmmoProvider;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketRequestDataUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        if(player.isSneaking()) {
            if(player.getHeldItemMainhand().getItem() instanceof GunItem) {
                mc.displayGuiScreen(new GuiChooseAmmo((GunItem) player.getHeldItemMainhand().getItem()));
            }
        } else {
            if(player.getHeldItemMainhand().getItem() instanceof GunItem) {
                ItemStack stack = player.getHeldItemMainhand();
                GunItem gun = (GunItem) stack.getItem();
                AmmoType ammoType = gun.getAmmoType();
                AmmoMaterial material = gun.getMaterialFromNBT(stack);
                if(material != null) {
                    int ammo = gun.getAmmo(stack);
                    int max = gun.getMaxAmmo(player);
                    boolean skip = player.isCreative();
                    if(skip) {
                        gun.getReloadManager().startReloading(player, gun, stack);
                    } else if(ammo < max) {
                        for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
                            ItemStack itemStack = player.inventory.getStackInSlot(i);
                            if(itemStack.getItem() instanceof IAmmoProvider) {
                                IAmmoProvider itemAmmo = (IAmmoProvider) itemStack.getItem();
                                if(itemAmmo.getAmmoType() == ammoType && itemAmmo.getMaterial() == material) {
                                    gun.getReloadManager().startReloading(player, gun, stack);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    mc.displayGuiScreen(new GuiChooseAmmo(gun));
                }
            }
        }
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
