package dev.toma.gunsrpg.client.animation.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.AnimationFactory;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.sided.ClientSideManager;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.common.util.LazyOptional;

public class AimingAnimation extends AnimationFactory {

    private final Vector3f animation;
    private IAnimator leftArm = (stack, f) -> {
    };
    private IAnimator rightArm = (stack, f) -> {
    };

    public AimingAnimation(float x, float y, float z) {
        this(new Vector3f(x, y, z));
    }

    public AimingAnimation(Vector3f animation) {
        this.animation = animation;
    }

    public AimingAnimation animateRight(IAnimator animator) {
        this.rightArm = animator;
        return this;
    }

    public AimingAnimation animateLeft(IAnimator animator) {
        this.leftArm = animator;
        return this;
    }

    @Override
    public float getCurrentProgress() {
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        if (!optional.isPresent())
            return 0.0F;
        return optional.orElse(null).getAimInfo().getProgress();
    }

    @Override
    public void animateItemHands(MatrixStack stack, float partialTicks) {

    }

    @Override
    public void animateItem(MatrixStack stack, float partialTicks) {
        stack.translate(ClientSideManager.instance().processor().isRenderingDualWield() ? -animation.x() * smooth : animation.x() * smooth, animation.y() * smooth, animation.z() * smooth);
    }

    @Override
    public void animateHands(MatrixStack stack, float partialTicks) {
    }

    @Override
    public void animateLeftArm(MatrixStack stack, float partialTicks) {
        leftArm.animate(stack, smooth);
    }

    @Override
    public void animateRightArm(MatrixStack stack, float partialTicks) {
        rightArm.animate(stack, smooth);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
