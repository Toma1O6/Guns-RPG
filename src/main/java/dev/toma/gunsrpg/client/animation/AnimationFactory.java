package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public abstract class AnimationFactory implements IAnimation {

    protected final PlayerEntity player;
    protected float current, prev;
    public float smooth;

    public AnimationFactory() {
        player = Minecraft.getInstance().player;
    }

    public abstract float getCurrentProgress();

    @Override
    public void setProgress(float progress) {
        this.current = progress;
    }

    public abstract void update();

    @Override
    public final void clientTick() {
        this.prev = current;
        this.update();
    }

    @Override
    public void animateRightArm(MatrixStack matrix, float partialTicks) {

    }

    @Override
    public void animateLeftArm(MatrixStack matrix, float partialTicks) {

    }

    @Override
    public void animateItem(MatrixStack matrix, float partialTicks) {

    }

    @Override
    public void animateHands(MatrixStack matrix, float partialTicks) {

    }

    @Override
    public void animateItemHands(MatrixStack matrix, float partialTicks) {

    }

    @Override
    public void frameTick(float partialTicks) {
        this.calculateSmoothValue(partialTicks);
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    @Override
    public boolean cancelsItemRender() {
        return false;
    }

    private void calculateSmoothValue(float partialTicks) {
        this.current = this.getCurrentProgress();
        this.smooth = this.prev + (this.current - this.prev) * partialTicks;
    }
}
