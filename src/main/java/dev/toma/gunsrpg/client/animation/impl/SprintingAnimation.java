package dev.toma.gunsrpg.client.animation.impl;

import dev.toma.gunsrpg.client.animation.AbstractAnimation;
import net.minecraft.client.renderer.GlStateManager;

public class SprintingAnimation extends AbstractAnimation {

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
    public void animateItemHands(float partialTicks) {
        GlStateManager.rotate(30.0F * smooth, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 0.2F * smooth);
    }

    @Override
    public void animateLeftArm(float partialTicks) {
        GlStateManager.translate(0.0F, -0.5F * smooth, 0.8F * smooth);
        GlStateManager.rotate(40.0F * smooth, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public boolean isFinished() {
        return !player.isSprinting() && current <= 0.0F;
    }
}
