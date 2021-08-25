package lib.toma.animations.engine;

import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;

public final class TickHandler {

    public void onGameTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        pipeline.handleGameTick();
    }

    public void onFrameTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        float deltaTime = event.renderTickTime;
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        pipeline.processFrame(deltaTime);
    }
}
