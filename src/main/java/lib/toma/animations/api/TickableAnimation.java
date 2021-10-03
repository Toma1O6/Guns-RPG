package lib.toma.animations.api;

import lib.toma.animations.AnimationUtils;

/**
 * Animation implementation where animation progress is based on defined time (ticks).
 * Animation is automatically stopped after its lifetime is over.
 */
public abstract class TickableAnimation implements IAnimation {

    private final int length;
    private int ticksRemaining;
    private float progress;
    private float progressOld;
    private float progressInterpolated;

    public TickableAnimation(int length) {
        this.length = length;
        this.ticksRemaining = length;
    }

    @Override
    public final void renderTick(float deltaRenderTime) {
        float old = progressInterpolated;
        progressInterpolated = AnimationUtils.linearInterpolate(progress, progressOld, deltaRenderTime);
        nextFrame(progressInterpolated, old);
    }

    /**
     * Gets current animation progress from remaining ticks.
     * Progress is calculated via following formula: </br>
     * {@code progress = 1 - (remainingTicks / initialLength)}.
     * @return Animation progress based on remaining ticks
     */
    public float getProgress() {
        return 1.0F - ((float) ticksRemaining / length);
    }

    /**
     * @return Interpolated progress value.
     */
    public float getInterpolatedProgress() {
        return progressInterpolated;
    }

    @Override
    public void gameTick() {
        --ticksRemaining;
        progressOld = progress;
        progress = getProgress();
    }

    @Override
    public boolean hasFinished() {
        return ticksRemaining <= 0;
    }

    public void nextFrame(float actualProgress, float previousProgress) {}
}
