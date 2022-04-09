package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.properties.Properties;
import lib.toma.animations.AnimationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
            float scale = 0.1F;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuilder();
            matrix.pushPose();
            Minecraft.getInstance().getTextureManager().bind(TRACER);
            matrix.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, projectile.yRotO, projectile.yRot) - 90.0F));
            matrix.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, projectile.xRotO, projectile.xRot)));
            matrix.mulPose(Vector3f.XP.rotationDegrees(45.0F));
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            builder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
            builder.vertex(pose, x1 - scale, y1 - scale, z1).color(r, g, b, a).uv(0.0f, 0.0f).uv2(MAX_BRIGHTNESS).endVertex();
            builder.vertex(pose, x1 + scale, y1 + scale, z1).color(r, g, b, a).uv(0.0f, 1.0f).uv2(MAX_BRIGHTNESS).endVertex();
            builder.vertex(pose, x2 + scale, y2 + scale, z2).color(r, g, b, a).uv(1.0f, 1.0f).uv2(MAX_BRIGHTNESS).endVertex();
            builder.vertex(pose, x2 - scale, y2 - scale, z2).color(r, g, b, a).uv(1.0f, 0.0f).uv2(MAX_BRIGHTNESS).endVertex();
            builder.vertex(pose, x1 - scale, y1 + scale, z1).color(r, g, b, a).uv(0.0f, 0.0f).uv2(MAX_BRIGHTNESS).endVertex();
            builder.vertex(pose, x1 + scale, y1 - scale, z1).color(r, g, b, a).uv(0.0f, 1.0f).uv2(MAX_BRIGHTNESS).endVertex();
            builder.vertex(pose, x2 + scale, y2 - scale, z2).color(r, g, b, a).uv(1.0f, 1.0f).uv2(MAX_BRIGHTNESS).endVertex();
            builder.vertex(pose, x2 - scale, y2 + scale, z2).color(r, g, b, a).uv(1.0f, 0.0f).uv2(MAX_BRIGHTNESS).endVertex();
            tessellator.end();
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            matrix.popPose();
        }
    }
}
