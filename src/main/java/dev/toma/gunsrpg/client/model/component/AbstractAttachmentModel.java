package dev.toma.gunsrpg.client.model.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.AbstractSolidEntityModel;
import dev.toma.gunsrpg.config.ModConfig;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractAttachmentModel extends AbstractSolidEntityModel {

    public static final ResourceLocation GLASS_TEXTURE = GunsRPG.makeResource("textures/scope/glass.png");
    private static final int LIGHT = LightTexture.pack(15, 15);

    public abstract void renderAttachment(MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay, float aimingProgress);

    protected static void renderReflexSight(MatrixStack matrix, IRenderTypeBuffer buffer, IOpticsProvider provider, float progress, int light, int overlay) {
        IVertexBuilder modelVertexBuilder = buffer.getBuffer(RenderType.entitySolid(provider.getComponentTextureMap()));
        provider.renderOptic(matrix, modelVertexBuilder, light, overlay);
        IVertexBuilder reticleVertexBuilder = buffer.getBuffer(RenderType.entityShadow(provider.getReticleTextureMap()));
        int reticleColor = provider.getReticleTintARGB();
        float cfgA = ((reticleColor >> 24) & 255) / 255.0F;
        float a = ModConfig.clientConfig.developerMode.get() ? 1.0F : Math.min(cfgA, progress);
        float r = ((reticleColor >> 16) & 255) / 255.0F;
        float g = ((reticleColor >>  8) & 255) / 255.0F;
        float b = ( reticleColor        & 255) / 255.0F;
        provider.getGlassModel().render(matrix, reticleVertexBuilder, LIGHT, overlay, r, g, b, a);
    }

    public static void renderScopeWithGlass(MatrixStack matrix, IRenderTypeBuffer buffer, IOpticsProvider provider, float progress, int light, int overlay) {
        float inv = 1.0F - progress;
        float sizeProgress = progress >= 0.9F ? progress : 0.0F;
        float sizeProgressInv = 1.0F - sizeProgress;
        matrix.pushPose();
        matrix.translate(0.0F, 0.0F, 0.78F * sizeProgress);
        matrix.scale(1.0F, 1.0F, 0.07F + 0.93F * sizeProgressInv);
        IVertexBuilder modelVertexBuilder = buffer.getBuffer(RenderType.entitySolid(provider.getComponentTextureMap()));
        provider.renderOptic(matrix, modelVertexBuilder, light, overlay);
        IVertexBuilder reticleVertexBuilder = buffer.getBuffer(RenderType.entityShadow(provider.getReticleTextureMap()));
        provider.getGlassModel().render(matrix, reticleVertexBuilder, light, overlay, 1.0F, 1.0F, 1.0F, progress);
        IVertexBuilder overlayVertexBuilder = buffer.getBuffer(RenderType.entityShadow(GLASS_TEXTURE)); // shadow works best for this use case
        provider.getOverlayModel().render(matrix, overlayVertexBuilder, light, overlay, 0.0F, 0.0F, 0.0F, inv);
        matrix.popPose();
    }

    public static void setBuiltInRender(ModelRenderer render) {
        setBuiltInRender(render, 0.0F);
    }

    public static void setBuiltInRender(ModelRenderer render, float offset) {
        for (ModelRenderer.ModelBox box : render.cubes) {
            for (ModelRenderer.TexturedQuad quad : box.polygons) {
                ModelRenderer.PositionTextureVertex ver1 = quad.vertices[0];
                ModelRenderer.PositionTextureVertex ver2 = quad.vertices[1];
                ModelRenderer.PositionTextureVertex ver3 = quad.vertices[2];
                ModelRenderer.PositionTextureVertex ver4 = quad.vertices[3];
                float max = 1.0F - offset;
                setUV(ver1, max, offset);
                setUV(ver2, offset, offset);
                setUV(ver3, offset, max);
                setUV(ver4, max, max);
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
