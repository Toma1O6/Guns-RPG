package dev.toma.gunsrpg.sided;

import dev.toma.gunsrpg.client.ClientEventHandler;
import dev.toma.gunsrpg.client.ModKeybinds;
import dev.toma.gunsrpg.client.animation.AnimationProcessor;
import dev.toma.gunsrpg.client.gui.GuiAirdrop;
import dev.toma.gunsrpg.client.gui.GuiBlastFurnace;
import dev.toma.gunsrpg.client.gui.GuiDeathCrate;
import dev.toma.gunsrpg.client.gui.GuiSmithingTable;
import dev.toma.gunsrpg.client.gui.skills.GuiPlayerSkills;
import dev.toma.gunsrpg.client.render.*;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.init.GRPGContainers;
import dev.toma.gunsrpg.common.init.GRPGEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Supplier;

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
        RenderingRegistry.registerEntityRenderingHandler(GRPGEntityTypes.AIRDROP.get(), RenderAirdrop::new);
        RenderingRegistry.registerEntityRenderingHandler(GRPGEntityTypes.EXPLOSIVE_SKELETON.get(), RenderExplosiveSkeleton::new);
        RenderingRegistry.registerEntityRenderingHandler(GRPGEntityTypes.EXPLOSIVE_ARROW.get(), RenderExplosiveArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(GRPGEntityTypes.ZOMBIE_GUNNER.get(), RenderZombieGunner::new);
        RenderingRegistry.registerEntityRenderingHandler(GRPGEntityTypes.BLOODMOON_GOLEM.get(), RenderBloodmoonGolem::new);
        RenderingRegistry.registerEntityRenderingHandler(GRPGEntityTypes.CROSSBOW_BOLT.get(), CrossbowBoltRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GRPGEntityTypes.GRENADE.get(), RenderGrenade::new);
        RenderingRegistry.registerEntityRenderingHandler(GRPGEntityTypes.ROCKET_ANGEL.get(), RenderRocketAngel::new);
        RenderingRegistry.registerEntityRenderingHandler(GRPGEntityTypes.GOLD_DRAGON.get(), RenderGoldenDragon::new);
        ModKeybinds.registerKeybinds();
        MinecraftForge.EVENT_BUS.register(new ModKeybinds());
        event.enqueueWork(this::screenSetup);
    }

    public static void runOnClient(Supplier<DistExecutor.SafeRunnable> runnableSupplier) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, runnableSupplier);
    }

    public void playDelayedSound(BlockPos pos, float volume, float pitch, SoundEvent event, SoundCategory category, int tickDelay) {
        Minecraft mc = Minecraft.getInstance();
        SoundHandler handler = mc.getSoundManager();
        handler.playDelayed(new SimpleSound(event, category, volume, pitch, pos), tickDelay);
    }

    public void onDataSync() {
        Minecraft mc = Minecraft.getInstance();
        if(mc.screen instanceof GuiPlayerSkills) {
            mc.screen.init(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
        }
        if(!PlayerDataFactory.getUnsafe(mc.player).getAimInfo().aiming) {
            ClientEventHandler.preAimFov.ifPresent(value -> mc.options.fov = value);
            ClientEventHandler.preAimSens.ifPresent(value -> mc.options.sensitivity = value);
        }
    }

    private void screenSetup() {
        ScreenManager.register(GRPGContainers.AIRDROP.get(), GuiAirdrop::new);
        ScreenManager.register(GRPGContainers.BLAST_FURNACE.get(), GuiBlastFurnace::new);
        ScreenManager.register(GRPGContainers.DEATH_CRATE.get(), GuiDeathCrate::new);
        ScreenManager.register(GRPGContainers.SMITHING_TABLE.get(), GuiSmithingTable::new);
    }
}
