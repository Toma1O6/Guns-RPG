package dev.toma.gunsrpg.common.skills.interfaces;

import dev.toma.gunsrpg.common.skills.core.ISkill;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface OverlayRenderer {

    @SideOnly(Side.CLIENT)
    void drawOnTop(int x, int y, int width, int heigth);

    @SideOnly(Side.CLIENT)
    void renderInHUD(ISkill skill, int renderIndex, int left, int top);
}
