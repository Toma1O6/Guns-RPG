package dev.toma.gunsrpg.client.animation.impl;

import com.mojang.blaze3d.matrix.MatrixStack;

public class ImprovedAimAnimation extends AimingAnimation {

    private IAnimator item = (stack, f) -> {};

    public ImprovedAimAnimation(float x, float y, float z) {
        super(x, y, z);
    }

    public ImprovedAimAnimation animateItem(IAnimator animation) {
        this.item = animation;
        return this;
    }

    @Override
    public void animateItem(MatrixStack matrix, float partialTicks) {
        super.animateItem(matrix, partialTicks);
        item.animate(matrix, smooth);
    }
}
