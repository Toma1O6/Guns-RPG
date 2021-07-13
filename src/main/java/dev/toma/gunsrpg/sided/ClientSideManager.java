package dev.toma.gunsrpg.sided;

import dev.toma.gunsrpg.client.ClientEventHandler;
import dev.toma.gunsrpg.client.ModKeybinds;
import dev.toma.gunsrpg.client.animation.AnimationProcessor;
import dev.toma.gunsrpg.client.gui.skills.GuiPlayerSkills;
import dev.toma.gunsrpg.client.render.*;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.*;
import dev.toma.gunsrpg.common.init.GRPGEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Supplier;

public class ClientSideManager {

    private static final AnimationProcessor ANIMATION_PROCESSOR = new AnimationProcessor();

    public static AnimationProcessor processor() {
        return ANIMATION_PROCESSOR;
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(AirdropEntity.class, RenderAirdrop::new);
        RenderingRegistry.registerEntityRenderingHandler(ExplosiveSkeletonEntity.class, RenderExplosiveSkeleton::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExplosiveArrow.class, RenderExplosiveArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(ZombieGunnerEntity.class, RenderZombieGunner::new);
        RenderingRegistry.registerEntityRenderingHandler(BloodmoonGolemEntity.class, RenderBloodmoonGolem::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCrossbowBolt.class, CrossbowBoltRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GRPGEntityTypes.GRENADE.get(), RenderGrenade::new);
        RenderingRegistry.registerEntityRenderingHandler(GRPGEntityTypes.ROCKET_ANGEL.get(), RenderRocketAngel::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGoldDragon.class, RenderGoldenDragon::new);
        ModKeybinds.registerKeybinds();
        MinecraftForge.EVENT_BUS.register(new ModKeybinds());
    }

    public static void runOnClient(Supplier<DistExecutor.SafeRunnable> runnableSupplier) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, runnableSupplier);
    }

    public static void onDataSync() {
        Minecraft mc = Minecraft.getInstance();
        if(mc.screen instanceof GuiPlayerSkills) {
            mc.screen.init(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
        }
        if(!PlayerDataFactory.getUnsafe(mc.player).getAimInfo().aiming) {
            ClientEventHandler.preAimFov.ifPresent(value -> mc.options.fov = value);
            ClientEventHandler.preAimSens.ifPresent(value -> mc.options.sensitivity = value);
        }
    }
}
