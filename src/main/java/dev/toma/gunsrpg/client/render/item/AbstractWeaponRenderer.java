package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.ModelWeapon;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractWeaponRenderer extends ItemStackTileEntityRenderer {

    private final ModelWeapon weaponModel;
    private final ResourceLocation gunTexture;

    public AbstractWeaponRenderer() {
        this.weaponModel = createModelInstance();
        this.gunTexture = createGunTextureInstance();
    }

    @Override
    public final void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrix, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        TextureManager manager = mc.getTextureManager();
        manager.bind(gunTexture);
        PlayerDataFactory.get(mc.player).ifPresent(data -> {
            matrix.pushPose();
            {
                prepareRender(matrix, transformType);
                weaponModel.renderWeapon(stack, data, matrix, renderBuffer.getBuffer(weaponModel.renderType(gunTexture)), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            matrix.popPose();
        });
    }

    public abstract ModelWeapon createModelInstance();

    public abstract ResourceLocation createGunTextureInstance();

    public void prepareRender(MatrixStack stack, ItemCameraTransforms.TransformType transform) {}
}
