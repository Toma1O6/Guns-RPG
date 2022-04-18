package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class HeaderWidget extends Widget {

    private final FontRenderer renderer;

    public HeaderWidget(int x, int y, int width, int height, ITextComponent text, FontRenderer renderer) {
        super(x, y, width, height, text);
        this.renderer = renderer;
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
        ITextComponent text = this.getMessage();
        int titleWidth = renderer.width(text);
        RenderUtils.drawSolid(poseStack.last().pose(), x, y, x + width, y + height, 0x44 << 24);
        renderer.drawShadow(poseStack, TextFormatting.UNDERLINE + text.getString(), x + (width - titleWidth) / 2.0F, y + (height - renderer.lineHeight) / 2.0F, 0xFFFFFF);
    }

    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        return false;
    }
}
