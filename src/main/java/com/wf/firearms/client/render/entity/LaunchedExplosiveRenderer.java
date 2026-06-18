package com.wf.firearms.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wf.firearms.entity.LaunchedExplosiveEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LaunchedExplosiveRenderer extends EntityRenderer<LaunchedExplosiveEntity> {
    private final ItemRenderer itemRenderer;

    public LaunchedExplosiveRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(
            LaunchedExplosiveEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack pose,
            MultiBufferSource buffer,
            int packedLight) {
        pose.pushPose();
        pose.translate(0, 0.15, 0);
        float spin = (entity.tickCount + partialTick) * 14f;
        pose.mulPose(Axis.XP.rotationDegrees(spin));
        pose.mulPose(Axis.YP.rotationDegrees(spin * 0.6f));
        pose.scale(1.35f, 1.35f, 1.35f);
        ItemStack stack = entity.getDisplayItem();
        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                pose,
                buffer,
                entity.level(),
                entity.getId());
        pose.popPose();
        super.render(entity, entityYaw, partialTick, pose, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(LaunchedExplosiveEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
