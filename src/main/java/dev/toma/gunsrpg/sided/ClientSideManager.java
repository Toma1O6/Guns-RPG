package dev.toma.gunsrpg.sided;

import dev.toma.gunsrpg.client.ClientEventHandler;
import dev.toma.gunsrpg.client.ModKeybinds;
import dev.toma.gunsrpg.client.animation.AnimationProcessor;
import dev.toma.gunsrpg.client.gui.AirdropScreen;
import dev.toma.gunsrpg.client.gui.BlastFurnaceScreen;
import dev.toma.gunsrpg.client.gui.DeathCrateScreen;
import dev.toma.gunsrpg.client.gui.SmithingTableScreen;
import dev.toma.gunsrpg.client.gui.skills.GuiPlayerSkills;
import dev.toma.gunsrpg.client.render.*;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.init.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSideManager {

    private static final ClientSideManager INSTANCE = new ClientSideManager();

    private final AnimationProcessor animations = new AnimationProcessor();

    public static ClientSideManager instance() {
        return INSTANCE;
    }

    public AnimationProcessor processor() {
        return animations;
    }

    public void clientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.AIRDROP.get(), AirdropRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.EXPLOSIVE_SKELETON.get(), ExplosiveSkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.EXPLOSIVE_ARROW.get(), ExplosiveArrowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_GUNNER.get(), ZombieGunnerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BLOODMOON_GOLEM.get(), BloodmoonGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.CROSSBOW_BOLT.get(), CrossbowBoltRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GRENADE.get(), GrenadeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ROCKET_ANGEL.get(), RocketAngelRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GOLD_DRAGON.get(), GoldenDragonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SHOTGUN_PELLET.get(), NoOpRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BULLET.get(), NoOpRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.FLARE.get(), NoOpRenderer::new);
        ModKeybinds.registerKeybinds();
        MinecraftForge.EVENT_BUS.register(new ModKeybinds());
        event.enqueueWork(this::screenSetup);
    }

    public void playDelayedSound(BlockPos pos, float volume, float pitch, SoundEvent event, SoundCategory category, int tickDelay) {
        Minecraft mc = Minecraft.getInstance();
        SoundHandler handler = mc.getSoundManager();
        handler.playDelayed(new SimpleSound(event, category, volume, pitch, pos), tickDelay);
    }

    public IPlayerData.ISynchCallback onDataSync() {
        return () -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen instanceof GuiPlayerSkills) {
                mc.screen.init(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
            }
            if (!PlayerData.getUnsafe(mc.player).getAimInfo().aiming) {
                ClientEventHandler.preAimFov.ifPresent(value -> mc.options.fov = value);
                ClientEventHandler.preAimSens.ifPresent(value -> mc.options.sensitivity = value);
            }
        };
    }

    private void screenSetup() {
        ScreenManager.register(ModContainers.AIRDROP.get(), AirdropScreen::new);
        ScreenManager.register(ModContainers.BLAST_FURNACE.get(), BlastFurnaceScreen::new);
        ScreenManager.register(ModContainers.DEATH_CRATE.get(), DeathCrateScreen::new);
        ScreenManager.register(ModContainers.SMITHING_TABLE.get(), SmithingTableScreen::new);
    }
}
