package lib.toma.animations.pipeline;

import com.mojang.blaze3d.matrix.MatrixStack;

import java.util.function.Supplier;

public interface IAnimationPipeline {

    <A extends IAnimation> void insert(AnimationType<A> type);

    <A extends IAnimation> void insert(AnimationType<A> type, A animation);

    <A extends IAnimation> void scheduleInsert(AnimationType<A> type, int gameTickDelay);

    <A extends IAnimation> void scheduleInsert(AnimationType<A> type, Supplier<A> supplier, int gameTickDelay);

    void remove(AnimationType<?> type);

    void removeScheduled(AnimationType<?> type);

    <A extends IAnimation> A get(AnimationType<A> type);

    boolean has(AnimationType<?> type);

    <A extends IAnimation> void handleGameTick();

    void processFrame(float deltaRenderTime);

    void animateStage(AnimationStage stage, MatrixStack matrix);

    boolean isItemRenderBlocked();
}
