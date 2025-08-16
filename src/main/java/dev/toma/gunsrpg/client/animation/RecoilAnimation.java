package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.ClientShootingManager;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.IAnimation;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Vector3f;

public class RecoilAnimation implements IAnimation {

    private float z;
    private float pitch;
    private float yaw;

    @Override
    public void animate(AnimationStage stage, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
        if (stage != AnimationStage.ITEM_AND_HANDS) return;
        matrixStack.translate(0.0, 0.0, z);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(pitch));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(yaw));
    }

    @Override
    public void renderTick(float deltaRenderTime) {
        this.z = ClientShootingManager.getActiveWeaponKickOffset(deltaRenderTime);
        this.pitch = ClientShootingManager.getActiveWeaponPitchOffset(deltaRenderTime);
        this.yaw = ClientShootingManager.getActiveWeaponYawOffset(deltaRenderTime);
    }

    @Override
    public void gameTick() {
    }

    @Override
    public boolean hasFinished() {
        return false;
    }
}
