package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.client.model.ModelBloodmoonGolem;
import dev.toma.gunsrpg.common.entity.BloodmoonGolemEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBloodmoonGolem extends RenderLiving<BloodmoonGolemEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/iron_golem.png");

    public RenderBloodmoonGolem(RenderManager manager) {
        super(manager, new ModelBloodmoonGolem(), 0.5F);
    }

    protected ResourceLocation getEntityTexture(BloodmoonGolemEntity entity) {
        return TEXTURE;
    }

    protected void applyRotations(BloodmoonGolemEntity entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
        if ((double) entityLiving.limbSwingAmount >= 0.01D) {
            float f = 13.0F;
            float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            GlStateManager.rotate(6.5F * f2, 0.0F, 0.0F, 1.0F);
        }
    }
}
