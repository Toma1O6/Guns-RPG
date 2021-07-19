package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ExplosiveSkeletonModel;
import dev.toma.gunsrpg.common.entity.ExplosiveSkeletonEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

public class ExplosiveSkeletonRenderer extends BipedRenderer<ExplosiveSkeletonEntity, ExplosiveSkeletonModel> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/entity/explosive_skeleton.png");

    public ExplosiveSkeletonRenderer(EntityRendererManager manager) {
        super(manager, new ExplosiveSkeletonModel(), 0.5F);
        addLayer(new BipedArmorLayer<>(this, new ExplosiveSkeletonModel(0.5F, true), new ExplosiveSkeletonModel(1.0F, true)));
    }

    @Override
    public ResourceLocation getTextureLocation(ExplosiveSkeletonEntity entity) {
        return TEXTURE;
    }
}
