package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.BloodmoonGolemModel;
import dev.toma.gunsrpg.common.entity.BloodmoonGolemEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class BloodmoonGolemRenderer extends MobRenderer<BloodmoonGolemEntity, BloodmoonGolemModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/iron_golem/iron_golem.png");

    public BloodmoonGolemRenderer(EntityRendererManager manager) {
        super(manager, new BloodmoonGolemModel(), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(BloodmoonGolemEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void setupRotations(BloodmoonGolemEntity entityLiving, MatrixStack stack, float age, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, stack, age, rotationYaw, partialTicks);
        if ((double) entityLiving.animationSpeed >= 0.01D) {
            float f1 = entityLiving.animationPosition - entityLiving.animationSpeed * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            stack.mulPose(Vector3f.ZP.rotationDegrees(6.5F * f2));
        }
    }
}
