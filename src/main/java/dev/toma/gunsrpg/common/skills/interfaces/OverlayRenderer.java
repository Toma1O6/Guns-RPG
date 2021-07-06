package dev.toma.gunsrpg.common.skills.interfaces;

import dev.toma.gunsrpg.common.skills.core.ISkill;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface OverlayRenderer {

    @OnlyIn(Dist.CLIENT)
    void drawOnTop(int x, int y, int width, int heigth);

    @OnlyIn(Dist.CLIENT)
    void renderInHUD(ISkill skill, int renderIndex, int left, int top);

    default boolean shouldRenderOnHUD() {
        return true;
    }
}
