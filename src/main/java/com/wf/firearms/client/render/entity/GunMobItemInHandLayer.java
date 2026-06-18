package com.wf.firearms.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wf.firearms.client.render.MobGunRenderContext;
import com.wf.firearms.entity.ExplosiveSkeletonEntity;
import com.wf.firearms.entity.ZombieGunnerEntity;
import com.wf.firearms.item.FirearmItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * 持枪怪物：原版腕部 + 肩高抬升，BEWLR 走怪物专用偏移向手臂延伸。
 */
public class GunMobItemInHandLayer<T extends LivingEntity, M extends HumanoidModel<T>>
        extends ItemInHandLayer<T, M> {
    private final ItemInHandRenderer itemRenderer;

    public GunMobItemInHandLayer(RenderLayerParent<T, M> parent, ItemInHandRenderer itemInHandRenderer) {
        super(parent, itemInHandRenderer);
        this.itemRenderer = itemInHandRenderer;
    }

    @Override
    public void renderArmWithItem(
            LivingEntity entity,
            ItemStack stack,
            ItemDisplayContext displayContext,
            HumanoidArm arm,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight) {
        if (isGunMob(entity) && arm == HumanoidArm.RIGHT && stack.getItem() instanceof FirearmItem) {
            renderMobMainHandFirearm(entity, stack, displayContext, poseStack, buffer, packedLight);
            return;
        }
        super.renderArmWithItem(entity, stack, displayContext, arm, poseStack, buffer, packedLight);
    }

    private void renderMobMainHandFirearm(
            LivingEntity entity,
            ItemStack stack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight) {
        poseStack.pushPose();
        getParentModel().rightArm.translateAndRotate(poseStack);
        applyVanillaWristTransform(poseStack, HumanoidArm.RIGHT);
        poseStack.translate(-0.02F, 0.34F, 0.46F);
        MobGunRenderContext.begin();
        try {
            itemRenderer.renderItem(entity, stack, displayContext, false, poseStack, buffer, packedLight);
        } finally {
            MobGunRenderContext.end();
        }
        poseStack.popPose();
    }

    private static void applyVanillaWristTransform(PoseStack poseStack, HumanoidArm arm) {
        poseStack.mulPose(Axis.YP.rotationDegrees(arm == HumanoidArm.RIGHT ? -90.0F : 90.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        float side = arm == HumanoidArm.LEFT ? -1.0F : 1.0F;
        poseStack.translate(side * 0.125F, 0.125F, -0.0625F);
    }

    private static boolean isGunMob(LivingEntity entity) {
        return entity instanceof ZombieGunnerEntity || entity instanceof ExplosiveSkeletonEntity;
    }
}
