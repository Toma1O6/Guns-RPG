package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.object.AimInfo;
import lib.toma.animations.Intepolation;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.IAnimation;
import lib.toma.animations.pipeline.frame.IKeyframe;
import lib.toma.animations.pipeline.frame.Keyframes;
import lib.toma.animations.pipeline.frame.SingleFrameProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Supplier;

public class AimAnimation implements IAnimation {

    private final SingleFrameProvider provider;
    private final Supplier<Float> progressSupplier;
    private float progress, progressO, progressI;
    private long ticksPlaying;

    public AimAnimation(SingleFrameProvider provider) {
        this.provider = provider;
        PlayerEntity player = Minecraft.getInstance().player;
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        if (optional.isPresent()) {
            AimInfo aim = optional.orElse(null).getAimInfo();
            this.progressSupplier = aim::getProgress;
        } else {
            progressSupplier = () -> 0.0F;
        }
    }

    @Override
    public void animate(AnimationStage stage, MatrixStack matrixStack) {
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
        progressI = Intepolation.linear(deltaRenderTime, progress, progressO);
    }

    @Override
    public boolean hasFinished() {
        return progressI == 0.0F && ticksPlaying >= 100;
    }
}