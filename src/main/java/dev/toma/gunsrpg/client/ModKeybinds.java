package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IJamInfo;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IReloadInfo;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.screen.ChooseAmmoScreen;
import dev.toma.gunsrpg.client.screen.skill.SkillTreeScreen;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.item.StashDetectorItem;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.*;
import dev.toma.gunsrpg.util.locate.ILocatorPredicate;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.Animation;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
                NetworkManager.sendServerPacket(new C2S_ChangeFiremodePacket());
            }
        });
    }

    private static void reloadPressed() {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        IPlayerData data = PlayerData.getUnsafe(player);
        IReloadInfo info = data.getReloadInfo();
        IJamInfo jamInfo = data.getJamInfo();
        ItemStack stack = player.getMainHandItem();
        if (player.isCrouching()) {
            if (stack.getItem() instanceof GunItem) {
                mc.setScreen(new ChooseAmmoScreen((GunItem) stack.getItem()));
            }
        } else {
            if (jamInfo.isUnjamming()) {
                return;
            }
            AnimationEngine engine = AnimationEngine.get();
            IAnimationPipeline pipeline = engine.pipeline();
            IAttributeProvider attributeProvider = data.getAttributes();
            if (stack.getItem() instanceof GunItem && !player.isSprinting() && pipeline.get(ModAnimations.CHAMBER) == null) {
                GunItem gun = (GunItem) stack.getItem();
                if (info.isReloading()) {
                    IReloadManager manager = gun.getReloadManager(player, data.getAttributes());
                    if (manager.isCancelable()) {
                        info.enqueueCancel();
                        NetworkManager.sendServerPacket(new C2S_SetReloadingPacket(false, 0));
                        return;
                    }
                } else if (gun.isJammed(stack)) {
                    int slot = player.inventory.selected;
                    int time = gun.getUnjamTime(stack, attributeProvider);
                    ResourceLocation animationPath = gun.getUnjamAnimationPath();
                    jamInfo.startUnjamming(player.inventory.selected, time);
                    NetworkManager.sendServerPacket(new C2S_PacketSetJamming(true, time, slot));
                    pipeline.insert(ModAnimations.UNJAM, AnimationUtils.createAnimation(animationPath, provider -> new Animation(provider, time)));
                    return;
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
                            NetworkManager.sendServerPacket(new C2S_SetReloadingPacket(true, gun.getReloadTime(data.getAttributes(), stack)));
                            return;
                        }
                        PlayerInventory inventory = player.inventory;
                        ILocatorPredicate<ItemStack> typeCheck = ItemLocator.typeAndMaterial(ammoType, material);
                        if (ItemLocator.hasItem(inventory, typeCheck)) {
                            int reloadTime = gun.getReloadTime(attributeProvider, stack);
                            info.startReloading(player, gun, stack, inventory.selected);
                            NetworkManager.sendServerPacket(new C2S_SetReloadingPacket(true, reloadTime));
                        }
                    }
                } else {
                    mc.setScreen(new ChooseAmmoScreen(gun));
                }
            } else if (stack.getItem() == ModItems.STASH_DETECTOR) {
                ItemStack batteryItem = ItemLocator.findFirst(player.inventory, StashDetectorItem::isValidBatterySource);
                if (stack.getDamageValue() > 0 && !batteryItem.isEmpty()) {
                    pipeline.insert(ModAnimations.STASH_DETECTOR, AnimationUtils.createAnimation(StashDetectorItem.CHARGE_BATTERY_ANIMATION, provider -> new Animation(provider, 120)));
                    NetworkManager.sendServerPacket(new C2S_RequestBatteryChange());
                }
            }
        }
    }

    private static void showClassesPressed() {
        Minecraft mc = Minecraft.getInstance();
        NetworkManager.sendServerPacket(new C2S_RequestDataUpdatePacket(mc.player.getUUID()));
        mc.setScreen(new SkillTreeScreen());
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
