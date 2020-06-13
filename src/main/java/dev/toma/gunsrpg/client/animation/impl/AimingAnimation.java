package dev.toma.gunsrpg.client.animation.impl;

import dev.toma.gunsrpg.client.animation.AnimationFactory;
import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3f;
import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
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
        return PlayerDataFactory.get(player).getAimInfo().getProgress();
    }

    @Override
    public void animateItemHands(float partialTicks) {

    }

    @Override
    public void animateItem(float partialTicks) {
        GlStateManager.translate(AnimationManager.renderingDualWield ? -animation.x * smooth : animation.x * smooth, animation.y * smooth, animation.z * smooth);
    }

    @Override
    public void animateHands(float partialTicks) {
    }

    @Override
    public void animateLeftArm(float partialTicks) {
        leftArm.accept(this);
    }

    @Override
    public void animateRightArm(float partialTicks) {
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
