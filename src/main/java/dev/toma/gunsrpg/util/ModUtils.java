package dev.toma.gunsrpg.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.LazyOptional;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class ModUtils {

    public static <T, U> U extractOptionalOrElse(LazyOptional<T> optional, Function<T, U> extractor, U fallback) {
        if (!optional.isPresent())
            return fallback;
        return extractor.apply(optional.orElse(null));
    }

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

    public static String formatTicksToTime(int ticks) {
        StringBuilder builder = new StringBuilder();
        int toParse = ticks / 20;
        int hours = toParse / 3600;
        toParse = toParse % 3600;
        int minutes = toParse / 60;
        toParse = toParse % 60;
        if(hours > 0) {
            builder.append(hours).append("h:");
        }
        if(hours > 0 || minutes > 0) {
            builder.append(minutes < 10 ? "0" : "").append(minutes).append("m:");
        }
        builder.append(toParse < 10 ? "0" : "").append(toParse).append("s");
        return builder.toString();
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

    public static int clamp(int n, int min, int max) {
        return n < min ? min : Math.min(n, max);
    }

    public static float clamp(float n, float min, float max) {
        return n < min ? min : Math.min(n, max);
    }

    public static double clamp(double n, double min, double max) {
        return n < min ? min : Math.min(n, max);
    }

    public static int getItemCountInInventory(Item item, IInventory inventory) {
        int count = 0;
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
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

    public static <T> Predicate<T> truePredicate() {
        return t -> true;
    }

    public static RayTraceResult raytraceBlocksIgnoreGlass(Vector3d start, Vector3d end, IBlockReader reader) {
        RayTraceContext context = new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null);
        return IBlockReader.traverseBlocks(context, (ctx, pos) -> {
            BlockState blockstate = reader.getBlockState(pos);
            FluidState fluidstate = reader.getFluidState(pos);
            Vector3d vector3d = ctx.getFrom();
            Vector3d vector3d1 = ctx.getTo();
            VoxelShape voxelshape = ctx.getBlockShape(blockstate, reader, pos);
            BlockRayTraceResult blockraytraceresult = reader.clipWithInteractionOverride(vector3d, vector3d1, pos, voxelshape, blockstate);
            VoxelShape voxelshape1 = ctx.getFluidShape(fluidstate, reader, pos);
            BlockRayTraceResult blockraytraceresult1 = voxelshape1.clip(vector3d, vector3d1, pos);
            double d0 = blockraytraceresult == null ? Double.MAX_VALUE : ctx.getFrom().distanceToSqr(blockraytraceresult.getLocation());
            double d1 = blockraytraceresult1 == null ? Double.MAX_VALUE : ctx.getFrom().distanceToSqr(blockraytraceresult1.getLocation());
            return d0 <= d1 ? blockraytraceresult : blockraytraceresult1;
        }, (ctx) -> {
            Vector3d vector3d = ctx.getFrom().subtract(ctx.getTo());
            return BlockRayTraceResult.miss(ctx.getTo(), Direction.getNearest(vector3d.x, vector3d.y, vector3d.z), new BlockPos(ctx.getTo()));
        });
    }

    public static Direction getFacing(PlayerEntity player) {
        float reach = getReachDistance(player);
        Vector3d vec1 = player.getEyePosition(1.0F);
        Vector3d vec2 = player.getLookAngle();
        Vector3d vec3 = vec1.add(vec2.x * reach, vec2.y * reach, vec2.z * reach);
        BlockRayTraceResult result = player.level.clip(new RayTraceContext(vec1, vec3, RayTraceContext.BlockMode.VISUAL, RayTraceContext.FluidMode.NONE, player));
        return result != null && result.getDirection() != null ? result.getDirection() : Direction.NORTH;
    }

    public static float getReachDistance(PlayerEntity player) {
        float attrib = (float) player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        return player.isCreative() ? attrib : attrib - 0.5F;
    }
}
