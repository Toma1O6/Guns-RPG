package dev.toma.gunsrpg.api.common;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IOverlayRender {

    @OnlyIn(Dist.CLIENT)
    void drawOnTop(MatrixStack stack, int x, int y, int width, int heigth);

    @OnlyIn(Dist.CLIENT)
    void renderInHUD(MatrixStack stack, ISkill skill, int renderIndex, int left, int top);

    default boolean shouldRenderOnHUD() {
        return true;
    }
}
