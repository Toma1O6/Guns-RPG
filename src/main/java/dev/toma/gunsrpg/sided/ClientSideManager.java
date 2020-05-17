package dev.toma.gunsrpg.sided;

import dev.toma.gunsrpg.client.ModKeybinds;
import dev.toma.gunsrpg.client.render.RenderAirdrop;
import dev.toma.gunsrpg.client.render.RenderExplosiveSkeleton;
import dev.toma.gunsrpg.common.entity.EntityAirdrop;
import dev.toma.gunsrpg.common.entity.EntityExplosiveSkeleton;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientSideManager extends SideManager {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityAirdrop.class, RenderAirdrop::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExplosiveSkeleton.class, RenderExplosiveSkeleton::new);
        ModKeybinds.registerKeybinds();
        MinecraftForge.EVENT_BUS.register(new ModKeybinds());
    }
}
