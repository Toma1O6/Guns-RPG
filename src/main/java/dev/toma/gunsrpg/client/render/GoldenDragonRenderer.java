package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EnderCrystalRenderer;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Random;

public class GoldenDragonRenderer extends EnderDragonRenderer {

    public static final ResourceLocation DRAGON_LOCATION = GunsRPG.makeResource("textures/entity/golden_dragon.png");
    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0D) / 2.0D);
    private static final ResourceLocation DRAGON_EXPLODING_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
    private static final ResourceLocation DRAGON_EYES_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(DRAGON_LOCATION);
    private static final RenderType DECAL = RenderType.entityDecal(DRAGON_LOCATION);
    private static final RenderType EYES = RenderType.eyes(DRAGON_EYES_LOCATION);

    public GoldenDragonRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public void render(EnderDragonEntity dragon, float yaw, float renderTickTime, MatrixStack stack, IRenderTypeBuffer renderBuffer, int light) {
        stack.pushPose();
        float f = (float)dragon.getLatencyPos(7, renderTickTime)[0];
        float f1 = (float)(dragon.getLatencyPos(5, renderTickTime)[1] - dragon.getLatencyPos(10, renderTickTime)[1]);
        stack.mulPose(Vector3f.YP.rotationDegrees(-f));
        stack.mulPose(Vector3f.XP.rotationDegrees(f1 * 10.0F));
        stack.translate(0.0D, 0.0D, 1.0D);
        stack.scale(-1.0F, -1.0F, 1.0F);
        stack.translate(0.0D, (double)-1.501F, 0.0D);
        boolean flag = dragon.hurtTime > 0;
        this.model.prepareMobModel(dragon, 0.0F, 0.0F, renderTickTime);
        if (dragon.dragonDeathTime > 0) {
            float f2 = (float)dragon.dragonDeathTime / 200.0F;
            IVertexBuilder ivertexbuilder = renderBuffer.getBuffer(RenderType.dragonExplosionAlpha(DRAGON_EXPLODING_LOCATION, f2));
            this.model.renderToBuffer(stack, ivertexbuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            IVertexBuilder ivertexbuilder1 = renderBuffer.getBuffer(DECAL);
            this.model.renderToBuffer(stack, ivertexbuilder1, light, OverlayTexture.pack(0.0F, flag), 1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            IVertexBuilder ivertexbuilder3 = renderBuffer.getBuffer(RENDER_TYPE);
            this.model.renderToBuffer(stack, ivertexbuilder3, light, OverlayTexture.pack(0.0F, flag), 1.0F, 1.0F, 1.0F, 1.0F);
        }

        IVertexBuilder ivertexbuilder4 = renderBuffer.getBuffer(EYES);
        this.model.renderToBuffer(stack, ivertexbuilder4, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (dragon.dragonDeathTime > 0) {
            float f5 = ((float)dragon.dragonDeathTime + renderTickTime) / 200.0F;
            float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
            Random random = new Random(432L);
            IVertexBuilder ivertexbuilder2 = renderBuffer.getBuffer(RenderType.lightning());
            stack.pushPose();
            stack.translate(0.0D, -1.0D, -2.0D);

            for(int i = 0; (float)i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
                stack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
                stack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
                stack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
                stack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
                stack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
                stack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + f5 * 90.0F));
                float f3 = random.nextFloat() * 20.0F + 5.0F + f7 * 10.0F;
                float f4 = random.nextFloat() * 2.0F + 1.0F + f7 * 2.0F;
                Matrix4f matrix4f = stack.last().pose();
                int j = (int)(255.0F * (1.0F - f7));
                vertex01(ivertexbuilder2, matrix4f, j);
                vertex2(ivertexbuilder2, matrix4f, f3, f4);
                vertex3(ivertexbuilder2, matrix4f, f3, f4);
                vertex01(ivertexbuilder2, matrix4f, j);
                vertex3(ivertexbuilder2, matrix4f, f3, f4);
                vertex4(ivertexbuilder2, matrix4f, f3, f4);
                vertex01(ivertexbuilder2, matrix4f, j);
                vertex4(ivertexbuilder2, matrix4f, f3, f4);
                vertex2(ivertexbuilder2, matrix4f, f3, f4);
            }

            stack.popPose();
        }

        stack.popPose();
        if (dragon.nearestCrystal != null) {
            stack.pushPose();
            float f6 = (float)(dragon.nearestCrystal.getX() - MathHelper.lerp(renderTickTime, dragon.xo, dragon.getX()));
            float f8 = (float)(dragon.nearestCrystal.getY() - MathHelper.lerp(renderTickTime, dragon.yo, dragon.getY()));
            float f9 = (float)(dragon.nearestCrystal.getZ() - MathHelper.lerp(renderTickTime, dragon.zo, dragon.getZ()));
            renderCrystalBeams(f6, f8 + EnderCrystalRenderer.getY(dragon.nearestCrystal, renderTickTime), f9, renderTickTime, dragon.tickCount, stack, renderBuffer, light);
            stack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EnderDragonEntity entity) {
        return DRAGON_LOCATION;
    }

    private static void vertex01(IVertexBuilder p_229061_0_, Matrix4f p_229061_1_, int p_229061_2_) {
        p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
        p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
    }

    private static void vertex2(IVertexBuilder p_229060_0_, Matrix4f p_229060_1_, float p_229060_2_, float p_229060_3_) {
        p_229060_0_.vertex(p_229060_1_, -HALF_SQRT_3 * p_229060_3_, p_229060_2_, -0.5F * p_229060_3_).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex3(IVertexBuilder p_229062_0_, Matrix4f p_229062_1_, float p_229062_2_, float p_229062_3_) {
        p_229062_0_.vertex(p_229062_1_, HALF_SQRT_3 * p_229062_3_, p_229062_2_, -0.5F * p_229062_3_).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex4(IVertexBuilder p_229063_0_, Matrix4f p_229063_1_, float p_229063_2_, float p_229063_3_) {
        p_229063_0_.vertex(p_229063_1_, 0.0F, p_229063_2_, 1.0F * p_229063_3_).color(255, 0, 255, 0).endVertex();
    }
}
