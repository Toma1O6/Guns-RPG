package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.sided.ClientSideManager;
import lib.toma.animations.Intepolation;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.IAnimation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class SprintAnimation implements IAnimation {

    private final PlayerEntity player;
    private final int length = 10;
    private int sprintTime;
    private float progress, progressOld, progressInterpolated;

    public SprintAnimation(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void animate(AnimationStage stage, MatrixStack matrixStack) {
        if (stage == AnimationStage.ITEM_AND_HANDS) {
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(-20.0F * progressInterpolated));
        } else if (stage == AnimationStage.LEFT_HAND) {
            if (!ClientSideManager.instance().isRenderingDualWield()) {
                matrixStack.translate(0.0, -0.5F * progressInterpolated, 0.8F * progressInterpolated);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(40.0F * progressInterpolated));
            }
        }
    }

    @Override
    public void gameTick() {
        int i = player.isSprinting() ? 1 : -1;
        sprintTime = MathHelper.clamp(sprintTime + i, 0, length);
        progressOld = progress;
        progress = getProgress();
    }

    @Override
    public void renderTick(float deltaRenderTime) {
        progressInterpolated = Intepolation.linear(deltaRenderTime, progress, progressOld);
    }

    @Override
    public boolean hasFinished() {
        return !player.isSprinting() && progressInterpolated <= 0.0F;
    }

    private float getProgress() {
        return sprintTime / (float) length;
    }
}