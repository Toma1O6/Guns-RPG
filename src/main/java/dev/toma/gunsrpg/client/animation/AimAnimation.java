package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IAimInfo;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import lib.toma.animations.Interpolate;
import lib.toma.animations.Keyframes;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.IAnimation;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.api.IKeyframeProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Supplier;

public class AimAnimation implements IAnimation {

    private final IKeyframeProvider provider;
    private final Supplier<Float> progressSupplier;
    private float progress, progressO, progressI;
    private long ticksPlaying;

    public AimAnimation(IKeyframeProvider provider) {
        this.provider = provider;
        PlayerEntity player = Minecraft.getInstance().player;
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        if (optional.isPresent()) {
            IAimInfo aim = optional.orElse(null).getAimInfo();
            this.progressSupplier = aim::getProgress;
        } else {
            progressSupplier = () -> 0.0F;
        }
    }

    @Override
    public void animate(AnimationStage stage, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
        IKeyframe frame = provider.getCurrentFrame(stage, progressI, 0);
        Keyframes.processFrame(frame, progressI, matrixStack);
    }

    @Override
    public void gameTick() {
        progressO = progress;
        progress = progressSupplier.get();
        ++ticksPlaying;
    }

    @Override
    public void renderTick(float deltaRenderTime) {
        progressI = Interpolate.linear(deltaRenderTime, progress, progressO);
    }

    @Override
    public boolean hasFinished() {
        return progressI == 0.0F && ticksPlaying >= 100;
    }
}
