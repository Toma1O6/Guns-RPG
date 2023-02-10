package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.client.model.AbstractTurretModel;
import dev.toma.gunsrpg.client.render.TurretRenderer;
import dev.toma.gunsrpg.common.entity.TurretEntity;
import dev.toma.gunsrpg.common.item.TurretItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class TurretItemRenderer extends ItemStackTileEntityRenderer {

    private AbstractTurretModel model;

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack poseStack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
        if (model != null) {
            IVertexBuilder builder = typeBuffer.getBuffer(RenderType.entityCutout(TurretRenderer.TEXTURE));
            float modelScale = 0.45F;
            float xRot = 0.0F;
            float yRot = 45.0F;
            poseStack.pushPose();
            poseStack.translate(0.5, 0.75, 0.0);
            poseStack.scale(modelScale, modelScale, modelScale);
            poseStack.mulPose(Vector3f.XP.rotation((float) Math.PI));
            poseStack.mulPose(Vector3f.XP.rotationDegrees(30.0F));
            model.setTurretRotations(xRot, xRot, yRot, yRot, 1.0F);
            model.renderToBuffer(poseStack, builder, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        } else {
            if (stack.getItem() instanceof TurretItem) {
                TurretItem item = (TurretItem) stack.getItem();
                EntityType<TurretEntity> type = item.getEntityType();
                Minecraft client = Minecraft.getInstance();
                EntityRendererManager rendererManager = client.getEntityRenderDispatcher();
                EntityRenderer<?> renderer = rendererManager.renderers.get(type);
                if (renderer instanceof TurretRenderer) {
                    this.model = ((TurretRenderer) renderer).getModel();
                }
            }
        }
    }
}
