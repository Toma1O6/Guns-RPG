package dev.toma.gunsrpg.client.animation.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.TickableAnimation;
import net.minecraft.util.math.vector.Vector3f;

public class RecoilAnimation extends TickableAnimation {

    protected RecoilAnimation(int time) {
        super(time);
    }

    public static RecoilAnimation newInstance(int length) {
        return new RecoilAnimation(length);
    }

    @Override
    public void animateItemHands(MatrixStack matrix, float partialTicks) {
        matrix.mulPose(Vector3f.XP.rotationDegrees(3.0F * smooth));
        matrix.translate(0.0F, 0.0F, 0.1F * smooth);
    }
}
