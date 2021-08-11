package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.AbstractWeaponModel;
import dev.toma.gunsrpg.common.capability.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public abstract class AbstractWeaponRenderer extends ItemStackTileEntityRenderer {

    private final ResourceLocation gunTexture;

    public AbstractWeaponRenderer() {
        this.gunTexture = createGunTextureInstance();
    }

    @Override
    public final void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrix, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        TextureManager manager = mc.getTextureManager();
        manager.bind(gunTexture);
        PlayerData.get(mc.player).ifPresent(data -> {
            matrix.pushPose();
            {
                matrix.translate(0.7, 0.5, 0.05);
                positionModel(matrix, transformType);
                matrix.mulPose(Vector3f.XP.rotationDegrees(180));
                matrix.mulPose(Vector3f.YP.rotationDegrees(180));
                float scaleFactor = scaleForTransform(transformType);
                matrix.scale(scaleFactor, scaleFactor, scaleFactor);
                if (transformType == ItemCameraTransforms.TransformType.GUI) {
                    defaultUITransform(matrix);
                }
                AbstractWeaponModel weaponModel = getWeaponModel();
                weaponModel.renderWeapon(stack, data, matrix, renderBuffer.getBuffer(weaponModel.renderType(gunTexture)), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            matrix.popPose();
        });
    }

    public abstract AbstractWeaponModel getWeaponModel();

    public abstract ResourceLocation createGunTextureInstance();

    protected void transformUI(MatrixStack matrix) {

    }

    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
    }

    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return 0.4F;
    }

    private void defaultUITransform(MatrixStack matrix) {
        transformUI(matrix);
        matrix.translate(-0.25, -0.3, 0.0);
        matrix.scale(0.7F, 0.7F, 0.7F);
        matrix.mulPose(Vector3f.ZN.rotationDegrees(45));
        matrix.mulPose(Vector3f.YN.rotationDegrees(90));
    }
}
