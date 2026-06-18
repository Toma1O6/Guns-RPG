package com.wf.firearms.client.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * 对标 Guns RPG {@code SimpleBakedModel}：无四边形 + {@code isCustomRenderer}。
 * particle 必须非空，否则部分路径下物品槽完全空白。
 */
public final class FirearmCustomBakedModel implements BakedModel {
    private final TextureAtlasSprite particle;

    private FirearmCustomBakedModel(TextureAtlasSprite particle) {
        this.particle = particle;
    }

    public static BakedModel create(TextureAtlasSprite particleIcon) {
        return new FirearmCustomBakedModel(particleIcon);
    }

    @Override
    public java.util.List<BakedQuad> getQuads(
            @Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        return java.util.List.of();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    /** false：创造栏按平面物品矩阵调用 BEWLR，避免 3D 槽位矩阵把 2D 图标压成右下角小点。 */
    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particle;
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }
}
