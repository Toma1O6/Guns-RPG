package com.wf.firearms.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wf.firearms.client.FirearmClientTextures;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * 枪械 3D 绘制。GUI / 手持链对齐 {@code wf_firearms}（适配 1.20 BEWLR），
 * 不再叠加 1.16 {@code HandRenderTransforms} 与双重 GUI 旋转。
 */
public abstract class AbstractWeaponRenderer {

    /** Blockbench 模型根在 y=24，先拉回原点。 */
    private static final float MODEL_PIVOT_Y = -1.5F;

    private final String weaponKey;

    protected AbstractWeaponRenderer(String weaponKey) {
        this.weaponKey = weaponKey;
    }

    protected final String weaponKey() {
        return weaponKey;
    }

    public final void render(
            ItemStack stack,
            ItemDisplayContext transformType,
            PoseStack matrix,
            MultiBufferSource renderBuffer,
            int light,
            int overlay) {
        if (transformType == ItemDisplayContext.GUI
                || transformType == ItemDisplayContext.GROUND
                || transformType == ItemDisplayContext.FIXED) {
            light = LightTexture.FULL_BRIGHT;
        }

        matrix.pushPose();
        if (isThirdPersonHand(transformType)) {
            renderOnArmAttachment(stack, transformType, matrix, renderBuffer, light, overlay);
            matrix.popPose();
            return;
        }

        matrix.translate(0.0F, MODEL_PIVOT_Y, 0.0F);
        positionModel(matrix, transformType, stack);
        setupAndRender(stack, matrix, transformType, renderBuffer, light, overlay);
        matrix.popPose();
    }

    private static boolean isThirdPersonHand(ItemDisplayContext transformType) {
        return transformType == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
                || transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }

    /**
     * 手臂挂点绘制：调用方（ItemInHandLayer 或 MobFirearmHeldLayer）已做 translateAndRotate + 标准腕部旋转，
     * 此处不再重复旋转，只套 Blockbench 枢轴与枪模朝向。
     */
    private void renderOnArmAttachment(
            ItemStack stack,
            ItemDisplayContext transformType,
            PoseStack matrix,
            MultiBufferSource renderBuffer,
            int light,
            int overlay) {
        if (MobGunRenderContext.isActive()) {
            FirstPersonHandTransforms.applyMobThirdPerson(matrix, weaponKey);
            matrix.translate(0.0F, MODEL_PIVOT_Y, 0.0F);
            float scale = handScaleForTransform(transformType);
            matrix.scale(scale, scale, scale);
            matrix.translate(0.32F, 0.36F, 0.12F);
        } else {
            positionModel(matrix, transformType, stack);
            float scale = handScaleForTransform(transformType);
            matrix.scale(scale, scale, scale);
            matrix.translate(0.0F, MODEL_PIVOT_Y, 0.0F);
            matrix.translate(0.58F, 0.5F, 0.05F);
        }
        matrix.mulPose(new org.joml.Quaternionf().rotationX((float) Math.PI));
        matrix.mulPose(new org.joml.Quaternionf().rotationY((float) Math.PI));
        if (MobGunRenderContext.isActive()) {
            MobHeldFirearmTransforms.applyMobGripOrientation(matrix, weaponKey);
        }
        drawWeapon(stack, matrix, renderBuffer, light, overlay);
    }

    public abstract AbstractWeaponModel getWeaponModel();

    protected void positionModel(PoseStack stack, ItemDisplayContext transform, ItemStack item) {
        if (transform == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
            if (AimRenderTransforms.isAiming(item)) {
                AimRenderTransforms.applyAim(stack, weaponKey, transform);
            } else {
                FirstPersonHandTransforms.applyHipFire(stack, weaponKey);
            }
        } else if (transform == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
            FirstPersonHandTransforms.applyThirdPerson(stack, weaponKey);
        }
    }

    /** 创造栏 / 物品栏：只用 {@link GuiRenderTransforms#guiScale}，子类勿覆盖。 */
    protected final float scaleForTransform(ItemDisplayContext transform) {
        if (transform == ItemDisplayContext.GUI) {
            return GuiRenderTransforms.guiScale(weaponKey);
        }
        return handScaleForTransform(transform);
    }

    /** 第一/三人称缩放；默认见 {@link FirstPersonHandTransforms#handScale}。 */
    protected float handScaleForTransform(ItemDisplayContext transform) {
        return FirstPersonHandTransforms.handScale(weaponKey, transform);
    }

    protected void transformUi(PoseStack matrix) {
        GuiRenderTransforms.applyGlobalSlotNudge(matrix);
        GuiRenderTransforms.apply(matrix, weaponKey);
    }

    private void setupAndRender(
            ItemStack stack,
            PoseStack matrix,
            ItemDisplayContext transformType,
            MultiBufferSource renderBuffer,
            int light,
            int overlay) {
        if (transformType == ItemDisplayContext.GUI) {
            renderGui(stack, matrix, renderBuffer, light, overlay);
            return;
        }
        matrix.translate(0.7F, 0.5F, 0.05F);
        matrix.mulPose(new org.joml.Quaternionf().rotationX((float) Math.PI));
        matrix.mulPose(new org.joml.Quaternionf().rotationY((float) Math.PI));
        scaleModel(matrix, transformType);
        drawWeapon(stack, matrix, renderBuffer, light, overlay);
    }

    private void renderGui(
            ItemStack stack,
            PoseStack matrix,
            MultiBufferSource renderBuffer,
            int light,
            int overlay) {
        matrix.translate(0.5F, 0.57F, 0.0F);
        matrix.mulPose(new org.joml.Quaternionf().rotationX((float) Math.PI));
        matrix.mulPose(new org.joml.Quaternionf().rotationY((float) (Math.PI * 0.75)));
        matrix.mulPose(new org.joml.Quaternionf().rotationZ((float) (-Math.PI / 6)));
        scaleModel(matrix, ItemDisplayContext.GUI);
        transformUi(matrix);
        drawWeapon(stack, matrix, renderBuffer, light, overlay);
    }

    protected void scaleModel(PoseStack matrixStack, ItemDisplayContext transform) {
        float factor = scaleForTransform(transform);
        matrixStack.scale(factor, factor, factor);
    }

    private void drawWeapon(
            ItemStack stack,
            PoseStack matrix,
            MultiBufferSource renderBuffer,
            int light,
            int overlay) {
        TextureAtlasSprite sprite = FirearmClientTextures.getWeaponSprite();
        VertexConsumer consumer =
                sprite.wrap(
                        renderBuffer.getBuffer(
                                RenderType.entityCutoutNoCull(sprite.atlasLocation())));
        getWeaponModel().render(stack, matrix, consumer, light, overlay);
    }
}
