package dev.toma.gunsrpg.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.api.common.IDisplayableSkill;
import dev.toma.gunsrpg.api.common.IOverlayRender;
import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.client.animation.AimAnimation;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.render.debuff.DebuffRenderManager;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.InputEventListenerType;
import dev.toma.gunsrpg.common.item.guns.util.ScopeDataRegistry;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.config.util.ScopeRenderer;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetAiming;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.Lifecycle;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.object.PropertyChangeListener;
import dev.toma.gunsrpg.util.object.ShootingManager;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.GameSettings;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GunsRPG.MODID)
public class ClientEventHandler {

    private static final PropertyChangeListener<Boolean, PlayerEntity> startSprintListener = new PropertyChangeListener<>(
            Entity::isSprinting,
            ClientEventHandler::dispatchSprintAnimation
    );
    public static float partialTicks;

    @SubscribeEvent
    public static void cancelOverlays(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof GunItem) {
                if (!ModConfig.clientConfig.developerMode.get()) {
                    event.setCanceled(true);
                }
            }
        }
    }

    // TODO clean up
    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            MatrixStack matrixStack = event.getMatrixStack();
            LazyOptional<IPlayerData> optional = PlayerData.get(player);
            optional.ifPresent(data -> {
                FontRenderer renderer = mc.font;
                long day = player.level.getGameTime() / 24000L;
                int cycle = ModConfig.worldConfig.bloodmoonCycle.get();
                MainWindow window = event.getWindow();
                if (cycle >= 0) {
                    boolean b = day % cycle == 0 && day > 0;
                    long l = b ? 0 : cycle - day % cycle;
                    String remainingDays = l + "";
                    mc.font.draw(matrixStack, remainingDays, window.getGuiScaledWidth() - 10 - mc.font.width(remainingDays) / 2f, 6, b ? 0xff0000 : l > 0 && l < 3 ? 0xffff00 : 0xffffff);
                }
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                ItemStack stack = player.getMainHandItem();
                int width = 26;
                int x = window.getGuiScaledWidth() - width - 34;
                int y = window.getGuiScaledHeight() - 22;
                IData genericData = data.getGenericData();
                if (stack.getItem() instanceof GunItem) {
                    GunItem gun = (GunItem) stack.getItem();
                    IKillData gunData = genericData.getWeaponStats((GunItem) stack.getItem());
                    int gunKills = gunData.getKills();
                    int gunRequiredKills = gunData.getRequiredKillCount();
                    int ammo = gun.getAmmo(stack);
                    int max = gun.getMaxAmmo(data.getAttributes());
                    float f = gunData.getLevel() == gunData.getLevelLimit() ? 1.0F : gunKills / (float) gunRequiredKills;
                    Lifecycle lifecycle = GunsRPG.getModLifecycle();
                    IAmmoProvider itemAmmo = lifecycle.getAmmoForWeapon(gun, stack);
                    if (itemAmmo != null) {
                        int c = 0;
                        for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                            ItemStack itemStack = player.inventory.getItem(i);
                            if (itemStack.getItem() instanceof IAmmoProvider) {
                                IAmmoProvider ammoProvider = (IAmmoProvider) itemStack.getItem();
                                if (ammoProvider.getMaterial() == itemAmmo.getMaterial() && ammoProvider.getAmmoType() == itemAmmo.getAmmoType()) {
                                    c += itemStack.getCount();
                                }
                            }
                        }
                        String text = ammo + " / " + c;
                        width = renderer.width(text);
                        x = window.getGuiScaledWidth() - width - 34;
                        Matrix4f pose = matrixStack.last().pose();
                        RenderUtils.drawGradient(pose, x, y, x + width + 22, y + 7, 0xFF << 24, 0xFF << 24);
                        RenderUtils.drawGradient(pose, x + 2, y + 2, x + (int) (f * (width + 20)), y + 5, 0xFFFFFF << 8, 0xFF8888 << 8);
                        mc.getItemRenderer().renderGuiItem(new ItemStack((Item) itemAmmo), x, y - 18);
                        mc.font.draw(matrixStack, text, x + 19, y - 14, 0xffffff);
                    }
                }
                int kills = genericData.getKills();
                int required = genericData.getRequiredKillCount();
                float levelProgress = genericData.getLevel() == genericData.getLevelLimit() ? 1.0F : kills / (float) required;
                Matrix4f pose = matrixStack.last().pose();
                RenderUtils.drawGradient(pose, x, y + 10, x + width + 22, y + 17, 0xFF << 24, 0xFF << 24);
                RenderUtils.drawGradient(pose, x + 2, y + 12, x + (int) (levelProgress * (width + 20)), y + 15, 0xFF00FFFF, 0xFF008888);

                ClientSideManager manager = ClientSideManager.instance();
                DebuffRenderManager debuffRenderManager = manager.getDebuffRenderManager();
                debuffRenderManager.drawDebuffsOnScreen(matrixStack, data.getAttributes(), data.getDebuffControl(), 0, window.getGuiScaledHeight() - 50, event.getPartialTicks());

                int renderIndex = 0;
                ISkills skills = data.getSkills();
                List<IDisplayableSkill> list = skills.getDisplayableSkills(); // TODO rework
                if (list == null) return;
                int left = 5;
                int top = window.getGuiScaledHeight() - 25;
                List<ISkill> renderSkills = new ArrayList<>();
                for (ISkill skill : list) {
                    if (skill instanceof IOverlayRender) {
                        skill = SkillUtil.getBestSkillFromOverrides(skill, player);
                        if (!renderSkills.contains(skill)) renderSkills.add(skill);
                    }
                }
                for (ISkill skill : renderSkills) {
                    IOverlayRender overlayRenderer = (IOverlayRender) skill;
                    if (skill.canApply(player) && overlayRenderer.shouldRenderOnHUD()) {
                        overlayRenderer.renderInHUD(matrixStack, skill, renderIndex, left, top);
                        ++renderIndex;
                    }
                }
            });
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
        ItemStack stack = event.getItemStack();
        if (event.getHand() == Hand.OFF_HAND) {
            if (event.getItemStack().getItem() == Items.SHIELD && player.getMainHandItem().getItem() instanceof GunItem) {
                event.setCanceled(true);
            }
        }
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        if (!optional.isPresent()) {
            event.setCanceled(true);
        } else {
            IPlayerData data = optional.orElse(null);
            IAimInfo info = data.getAimInfo();
            ScopeRenderer renderer = ModConfig.clientConfig.scopeRenderer.get();
            Item item = stack.getItem();
            if (info.isAiming() && renderer == ScopeRenderer.TEXTURE && (PlayerData.hasActiveSkill(player, Skills.KAR98K_SCOPE) && item == ModItems.KAR98K || PlayerData.hasActiveSkill(player, Skills.CROSSBOW_SCOPE) && item == ModItems.WOODEN_CROSSBOW)) {
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
                ISkills skills = data.getSkills();
                if (entry.isApplicable(skills)) {
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
        NetworkManager.sendServerPacket(new SPacketSetAiming(!aim));
    }

    private static void dispatchSprintAnimation(boolean isSprinting, PlayerEntity player) {
        ItemStack stack = player.getMainHandItem();
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        optional.ifPresent(data -> {
            IReloadInfo info = data.getReloadInfo();
            if (stack.getItem() instanceof GunItem && !info.isReloading()) {
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
