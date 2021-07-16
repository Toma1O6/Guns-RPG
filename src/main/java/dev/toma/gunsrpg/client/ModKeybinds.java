package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.client.animation.AnimationProcessor;
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
import dev.toma.gunsrpg.sided.ClientSideManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModKeybinds {

    private static final List<ModKeyBind> keyBinds = new ArrayList<>(2);

    public static void registerKeybinds() {
        register("reload", GLFW.GLFW_KEY_R, ModKeybinds::reloadPressed);
        register("class_list", GLFW.GLFW_KEY_O, ModKeybinds::showClassesPressed);
        register("firemode", GLFW.GLFW_KEY_B, () -> {
            PlayerEntity player = Minecraft.getInstance().player;
            if(player.getMainHandItem().getItem() instanceof GunItem) {
                NetworkManager.sendServerPacket(new SPacketChangeFiremode());
            }
        });
    }

    private static void reloadPressed() {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        ReloadInfo info = PlayerDataFactory.getUnsafe(player).getReloadInfo();
        ItemStack stack = player.getMainHandItem();
        if(player.isCrouching()) {
            if(stack.getItem() instanceof GunItem) {
                mc.setScreen(new GuiChooseAmmo((GunItem) stack.getItem()));
            }
        } else {
            AnimationProcessor processor = ClientSideManager.instance().processor();
            if(stack.getItem() instanceof GunItem && !player.isSprinting() && processor.getByID(Animations.REBOLT) == null) {
                GunItem gun = (GunItem) stack.getItem();
                if(info.isReloading()) {
                    if(gun.getReloadManager().canBeInterrupted(gun, stack)) {
                        info.cancelReload();
                        processor.stop(Animations.RELOAD);
                        NetworkManager.sendServerPacket(new SPacketSetReloading(false, 0));
                        return;
                    }
                }
                AmmoType ammoType = gun.getAmmoType();
                AmmoMaterial material = gun.getMaterialFromNBT(stack);
                if(material != null) {
                    int ammo = gun.getAmmo(stack);
                    int max = gun.getMaxAmmo(player);
                    boolean skip = player.isCreative();
                    boolean reloading = info.isReloading();
                    if(!reloading && ammo < max) {
                        if(skip) {
                            gun.getReloadManager().startReloading(player, gun.getReloadTime(player), stack);
                            return;
                        }
                        for(int i = 0; i < player.inventory.getContainerSize(); i++) {
                            ItemStack itemStack = player.inventory.getItem(i);
                            if(itemStack.getItem() instanceof IAmmoProvider) {
                                IAmmoProvider itemAmmo = (IAmmoProvider) itemStack.getItem();
                                if(itemAmmo.getAmmoType() == ammoType && itemAmmo.getMaterial() == material) {
                                    int time = gun.getReloadTime(player);
                                    gun.getReloadManager().startReloading(player, time, stack);
                                    NetworkManager.sendServerPacket(new SPacketSetReloading(true, time));
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    mc.setScreen(new GuiChooseAmmo(gun));
                }
            }
        }
    }

    private static void showClassesPressed() {
        Minecraft mc = Minecraft.getInstance();
        NetworkManager.sendServerPacket(new SPacketRequestDataUpdate(mc.player.getUUID()));
        mc.setScreen(new GuiPlayerSkills());
    }

    private static void register(String name, int key, Runnable onPress) {
        ModKeyBind bind = new ModKeyBind(String.format("gunsrpg.key.%s", name), key, "gunsrpg.category.keys", onPress);
        ClientRegistry.registerKeyBinding(bind);
        keyBinds.add(bind);
    }

    @SubscribeEvent
    public void onInput(InputEvent.KeyInputEvent event) {
        for(ModKeyBind bind : keyBinds) {
            if(bind.isDown()) {
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
