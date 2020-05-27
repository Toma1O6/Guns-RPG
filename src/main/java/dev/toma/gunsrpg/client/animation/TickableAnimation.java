package dev.toma.gunsrpg.client.animation;

public abstract class TickableAnimation extends AbstractAnimation {

    protected final int length;
    protected int ticksLeft;

    public TickableAnimation(int ticks) {
        this.length = ticks;
        this.ticksLeft = ticks;
    }

    @Override
    public float getCurrentProgress() {
        return ticksLeft / (float) length;
    }

    public int getTicksLeft() {
        return ticksLeft;
    }

    @Override
    public void update() {
        --ticksLeft;
    }

    @Override
    public boolean isFinished() {
        return ticksLeft <= 0;
    }
}
