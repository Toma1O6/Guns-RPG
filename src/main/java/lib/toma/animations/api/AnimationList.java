package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.AnimationEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AnimationList<A extends IAnimation> implements IAnimationList<A> {

    private final List<A> animations = new LinkedList<>();
    private int runningAnimations;

    protected AnimationList(PlayerEntity player) {
    }

    public static <A extends IAnimation> AnimationList<A> newList(PlayerEntity player) {
        return new AnimationList<>(player);
    }

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
        L animationList;
        if (exists) {
            animationList = pipeline.get(listAnimationType);
        } else {
            animationList = listAnimationType.create(Minecraft.getInstance().player);
            pipeline.insert(listAnimationType, animationList);
        }
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
    public void animate(AnimationStage stage, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
        for (A anim : animations) {
            matrixStack.pushPose();
            anim.animate(stage, matrixStack, typeBuffer, light, overlay);
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
