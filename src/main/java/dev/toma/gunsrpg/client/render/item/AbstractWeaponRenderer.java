package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.ClientEventHandler;
import dev.toma.gunsrpg.client.model.AbstractAttachmentModel;
import dev.toma.gunsrpg.client.model.ScopeModel;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public abstract class AbstractWeaponRenderer extends ItemStackTileEntityRenderer {

    public static final ResourceLocation ATTACHMENTS = GunsRPG.makeResource("textures/item/attachments.png");
    public static final ResourceLocation WEAPON = GunsRPG.makeResource("textures/item/weapon_texture_map.png");

    @Override
    public final void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrix, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        boolean held = stack == mc.player.getMainHandItem();
        PlayerData.get(mc.player).ifPresent(data -> {
            matrix.pushPose();
            {
                positionModel(matrix, transformType);
                setupAndRender(stack, matrix, transformType, data, renderBuffer, light, overlay, held);
                if (hasCustomAttachments() && canRenderAttachments(transformType)) {
                    matrix.pushPose();
                    float aimProgress = data.getAimInfo().getProgress(ClientEventHandler.partialTicks);
                    renderAttachments(data, matrix, renderBuffer, light, overlay, aimProgress);
                    matrix.popPose();
                }
            }
            matrix.popPose();
        });
    }

    public abstract AbstractWeaponModel getWeaponModel();

    protected boolean hasCustomAttachments() {
        return false;
    }

    protected void transformUI(MatrixStack matrix) {
    }

    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
    }

    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return 0.4F;
    }

    protected void renderAttachments(IPlayerData data, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light, int overlay, float progress) {
    }

    protected static void renderReflex(IRenderConfig config, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay, float progress) {
        renderConfigured(WeaponModels.REFLEX, config, stack, buffer, light, overlay, progress);
    }

    protected static void renderSuppressor(IRenderConfig config, MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay, float progress) {
        renderConfigured(WeaponModels.SUPPRESSOR, config, stack, buffer, light, overlay, progress);
    }

    protected static void renderScope(IRenderConfig config, MatrixStack poseStack, IRenderTypeBuffer buffer, int light, int overlay, float progress) {
        renderConfigured(WeaponModels.SCOPE, config, poseStack, buffer, light, overlay, progress);
    }

    protected static void renderScope(IRenderConfig config, MatrixStack poseStack, IRenderTypeBuffer buffer, int light, int overlay, float progress, ResourceLocation reticleTexture) {
        ScopeModel.prepare(reticleTexture);
        renderConfigured(WeaponModels.SCOPE, config, poseStack, buffer, light, overlay, progress);
    }

    protected static void renderConfigured(AbstractAttachmentModel model, IRenderConfig config, MatrixStack pose, IRenderTypeBuffer buffer, int light, int overlay, float aimProgress) {
        pose.pushPose();
        config.applyTo(pose);
        model.renderAttachment(pose, buffer, light, overlay, aimProgress);
        pose.popPose();
    }

    private void defaultUITransform(MatrixStack matrix) {
        transformUI(matrix);
        matrix.translate(-0.25, -0.3, 0.0);
        matrix.scale(0.7F, 0.7F, 0.7F);
        matrix.mulPose(Vector3f.ZN.rotationDegrees(45));
        matrix.mulPose(Vector3f.YN.rotationDegrees(90));
    }

    private boolean canRenderAttachments(ItemCameraTransforms.TransformType type) {
        return type != ItemCameraTransforms.TransformType.GUI;
    }

    private void setupAndRender(ItemStack stack, MatrixStack matrix, ItemCameraTransforms.TransformType transformType, IPlayerData data, IRenderTypeBuffer renderBuffer, int light, int overlay, boolean animate) {
        matrix.translate(0.7, 0.5, 0.05);
        matrix.mulPose(Vector3f.XP.rotationDegrees(180));
        matrix.mulPose(Vector3f.YP.rotationDegrees(180));
        float scaleFactor = scaleForTransform(transformType);
        matrix.scale(scaleFactor, scaleFactor, scaleFactor);
        if (transformType == ItemCameraTransforms.TransformType.GUI) {
            defaultUITransform(matrix);
        }
        AbstractWeaponModel weaponModel = getWeaponModel();
        weaponModel.render(stack, data, matrix, renderBuffer, renderBuffer.getBuffer(weaponModel.renderType(WEAPON)), light, overlay, animate && transformType.firstPerson());
    }
}
