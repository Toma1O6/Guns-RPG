package dev.toma.gunsrpg.client.animation.impl;

import dev.toma.gunsrpg.client.animation.TickableAnimation;
import net.minecraft.client.renderer.GlStateManager;

public class RecoilAnimation extends TickableAnimation {

    protected RecoilAnimation(int time) {
        super(time);
    }

    public static RecoilAnimation newInstance(int length) {
        return new RecoilAnimation(length);
    }

    @Override
    public void animateItemHands(float partialTicks) {
        GlStateManager.rotate(3.0F * smooth, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 0.1F * smooth);
    }
}
