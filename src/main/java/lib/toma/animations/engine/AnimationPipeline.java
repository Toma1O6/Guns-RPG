package lib.toma.animations.engine;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.AnimationType;
import lib.toma.animations.api.IAnimation;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.profiler.IProfiler;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;
import java.util.function.Supplier;

public final class AnimationPipeline implements IAnimationPipeline {

    private final Marker MARKER = MarkerManager.getMarker("Pipeline");
    private final Map<AnimationType<?>, IAnimation> playingAnimations = new IdentityHashMap<>();
    private final List<ScheduledElement<?>> scheduledAnimations = new ArrayList<>();

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
        scheduleInsert(type, () -> type.create(Minecraft.getInstance().player), gameTickDelay);
    }

    @Override
    public <A extends IAnimation> void scheduleInsert(AnimationType<A> type, Supplier<A> supplier, int gameTickDelay) {
        if (gameTickDelay < 0)
            throw new IllegalArgumentException("Delay must be bigger than 0!");
        if (supplier == null) {
            AnimationEngine.logger.fatal(MARKER, "Attempted to schedule null animation for {} type", type);
            return;
        }
        if (gameTickDelay == 0) {
            insert(type, supplier.get());
        } else {
            scheduledAnimations.add(new ScheduledElement<>(Objects.requireNonNull(type), supplier, gameTickDelay));
        }
    }

    @Override
    public void remove(AnimationType<?> type) {
        playingAnimations.remove(type);
    }

    @Override
    public void removeScheduled(AnimationType<?> type) {
        scheduledAnimations.removeIf(element -> element.type.getKey().equals(type.getKey()));
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
    public void handleGameTick() {
        IProfiler profiler = Minecraft.getInstance().getProfiler();
        profiler.push("Animation tick");
        profiler.push("Scheduled animations");
        tickScheduled();
        profiler.popPush("Live animations");
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
        playingAnimations.values().forEach(anim -> anim.animate(stage, matrix, buffer, light, overlay));
    }

    @SuppressWarnings("unchecked")
    private <A extends IAnimation> void tickScheduled() {
        Iterator<ScheduledElement<?>> iterator = scheduledAnimations.iterator();
        while (iterator.hasNext()) {
            ScheduledElement<A> element = (ScheduledElement<A>) iterator.next();
            if (element.isDone()) {
                iterator.remove();
                insert(element.type, element.supplier.get());
            } else {
                element.tick();
            }
        }
    }

    private static class ScheduledElement<A extends IAnimation> {

        private final AnimationType<A> type;
        private final Supplier<A> supplier;
        private int gameTicksRemaining;

        private ScheduledElement(AnimationType<A> type, Supplier<A> supplier, int delay) {
            this.type = type;
            this.supplier = supplier;
            this.gameTicksRemaining = delay;
        }

        private void tick() {
            --gameTicksRemaining;
        }

        private boolean isDone() {
            return gameTicksRemaining == 0;
        }
    }
}