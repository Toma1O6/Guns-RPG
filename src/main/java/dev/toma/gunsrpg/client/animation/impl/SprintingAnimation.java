package dev.toma.gunsrpg.client.animation.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.AnimationFactory;
import dev.toma.gunsrpg.client.animation.AnimationProcessor;
import dev.toma.gunsrpg.sided.ClientSideManager;
import net.minecraft.util.math.vector.Vector3f;

public class SprintingAnimation extends AnimationFactory {

    @Override
    public float getCurrentProgress() {
        return current;
    }

    @Override
    public void update() {
        float speed = 0.15F;
        if(player.isSprinting() && current < 1.0F) {
            current = Math.min(1.0F, current + speed);
        } else if(!player.isSprinting() && current > 0.0F) {
            current = Math.max(0.0F, current - speed);
        }
    }

    @Override
    public void animateItemHands(MatrixStack matrix, float partialTicks) {
        matrix.mulPose(Vector3f.XP.rotationDegrees(-20.0F * smooth));
    }

    @Override
    public void animateLeftArm(MatrixStack matrix, float partialTicks) {
        AnimationProcessor processor = ClientSideManager.instance().processor();
        if(!processor.isRenderingDualWield()) {
            matrix.translate(0.0, -0.5 * smooth, 0.8 * smooth);
            matrix.mulPose(Vector3f.YP.rotationDegrees(40.0F * smooth));
        }
    }

    @Override
    public boolean isFinished() {
        return !player.isSprinting() && current <= 0.0F;
    }
}
