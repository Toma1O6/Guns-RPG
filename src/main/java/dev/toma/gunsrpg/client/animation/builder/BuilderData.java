package dev.toma.gunsrpg.client.animation.builder;

import dev.toma.gunsrpg.client.animation.Animation;

import java.util.ArrayList;
import java.util.List;

public class BuilderData {

    public static Context context = Context.TRANSLATION;
    public static Axis axis = Axis.X;
    public static Part part = Part.ITEM_AND_HANDS;
    public static float translateValue = 0.1F, translateValueSneak = 0.05F;
    public static float rotateValue = 10.0F, rotateValueSneak = 1.0F;
    public static List<BuilderAnimationStep> steps = new ArrayList<>();
    static { steps.add(new BuilderAnimationStep()); }
    public static BuilderAnimationStep current = steps.get(0);
    public static int animationLength = 20;

    public static Animation asAnimation() {
        return new BuiltAnimation(animationLength);
    }

    public static void initNewStep() {
        steps.add(current);
        current = new BuilderAnimationStep(current);
    }

    public static void resetToDefaultState() {
        if(steps.isEmpty()) {
            current = new BuilderAnimationStep();
        } else {
            BuilderAnimationStep step = steps.get(steps.size() - 1);
            current = new BuilderAnimationStep(step);
        }
    }

    public static void add(boolean sneak) {
        if(context == Context.TRANSLATION) {
            current.updateValue(sneak ? translateValueSneak : translateValue);
        } else {
            current.updateValue(sneak ? rotateValueSneak : rotateValue);
        }
    }

    public static void subtract(boolean sneak) {
        if(context == Context.TRANSLATION) {
            current.updateValue(sneak ? -translateValueSneak : -translateValue);
        } else {
            current.updateValue(sneak ? -rotateValueSneak : -rotateValue);
        }
    }

    public enum Context {
        TRANSLATION, ROTATION
    }

    public enum Axis {
        X(1),
        Y(0),
        Z(2);

        int index;

        Axis(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public enum Part {
        ITEM_AND_HANDS,
        HANDS,
        RIGHT_HAND,
        LEFT_HAND,
        ITEM
    }
}
