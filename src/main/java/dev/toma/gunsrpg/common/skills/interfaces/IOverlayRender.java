package dev.toma.gunsrpg.common.skills.interfaces;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.skills.core.ISkill;
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
