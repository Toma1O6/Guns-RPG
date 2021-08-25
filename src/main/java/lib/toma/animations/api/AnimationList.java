package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import lib.toma.animations.AnimationEngine;
import net.minecraft.client.Minecraft;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class AnimationList<A extends IAnimation> implements IAnimationList<A> {

    private final List<A> animations = new LinkedList<>();
    private int runningAnimations;

    /**
     * Enqueues supplied animation for playing. Creates new animation list if no list exists.
     * @param listAnimationType List type
     * @param animation Animation to play
     * @param <A> Type of animation
     * @param <L> Type of list
     * @return List instance
     */
    public static <A extends IAnimation, L extends AnimationList<A>> L enqueue(AnimationType<L> listAnimationType, A animation) {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        boolean exists = pipeline.has(listAnimationType);
        L animationList = exists ? pipeline.get(listAnimationType) : listAnimationType.create(Minecraft.getInstance().player);
        animationList.enqueue(animation);
        return animationList;
    }

    @Override
    public void enqueue(A animation) {
        animations.add(animation);
        ++runningAnimations;
    }

    @Override
    public int size() {
        return runningAnimations;
    }

    @Override
    public void animateSpecial(AnimationStage stage, MatrixStack poseStack, IVertexBuilder vertexBuilder, int light, int overlay) {
        for (A anim : animations) {
            poseStack.pushPose();
            anim.animate(stage, poseStack);
            renderAdditional(poseStack, vertexBuilder, light, overlay);
            poseStack.popPose();
        }
    }

    @Override
    public void animate(AnimationStage stage, MatrixStack matrixStack) {
        for (A anim : animations) {
            matrixStack.pushPose();
            anim.animate(stage, matrixStack);
            matrixStack.popPose();
        }
    }

    @Override
    public void gameTick() {
        Iterator<A> iterator = animations.iterator();
        while (iterator.hasNext()) {
            A animation = iterator.next();
            if (animation.hasFinished()) {
                iterator.remove();
                --runningAnimations;
            }
            animation.gameTick();
        }
    }

    @Override
    public void renderTick(float deltaRenderTime) {
        for (A anim : animations) {
            anim.renderTick(deltaRenderTime);
        }
    }

    @Override
    public boolean hasFinished() {
        return runningAnimations == 0;
    }
}
