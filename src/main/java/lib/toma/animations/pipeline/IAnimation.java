package lib.toma.animations.pipeline;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IAnimation {

    void animate(AnimationStage stage, MatrixStack matrixStack);

    void gameTick();

    void renderTick(float deltaRenderTime);

    boolean hasFinished();

    default boolean disallowItemRendering() {
        return false;
    }
}
