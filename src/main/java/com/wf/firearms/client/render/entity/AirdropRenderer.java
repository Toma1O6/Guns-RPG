package com.wf.firearms.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.client.model.AirdropModel;
import com.wf.firearms.entity.AirdropEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class AirdropRenderer extends EntityRenderer<AirdropEntity> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/entity/airdrop.png");
    private final AirdropModel model = new AirdropModel();

    public AirdropRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AirdropEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(
            AirdropEntity entity,
            float yaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 1.8F, 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(180F));
        model.renderToBuffer(
                poseStack,
                buffer.getBuffer(model.renderType(getTextureLocation(entity))),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                1.0F);
        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, buffer, packedLight);
    }
}
