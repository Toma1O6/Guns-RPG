package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IAnimation {

    void animateItemHands(MatrixStack matrix, float partialTicks);

    void animateHands(MatrixStack matrix, float partialTicks);

    void animateRightArm(MatrixStack matrix, float partialTicks);

    void animateLeftArm(MatrixStack matrix, float partialTicks);

    void animateItem(MatrixStack matrix, float partialTicks);

    void clientTick();

    void frameTick(float partialTicks);

    boolean isFinished();

    void setProgress(float progress);

    boolean cancelsItemRender();
}
