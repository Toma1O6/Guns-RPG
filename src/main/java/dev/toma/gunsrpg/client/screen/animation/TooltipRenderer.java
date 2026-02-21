package dev.toma.gunsrpg.client.screen.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.IReorderingProcessor;

import java.util.List;

@FunctionalInterface
public interface TooltipRenderer {

    void renderTooltip(MatrixStack matrix, List<IReorderingProcessor> tooltip, int mouseX, int mouseY);
}
