package dev.toma.gunsrpg.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class ModUtils {

    public static void renderTexture(int x, int y, int x2, int y2, ResourceLocation location) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x, y2, 0).tex(0, 1).endVertex();
        builder.pos(x2, y2, 0).tex(1, 1).endVertex();
        builder.pos(x2, y, 0).tex(1, 0).endVertex();
        builder.pos(x, y, 0).tex(0, 0).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
    }

    public static void renderTextureWithColor(int x, int y, int x2, int y2, ResourceLocation location, float r, float g, float b, float a) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        builder.pos(x, y2, 0).tex(0, 1).color(r, g, b, a).endVertex();
        builder.pos(x2, y2, 0).tex(1, 1).color(r, g, b, a).endVertex();
        builder.pos(x2, y, 0).tex(1, 0).color(r, g, b, a).endVertex();
        builder.pos(x, y, 0).tex(0, 0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
    }

    public static void renderColor(int x, int y, int x2, int y2, float r, float g, float b, float a) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x, y2, 0).color(r, g, b, a).endVertex();
        builder.pos(x2, y2, 0).color(r, g, b, a).endVertex();
        builder.pos(x2, y, 0).color(r, g, b, a).endVertex();
        builder.pos(x, y, 0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void renderLine(int x, int y, int x2, int y2, float r, float g, float b, float a, int width) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.glLineWidth(width);
        builder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x, y, 0).color(r, g, b, a).endVertex();
        builder.pos(x2, y2, 0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.glLineWidth(1);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    @SafeVarargs
    public static <T> List<T> newList(T... ts) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, ts);
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <A, B> B[] convert(A[] as, Function<A, B> convert) {
        B[] arr = (B[]) new Object[as.length];
        for (int i = 0; i < as.length; i++) {
            arr[i] = convert.apply(as[i]);
        }
        return arr;
    }

    public static <A, B> boolean contains(A a, Collection<B> collection, BiPredicate<A, B> comparator) {
        for (B b : collection) {
            if (comparator.test(a, b)) {
                return true;
            }
        }
        return false;
    }

    public static int wrap(int n, int min, int max) {
        return n < min ? min : n > max ? max : n;
    }

    public static float wrap(float n, float min, float max) {
        return n < min ? min : n > max ? max : n;
    }

    public static double wrap(double n, double min, double max) {
        return n < min ? min : n > max ? max : n;
    }

    public static int getLastIndexOfArray(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) return i;
        }
        return array.length - 1;
    }

    public static <K, V> V getNonnullFromMap(Map<K, V> map, K key, V def) {
        V v = map.get(key);
        return v != null ? v : Objects.requireNonNull(def);
    }
}
