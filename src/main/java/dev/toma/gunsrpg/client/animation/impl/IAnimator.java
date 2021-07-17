package dev.toma.gunsrpg.client.animation.impl;

import com.mojang.blaze3d.matrix.MatrixStack;

@FunctionalInterface
public interface IAnimator {
    void animate(MatrixStack stack, float interpolatedValue);
}
