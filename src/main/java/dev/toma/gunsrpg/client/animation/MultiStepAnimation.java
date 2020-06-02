package dev.toma.gunsrpg.client.animation;

import dev.toma.gunsrpg.client.animation.impl.SimpleAnimation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiStepAnimation extends TickableAnimation {

    protected final List<Pair<Range, Animation>> steps = new ArrayList<>();
    private int index;
    private Pair<Range, Animation> current;

    public MultiStepAnimation(int length) {
        super(length);
        this.createAnimationSteps();
        if(steps.isEmpty()) throw new IllegalArgumentException("Animation cannot be empty!");
        current = steps.get(index);
    }

    public abstract void createAnimationSteps();

    public final void addStep(float from, float to, Animation animation) {
        this.steps.add(Pair.of(new Range(from, to), animation));
    }

    @Override
    public void animateItemHands(float partialTicks) {
        current.getRight().animateItemHands(partialTicks);
    }

    @Override
    public void animateHands(float partialTicks) {
        current.getRight().animateHands(partialTicks);
    }

    @Override
    public void animateRightArm(float partialTicks) {
        current.getRight().animateRightArm(partialTicks);
    }

    @Override
    public void animateLeftArm(float partialTicks) {
        current.getRight().animateLeftArm(partialTicks);
    }

    @Override
    public void animateItem(float partialTicks) {
        current.getRight().animateItem(partialTicks);
    }

    @Override
    public void renderTick(float partialTicks, TickEvent.Phase phase) {
        current.getRight().renderTick(partialTicks, phase);
    }

    @Override
    public void update() {
        super.update();
        float f = 1.0F - this.getCurrentProgress();
        Range range = current.getLeft();
        Animation animation = current.getRight();
        if(range.isIn(f)) {
            animation.clientTick();
            float f1 = range.getProgress(f);
            animation.setProgress(f1);
        } else if(range.isOver(f)) {
            if(!this.isAtLastStep()) {
                ++index;
                current = steps.get(index);
            }
        }
    }

    public boolean isAtLastStep() {
        return index == steps.size() - 1;
    }

    public static SimpleAnimation.Builder animation() {
        return SimpleAnimation.newSimpleAnimation();
    }

    public static class SR extends MultiStepAnimation {
        public SR(int time) {
            super(time);
        }
        @Override
        public void createAnimationSteps() {

        }
    }

    public static class SG extends MultiStepAnimation {
        public SG(int time) {
            super(time);
        }
        @Override
        public void createAnimationSteps() {

        }
    }

    public static class Pistol extends MultiStepAnimation {

        public Pistol(int time) {
            super(time);
        }

        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.1F, animation()
                    .itemHand(f -> GlStateManager.rotate(15.0F * f, 1.0F, 0.0F, 0.0F))
                    .create());

            addStep(0.1F, 0.2F, animation()
                    .itemHand(f -> GlStateManager.rotate(15.0F, 1.0F, 0.0F, 0.0F))
                    .leftHand(f -> GlStateManager.rotate(-60.0F * f, 1.0F, 0.0F, 0.0F))
                    .create());

            addStep(0.2F, 0.7F, animation()
                    .itemHand(f -> GlStateManager.rotate(15.0F, 1.0F, 0.0F, 0.0F))
                    .leftHand(f -> GlStateManager.rotate(-60.0F, 1.0F, 0.0F, 0.0F))
                    .create());

            addStep(0.7F, 0.8F, animation()
                    .itemHand(f -> GlStateManager.rotate(15.0F, 1.0F, 0.0F, 0.0F))
                    .leftHand(f -> GlStateManager.rotate(-60.0F + 60.0F * f, 1.0F, 0.0F, 0.0F))
                    .create());

            addStep(0.8F, 0.85F, animation()
                    .itemHand(f -> GlStateManager.rotate(15.0F + 5.0F * f, 1.0F, 0.0F, 0.0F))
                    .create());

            addStep(0.85F, 1.0F, animation()
                    .itemHand(f -> GlStateManager.rotate(20.0F - 20.0F * f, 1.0F, 0.0F, 0.0F))
                    .create());
        }
    }

    public static class Range {

        public final float min, max;

        public Range(float min, float max) {
            this.min = min;
            this.max = max;
        }

        public float getProgress(float progress) {
            return (progress - min) / (max - min);
        }

        public boolean isIn(float val) {
            return val > min && val <= max;
        }

        public boolean isOver(float val) {
            return val >= max;
        }

        @Override
        public String toString() {
            return "Range{" +
                    "min=" + min +
                    ", max=" + max +
                    '}';
        }
    }
}
