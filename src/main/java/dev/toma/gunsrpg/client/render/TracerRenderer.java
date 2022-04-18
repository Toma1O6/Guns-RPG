package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.properties.Properties;
import lib.toma.animations.AnimationUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class TracerRenderer<P extends AbstractProjectile> extends EntityRenderer<P> {

    private static final ResourceLocation TRACER = GunsRPG.makeResource("textures/entity/tracer.png");
    private static final int MAX_BRIGHTNESS = LightTexture.pack(15, 15);

    public TracerRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(P projectile) {
        return TRACER;
    }

    @Override
    public void render(P projectile, float yaw, float partialTicks, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light) {
        Integer rgb = projectile.getProperty(Properties.TRACER);
        if (rgb != null && projectile.tickCount > 0) {
            float r = RenderUtils.red(rgb);
            float g = RenderUtils.green(rgb);
            float b = RenderUtils.blue(rgb);
            float a = 1.0F;
            float x = AnimationUtils.linearInterpolate((float) projectile.getX(), (float) projectile.xo, partialTicks);
            float y = AnimationUtils.linearInterpolate((float) projectile.getY(), (float) projectile.yo, partialTicks);
            float z = AnimationUtils.linearInterpolate((float) projectile.getZ(), (float) projectile.zo, partialTicks);
            float sizeScale = 0.2f;
            Vector3d vec1 = projectile.position();
            Vector3d vec2 = vec1.add(projectile.getDeltaMovement());
            float x1 = (float) vec1.x - x;
            float y1 = (float) vec1.y - y - 0.1f;
            float z1 = (float) vec1.z - z;
            float x2 = sizeScale * (float) (vec2.x - x);
            float y2 = sizeScale * (float) (vec2.y - y - 0.1f);
            float z2 = sizeScale * (float) (vec2.z - z);
            MatrixStack.Entry entry = matrix.last();
            Matrix4f pose = entry.pose();
            Matrix3f normal = entry.normal();
            float scale = 0.1F;
            IVertexBuilder builder = typeBuffer.getBuffer(RenderType.entityCutout(TRACER));
            matrix.pushPose();
            matrix.mulPose(Vector3f.YP.rotation((float) Math.PI));
            matrix.mulPose(Vector3f.YN.rotationDegrees(projectile.yRot));
            matrix.mulPose(Vector3f.XN.rotationDegrees(projectile.xRot));
            int nx = 1;
            int ny = 1;
            int nz = 1;
            builder.vertex(pose, x1 - scale, y1 - scale, z1).color(r, g, b, a).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(MAX_BRIGHTNESS).normal(normal, nx, ny, nz).endVertex();
            builder.vertex(pose, x1 + scale, y1 + scale, z1).color(r, g, b, a).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(MAX_BRIGHTNESS).normal(normal, nx, ny, nz).endVertex();
            builder.vertex(pose, x2 + scale, y2 + scale, z2).color(r, g, b, a).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(MAX_BRIGHTNESS).normal(normal, nx, ny, nz).endVertex();
            builder.vertex(pose, x2 - scale, y2 - scale, z2).color(r, g, b, a).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(MAX_BRIGHTNESS).normal(normal, nx, ny, nz).endVertex();
            builder.vertex(pose, x1 - scale, y1 + scale, z1).color(r, g, b, a).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(MAX_BRIGHTNESS).normal(normal, nx, ny, nz).endVertex();
            builder.vertex(pose, x1 + scale, y1 - scale, z1).color(r, g, b, a).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(MAX_BRIGHTNESS).normal(normal, nx, ny, nz).endVertex();
            builder.vertex(pose, x2 + scale, y2 - scale, z2).color(r, g, b, a).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(MAX_BRIGHTNESS).normal(normal, nx, ny, nz).endVertex();
            builder.vertex(pose, x2 - scale, y2 + scale, z2).color(r, g, b, a).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(MAX_BRIGHTNESS).normal(normal, nx, ny, nz).endVertex();
            matrix.popPose();
        }
    }
}
