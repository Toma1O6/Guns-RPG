package lib.toma.animations.api;

public final class ScheduledAnimation<A extends IAnimation> {

    private final AnimationType<A> type;
    private final A animation;

    public ScheduledAnimation(AnimationType<A> type, A animation) {
        this.type = type;
        this.animation = animation;
    }

    public AnimationType<A> getType() {
        return type;
    }

    public A getAnimation() {
        return animation;
    }
}
