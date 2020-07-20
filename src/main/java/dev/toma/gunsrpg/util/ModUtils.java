package dev.toma.gunsrpg.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public static void renderTexture(int x, int y, int x2, int y2, double uMin, double vMin, double uMax, double vMax, ResourceLocation location) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x, y2, 0).tex(uMin, vMax).endVertex();
        builder.pos(x2, y2, 0).tex(uMax, vMax).endVertex();
        builder.pos(x2, y, 0).tex(uMax, vMin).endVertex();
        builder.pos(x, y, 0).tex(uMin, vMin).endVertex();
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

    public static <A> boolean contains(A lookingFor, Collection<A> collection) {
        for(A a : collection) {
            if(a == lookingFor) {
                return true;
            }
        }
        return false;
    }

    public static <A, B> boolean contains(A a, Collection<B> collection, BiPredicate<A, B> comparator) {
        for (B b : collection) {
            if (comparator.test(a, b)) {
                return true;
            }
        }
        return false;
    }

    public static <A> boolean contains(A obj, A[] array) {
        for(A a : array) {
            if(a == obj) {
                return true;
            }
        }
        return false;
    }

    public static <A, B> boolean contains(A obj, B[] array, BiPredicate<A, B> comparator) {
        for(B b : array) {
            if(comparator.test(obj, b)) {
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

    public static int getItemCountInInventory(Item item, IInventory inventory) {
        int count = 0;
        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if(stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
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

    public static RayTraceResult raytraceBlocksIgnoreGlass(World world, Vec3d start, Vec3d end, Predicate<IBlockState> statePredicate) {
        if(Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z) || Double.isNaN(end.x) || Double.isNaN(end.y) || Double.isNaN(end.z)) {
            return null;
        }
        BlockPos current = new BlockPos(start);
        IBlockState currentState = world.getBlockState(current);
        if(currentState.getCollisionBoundingBox(world, current) != Block.NULL_AABB && currentState.getBlock().canCollideCheck(currentState, false) && statePredicate.test(currentState)) {
            RayTraceResult rayTraceResult = currentState.collisionRayTrace(world, current, start, end);
            if(rayTraceResult != null) {
                return rayTraceResult;
            }
        }
        int checks = getPartialChecksAmount(start, end);
        double x = (end.x - start.x) / checks;
        double y = (end.y - start.y) / checks;
        double z = (end.z - start.z) / checks;
        for(int i = 1; i <= checks; i++) {
            BlockPos pos = new BlockPos(start.x + x * i, start.y + y * i, start.z + z * i);
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if(statePredicate.test(state) && state.getCollisionBoundingBox(world, pos) != Block.NULL_AABB && block.canCollideCheck(state, false)) {
                RayTraceResult rayTraceResult = state.collisionRayTrace(world, pos, new Vec3d(start.x + x, start.y + y, start.z + z), end);
                if(rayTraceResult != null) {
                    return rayTraceResult;
                }
            }
        }
        return null;
    }

    @Nullable
    @SafeVarargs
    public static <T> T firstNonnull(T... values) {
        for (T t : values) {
            if (t != null) return t;
        }
        return null;
    }

    private static int getPartialChecksAmount(Vec3d start, Vec3d end) {
        double distX = sqr(start.x - end.x);
        double distY = sqr(start.y - end.y);
        double distZ = sqr(start.z - end.z);
        double distance = Math.sqrt(distX + distY + distZ);
        return (int) Math.max(1.0D, distance * 2);
    }

    private static double sqr(double n) {
        return n * n;
    }
}
