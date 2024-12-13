package dev.toma.gunsrpg.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.world.cap.WorldData;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resources.IResourceManagerReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements IResourceManagerReloadListener, AutoCloseable {

    @Shadow private ClientWorld level;

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I"))
    private void gunsrpg$setMoonColor(MatrixStack stack, float delta, CallbackInfo ci) {
        int bloodmoonCycle = GunsRPG.config.world.bloodmoonCycle;
        if (bloodmoonCycle == -1)
            return;
        long day = level.getDayTime() / 24000L;
        boolean isNight = level.getDayTime() % 24000L >= 11000L;
        boolean isBloodmoonDay = day > 0 && (bloodmoonCycle == 0 || day % bloodmoonCycle == 0);
        if (isNight && isBloodmoonDay)
            RenderSystem.color4f(1.0F, 0.0F, 0.0F, 1.0F);
    }

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getStarBrightness(F)F"))
    private void gunsrpg$clearMoonColor(MatrixStack stack, float delta, CallbackInfo ci) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
