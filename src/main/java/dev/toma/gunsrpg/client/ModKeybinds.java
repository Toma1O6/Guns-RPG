package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IReloadInfo;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.screen.ChooseAmmoScreen;
import dev.toma.gunsrpg.client.screen.skills.PlayerSkillsScreen;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketChangeFiremode;
import dev.toma.gunsrpg.network.packet.SPacketRequestDataUpdate;
import dev.toma.gunsrpg.network.packet.SPacketSetReloading;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.IAnimationPipeline;
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
            if (player.getMainHandItem().getItem() instanceof GunItem) {
                NetworkManager.sendServerPacket(new SPacketChangeFiremode());
            }
        });
    }

    private static void reloadPressed() {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        IPlayerData data = PlayerData.getUnsafe(player);
        IReloadInfo info = data.getReloadInfo();
        ItemStack stack = player.getMainHandItem();
        if (player.isCrouching()) {
            if (stack.getItem() instanceof GunItem) {
                mc.setScreen(new ChooseAmmoScreen((GunItem) stack.getItem()));
            }
        } else {
            AnimationEngine engine = AnimationEngine.get();
            IAnimationPipeline pipeline = engine.pipeline();
            if (stack.getItem() instanceof GunItem && !player.isSprinting() && pipeline.get(ModAnimations.CHAMBER) == null) {
                GunItem gun = (GunItem) stack.getItem();
                if (info.isReloading()) {
                    IReloadManager manager = gun.getReloadManager(player);
                    if (manager.isCancelable()) {
                        info.enqueueCancel();
                        pipeline.remove(ModAnimations.RELOAD);
                        NetworkManager.sendServerPacket(new SPacketSetReloading(false, 0));
                        return;
                    }
                }
                AmmoType ammoType = gun.getAmmoType();
                IAmmoMaterial material = gun.getMaterialFromNBT(stack);
                if (material != null) {
                    int ammo = gun.getAmmo(stack);
                    int max = gun.getMaxAmmo(data.getAttributes());
                    boolean skip = player.isCreative();
                    boolean reloading = info.isReloading();
                    if (!reloading && ammo < max) {
                        if (skip) {
                            info.startReloading(player, gun, stack, player.inventory.selected);
                            NetworkManager.sendServerPacket(new SPacketSetReloading(true, gun.getReloadTime(data.getAttributes())));
                            return;
                        }
                        for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                            ItemStack itemStack = player.inventory.getItem(i);
                            if (itemStack.getItem() instanceof IAmmoProvider) {
                                IAmmoProvider itemAmmo = (IAmmoProvider) itemStack.getItem();
                                if (itemAmmo.getAmmoType() == ammoType && itemAmmo.getMaterial().equals(material)) {
                                    int time = gun.getReloadTime(data.getAttributes());
                                    info.startReloading(player, gun, stack, player.inventory.selected);
                                    NetworkManager.sendServerPacket(new SPacketSetReloading(true, time));
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    mc.setScreen(new ChooseAmmoScreen(gun));
                }
            }
        }
    }

    private static void showClassesPressed() {
        Minecraft mc = Minecraft.getInstance();
        NetworkManager.sendServerPacket(new SPacketRequestDataUpdate(mc.player.getUUID()));
        mc.setScreen(new PlayerSkillsScreen());
    }

    private static void register(String name, int key, Runnable onPress) {
        ModKeyBind bind = new ModKeyBind(String.format("gunsrpg.key.%s", name), key, "gunsrpg.category.keys", onPress);
        ClientRegistry.registerKeyBinding(bind);
        keyBinds.add(bind);
    }

    @SubscribeEvent
    public void onInput(InputEvent.KeyInputEvent event) {
        for (ModKeyBind bind : keyBinds) {
            if (bind.isDown()) {
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
