package dev.toma.gunsrpg.sided;

import dev.toma.gunsrpg.client.ModKeybinds;
import dev.toma.gunsrpg.client.animation.ScriptLoader;
import dev.toma.gunsrpg.client.animation.builder.BuilderMain;
import dev.toma.gunsrpg.client.render.*;
import dev.toma.gunsrpg.common.entity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientSideManager extends SideManager {

    public static ScriptLoader scriptLoader = new ScriptLoader();

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityAirdrop.class, RenderAirdrop::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExplosiveSkeleton.class, RenderExplosiveSkeleton::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExplosiveArrow.class, RenderExplosiveArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityZombieGunner.class, RenderZombieGunner::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBloodmoonGolem.class, RenderBloodmoonGolem::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCrossbowBolt.class, CrossbowBoltRenderer::new);
        ModKeybinds.registerKeybinds();
        MinecraftForge.EVENT_BUS.register(new ModKeybinds());
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(scriptLoader);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        BuilderMain.init();
    }

    @Override
    public void playDelayedSound(float x, float y, float z, float volume, float pitch, SoundEvent event, SoundCategory category, int delay) {
        SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
        PositionedSoundRecord sound = new PositionedSoundRecord(event, category, volume, pitch, x, y, z);
        handler.playDelayedSound(sound, delay);
    }
}
