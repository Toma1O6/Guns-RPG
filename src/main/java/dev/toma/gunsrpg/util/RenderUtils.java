package dev.toma.gunsrpg.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GraphicsFanciness;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
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

    public static int darken(int rgb, float factor) {
        int red = (int) (red_i(rgb) * factor);
        int green = (int) (green_i(rgb) * factor);
        int blue = (int) (blue_i(rgb) * factor);
        return (red << 16) | (green << 8) | blue;
    }

    public static int combine3f(float red, float green, float blue) {
        return combine4f(red, green, blue, 1.0F);
    }

    public static int multiplyRGB(int color, double multiplier) {
        int red = (int) (red_i(color) * multiplier);
        int green = (int) (green_i(color) * multiplier);
        int blue = (int) (blue_i(color) * multiplier);
        int alpha = alpha_i(color);
        return combine4i(red, green, blue, alpha);
    }

    public static int parseColor(String input) {
        try {
            long color = Long.decode(input);
            return (int) color;
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public static void fillSolid(IVertexBuilder builder, Matrix4f pose, float x1, float y1, float x2, float y2, int color) {
        int a = alpha_i(color);
        int r = red_i(color);
        int g = green_i(color);
        int b = blue_i(color);
        builder.vertex(pose, x1, y1, 0).color(r, g, b, a).endVertex();
        builder.vertex(pose, x1, y2, 0).color(r, g, b, a).endVertex();
        builder.vertex(pose, x2, y2, 0).color(r, g, b, a).endVertex();
        builder.vertex(pose, x2, y1, 0).color(r, g, b, a).endVertex();
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

    public static void drawLine(Matrix4f pose, int x1, int y1, int x2, int y2, int color) {
        drawLine(pose, x1, y1, x2, y2, color, color);
    }

    public static void drawLine(Matrix4f pose, float x1, float y1, float x2, float y2, int color1, int color2) {
        int a1 = alpha_i(color1);
        int a2 = alpha_i(color2);
        int r1 = red_i(color1);
        int r2 = red_i(color2);
        int g1 = green_i(color1);
        int g2 = green_i(color2);
        int b1 = blue_i(color1);
        int b2 = blue_i(color2);
        setupColorRenderState();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        builder.vertex(pose, x1, y1, 0).color(r1, g1, b1, a1).endVertex();
        builder.vertex(pose, x2, y2, 0).color(r2, g2, b2, a2).endVertex();
        tessellator.end();
        resetColorRenderState();
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

    public static void drawTex(Matrix4f pose, int x1, int y1, int x2, int y2) {
        drawTex(pose, x1, y1, x2, y2, 0);
    }

    public static void drawTex(Matrix4f pose, int x1, int y1, int x2, int y2, int depth) {
        drawTex(pose, x1, y1, x2, y2, depth, 0.0F, 0.0F, 1.0F, 1.0F);
    }

    public static void drawTex(Matrix4f pose, int x1, int y1, int x2, int y2, float u1, float v1, float u2, float v2) {
        drawTex(pose, x1, y1, x2, y2, 0, u1, v1, u2, v2);
    }

    public static void drawTex(Matrix4f pose, int x1, int y1, int x2, int y2, int depth, float u1, float v1, float u2, float v2) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.vertex(pose, x1, y2, depth).uv(u1, v2).endVertex();
        builder.vertex(pose, x2, y2, depth).uv(u2, v2).endVertex();
        builder.vertex(pose, x2, y1, depth).uv(u2, v1).endVertex();
        builder.vertex(pose, x1, y1, depth).uv(u1, v1).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
    }

    public static void drawColoredTex(Matrix4f pose, int x1, int y1, int x2, int y2, int color) {
        drawColoredTex(pose, x1, y1, x2, y2, color, color);
    }

    public static void drawColoredTex(Matrix4f pose, int x1, int y1, int x2, int y2, int color1, int color2) {
        drawColoredTex(pose, x1, y1, x2, y2, 0, 0.0F, 0.0F, 1.0F, 1.0F, color1, color2);
    }

    public static void drawColoredTex(Matrix4f pose, int x1, int y1, int x2, int y2, int depth, float u1, float v1, float u2, float v2, int color1, int color2) {
        int a1 = alpha_i(color1);
        int a2 = alpha_i(color2);
        int r1 = red_i(color1);
        int r2 = red_i(color2);
        int g1 = green_i(color1);
        int g2 = green_i(color2);
        int b1 = blue_i(color1);
        int b2 = blue_i(color2);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
        builder.vertex(pose, x1, y2, depth).color(r2, g2, b2, a2).uv(u1, v2).endVertex();
        builder.vertex(pose, x2, y2, depth).color(r2, g2, b2, a2).uv(u2, v2).endVertex();
        builder.vertex(pose, x2, y1, depth).color(r1, g1, b1, a1).uv(u2, v1).endVertex();
        builder.vertex(pose, x1, y1, depth).color(r1, g1, b1, a1).uv(u1, v1).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
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

    public static void drawSolid(Matrix4f pose, float x1, float y1, float x2, float y2, int color) {
        int a = alpha_i(color);
        int r = red_i(color);
        int g = green_i(color);
        int b = blue_i(color);
        setupColorRenderState();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        builder.vertex(pose, x1, y2, 0).color(r, g, b, a).endVertex();
        builder.vertex(pose, x2, y2, 0).color(r, g, b, a).endVertex();
        builder.vertex(pose, x2, y1, 0).color(r, g, b, a).endVertex();
        builder.vertex(pose, x1, y1, 0).color(r, g, b, a).endVertex();
        tessellator.end();
        resetColorRenderState();
    }

    public static void drawBorder(Matrix4f pose, float x1, float y1, float width, float height, float thickness, int color) {
        float x2 = x1 + width;
        float y2 = y1 + height;
        setupColorRenderState();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        fillSolid(builder, pose, x1, y1, x2, y1 + thickness, color);
        fillSolid(builder, pose, x1, y2 - thickness, x2, y2, color);
        fillSolid(builder, pose, x1, y1, x1 + thickness, y2, color);
        fillSolid(builder, pose, x2 - thickness, y1, x2, y2, color);
        tessellator.end();
        resetColorRenderState();
    }

    public static boolean hasGraphicsMode(GraphicsFanciness fanciness) {
        GameSettings settings = Minecraft.getInstance().options;
        int id = settings.graphicsMode.getId();
        int requireId = fanciness.getId();
        return id >= requireId;
    }

    public static void drawCenteredText(MatrixStack stack, ITextComponent component, FontRenderer renderer, Widget widget, int color) {
        float x = getHorizontalCenter(component, renderer, widget);
        float y = getVerticalCenter(renderer, widget);
        renderer.draw(stack, component, x, y, color);
    }

    public static void drawCenteredShadowText(MatrixStack stack, ITextComponent component, FontRenderer renderer, Widget widget, int color) {
        float x = getHorizontalCenter(component, renderer, widget);
        float y = getVerticalCenter(renderer, widget);
        renderer.drawShadow(stack, component, x, y, color);
    }

    public static float getHorizontalCenter(ITextComponent component, FontRenderer renderer, Widget widget) {
        return widget.x + (widget.getWidth() - renderer.width(component)) / 2.0F;
    }

    public static float getVerticalCenter(FontRenderer renderer, Widget widget) {
        return widget.y + (widget.getHeight() - renderer.lineHeight) / 2.0F;
    }
}
