package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelAirdrop;
import dev.toma.gunsrpg.common.entity.EntityAirdrop;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderAirdrop extends Render<EntityAirdrop> {

    private static final ResourceLocation TEXTURES = GunsRPG.makeResource("textures/entity/airdrop.png");
    private final ModelAirdrop model = new ModelAirdrop();

    public RenderAirdrop(RenderManager manager) {
        super(manager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityAirdrop entity) {
        return TEXTURES;
    }

    @Override
    public void doRender(EntityAirdrop entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.bindTexture(this.getEntityTexture(entity));
        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x, y, z);
            GlStateManager.translate(0f, 1.8f, 0f);
            GlStateManager.scale(0.08f, 0.08f, 0.08f);
            GlStateManager.rotate(180f, 1f, 0f, 0f);
            model.render();
        }
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
