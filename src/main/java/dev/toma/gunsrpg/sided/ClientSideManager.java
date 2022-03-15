package dev.toma.gunsrpg.sided;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.ModKeybinds;
import dev.toma.gunsrpg.client.render.*;
import dev.toma.gunsrpg.client.render.debuff.DebuffRenderManager;
import dev.toma.gunsrpg.client.render.debuff.IconDebuffRenderer;
import dev.toma.gunsrpg.client.render.skill.SkillCooldownRenderer;
import dev.toma.gunsrpg.client.render.skill.SkillRendererRegistry;
import dev.toma.gunsrpg.client.screen.*;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.container.CulinaryTableContainer;
import dev.toma.gunsrpg.common.container.MedstationContainer;
import dev.toma.gunsrpg.common.container.SmithingTableContainer;
import dev.toma.gunsrpg.common.init.Debuffs;
import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.util.IDualWieldGun;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.util.object.ShootingManager;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.*;
import lib.toma.animations.api.event.AnimationEventType;
import lib.toma.animations.api.lifecycle.Registries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Function;

import static dev.toma.gunsrpg.client.animation.ModAnimations.*;

public class ClientSideManager {

    private static final ClientSideManager INSTANCE = new ClientSideManager();

    private final DebuffRenderManager debuffRenderManager = new DebuffRenderManager();

    public DebuffRenderManager getDebuffRenderManager() {
        return debuffRenderManager;
    }

    public static ClientSideManager instance() {
        return INSTANCE;
    }

    public void clientInit() {
        animationSetup();
        IRenderPipeline renderPipeline = AnimationEngine.get().renderPipeline();
        renderPipeline.register(MinecraftForge.EVENT_BUS);
        AnimationEngine.get().startEngine(ModConfig.clientConfig.developerMode.get());
    }

    public void animationSetup() {
        Registries.ANIMATION_TYPES.addCallback(dev -> gatherAnimationTypes());
        Registries.ANIMATION_STAGES.addCallback(dev -> gatherAnimationStages());
        Registries.EVENTS.addCallback(dev -> gatherEvents());
    }

    public void clientSetup(FMLClientSetupEvent event) {

        // entity renderers
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.AIRDROP.get(),             AirdropRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.EXPLOSIVE_SKELETON.get(),  ExplosiveSkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.EXPLOSIVE_ARROW.get(),     ExplosiveArrowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_GUNNER.get(),       ZombieGunnerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BLOODMOON_GOLEM.get(),     BloodmoonGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BOLT.get(),                CrossbowBoltRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GRENADE.get(),             GrenadeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ROCKET_ANGEL.get(),        RocketAngelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GOLD_DRAGON.get(),         GoldenDragonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.PELLET.get(),              NoOpRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BULLET.get(),              NoOpRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.FLARE.get(),               NoOpRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.PEBBLE.get(),              PebbleRenderer::new);

        // keybinds
        ModKeybinds.registerKeybinds();
        MinecraftForge.EVENT_BUS.register(new ModKeybinds());

        // screens
        event.enqueueWork(this::screenSetup);

        // debuff renderers
        debuffRenderManager.registerRenderer(Debuffs.POISON,    new IconDebuffRenderer<>(IconDebuffRenderer.POISON_ICON));
        debuffRenderManager.registerRenderer(Debuffs.INFECTION, new IconDebuffRenderer<>(IconDebuffRenderer.INFECTION_ICON));
        debuffRenderManager.registerRenderer(Debuffs.FRACTURE,  new IconDebuffRenderer<>(IconDebuffRenderer.FRACTURE_ICON));
        debuffRenderManager.registerRenderer(Debuffs.BLEED,     new IconDebuffRenderer<>(IconDebuffRenderer.BLEED_ICON));

        // animation setup
        setupRenderPipeline();

        // skill renderers
        SkillRendererRegistry.registerRenderers(new SkillCooldownRenderer<>(), Skills.LIKE_A_CAT_I, Skills.LIKE_A_CAT_II, Skills.LIKE_A_CAT_III);
        SkillRendererRegistry.registerRenderers(new SkillCooldownRenderer<>(), Skills.SECOND_CHANCE_I, Skills.SECOND_CHANCE_II, Skills.SECOND_CHANCE_III);
        SkillRendererRegistry.registerRenderers(new SkillCooldownRenderer<>(), Skills.IRON_BUDDY_I, Skills.IRON_BUDDY_II, Skills.IRON_BUDDY_III);
        SkillRendererRegistry.registerRenderer(new SkillCooldownRenderer<>(), Skills.GOD_HELP_US);

