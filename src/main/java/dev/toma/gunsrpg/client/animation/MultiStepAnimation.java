package dev.toma.gunsrpg.client.animation;

import com.google.gson.*;
import dev.toma.gunsrpg.client.animation.impl.SimpleAnimation;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public abstract class MultiStepAnimation extends TickableAnimation {

    protected final List<Pair<Range, Animation>> steps = new ArrayList<>();
    private int index;
    private Pair<Range, Animation> current;

    public MultiStepAnimation(int length) {
        super(length);
    }

    public final void init() {
        this.createAnimationSteps();
        current = steps.get(0);
    }

    public abstract void createAnimationSteps();

    public final void addStep(float from, float to, Animation animation) {
        this.steps.add(Pair.of(new Range(from, to), animation));
    }

    public final void addStep(Range range, Supplier<SimpleAnimation> animation) {
        this.steps.add(Pair.of(range, animation.get()));
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

    public static class Configurable extends MultiStepAnimation {
        private final String fileName;

        public Configurable(int time, String fileName) {
            super(time);
            this.fileName = fileName;
            this.init();
        }

        @Override
        public void createAnimationSteps() {
            for(Pair<Range, Supplier<SimpleAnimation>> pair : ModUtils.getNonnullFromMap(AnimationManager.SCRIPT_ANIMATIONS, this.fileName, Collections.emptyList())) {
                this.addStep(pair.getLeft(), pair.getRight());
            }
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

        public static class Deserializer implements JsonDeserializer<Range> {
            @Override
            public Range deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject obj = json.getAsJsonObject();
                return new Range(obj.get("from").getAsFloat(), obj.get("to").getAsFloat());
            }
        }
    }
}
