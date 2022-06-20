package lib.toma.animations.engine;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.profiler.IProfiler;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;

public final class AnimationPipeline implements IAnimationPipeline {

    private final Marker MARKER = MarkerManager.getMarker("Pipeline");
    private final Map<AnimationType<?>, IAnimation> playingAnimations = new TreeMap<>();
    private final List<Schedule<ScheduledAnimation<?>>> scheduledAnimations = new ArrayList<>();

    public AnimationPipeline() {}

    @Override
    public <A extends IAnimation> void insert(AnimationType<A> type) {
        if (!type.hasCreator())
            AnimationEngine.logger.fatal(MARKER, "Cannot create default animation from animation type ({}) with undefined AnimationCreator", type);
        A animation = type.create(Minecraft.getInstance().player);
        insert(type, animation);
    }

    @Override
    public <A extends IAnimation> void insert(AnimationType<A> type, A animation) {
        if (animation == null) {
            AnimationEngine.logger.fatal(MARKER, "Attempted to play null animation for {} type, aborting!", type);
            return;
        }
        playingAnimations.put(Objects.requireNonNull(type), animation);
    }

    @Override
    public <A extends IAnimation> void scheduleInsert(AnimationType<A> type, int gameTickDelay) {
        if (!type.hasCreator())
            AnimationEngine.logger.fatal(MARKER, "Cannot create default animation from animation type ({}) with undefined AnimationCreator", type);
        scheduleInsert(type, type.create(Minecraft.getInstance().player), gameTickDelay);
    }

    @Override
    public <A extends IAnimation> void scheduleInsert(AnimationType<A> type, A animation, int gameTickDelay) {
        if (gameTickDelay < 0)
            throw new IllegalArgumentException("Delay must be bigger than 0!");
        if (animation == null) {
            AnimationEngine.logger.fatal(MARKER, "Attempted to schedule null animation for {} type", type);
            return;
        }
        if (gameTickDelay == 0) {
            insert(type, animation);
        } else {
            scheduledAnimations.add(new Schedule<>(gameTickDelay, () -> new ScheduledAnimation<>(Objects.requireNonNull(type), animation)));
        }
    }

    @Override
    public void remove(AnimationType<?> type) {
        playingAnimations.remove(type);
    }

    @Override
    public void removeScheduled(AnimationType<?> type) {
        scheduledAnimations.removeIf(schedule -> schedule.get().getType().getKey().equals(type.getKey()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A extends IAnimation> A get(AnimationType<A> type) {
        return (A) playingAnimations.get(type);
    }

    @Override
    public boolean has(AnimationType<?> type) {
        return playingAnimations.containsKey(type);
    }

    @Override
    public boolean hasScheduled(AnimationType<?> type) {
        for (Schedule<ScheduledAnimation<?>> schedule : scheduledAnimations) {
            ScheduledAnimation<?> animation = schedule.get();
            if (animation.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleGameTick() {
        IProfiler profiler = Minecraft.getInstance().getProfiler();
        profiler.push("animationTick");
        profiler.push("scheduledTick");
        tickScheduled();
        profiler.popPush("activeTick");
        Iterator<IAnimation> it1 = playingAnimations.values().iterator();
        while (it1.hasNext()) {
            IAnimation animation = it1.next();
            if (animation.hasFinished())
                it1.remove();
            animation.gameTick();
        }
        profiler.pop();
        profiler.pop();
    }

    @Override
    public void processFrame(float deltaRenderTime) {
        playingAnimations.values().forEach(anim -> anim.renderTick(deltaRenderTime));
    }

    @Override
    public void animateStage(AnimationStage stage, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay) {
        IProfiler profiler = Minecraft.getInstance().getProfiler();
        profiler.push("animationRender");
        playingAnimations.values().forEach(anim -> anim.animate(stage, matrix, buffer, light, overlay));
        profiler.pop();
    }

    @SuppressWarnings("unchecked")
    private <A extends IAnimation> void tickScheduled() {
        Iterator<Schedule<ScheduledAnimation<?>>> iterator = scheduledAnimations.iterator();
        while (iterator.hasNext()) {
            Schedule<ScheduledAnimation<?>> schedule = iterator.next();
            if (schedule.done()) {
                iterator.remove();
                ScheduledAnimation<A> scheduledAnimation = (ScheduledAnimation<A>) schedule.get();
                AnimationType<A> type = scheduledAnimation.getType();
                A animation = scheduledAnimation.getAnimation();
                insert(type, animation);
            } else {
                schedule.update();
            }
        }
    }
}
