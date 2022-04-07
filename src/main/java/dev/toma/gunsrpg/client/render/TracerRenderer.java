package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.entity.projectile.TracerInfo;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.properties.Properties;
import lib.toma.animations.AnimationUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;

public class TracerRenderer<P extends AbstractProjectile> extends EntityRenderer<P> {

    public TracerRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(P projectile) {
        return null;
    }

    @Override
    public void render(P projectile, float yaw, float partialTicks, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light) {
        TracerInfo info = projectile.getProperty(Properties.TRACER);
        if (info != null) {
            int rgb = info.getRgb();
            float r = RenderUtils.red(rgb);
            float g = RenderUtils.green(rgb);
            float b = RenderUtils.blue(rgb);
            Vector3d[] positions = info.getPositions();
            int length = positions.length;
            float x = AnimationUtils.linearInterpolate((float) projectile.getX(), (float) projectile.xo, partialTicks);
            float y = AnimationUtils.linearInterpolate((float) projectile.getY(), (float) projectile.yo, partialTicks);
            float z = AnimationUtils.linearInterpolate((float) projectile.getZ(), (float) projectile.zo, partialTicks);
            for (int i = 0; i < length - 1; i++) {
                Vector3d vec1 = positions[i];
                Vector3d vec2 = positions[i + 1];
                if (vec1 == null || vec2 == null) break;
                Matrix4f pose = matrix.last().pose();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder builder = tessellator.getBuilder();
                RenderSystem.disableTexture();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                builder.begin(1, DefaultVertexFormats.POSITION_COLOR);
                float x1 = (float) vec1.x - x;
                float y1 = (float) vec1.y - y;
                float z1 = (float) vec1.z - z;
                float x2 = (float) vec2.x - x;
                float y2 = (float) vec2.y - y;
                float z2 = (float) vec2.z - z;
                float sc = 0.05f;
                RenderSystem.lineWidth(2.0F);
                builder.vertex(pose, x1, y1 - sc, z1).color(r, g, b, 1.0F).endVertex();
                builder.vertex(pose, x2, y2 - sc, z2).color(r, g, b, 1.0F).endVertex();
                tessellator.end();
                RenderSystem.disableBlend();
                RenderSystem.enableTexture();
            }
        }
    }
}
