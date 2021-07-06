package dev.toma.gunsrpg.client.animation.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.AnimationFactory;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.sided.ClientSideManager;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Consumer;

public class AimingAnimation extends AnimationFactory {

    private final Vector3f animation;
    private Consumer<AimingAnimation> leftArm = animation -> {};
    private Consumer<AimingAnimation> rightArm = animation -> {};

    public AimingAnimation(float x, float y, float z) {
        this(new Vector3f(x, y, z));
    }

    public AimingAnimation(Vector3f animation) {
        this.animation = animation;
    }

    public AimingAnimation animateRight(Consumer<AimingAnimation> consumer) {
        this.rightArm = consumer;
        return this;
    }

    public AimingAnimation animateLeft(Consumer<AimingAnimation> consumer) {
        this.leftArm = consumer;
        return this;
    }

    @Override
    public float getCurrentProgress() {
        LazyOptional<PlayerData> optional = PlayerDataFactory.get(player);
        if (!optional.isPresent())
            return 0.0F;
        return optional.orElse(null).getAimInfo().getProgress();
    }

    @Override
    public void animateItemHands(MatrixStack stack, float partialTicks) {

    }

    @Override
    public void animateItem(MatrixStack stack, float partialTicks) {
        stack.translate(ClientSideManager.processor().isRenderingDualWield() ? -animation.x() * smooth : animation.x() * smooth, animation.y() * smooth, animation.z() * smooth);
    }

    @Override
    public void animateHands(MatrixStack stack, float partialTicks) {
    }

    @Override
    public void animateLeftArm(MatrixStack stack, float partialTicks) {
        leftArm.accept(this);
    }

    @Override
    public void animateRightArm(MatrixStack stack, float partialTicks) {
        rightArm.accept(this);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
