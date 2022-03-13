package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.StashDetectorModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class StashDetectorRenderer extends ItemStackTileEntityRenderer {

    private final StashDetectorModel model = new StashDetectorModel();
    private final ResourceLocation texture = GunsRPG.makeResource("textures/block/crystal_map.png");

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType type, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
        matrixStack.pushPose();
        IVertexBuilder builder = typeBuffer.getBuffer(RenderType.entitySolid(texture));
        matrixStack.translate(0.5, 0.8, 0.5);
        if (type == ItemCameraTransforms.TransformType.GUI) {
            matrixStack.translate(0.0, -0.2, 0.0);
            matrixStack.scale(1.5F, 1.5F, 1.5F);
        }
        matrixStack.mulPose(Vector3f.XP.rotation((float) Math.PI));
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(45.0F));
        matrixStack.scale(0.12F, 0.12F, 0.12F);
        model.renderStashDetector(matrixStack, typeBuffer, builder, light, overlay, type);
        matrixStack.popPose();
    }
}
