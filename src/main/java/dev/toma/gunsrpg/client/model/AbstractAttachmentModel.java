package dev.toma.gunsrpg.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.item.AbstractWeaponRenderer;
import dev.toma.gunsrpg.config.ModConfig;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractAttachmentModel extends AbstractSolidEntityModel {

    public static final ResourceLocation GLASS_TEXTURE = GunsRPG.makeResource("textures/scope/glass.png");

    public abstract void renderAttachment(MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay, float aimingProgress);

    protected static void renderReflexSight(MatrixStack matrix, IRenderTypeBuffer buffer, ModelRenderer bone, ModelRenderer glass, float progress, ResourceLocation reticleTexture, int reticleColor, int light, int overlay) {
        IVertexBuilder modelVertexBuilder = buffer.getBuffer(RenderType.entitySolid(AbstractWeaponRenderer.ATTACHMENTS));
        bone.render(matrix, modelVertexBuilder, light, overlay);
        IVertexBuilder reticleVertexBuilder = buffer.getBuffer(RenderType.entityShadow(reticleTexture));
        float a = ModConfig.clientConfig.developerMode.get() ? 1.0F : progress;
        float r = ((reticleColor >> 16) & 255) / 255.0F;
        float g = ((reticleColor >>  8) & 255) / 255.0F;
        float b = ( reticleColor        & 255) / 255.0F;
        glass.render(matrix, reticleVertexBuilder, light, overlay, r, g, b, a);
    }

    protected static void renderScopeWithGlass(MatrixStack matrix, IRenderTypeBuffer buffer, ModelRenderer scopeModel, ModelRenderer reticleModel, ModelRenderer overlayModel, float progress, ResourceLocation reticleTexture, int light, int overlay) {
        float inv = 1.0F - progress;
        matrix.pushPose();
        matrix.translate(0.0F, 0.0F, 0.78F * progress);
        matrix.scale(1.0F, 1.0F, 0.07F + 0.93F * inv);
        IVertexBuilder modelVertexBuilder = buffer.getBuffer(RenderType.entitySolid(AbstractWeaponRenderer.ATTACHMENTS));
        scopeModel.render(matrix, modelVertexBuilder, light, overlay);
        IVertexBuilder reticleVertexBuilder = buffer.getBuffer(RenderType.entityShadow(reticleTexture));
        reticleModel.render(matrix, reticleVertexBuilder, light, overlay, 1.0F, 1.0F, 1.0F, progress);
        IVertexBuilder overlayVertexBuilder = buffer.getBuffer(RenderType.entityShadow(GLASS_TEXTURE)); // shadow works best for this use case
        overlayModel.render(matrix, overlayVertexBuilder, light, overlay, 0.0F, 0.0F, 0.0F, inv);
        matrix.popPose();
    }

    public static void setBuiltInRender(ModelRenderer render) {
        for (ModelRenderer.ModelBox box : render.cubes) {
            for (ModelRenderer.TexturedQuad quad : box.polygons) {
                ModelRenderer.PositionTextureVertex ver1 = quad.vertices[0];
                ModelRenderer.PositionTextureVertex ver2 = quad.vertices[1];
                ModelRenderer.PositionTextureVertex ver3 = quad.vertices[2];
                ModelRenderer.PositionTextureVertex ver4 = quad.vertices[3];
                setUV(ver1, 0.0F, 0.0F);
                setUV(ver2, 0.0F, 1.0F);
                setUV(ver3, 1.0F, 1.0F);
                setUV(ver4, 1.0F, 0.0F);
            }
        }
    }

    @Override
    public final void renderToBuffer(MatrixStack matrix, IVertexBuilder vertexbuilder, int light, int overlay, float red, float green, float blue, float alpha) {
    }

    private static void setUV(ModelRenderer.PositionTextureVertex vertex, float u, float v) {
        vertex.u = u;
        vertex.v = v;
    }
}
