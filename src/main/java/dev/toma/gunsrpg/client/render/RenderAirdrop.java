package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelAirdrop;
import dev.toma.gunsrpg.common.entity.AirdropEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class RenderAirdrop extends EntityRenderer<AirdropEntity> {

    private static final ResourceLocation TEXTURES = GunsRPG.makeResource("textures/entity/airdrop.png");
    private final ModelAirdrop model = new ModelAirdrop();

    public RenderAirdrop(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(AirdropEntity entity) {
        return TEXTURES;
    }

    @Override
    public void render(AirdropEntity airdrop, float rotation, float partialTicks, MatrixStack matrix, IRenderTypeBuffer renderBuffer, int light) {
        matrix.pushPose();
        {
            matrix.translate(0.0F, 1.8F, 0.0F);
            matrix.scale(0.08F, 0.08F, 0.08F);
            matrix.mulPose(Vector3f.XP.rotationDegrees(180F));
            IVertexBuilder builder = renderBuffer.getBuffer(model.renderType(getTextureLocation(airdrop)));
            model.renderToBuffer(matrix, builder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        matrix.popPose();
        super.render(airdrop, rotation, partialTicks, matrix, renderBuffer, light);
    }
}
