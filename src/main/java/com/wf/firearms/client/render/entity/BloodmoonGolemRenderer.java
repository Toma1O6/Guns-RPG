package com.wf.firearms.client.render.entity;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.client.model.BloodmoonGolemModel;
import com.wf.firearms.entity.BloodmoonGolemEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BloodmoonGolemRenderer extends MobRenderer<BloodmoonGolemEntity, BloodmoonGolemModel> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/entity/bloodmoon_golem.png");

    public BloodmoonGolemRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BloodmoonGolemModel(ctx.bakeLayer(BloodmoonGolemModel.LAYER)), 0.7f);
    }

    @Override
    public ResourceLocation getTextureLocation(BloodmoonGolemEntity entity) {
        return TEXTURE;
    }
}
