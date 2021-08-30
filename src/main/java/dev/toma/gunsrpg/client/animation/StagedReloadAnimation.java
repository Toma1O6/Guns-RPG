package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.Interpolate;
import lib.toma.animations.Keyframes;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.api.IKeyframeProvider;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class StagedReloadAnimation implements IModifiableProgress {

    private final IKeyframeProvider baseProvider;
    private final BooleanSupplier finished;
    private final Supplier<Float> progressFetcher;
    private final Map<AnimationStage, IKeyframe[]> frameCache;
    private final Map<AnimationStage, Integer> cache = new HashMap<>();
    private final int indexShift;
    private float progress, progressOld, progressInterpolated;
    private int indexes;

    public StagedReloadAnimation(IKeyframeProvider provider, BooleanSupplier finished, Supplier<Float> progressFetcher) {
        this.baseProvider = provider;
        this.frameCache = baseProvider.getFrameMap();
        this.finished = finished;
        this.progressFetcher = progressFetcher;
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
        float old = progressInterpolated;
        progressInterpolated = Interpolate.linear(deltaRenderTime, progress, progressOld);
        if (progressInterpolated != old) {
            updateCache();
        }
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

    private void updateCache() {
        for (Map.Entry<AnimationStage, IKeyframe[]> entry : frameCache.entrySet()) {
            AnimationStage stage = entry.getKey();
            IKeyframe[] frames = entry.getValue();
            int index = cache.computeIfAbsent(stage, k -> 0);
            IKeyframe current = frames[index];
            float end = current.endpoint();
            if (progressInterpolated > end) {
                cache.put(stage, Math.min(frames.length - 1, index + 1));
            } else if (index > 1) {
                IKeyframe prev = frames[index - 1];
                float endO = prev.endpoint();
                if (progressInterpolated <= endO) {
                    cache.put(stage, index - 1);
                }
            }
        }
    }
}
