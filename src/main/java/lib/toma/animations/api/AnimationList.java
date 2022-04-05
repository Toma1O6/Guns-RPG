package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.engine.Schedule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class AnimationList<A extends IAnimation> implements IAnimationList<A> {

    private final List<A> animations = new LinkedList<>();
    private final List<Schedule<ListAnimationSchedule<A>>> schedules = new ArrayList<>();
    private int runningAnimations;

    protected AnimationList(PlayerEntity player) {
    }

    public static <A extends IAnimation> AnimationList<A> newList(PlayerEntity player) {
        return new AnimationList<>(player);
    }

    public static <A extends IAnimation, L extends AnimationList<A>> L schedule(AnimationType<L> listAnimationType, A animation, int scheduleIn) {
        L list = getOrCreateAnimationList(listAnimationType);
        list.schedule(scheduleIn, animation);
        return list;
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
        L list = getOrCreateAnimationList(listAnimationType);
        list.enqueue(animation);
        return list;
    }

    public static <L extends AnimationList<?>> L getOrCreateAnimationList(AnimationType<L> type) {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        boolean exists = pipeline.has(type);
        L list;
        if (exists) {
            list = pipeline.get(type);
        } else {
            PlayerEntity client = Minecraft.getInstance().player;
            list = type.create(client);
            pipeline.insert(type, list);
        }
        return list;
    }

    @Override
    public void enqueue(A animation) {
        animations.add(animation);
        ++runningAnimations;
    }

    public <L extends AnimationList<A>> void schedule(int ticks, A animation) {
        Supplier<ListAnimationSchedule<A>> animationSchedule = () -> new ListAnimationSchedule<>(animation);
        Schedule<ListAnimationSchedule<A>> schedule = new Schedule<>(ticks, animationSchedule);
        schedules.add(schedule);
    }

    @Override
    public int size() {
        return runningAnimations;
    }

    @Override
    public void animate(AnimationStage stage, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
        for (A anim : animations) {
            anim.animate(stage, matrixStack, typeBuffer, light, overlay);
        }
    }

    @Override
    public void gameTick() {
        tickSchedules();
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
        return schedules.isEmpty() && runningAnimations == 0;
    }

    private void tickSchedules() {
        Iterator<Schedule<ListAnimationSchedule<A>>> iterator = schedules.iterator();
        while (iterator.hasNext()) {
            Schedule<ListAnimationSchedule<A>> schedule = iterator.next();
            if (schedule.done()) {
                ListAnimationSchedule<A> listSchedule = schedule.get();
                A animation = listSchedule.animation;
                enqueue(animation);
                iterator.remove();
            } else {
                schedule.update();
            }
        }
    }

    private static class ListAnimationSchedule<A extends IAnimation> {

        private final A animation;

        public ListAnimationSchedule(A animation) {
            this.animation = animation;
        }
    }
}
