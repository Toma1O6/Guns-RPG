package dev.toma.gunsrpg.client.animation;

import com.google.gson.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.impl.SimpleAnimation;
import dev.toma.gunsrpg.util.object.Pair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class MultiStepAnimation extends TickableAnimation {

    protected final List<Pair<Range, IAnimation>> steps = new ArrayList<>();
    protected int index;
    private Pair<Range, IAnimation> current;

    public MultiStepAnimation(int length) {
        super(length);
    }

    public final void init() {
        this.createAnimationSteps();
        current = steps.get(0);
    }

    public abstract void createAnimationSteps();

    public final void addStep(float from, float to, IAnimation animation) {
        this.steps.add(Pair.of(new Range(from, to), animation));
    }

    public final void addStep(Range range, Supplier<SimpleAnimation> animation) {
        this.steps.add(Pair.of(range, animation.get()));
    }

    @Override
    public void animateItemHands(MatrixStack matrix, float partialTicks) {
        current.getRight().animateItemHands(matrix, partialTicks);
    }

    @Override
    public void animateHands(MatrixStack matrix, float partialTicks) {
        current.getRight().animateHands(matrix, partialTicks);
    }

    @Override
    public void animateRightArm(MatrixStack matrix, float partialTicks) {
        current.getRight().animateRightArm(matrix, partialTicks);
    }

    @Override
    public void animateLeftArm(MatrixStack matrix, float partialTicks) {
        current.getRight().animateLeftArm(matrix, partialTicks);
    }

    @Override
    public void animateItem(MatrixStack matrix, float partialTicks) {
        current.getRight().animateItem(matrix, partialTicks);
    }

    @Override
    public void frameTick(float partialTicks) {
        current.getRight().frameTick(partialTicks);
    }

    @Override
    public void update() {
        super.update();
        float f = 1.0F - this.getCurrentProgress();
        Range range = current.getLeft();
        IAnimation animation = current.getRight();
        if (range.isIn(f)) {
            animation.clientTick();
            float f1 = range.getProgress(f);
            animation.setProgress(f1);
        } else if (range.isOver(f)) {
            if (!this.isAtLastStep()) {
                ++index;
                current = steps.get(index);
                onStepChanged();
            }
        }
    }

    public void onStepChanged() {

    }

    public boolean isAtLastStep() {
        return index == steps.size() - 1;
    }

    public List<Pair<Range, IAnimation>> getSteps() {
        return steps;
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

        public static class Deserializer implements JsonDeserializer<Range> {
            @Override
            public Range deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject obj = json.getAsJsonObject();
                return new Range(obj.get("from").getAsFloat(), obj.get("to").getAsFloat());
            }
        }
    }
}
