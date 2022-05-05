package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IHandState;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.animation.AimAnimation;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.InputEventListenerType;
import dev.toma.gunsrpg.common.item.guns.util.ScopeDataRegistry;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_SetAimingPacket;
import dev.toma.gunsrpg.util.object.PropertyChangeListener;
import dev.toma.gunsrpg.util.object.ShootingManager;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GunsRPG.MODID)
public class ClientEventHandler {

    private static final PropertyChangeListener<Boolean, PlayerEntity> startSprintListener = new PropertyChangeListener<>(
            Entity::isSprinting,
            ClientEventHandler::dispatchSprintAnimation
    );
    public static float partialTicks;

    @SubscribeEvent
    public static void cancelHandSwinging(InputEvent.ClickInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player != null && player.getMainHandItem().getItem() instanceof AbstractGun) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }

    @SubscribeEvent
    public static void mouseInputEvent(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        GameSettings settings = mc.options;
        if (player != null) {
            GunItem item = ShootingManager.getGunFrom(player);
            ItemStack stack = player.getMainHandItem();
            AnimationEngine engine = AnimationEngine.get();
            IAnimationPipeline pipeline = engine.pipeline();
            LazyOptional<IPlayerData> optional = PlayerData.get(player);
            if (item != null) {
                if (settings.keyAttack.isDown()) {
                    Firemode firemode = item.getFiremode(stack);
                    InputEventListenerType inputEvent = InputEventListenerType.ON_INPUT;
                    optional.ifPresent(data -> firemode.triggerEvent(inputEvent, player, stack, data));
                } else if (settings.keyUse.isDown() && pipeline.get(ModAnimations.CHAMBER) == null && !player.isSprinting()) {
                    handleAim(optional, settings, player, item, stack, pipeline);
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderHandEvent(RenderHandEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (event.getHand() == Hand.OFF_HAND) {
            if (event.getItemStack().getItem() == Items.SHIELD && player.getMainHandItem().getItem() instanceof GunItem) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (event.phase == TickEvent.Phase.END && player != null) {
            LazyOptional<IPlayerData> optional = PlayerData.get(player);
            if (!optional.isPresent())
                return;
            IPlayerData data = optional.orElse(null);
            ShootingManager.tickShootingDelay();
            startSprintListener.refresh(player);
            GameSettings settings = mc.options;
            if (settings.keyAttack.isDown()) {
                dispatchWeaponInputEvent(InputEventListenerType.ON_TICK, player, data);
            }
            if (ShootingManager.Client.isBurstModeActive()) {
                dispatchWeaponInputEvent(InputEventListenerType.ON_BURST_TICK, player, data);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        partialTicks = event.renderTickTime;
    }

    private static void handleAim(LazyOptional<IPlayerData> optional, GameSettings settings, PlayerEntity player, GunItem item, ItemStack stack, IAnimationPipeline pipeline) {
        boolean aim = optional.isPresent() && optional.orElse(null).getAimInfo().startedAiming();
        if (!aim) {
            ShootingManager.Client.saveSettings(settings);
            ScopeDataRegistry.Entry entry = ScopeDataRegistry.getRegistry().getRegistryEntry(item);
            optional.ifPresent(data -> {
                ISkillProvider provider = data.getSkillProvider();
                if (entry.isApplicable(provider)) {
                    ShootingManager.Client.applySettings(settings, entry);
                }
            });
            ResourceLocation aimAnimationPath = item.getAimAnimationPath(stack, player);
            if (aimAnimationPath != null) {
                pipeline.insert(ModAnimations.AIM_ANIMATION, AnimationUtils.createAnimation(aimAnimationPath, AimAnimation::new));
            }
        } else {
            ShootingManager.Client.loadSettings(settings);
        }
        NetworkManager.sendServerPacket(new C2S_SetAimingPacket(!aim));
    }

    private static void dispatchSprintAnimation(boolean isSprinting, PlayerEntity player) {
        if (!isSprinting) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        optional.ifPresent(data -> {
            IHandState handState = data.getHandState();
            if (stack.getItem() instanceof GunItem && !handState.areHandsBusy()) {
                IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
                pipeline.insert(ModAnimations.SPRINT);
            }
        });
    }

    private static void dispatchWeaponInputEvent(InputEventListenerType event, PlayerEntity player, IPlayerData data) {
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof GunItem) {
            GunItem item = (GunItem) stack.getItem();
            Firemode firemode = item.getFiremode(stack);
            firemode.triggerEvent(event, player, stack, data);
        }
    }
}
