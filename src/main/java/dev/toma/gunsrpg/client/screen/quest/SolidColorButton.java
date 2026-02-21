package dev.toma.gunsrpg.client.screen.quest;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.screen.animation.FadeAnimation;
import dev.toma.gunsrpg.client.screen.animation.Tooltip;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.text.ITextComponent;

public class SolidColorButton extends AbstractButton {

    private final ClickHandler clickHandler;
    private FadeAnimation fadeOutAnim = FadeAnimation.NO_FADE;
    private int color = 0x44 << 24;
    private Tooltip tooltip;

    public SolidColorButton(int x, int y, int width, int height, ITextComponent text, ClickHandler clickHandler) {
        super(x, y, width, height, text);
        this.clickHandler = clickHandler;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setFadeOutAnim(FadeAnimation fadeOutAnim) {
        this.fadeOutAnim = fadeOutAnim;
    }

    public void setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float delta) {
        fill(matrix, this.x, this.y, this.x + this.width, this.y + this.height, this.color);
        if (this.active && this.isHovered)
            this.fadeOutAnim.reset();
        float progress = this.fadeOutAnim.getInvertedProgress();
        int hoverColor = this.fadeOutAnim.getAdjustedAlphaColor(0x88FFFFFF, progress);
        if (RenderUtils.isVisible(hoverColor)) {
            fill(matrix, this.x, this.y, this.x + this.width, this.y + this.height, hoverColor);
        }

        FontRenderer font = Minecraft.getInstance().font;
        ITextComponent label = this.getMessage();
        font.drawShadow(matrix, label, this.x + (this.width - font.width(label)) / 2.0F, 1 + this.y + (this.height - font.lineHeight) / 2.0F, 0xFFFFFF);

        if (this.tooltip != null && this.isHovered) {
            this.tooltip.render(matrix, mouseX, mouseY);
        }
    }

    @Override
    public void onPress() {
        this.clickHandler.onClick();
    }

    @FunctionalInterface
    public interface ClickHandler {
        void onClick();
    }
}
