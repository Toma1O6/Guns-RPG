package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.gui.GuiChooseAmmo;
import dev.toma.gunsrpg.client.gui.skills.GuiPlayerSkills;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.ReloadInfo;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.IAmmoProvider;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketChangeFiremode;
import dev.toma.gunsrpg.network.packet.SPacketRequestDataUpdate;
import dev.toma.gunsrpg.network.packet.SPacketSetReloading;
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

    private static final List<ModKeyBind> keyBinds = new ArrayList<>(2);

    public static void registerKeybinds() {
        register("reload", Keyboard.KEY_R, ModKeybinds::reloadPressed);
        register("class_list", Keyboard.KEY_L, ModKeybinds::showClassesPressed);
        register("firemode", Keyboard.KEY_B, () -> {
            EntityPlayer player = Minecraft.getMinecraft().player;
            if(player.getHeldItemMainhand().getItem() instanceof GunItem) {
                NetworkManager.toServer(new SPacketChangeFiremode());
            }
        });
        register("sight_type", Keyboard.KEY_PRIOR, () -> PlayerDataFactory.get(Minecraft.getMinecraft().player).getScopeData().updateType());
        register("sight_color", Keyboard.KEY_NEXT, () -> PlayerDataFactory.get(Minecraft.getMinecraft().player).getScopeData().updateColor());
    }

    private static void reloadPressed() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        ReloadInfo info = PlayerDataFactory.get(player).getReloadInfo();
        if(player.isSneaking()) {
            if(player.getHeldItemMainhand().getItem() instanceof GunItem) {
                mc.displayGuiScreen(new GuiChooseAmmo((GunItem) player.getHeldItemMainhand().getItem()));
            }
        } else {
            if(player.getHeldItemMainhand().getItem() instanceof GunItem && !player.isSprinting() && AnimationManager.getAnimationByID(Animations.REBOLT) == null) {
                ItemStack stack = player.getHeldItemMainhand();
                GunItem gun = (GunItem) stack.getItem();
                if(info.isReloading()) {
                    if(gun.getReloadManager().canBeInterrupted(gun, stack)) {
                        info.cancelReload();
                        AnimationManager.cancelAnimation(Animations.RELOAD);
                        NetworkManager.toServer(new SPacketSetReloading(false, 0));
                        return;
                    }
                }
                AmmoType ammoType = gun.getAmmoType();
                AmmoMaterial material = gun.getMaterialFromNBT(stack);
                if(material != null) {
                    int ammo = gun.getAmmo(stack);
                    int max = gun.getMaxAmmo(player);
                    boolean skip = player.isCreative();
                    boolean reloading = PlayerDataFactory.get(player).getReloadInfo().isReloading();
                    if(!reloading && ammo < max) {
                        if(skip) {
                            gun.getReloadManager().startReloading(player, gun.getReloadTime(player), stack);
                            return;
                        }
                        for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
                            ItemStack itemStack = player.inventory.getStackInSlot(i);
                            if(itemStack.getItem() instanceof IAmmoProvider) {
                                IAmmoProvider itemAmmo = (IAmmoProvider) itemStack.getItem();
                                if(itemAmmo.getAmmoType() == ammoType && itemAmmo.getMaterial() == material) {
                                    int time = gun.getReloadTime(player);
                                    gun.getReloadManager().startReloading(player, time, stack);
                                    NetworkManager.toServer(new SPacketSetReloading(true, time));
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
        mc.displayGuiScreen(new GuiPlayerSkills());
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
