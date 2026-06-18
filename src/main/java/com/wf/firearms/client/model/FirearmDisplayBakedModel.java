package com.wf.firearms.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

/**
 * 包装平面物品模型：创造栏 / EMI 直接 {@link #getQuads} 时也能看到 2D 图标；
 * 手持 {@link #applyTransform} 返回自身 → BEWLR 3D。
 */
public final class FirearmDisplayBakedModel implements BakedModel {
    private final BakedModel guiFlatModel;
    private final TextureAtlasSprite particle;

    private FirearmDisplayBakedModel(BakedModel guiFlatModel, TextureAtlasSprite particle) {
        this.guiFlatModel = guiFlatModel;
        this.particle = particle;
    }

    public static BakedModel create(@Nullable BakedModel guiFlatModel, TextureAtlasSprite particle) {
        if (guiFlatModel != null && !guiFlatModel.isCustomRenderer()) {
            return new FirearmDisplayBakedModel(guiFlatModel, particle);
        }
        return FirearmCustomBakedModel.create(particle);
    }

    private static boolean isHandContext(ItemDisplayContext ctx) {
        return ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                || ctx == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || ctx == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                || ctx == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
                || ctx == ItemDisplayContext.HEAD;
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean leftHand) {
        if (isHandContext(transformType)) {
            // 手持：无 2D 四边形，仅 BEWLR 画 3D（避免与 item/generated 叠成虚影）
            return FirearmCustomBakedModel.create(particle);
        }
        return guiFlatModel.applyTransform(transformType, poseStack, leftHand);
    }

    @Override
    public java.util.List<BakedQuad> getQuads(
            @Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        return guiFlatModel.getQuads(state, direction, random);
    }

    @Override
    public java.util.List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction direction,
            RandomSource random,
            ModelData data,
            @Nullable RenderType renderType) {
        return guiFlatModel.getQuads(state, direction, random, data, renderType);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return guiFlatModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return guiFlatModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particle != null ? particle : guiFlatModel.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return guiFlatModel.getOverrides();
    }

    @Override
    public ItemTransforms getTransforms() {
        return guiFlatModel.getTransforms();
    }
}
