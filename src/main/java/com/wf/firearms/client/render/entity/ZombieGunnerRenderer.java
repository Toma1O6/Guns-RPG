package com.wf.firearms.client.render.entity;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.client.model.ZombieGunnerModel;
import com.wf.firearms.client.render.entity.GunMobItemInHandLayer;
import com.wf.firearms.entity.ZombieGunnerEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class ZombieGunnerRenderer
        extends HumanoidMobRenderer<ZombieGunnerEntity, ZombieGunnerModel> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/entity/zombie_gunner.png");

    public ZombieGunnerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ZombieGunnerModel(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
        this.layers.removeIf(layer -> layer instanceof ItemInHandLayer);
        this.addLayer(new GunMobItemInHandLayer<>(this, ctx.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieGunnerEntity entity) {
        return TEXTURE;
    }
}