        // event handlers
        MinecraftForge.EVENT_BUS.register(new HUDRenderer());
    }

    public void playDelayedSound(BlockPos pos, float volume, float pitch, SoundEvent event, SoundCategory category, int tickDelay) {
        Minecraft mc = Minecraft.getInstance();
        SoundHandler handler = mc.getSoundManager();
        handler.playDelayed(new SimpleSound(event, category, volume, pitch, pos), tickDelay);
    }

    public IPlayerData.ISynchCallback onDataSync() {
        return () -> {
            Minecraft mc = Minecraft.getInstance();
            if (!PlayerData.getUnsafe(mc.player).getAimInfo().startedAiming()) {
                ShootingManager.Client.loadSettings(mc.options);
            }
        };
    }

    private void screenSetup() {
        ScreenManager.register(ModContainers.CRATE.get(), CrateScreen::new);
        ScreenManager.register(ModContainers.BLAST_FURNACE.get(), BlastFurnaceScreen::new);
        ScreenManager.register(ModContainers.DEATH_CRATE.get(), DeathCrateScreen::new);
        // has to be written like this for some reason
        ScreenManager.register(ModContainers.SMITHING_TABLE.get(), (SmithingTableContainer container, PlayerInventory inventory, ITextComponent title) -> new SkilledWorkbenchScreen<>(container, inventory, title));
        ScreenManager.register(ModContainers.CULINARY_TABLE.get(), (CulinaryTableContainer container, PlayerInventory inventory, ITextComponent title) -> new SkilledWorkbenchScreen<>(container, inventory, title));
        ScreenManager.register(ModContainers.MEDSTATION.get(), (MedstationContainer container, PlayerInventory inventory, ITextComponent title) -> new SkilledWorkbenchScreen<>(container, inventory, title));
        ScreenManager.register(ModContainers.LUNCH_BOX.get(), LunchBoxScreen::new);
        ScreenManager.register(ModContainers.AMMO_CASE.get(), AmmoCaseScreen::new);
        ScreenManager.register(ModContainers.GRENADE_CASE.get(), GrenadeCaseScreen::new);
        ScreenManager.register(ModContainers.MEDS_CASE.get(), MedsCaseScreen::new);
        ScreenManager.register(ModContainers.ITEM_CASE.get(), ItemCaseScreen::new);
        ScreenManager.register(ModContainers.CRYSTAL_CASE.get(), CrystalCaseScreen::new);
        ScreenManager.register(ModContainers.COOKER.get(), CookerScreen::new);
        ScreenManager.register(ModContainers.REPAIR_STATION.get(), RepairStationScreen::new);
    }

    private void setupRenderPipeline() {
        IRenderPipeline pipeline = AnimationEngine.get().renderPipeline();
        pipeline.setPostAnimateCallback(this::animateDualWield);
    }

    private void animateDualWield(MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float swing, float equip, Function<HandSide, IRenderConfig> selector,
                                  IAnimationPipeline pipeline, FirstPersonRenderer fpRenderer, PlayerEntity player, ItemStack stack, ItemCameraTransforms.TransformType type,
                                  boolean mainHand) {

        IRenderPipeline renderPipeline = AnimationEngine.get().renderPipeline();
        IItemRenderer itemRenderer = renderPipeline.getItemRenderer();
        if (stack.getItem() instanceof IDualWieldGun) {
            IDualWieldGun dualWieldGun = (IDualWieldGun) stack.getItem();
            if (PlayerData.hasActiveSkill(player, dualWieldGun.getSkillForDualWield())) {
                poseStack.pushPose();
                {
                    pipeline.animateStage(DUAL_WIELD_ITEM, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
                    itemRenderer.renderItem(fpRenderer, player, stack, ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, true, poseStack, buffer, light, swing, equip);
                }
                poseStack.popPose();
            }
        }
    }

    private AnimationType<?>[] gatherAnimationTypes() {
        return new AnimationType[] {
                AIM_ANIMATION, SPRINT, CHAMBER, HEAL, RELOAD, RELOAD_BULLET, FIREMODE, BULLET_EJECTION, RECOIL, GRENADE, UNJAM, STASH_DETECTOR
        };
    }

    private AnimationStage[] gatherAnimationStages() {
        return new AnimationStage[] {
                DUAL_WIELD_ITEM, MAGAZINE, SLIDE, CHARGING_HANDLE, BULLET, BOLT, BOLT_CARRIER, BARRELS, BULLET_2, BATTERY, BATTERY_COVER
        };
    }

    private AnimationEventType<?>[] gatherEvents() {
        return new AnimationEventType[] {
                STASH_DETECTOR_EVENT
        };
    }
}
