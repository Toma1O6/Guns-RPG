package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.AbstractTurretModel;
import dev.toma.gunsrpg.common.entity.TurretEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class TurretRenderer extends EntityRenderer<TurretEntity> {

    public static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/item/weapon_texture_map.png");
    private final AbstractTurretModel model;

    public TurretRenderer(EntityRendererManager manager, AbstractTurretModel model) {
        super(manager);
        this.model = model;
    }

    @Override
    public ResourceLocation getTextureLocation(TurretEntity turret) {
        return TEXTURE;
    }

    public AbstractTurretModel getModel() {
        return model;
    }

    @Override
    public void render(TurretEntity turret, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer typeBuffer, int light) {
        stack.pushPose();
        IVertexBuilder builder = typeBuffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(turret)));
        stack.translate(0.0, 1.5, 0.0);
        stack.mulPose(Vector3f.XP.rotation((float) Math.PI));
        model.setTurretRotations(turret.xRot, turret.xRotO, turret.yRot, turret.yRotO, partialTicks);
        model.renderToBuffer(stack, builder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.popPose();
    }
}
