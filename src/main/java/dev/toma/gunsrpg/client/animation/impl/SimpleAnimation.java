package dev.toma.gunsrpg.client.animation.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.util.object.OptionalObject;

import java.util.Objects;

public class SimpleAnimation implements IAnimation {

    private final OptionalObject<IAnimator> itemHandAnimation = OptionalObject.empty();
    private final OptionalObject<IAnimator> handAnimation = OptionalObject.empty();
    private final OptionalObject<IAnimator> rightHandAnimation = OptionalObject.empty();
    private final OptionalObject<IAnimator> leftHandAnimation = OptionalObject.empty();
    private final OptionalObject<IAnimator> itemAnimation = OptionalObject.empty();
    private float current, prev, smooth;

    private SimpleAnimation() {
    }

    public static Builder newSimpleAnimation() {
        return new Builder();
    }

    @Override
    public void animateItemHands(MatrixStack matrix, float partialTicks) {
        itemHandAnimation.ifPresent(animator -> animator.animate(matrix, smooth));
    }

    @Override
    public void animateHands(MatrixStack matrix, float partialTicks) {
        handAnimation.ifPresent(animator -> animator.animate(matrix, smooth));
    }

    @Override
    public void animateRightArm(MatrixStack matrix, float partialTicks) {
        rightHandAnimation.ifPresent(animator -> animator.animate(matrix, smooth));
    }

    @Override
    public void animateLeftArm(MatrixStack matrix, float partialTicks) {
        leftHandAnimation.ifPresent(animator -> animator.animate(matrix, smooth));
    }

    @Override
    public void animateItem(MatrixStack matrix, float partialTicks) {
        itemAnimation.ifPresent(animator -> animator.animate(matrix, smooth));
    }

    @Override
    public void clientTick() {
        prev = current;
    }

    @Override
    public void frameTick(float partialTicks) {
        this.calculateSmoothValue(partialTicks);
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

    /**
     * Not called, override {@link MultiStepAnimation#cancelsItemRender()}
     *
     * @return whether item should be rendered
     */
    @Override
    public boolean cancelsItemRender() {
        return false;
    }

    public static class Builder {

        private IAnimator itemAndHandsAnimator;
        private IAnimator handsAnimator;
        private IAnimator rightHandAnimator;
        private IAnimator leftHandAnimator;
        private IAnimator itemAnimator;

        private Builder() {
        }

        public SimpleAnimation create() {
            SimpleAnimation simpleImpl = new SimpleAnimation();
            simpleImpl.itemHandAnimation.map(itemAndHandsAnimator);
            simpleImpl.handAnimation.map(handsAnimator);
            simpleImpl.rightHandAnimation.map(rightHandAnimator);
            simpleImpl.leftHandAnimation.map(leftHandAnimator);
            simpleImpl.itemAnimation.map(itemAnimator);
            return simpleImpl;
        }

        public Builder itemHand(IAnimator animator) {
            itemAndHandsAnimator = Objects.requireNonNull(animator, "Animation cannot be null!");
            return this;
        }

        public Builder hand(IAnimator animator) {
            handsAnimator = Objects.requireNonNull(animator, "Animation cannot be null!");
            return this;
        }

        public Builder rightHand(IAnimator animator) {
            rightHandAnimator = Objects.requireNonNull(animator, "Animation cannot be null!");
            return this;
        }

        public Builder leftHand(IAnimator animator) {
            leftHandAnimator = Objects.requireNonNull(animator, "Animation cannot be null!");
            return this;
        }

        public Builder item(IAnimator animator) {
            itemAnimator = Objects.requireNonNull(animator, "Animation cannot be null!");
            return this;
        }
    }
}
