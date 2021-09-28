package dev.toma.gunsrpg.util.math;

import lib.toma.animations.Interpolate;
import net.minecraft.util.math.MathHelper;

public final class Mth {

    public static float invert(float value) {
        return 1.0F - value;
    }

    public static float line_linear_rise(float value, float minBound, float maxBound) {
        return (value - minBound) / (maxBound - minBound);
    }

    public static float line_linear_fall(float value, float minBound, float maxBound) {
        return invert(line_linear_rise(value, minBound, maxBound));
    }

    public static float line_linear_MinMaxMin(float input) {
        return input <= 0.5F ? line_linear_rise(input, 0.0F, 0.5F) : line_linear_fall(input, 0.5F, 1.0F);
    }

    public static float asLinearFunction(int value, int min, int max) {
        float f = (value - min) / (float) (max - min);
        return MathHelper.clamp(f, 0.0F, 1.0F);
    }

    public static float asLinearInterpolatedFunction(int value, int min, int max, float partial) {
        if (value > max) {
            return 1.0F;
        }
        int old = value - 1;
        float f1 = asLinearFunction(value, min, max);
        float f2 = asLinearFunction(old, min, max);
        return Interpolate.linear(partial, f1, f2);
    }

    public static int isWithinOrDefault(int value, int min, int max, int _default) {
        if (value >= min && value <= max) {
            return value;
        }
        return _default;
    }

    private Mth() {}
}