package dev.toma.gunsrpg.sided;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.format.ConfigFormats;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.ModKeybinds;
import dev.toma.gunsrpg.client.model.*;
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
import dev.toma.gunsrpg.config.client.GunsrpgConfigClient;
import dev.toma.gunsrpg.util.object.ShootingManager;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.AnimationType;
import lib.toma.animations.api.IRenderPipeline;
import lib.toma.animations.api.event.AnimationEventType;
import lib.toma.animations.api.lifecycle.Registries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static dev.toma.gunsrpg.client.animation.ModAnimations.*;

public class ClientSideManager {

    private static final ClientSideManager INSTANCE = new ClientSideManager();

    private final DebuffRenderManager debuffRenderManager = new DebuffRenderManager();
    public static GunsrpgConfigClient config;

    public DebuffRenderManager getDebuffRenderManager() {
        return debuffRenderManager;
    }

    public static ClientSideManager instance() {
        return INSTANCE;
    }

    public void clientInit() {
        config = Configuration.registerConfig(GunsrpgConfigClient.class, ConfigFormats.yaml()).getConfigInstance();
        animationSetup();
        IRenderPipeline renderPipeline = AnimationEngine.get().renderPipeline();
        renderPipeline.register(MinecraftForge.EVENT_BUS);
        AnimationEngine.get().startEngine(config.developerMode);
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
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_GUNNER.get(),       ZombieGunnerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BLOODMOON_GOLEM.get(),     BloodmoonGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BOLT.get(),                CrossbowBoltRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GRENADE.get(),             GrenadeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ROCKET_ANGEL.get(),        RocketAngelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GOLD_DRAGON.get(),         GoldenDragonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.PELLET.get(),              TracerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BULLET.get(),              TracerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.FLARE.get(),               NoOpRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.PEBBLE.get(),              PebbleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GRENADE_SHELL.get(),       manager -> new ExplosiveProjectileRenderer<>(manager, new GrenadeShellModel()));
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ROCKET.get(),              manager -> new ExplosiveProjectileRenderer<>(manager, new RocketModel()));
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.MAYOR.get(),               MayorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_KNIGHT.get(),       ZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_NIGHTMARE.get(),    ZombieNightmareRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SMG_TURRET.get(),          manager -> new TurretRenderer(manager, new SmgTurretModel()));
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.AR_TURRET.get(),           manager -> new TurretRenderer(manager, new ArTurretModel()));
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ROCKET_TURRET.get(),       manager -> new TurretRenderer(manager, new RocketTurretModel()));

        // keybinds
        ModKeybinds.registerKeybinds();
        MinecraftForge.EVENT_BUS.register(new ModKeybinds());

        // screens
        event.enqueueWork(this::screenSetup);

        // debuff renderers
        debuffRenderManager.registerRenderer(Debuffs.POISON,            new IconDebuffRenderer<>(IconDebuffRenderer.POISON_ICON));
        debuffRenderManager.registerRenderer(Debuffs.INFECTION,         new IconDebuffRenderer<>(IconDebuffRenderer.INFECTION_ICON));
        debuffRenderManager.registerRenderer(Debuffs.FRACTURE,          new IconDebuffRenderer<>(IconDebuffRenderer.FRACTURE_ICON));
        debuffRenderManager.registerRenderer(Debuffs.BLEED,             new IconDebuffRenderer<>(IconDebuffRenderer.BLEED_ICON));
        debuffRenderManager.registerRenderer(Debuffs.POISON_BLOCK,      new IconDebuffRenderer<>(IconDebuffRenderer.POISON_ICON));
        debuffRenderManager.registerRenderer(Debuffs.INFECTION_BLOCK,   new IconDebuffRenderer<>(IconDebuffRenderer.INFECTION_ICON));
        debuffRenderManager.registerRenderer(Debuffs.FRACTURE_BLOCK,    new IconDebuffRenderer<>(IconDebuffRenderer.FRACTURE_ICON));
        debuffRenderManager.registerRenderer(Debuffs.BLEED_BLOCK,       new IconDebuffRenderer<>(IconDebuffRenderer.BLEED_ICON));

        // skill renderers
        SkillRendererRegistry.registerRenderers(new SkillCooldownRenderer<>(), Skills.LIKE_A_CAT_I, Skills.LIKE_A_CAT_II, Skills.LIKE_A_CAT_III);
        SkillRendererRegistry.registerRenderers(new SkillCooldownRenderer<>(), Skills.SECOND_CHANCE_I, Skills.SECOND_CHANCE_II, Skills.SECOND_CHANCE_III);
        SkillRendererRegistry.registerRenderers(new SkillCooldownRenderer<>(), Skills.IRON_BUDDY_I, Skills.IRON_BUDDY_II, Skills.IRON_BUDDY_III);
        SkillRendererRegistry.registerRenderer(new SkillCooldownRenderer<>(), Skills.GOD_HELP_US);

        // event handlers
        MinecraftForge.EVENT_BUS.register(new HUDRenderer());
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
        ScreenManager.register(ModContainers.CRYSTAL_STATION.get(), CrystalStationScreen::new);
        ScreenManager.register(ModContainers.CRYSTAL_FUSE.get(), CrystalFuseStationScreen::new);
        ScreenManager.register(ModContainers.CRYSTAL_PURIFICATION.get(), CrystalPurificationStationScreen::new);
        ScreenManager.register(ModContainers.TURRET_CONTAINER.get(), TurretScreen::new);
        ScreenManager.register(ModContainers.AMMO_BENCH_CONTAINER.get(), AmmoBenchScreen::new);
    }

    private AnimationType<?>[] gatherAnimationTypes() {
        return new AnimationType[] {
                AIM_ANIMATION, SPRINT, CHAMBER, HEAL, RELOAD, RELOAD_BULLET, FIREMODE, BULLET_EJECTION, RECOIL, GRENADE, UNJAM, STASH_DETECTOR
        };
    }

    private AnimationStage[] gatherAnimationStages() {
        return new AnimationStage[] {
                MAGAZINE, SLIDE, CHARGING_HANDLE, BULLET, BOLT, BOLT_CARRIER, BARRELS, BULLET_2, BATTERY, BATTERY_COVER
        };
    }

    private AnimationEventType<?>[] gatherEvents() {
        return new AnimationEventType[] {
                STASH_DETECTOR_EVENT
        };
    }
}
