package dev.toma.gunsrpg.client.animation.impl;

import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.util.object.OptionalObject;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;
import java.util.function.Consumer;

public class SimpleAnimation implements Animation {

    private float current, prev, smooth;
    private final OptionalObject<Consumer<Float>> itemHandAnimation = OptionalObject.empty();
    private final OptionalObject<Consumer<Float>> handAnimation = OptionalObject.empty();
    private final OptionalObject<Consumer<Float>> rightHandAnimation = OptionalObject.empty();
    private final OptionalObject<Consumer<Float>> leftHandAnimation = OptionalObject.empty();
    private final OptionalObject<Consumer<Float>> itemAnimation = OptionalObject.empty();

    private SimpleAnimation() {}

    public static Builder newSimpleAnimation() {
        return new Builder();
    }

    @Override
    public void animateItemHands(float partialTicks) {
        itemHandAnimation.ifPresent(f -> f.accept(smooth));
    }

    @Override
    public void animateHands(float partialTicks) {
        handAnimation.ifPresent(f -> f.accept(smooth));
    }

    @Override
    public void animateRightArm(float partialTicks) {
        rightHandAnimation.ifPresent(f -> f.accept(smooth));
    }

    @Override
    public void animateLeftArm(float partialTicks) {
        leftHandAnimation.ifPresent(f -> f.accept(smooth));
    }

    @Override
    public void animateItem(float partialTicks) {
        itemAnimation.ifPresent(f -> f.accept(smooth));
    }

    @Override
    public void clientTick() {
        prev = current;
    }

    @Override
    public void renderTick(float partialTicks, TickEvent.Phase phase) {
        if(phase == TickEvent.Phase.START) {
            this.calculateSmoothValue(partialTicks);
        }
    }

    // Doesn't do anything anyway
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void setProgress(float progress) {
        this.current = progress;
    }

    private void calculateSmoothValue(float partialTicks) {
        smooth = prev + (current - prev) * partialTicks;
    }

    public static class Builder {

        private Consumer<Float> iha;
        private Consumer<Float> ha;
        private Consumer<Float> rha;
        private Consumer<Float> lha;
        private Consumer<Float> ia;

        private Builder() {}

        public SimpleAnimation create() {
            SimpleAnimation simpleImpl = new SimpleAnimation();
            simpleImpl.itemHandAnimation.map(iha);
            simpleImpl.handAnimation.map(ha);
            simpleImpl.rightHandAnimation.map(rha);
            simpleImpl.leftHandAnimation.map(lha);
            simpleImpl.itemAnimation.map(ia);
            return simpleImpl;
        }

        public Builder itemHand(Consumer<Float> properties) {
            iha = Objects.requireNonNull(properties, "Animation cannot be null!");
            return this;
        }

        public Builder hand(Consumer<Float> properties) {
            ha = Objects.requireNonNull(properties, "Animation cannot be null!");
            return this;
        }

        public Builder rightHand(Consumer<Float> properties) {
            rha = Objects.requireNonNull(properties, "Animation cannot be null!");
            return this;
        }

        public Builder leftHand(Consumer<Float> properties) {
            lha = Objects.requireNonNull(properties, "Animation cannot be null!");
            return this;
        }

        public Builder item(Consumer<Float> properties) {
            ia = Objects.requireNonNull(properties, "Animation cannot be null!");
            return this;
        }
    }
}
