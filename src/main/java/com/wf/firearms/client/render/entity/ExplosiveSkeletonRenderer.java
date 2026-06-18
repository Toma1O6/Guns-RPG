package com.wf.firearms.client.render.entity;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.client.model.ExplosiveSkeletonModel;
import com.wf.firearms.client.render.entity.GunMobItemInHandLayer;
import com.wf.firearms.entity.ExplosiveSkeletonEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class ExplosiveSkeletonRenderer
        extends HumanoidMobRenderer<ExplosiveSkeletonEntity, ExplosiveSkeletonModel> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/entity/explosive_skeleton.png");

    public ExplosiveSkeletonRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ExplosiveSkeletonModel(ctx.bakeLayer(ModelLayers.SKELETON)), 0.5f);
        this.layers.removeIf(layer -> layer instanceof ItemInHandLayer);
        this.addLayer(new GunMobItemInHandLayer<>(this, ctx.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(ExplosiveSkeletonEntity entity) {
        return TEXTURE;
    }
}
