package dev.toma.gunsrpg.client.animation.impl;

import com.google.gson.*;
import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.util.object.OptionalObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
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

    /**
     * Not called, override {@link MultiStepAnimation#cancelsItemRender()}
     * @return whether item should be rendered
     */
    @Override
    public boolean cancelsItemRender() {
        return false;
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

        enum AnimationType {
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
}
