package com.wf.firearms.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wf.firearms.entity.GrenadeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/** 飞行中显示手雷物品模型（带旋转），避免默认 TNT 小方块。 */
public class GrenadeRenderer extends EntityRenderer<GrenadeEntity> {
    private final ItemRenderer itemRenderer;

    public GrenadeRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(
            GrenadeEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack pose,
            MultiBufferSource buffer,
            int packedLight) {
        pose.pushPose();
        pose.translate(0, 0.15, 0);
        float spin = (entity.tickCount + partialTick) * 12f;
        pose.mulPose(Axis.XP.rotationDegrees(spin));
        pose.mulPose(Axis.YP.rotationDegrees(spin * 0.5f));
        pose.scale(1.25f, 1.25f, 1.25f);
        ItemStack stack = entity.getDisplayItem();
        if (stack.isEmpty()) {
            stack = entity.getItem();
        }
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
    public ResourceLocation getTextureLocation(GrenadeEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
