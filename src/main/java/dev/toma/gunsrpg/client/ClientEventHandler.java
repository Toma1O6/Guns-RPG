package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IHandState;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.animation.AimAnimation;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.InputEventListenerType;
import dev.toma.gunsrpg.common.item.guns.util.ScopeDataRegistry;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_SetAimingPacket;
import dev.toma.gunsrpg.sided.ClientSideManager;
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
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
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
    public static boolean bloodmoon;

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
                    if (!item.hasAmmo(stack)) {
                        player.playSound(ModSounds.EMPTY_GUN_CLICK, 1.0F, 1.0F);
                    }
                } else if (settings.keyUse.isDown() && !AnimationUtils.isActiveOrScheduled(pipeline, ModAnimations.CHAMBER) && !player.isSprinting()) {
                    optional.ifPresent(data -> {
                        if (!data.getAimInfo().isAiming() || !ClientSideManager.config.aimInputType.isHold()) {
                            handleAim(optional, settings, player, item, stack, pipeline);
                        }
                    });

                }
            }
        }
    }

    @SubscribeEvent
    public static void renderHandEvent(RenderHandEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (event.getHand() == Hand.OFF_HAND) {
            if (event.getItemStack().getItem() instanceof ShieldItem && player.getMainHandItem().getItem() instanceof GunItem) {
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
            updateCurrentBloodmoonStatus(player);
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
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof GunItem) {
                GunItem gun = (GunItem) stack.getItem();
                if (data.getAimInfo().isAiming() && ClientSideManager.config.aimInputType.isHold() && !settings.keyUse.isDown()) {
                    IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
                    handleAim(optional, settings, player, gun, stack, pipeline);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        partialTicks = event.renderTickTime;
    }

    @SubscribeEvent
    public static void onSetFogDensity(EntityViewRenderEvent.FogDensity event) {
        if (bloodmoon) {
            World world = Minecraft.getInstance().level;
            long dayTime = world.getDayTime() % 24000L;
            float diff = 1.0F;
            if (dayTime < 14000L) {
                diff = (dayTime - 12500L) / (1500.0F);
            }
            if (dayTime > 22500L) {
                diff = 1.0F - ((dayTime - 22500L) / (1500.0F));
            }
            float baseFog = 0.003F;
            float fog = (0.03F - baseFog) * diff;
            event.setDensity(baseFog + fog);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onSetFogColor(EntityViewRenderEvent.FogColors event) {
        if (!bloodmoon) return;
        World world = Minecraft.getInstance().level;
        long dayTime = world.getDayTime() % 24000L;
        float diff = 1.0F;
        if (dayTime < 14000L) {
            diff = (dayTime - 12500L) / (1500.0F);
        }
        if (dayTime > 22500L) {
            diff = 1.0F - ((dayTime - 22500L) / (1500.0F));
        }
        float r = event.getRed();
        float g = event.getGreen();
        float b = event.getBlue();
        float r1 = 1.0F - r;
        float idiff = 1.0F - diff;
        event.setRed(r + r1 * diff * 0.4F);
        event.setGreen(g * idiff);
        event.setBlue(b * idiff);
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

    private static void updateCurrentBloodmoonStatus(PlayerEntity player) {
        World world = player.level;
        long actualDay = world.getDayTime() / 24000L;
        int cycle = GunsRPG.config.world.bloodmoonCycle;
        if (cycle == -1) return;
        boolean isBloodmoonDay = actualDay > 0 && (cycle == 0 || actualDay % cycle == 0);
        boolean isNight = world.getDayTime() % 24000L >= 12500L;
        bloodmoon = isBloodmoonDay && isNight;
    }
}
