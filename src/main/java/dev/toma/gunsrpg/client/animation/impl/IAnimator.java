package dev.toma.gunsrpg.client.animation.impl;

import com.mojang.blaze3d.matrix.MatrixStack;

@Deprecated
@FunctionalInterface
public interface IAnimator {
    void animate(MatrixStack stack, float interpolatedValue);
}
