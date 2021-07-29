package lib.toma.animations.pipeline;

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
        progressInterpolated = progressOld + (progress - progressOld) * deltaRenderTime;
        nextFrame(progressInterpolated, old);
    }

    public float getProgress() {
        return 1.0F - ((float) ticksRemaining / length);
    }

    public float getIntepolatedProgress() {
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
