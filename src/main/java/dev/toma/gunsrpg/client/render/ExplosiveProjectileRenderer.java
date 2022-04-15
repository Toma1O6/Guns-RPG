package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class ExplosiveProjectileRenderer<T extends Entity> extends EntityRenderer<T> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/item/weapon_texture_map.png");
    private final Model model;

    public ExplosiveProjectileRenderer(EntityRendererManager manager, Model model) {
        super(manager);
        this.model = model;
        shadowRadius = 0;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light) {
        if (entity.tickCount < 1) return;
        matrixStack.pushPose();
        IVertexBuilder builder = typeBuffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
        matrixStack.translate(0.0, -1.0, 0.0);
        matrixStack.mulPose(Vector3f.YP.rotation((float) Math.PI));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(entityYaw));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(entity.xRot));
        matrixStack.scale(0.8F, 0.8F, 0.8F);
        model.renderToBuffer(matrixStack, builder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }
}
