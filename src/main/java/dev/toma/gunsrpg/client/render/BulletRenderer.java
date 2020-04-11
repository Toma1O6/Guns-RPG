package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.common.entity.EntityBullet;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class BulletRenderer extends Render<EntityBullet> {

    public BulletRenderer(RenderManager manager) {
        super(manager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityBullet entity) {
        return null;
    }

    @Override
    public void doRender(EntityBullet entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(-x, -y, -z);
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(entity.posX, entity.posY, entity.posZ).color(0.0F, 0.0F, 0.0F, 1.0F);
        builder.pos(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).color(0.0F, 0.0F, 0.0F, 1.0F);
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}
