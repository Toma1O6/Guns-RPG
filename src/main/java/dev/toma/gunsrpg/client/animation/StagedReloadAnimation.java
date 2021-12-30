package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.client.IModifiableProgress;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.Keyframes;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.api.IKeyframeProvider;
import lib.toma.animations.api.event.FlowDirection;
import lib.toma.animations.api.event.IAnimationDirectionProvider;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class StagedReloadAnimation implements IModifiableProgress, IAnimationDirectionProvider {

    private final BooleanSupplier finished;
    private final Supplier<Float> progressFetcher;
    private final Map<AnimationStage, IKeyframe[]> frameCache;
    private float progress, progressOld, progressInterpolated;
    private FlowDirection direction = FlowDirection.FORWARD;

    public StagedReloadAnimation(IKeyframeProvider provider, BooleanSupplier finished, Supplier<Float> progressFetcher) {
        this.frameCache = provider.getFrameMap();
        this.finished = finished;
        this.progressFetcher = progressFetcher;
    }

    @Override
    public FlowDirection getDirection() {
        return direction;
    }

    @Override
    public void gameTick() {
        progressOld = progress;
        progress = getProgress();
        direction = progress > progressOld ? FlowDirection.FORWARD : progress < progressOld ? FlowDirection.BACKWARD : direction;
    }

    @Override
    public void renderTick(float deltaRenderTime) {
        progressInterpolated = AnimationUtils.linearInterpolate(progress, progressOld, deltaRenderTime);
    }

    @Override
    public void animate(AnimationStage stage, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
        IKeyframe[] frames = frameCache.get(stage);
        if (frames == null)
            return;
        int frameIndex = getFrameIndex(frames);
        IKeyframe frame = getKeyframe(frameIndex, frames);
        IKeyframe old = getKeyframe(frameIndex - 1, frames);
        float actualProgress = frame.endpoint();
        float prevProgress = old.endpoint();
        float min = actualProgress - prevProgress;
        float progress = min == 0.0F ? 1.0F : (progressInterpolated - prevProgress) / (actualProgress - prevProgress);
        Keyframes.processFrame(frame, progress, matrixStack);
    }

    @Override
    public boolean hasFinished() {
        return finished.getAsBoolean();
    }

    @Override
    public float getProgress() {
        return progressFetcher.get();
    }

    private int getFrameIndex(IKeyframe[] frames) {
        boolean inverse = progressInterpolated > 0.5;
        if (inverse) {
            for (int i = frames.length - 2; i >= 0; i--) {
                IKeyframe keyframe = frames[i];
                if (keyframe.endpoint() <= progressInterpolated) {
                    return i + 1;
                }
            }
        } else {
            IKeyframe last = frames[0];
            for (int i = 1; i < frames.length; i++) {
                IKeyframe keyframe = frames[i];
                if (last.endpoint() <= progressInterpolated && keyframe.endpoint() > progressInterpolated) {
                    return i;
                }
                last = keyframe;
            }
        }
        return -1;
    }

    private IKeyframe getKeyframe(int index, IKeyframe[] frames) {
        return index < 0 ? Keyframes.none() : frames[index];
    }
}
