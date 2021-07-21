package dev.toma.gunsrpg.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL11;

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
        int a_sum = ModUtils.sum(alphaComponents);
        int r_sum = ModUtils.sum(redComponents);
        int g_sum = ModUtils.sum(greenComponents);
        int b_sum = ModUtils.sum(blueComponents);
        int a = a_sum / len;
        int r = r_sum / len;
        int g = g_sum / len;
        int b = b_sum / len;
        return combine4i(r, g, b, a);
    }

    public static int multiplyRGB(int color, double multiplier) {
        int red = (int) (red_i(color) * multiplier);
        int green = (int) (green_i(color) * multiplier);
        int blue = (int) (blue_i(color) * multiplier);
        int alpha = alpha_i(color);
        return combine4i(red, green, blue, alpha);
    }

    public static void fillSolid(IVertexBuilder builder, Matrix4f pose, int x1, int y1, int x2, int y2, int depth, int color) {
        fillGradient(builder, pose, x1, y1, x2, y2, depth, color, color);
    }

    public static void fillGradient(IVertexBuilder builder, Matrix4f pose, int x1, int y1, int x2, int y2, int depth, int color1, int color2) {
        int a1 = alpha_i(color1);
        int a2 = alpha_i(color2);
        int r1 = red_i(color1);
        int r2 = red_i(color2);
        int g1 = green_i(color1);
        int g2 = green_i(color2);
        int b1 = blue_i(color1);
        int b2 = blue_i(color2);
        builder.vertex(pose, x1, y2, depth).color(r2, g2, b2, a2).endVertex();
        builder.vertex(pose, x2, y2, depth).color(r2, g2, b2, a2).endVertex();
        builder.vertex(pose, x2, y1, depth).color(r1, g1, b1, a1).endVertex();
        builder.vertex(pose, x1, y1, depth).color(r1, g1, b1, a1).endVertex();
    }

    public static void drawSolid(Matrix4f pose, int x1, int y1, int x2, int y2, int color) {
        drawSolid(pose, x1, y1, x2, y2, 0, color);
    }

    public static void drawSolid(Matrix4f pose, int x1, int y1, int x2, int y2, int depth, int color) {
        drawGradient(pose, x1, y1, x2, y2, depth, color, color);
    }

    public static void drawGradient(Matrix4f pose, int x1, int y1, int x2, int y2, int color1, int color2) {
        drawGradient(pose, x1, y1, x2, y2, 0, color1, color2);
    }

    public static void drawGradient(Matrix4f pose, int x1, int y1, int x2, int y2, int depth, int color1, int color2) {
        setupColorRenderState();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        fillGradient(builder, pose, x1, y1, x2, y2, depth, color1, color2);
        tessellator.end();
        resetColorRenderState();
    }

    public static void setupColorRenderState() {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
    }

    public static void resetColorRenderState() {
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
