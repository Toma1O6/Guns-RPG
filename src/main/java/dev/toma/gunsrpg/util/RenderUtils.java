package dev.toma.gunsrpg.util;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;

public class RenderUtils {

    public static final int MAX_COLOR_INT_VALUE = 0xff;

    public static int asInt(float value) {
        return MathHelper.ceil(value * 255);
    }

    public static float asFloat(int value) {
        return value / 255.0F;
    }

    public static float red(int color) {
        return red_i(color) / 255.0F;
    }

    public static int red_i(int color) {
        return (color >> 16) & 255;
    }

    public static float green(int color) {
        return green_i(color) / 255.0F;
    }

    public static int green_i(int color) {
        return (color >> 8) & 255;
    }

    public static float blue(int color) {
        return blue_i(color) / 255.0F;
    }

    public static int blue_i(int color) {
        return color & 255;
    }

    public static float alpha(int color) {
        return alpha_i(color) / 255.0F;
    }

    public static int alpha_i(int color) {
        return (color >> 24) & 255;
    }

    public static int combine4i(int red, int green, int blue, int alpha) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static int combine4f(float red, float green, float blue, float alpha) {
        return combine4i(asInt(red), asInt(green), asInt(blue), asInt(alpha));
    }

    public static int combine3i(int red, int green, int blue) {
        return combine4i(red, green, blue, MAX_COLOR_INT_VALUE);
    }

    public static int combine3f(float red, float green, float blue) {
        return combine4f(red, green, blue, 1.0F);
    }

    public static int mix(int... colors) {
        int len = colors.length;
        if (len == 1)
            return colors[0];

        int[] alphaComponents = new int[len];
        int[] redComponents = new int[len];
        int[] greenComponents = new int[len];
        int[] blueComponents = new int[len];
        int i = 0;
        for (int color : colors) {
            alphaComponents[i] = alpha_i(color);
            redComponents[i] = red_i(color);
            greenComponents[i] = green_i(color);
            blueComponents[i] = blue_i(color);
        }
    }

    public static void fillGradientColor(IVertexBuilder builder, Matrix4f matrix, int x1, int y1, int x2, int y2, int depth, int color1, int color2) {

    }
}
