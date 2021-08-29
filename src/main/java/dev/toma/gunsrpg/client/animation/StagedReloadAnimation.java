package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import lib.toma.animations.Interpolate;
import lib.toma.animations.Keyframes;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.api.IKeyframeProvider;
import lib.toma.animations.engine.frame.SingleFrameProvider;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class StagedReloadAnimation implements IModifiableProgress {

    private final IKeyframeProvider baseProvider;
    private final BooleanSupplier finished;
    private final Supplier<Float> progressFetcher;
    private final int indexShift;
    private float progress, progressOld, progressInterpolated;
    private int indexes;

    public StagedReloadAnimation(IKeyframeProvider provider, BooleanSupplier finished, Supplier<Float> progressFetcher) {
        this.baseProvider = provider;
        this.finished = finished;
        this.progressFetcher = progressFetcher;
        if (!(baseProvider instanceof SingleFrameProvider)) // TODO bake into single frame provider
            GunsRPG.log.warn("Invalid keyframe provider for staged reload animation, expected single frame, got {}", provider.getClass().getSimpleName());
        Set<AnimationStage> stages = provider.getFrameMap().keySet();
        indexShift = stages.stream().mapToInt(AnimationStage::getIndex).min().orElse(0);
        for (AnimationStage stage : provider.getFrameMap().keySet()) {
            saveIndex(stage.getIndex() - indexShift);
        }
    }

    @Override
    public void gameTick() {
        progressOld = progress;
        progress = getProgress();
    }

    @Override
    public void renderTick(float deltaRenderTime) {
        progressInterpolated = Interpolate.linear(deltaRenderTime, progress, progressOld);
    }

    @Override
    public void animate(AnimationStage stage, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
        if (!containsIndex(stage.getIndex() - indexShift)) return;
        IKeyframe frame = baseProvider.getCurrentFrame(stage, progressInterpolated, 0);
        Keyframes.processFrame(frame, progressInterpolated, matrixStack);
    }

    @Override
    public boolean hasFinished() {
        return finished.getAsBoolean();
    }

    @Override
    public float getProgress() {
        return progressFetcher.get();
    }

    private void saveIndex(int index) {
        indexes |= 1 << index;
    }

    private boolean containsIndex(int index) {
        int value = 1 << index;
        return (indexes & value) == value;
    }
}
