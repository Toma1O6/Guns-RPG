package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.client.model.ModelRocketAngel;
import dev.toma.gunsrpg.common.entity.RocketAngelEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderRocketAngel extends RenderBiped<RocketAngelEntity> {

    private static final ResourceLocation VEX_TEXTURE = new ResourceLocation("textures/entity/illager/vex.png");

    public RenderRocketAngel(RenderManager manager) {
        super(manager, new ModelRocketAngel(), 0.6F);
    }

    @Override
    protected ResourceLocation getEntityTexture(RocketAngelEntity entity) {
        return VEX_TEXTURE;
    }

    @Override
    protected void preRenderCallback(RocketAngelEntity entitylivingbaseIn, float partialTickTime) {
        GlStateManager.translate(0.0F, 0.4F, 0.0F);
    }
}
