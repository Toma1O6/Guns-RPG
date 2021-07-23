package lib.toma.animations.pipeline;

public interface IAnimation {

    void animate(AnimationStage stage);

    void gameTick();

    void renderTick(float deltaRenderTime);

    boolean hasFinished();

    default boolean disallowItemRendering() {
        return false;
    }
}
