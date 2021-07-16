package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.common.entity.EntityCrossbowBolt;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TippedArrowRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;

public class CrossbowBoltRenderer extends EntityRenderer<EntityCrossbowBolt> {

    public CrossbowBoltRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(EntityCrossbowBolt entity) {
        return TippedArrowRenderer.NORMAL_ARROW_LOCATION;
    }

    @Override
    public void render(EntityCrossbowBolt bolt, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
        stack.pushPose();
        stack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, bolt.yRotO, bolt.yRot) - 90.0F));
        stack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, bolt.xRotO, bolt.xRot)));
        stack.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        stack.scale(0.05625F, 0.05625F, 0.05625F);
        stack.translate(-4.0D, 0.0D, 0.0D);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(bolt)));
        MatrixStack.Entry matrixstack$entry = stack.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, light);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, light);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, light);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, light);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, light);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, light);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, light);
        this.vertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, light);

        for(int j = 0; j < 4; ++j) {
            stack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, light);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, light);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, light);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, light);
        }

        stack.popPose();
        super.render(bolt, entityYaw, partialTicks, stack, buffer, light);
    }

    public void vertex(Matrix4f pose, Matrix3f normal, IVertexBuilder builder, int x, int y, int z, float u, float v, int normalX, int normalY, int normalZ, int light) {
        builder.vertex(pose, (float)x, (float)y, (float)z).color(255, 255, 255, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, (float)normalX, (float)normalZ, (float)normalY).endVertex();
    }
}
