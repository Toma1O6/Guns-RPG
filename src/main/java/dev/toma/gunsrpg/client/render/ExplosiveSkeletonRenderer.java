package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ExplosiveSkeletonModel;
import dev.toma.gunsrpg.client.render.layer.CustomHeldLayer;
import dev.toma.gunsrpg.common.entity.ExplosiveSkeletonEntity;
import dev.toma.gunsrpg.sided.ClientSideManager;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.util.ResourceLocation;

public class ExplosiveSkeletonRenderer extends BipedRenderer<ExplosiveSkeletonEntity, ExplosiveSkeletonModel> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/entity/explosive_skeleton.png");

    public ExplosiveSkeletonRenderer(EntityRendererManager manager) {
        super(manager, new ExplosiveSkeletonModel(), 0.5F);
        layers.clear();
        addLayer(new HeadLayer<>(this, 1.0F, 1.0F, 1.0F));
        addLayer(new BipedArmorLayer<>(this, new ExplosiveSkeletonModel(0.5F, true), new ExplosiveSkeletonModel(1.0F, true)));
        addLayer(new CustomHeldLayer<>(this, ClientSideManager.config.explosiveSkeletonHeldItemRender));
    }

    @Override
    public ResourceLocation getTextureLocation(ExplosiveSkeletonEntity entity) {
        return TEXTURE;
    }
}
