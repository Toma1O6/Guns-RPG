package dev.toma.gunsrpg.client.animation.impl;

import com.mojang.blaze3d.matrix.MatrixStack;

import java.util.function.Consumer;

public class ImprovedAimAnimation extends AimingAnimation {

    private Consumer<ImprovedAimAnimation> item = it -> {};

    public ImprovedAimAnimation(float x, float y, float z) {
        super(x, y, z);
    }

    public ImprovedAimAnimation animateItem(Consumer<ImprovedAimAnimation> consumer) {
        this.item = consumer;
        return this;
    }

    @Override
    public void animateItem(MatrixStack matrix, float partialTicks) {
        super.animateItem(matrix, partialTicks);
        item.accept(this);
    }
}
