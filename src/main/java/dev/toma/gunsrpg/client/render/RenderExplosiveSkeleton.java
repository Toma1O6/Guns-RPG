package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelExplosiveSkeleton;
import dev.toma.gunsrpg.common.entity.ExplosiveSkeletonEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

public class RenderExplosiveSkeleton extends BipedRenderer<ExplosiveSkeletonEntity, ModelExplosiveSkeleton> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/entity/explosive_skeleton.png");

    public RenderExplosiveSkeleton(EntityRendererManager manager) {
        super(manager, new ModelExplosiveSkeleton(), 0.5F);
        addLayer(new BipedArmorLayer<>(this, new ModelExplosiveSkeleton(0.5F, true), new ModelExplosiveSkeleton(1.0F, true)));
    }

    @Override
    public ResourceLocation getTextureLocation(ExplosiveSkeletonEntity entity) {
        return TEXTURE;
    }
}
