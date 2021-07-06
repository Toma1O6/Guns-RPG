package dev.toma.gunsrpg.client.animation.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.util.object.OptionalObject;

import java.util.Objects;

public class SimpleAnimation implements IAnimation {

    private float current, prev, smooth;
    private final OptionalObject<IAnimator> itemHandAnimation = OptionalObject.empty();
    private final OptionalObject<IAnimator> handAnimation = OptionalObject.empty();
    private final OptionalObject<IAnimator> rightHandAnimation = OptionalObject.empty();
    private final OptionalObject<IAnimator> leftHandAnimation = OptionalObject.empty();
    private final OptionalObject<IAnimator> itemAnimation = OptionalObject.empty();

    private SimpleAnimation() {}

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
     * @return whether item should be rendered
     */
    @Override
    public boolean cancelsItemRender() {
        return false;
    }

    @FunctionalInterface
    public interface IAnimator {
        void animate(MatrixStack stack, float interpolatedValue);
    }

    public static class Builder {

        private IAnimator itemAndHandsAnimator;
        private IAnimator handsAnimator;
        private IAnimator rightHandAnimator;
        private IAnimator leftHandAnimator;
        private IAnimator itemAnimator;

        private Builder() {}

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

    /*
    public static class Deserializer implements JsonDeserializer<SimpleAnimation> {

        @Override
        public SimpleAnimation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Builder builder = newSimpleAnimation();
            for(Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
                AnimationType type = AnimationType.valueOf(entry.getKey().toUpperCase());
                type.apply(builder, this.compileAnimation(json.getAsJsonObject().getAsJsonObject(entry.getKey())));
            }
            return builder.create();
        }

        Consumer<Float> compileAnimation(JsonObject object) {
            return f -> {
                if(object.has("rotate")) {
                    JsonObject rotate = object.getAsJsonObject("rotate");
                    float[] f0 = new float[4];
                    float[] f1 = new float[4];
                    if(rotate.has("value")) {
                        f0 = fromJsonArray(rotate.getAsJsonArray("value"), 4);
                    }
                    if(rotate.has("smooth")) {
                        f1 = fromJsonArray(rotate.getAsJsonArray("smooth"), 4);
                    }
                    GlStateManager.rotate(f0[0] + f1[0] * f, val(f0[1], f1[1]), val(f0[2], f1[2]), val(f0[3], f1[3]));
                }
                if(object.has("translate")) {
                    JsonObject translate = object.getAsJsonObject("translate");
                    float[] f0 = new float[3];
                    float[] f1 = new float[3];
                    if(translate.has("value")) {
                        f0 = fromJsonArray(translate.getAsJsonArray("value"), 3);
                    }
                    if(translate.has("smooth")) {
                        f1 = fromJsonArray(translate.getAsJsonArray("smooth"), 3);
                    }
                    GlStateManager.translate(f0[0] + f1[0] * f, f0[1] + f1[1] * f, f0[2] + f1[2] * f);
                }
            };
        }

        float val(float a, float b) {
            return Math.min(1.0F, a + b);
        }

        float[] fromJsonArray(JsonArray array, int size) {
            float[] fs = new float[size];
            for(int i = 0; i < size; i++) {
                fs[i] = array.get(i).getAsFloat();
            }
            return fs;
        }

        public enum AnimationType {
            ITEM_HAND(Builder::itemHand),
            HANDS(Builder::hand),
            RIGHT_HAND(Builder::rightHand),
            LEFT_HAND(Builder::leftHand),
            ITEM(Builder::item);

            BiConsumer<Builder, Consumer<Float>> func;

            AnimationType(BiConsumer<Builder, Consumer<Float>> func) {
                this.func = func;
            }

            void apply(Builder builder, Consumer<Float> consumer) {
                func.accept(builder, consumer);
            }
        }
    }
    */
}
