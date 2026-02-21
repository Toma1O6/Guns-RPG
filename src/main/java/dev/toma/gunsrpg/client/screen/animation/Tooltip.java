package dev.toma.gunsrpg.client.screen.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Tooltip {

    private final TooltipRenderer renderer;
    private final List<IReorderingProcessor> tooltip;

    private Tooltip(TooltipRenderer renderer, List<IReorderingProcessor> tooltip) {
        this.renderer = renderer;
        this.tooltip = tooltip;
    }

    public void render(MatrixStack matrix, int mouseX, int mouseY) {
        this.renderer.renderTooltip(matrix, this.tooltip, mouseX, mouseY);
    }

    public static Tooltip multiline(TooltipRenderer renderer, List<ITextComponent> tooltip) {
        List<IReorderingProcessor> list = tooltip.stream()
                .map(ITextComponent::getVisualOrderText)
                .collect(Collectors.toList());
        return new Tooltip(renderer, list);
    }

    public static Tooltip create(TooltipRenderer renderer, ITextComponent tooltip) {
        return multiline(renderer, Collections.singletonList(tooltip));
    }
}
