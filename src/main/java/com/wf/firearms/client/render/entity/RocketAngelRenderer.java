package com.wf.firearms.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.client.model.RocketAngelModel;
import com.wf.firearms.entity.RocketAngelEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class RocketAngelRenderer extends MobRenderer<RocketAngelEntity, RocketAngelModel> {
    private static final ResourceLocation[] TEXTURES = {
        ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/entity/rocket_angel_0.png"),
        ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/entity/rocket_angel_1.png"),
        ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/entity/rocket_angel_2.png"),
        ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/entity/rocket_angel_3.png"),
        ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/entity/rocket_angel_4.png")
    };

    public RocketAngelRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new RocketAngelModel(ctx.bakeLayer(RocketAngelModel.LAYER)), 0.6f);
    }

    @Override
    protected int getBlockLightLevel(RocketAngelEntity entity, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(RocketAngelEntity entity) {
        int idx = Math.min(entity.getTextureIndex(), TEXTURES.length - 1);
        return TEXTURES[Math.max(0, idx)];
    }

    @Override
    protected void scale(RocketAngelEntity entity, PoseStack pose, float partialTick) {
        pose.translate(0.0f, 0.4f, 0.0f);
    }
}
