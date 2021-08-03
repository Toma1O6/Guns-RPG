package lib.toma.animations.screen.animator.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;

public class LabelWidget extends Widget {

    public int foregroundColor = 0xFFFFFF;
    public float horizontalOffset;
    public float verticalOffset;
    public boolean castShadow = true;

    private final FontRenderer font;

    public LabelWidget(int x, int y, int width, int height, ITextComponent message, FontRenderer font) {
        super(x, y, width, height, message);
        this.font = font;
        this.verticalOffset = (height - font.lineHeight) / 2.0F;
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (castShadow) {
            font.drawShadow(stack, getMessage(), x + horizontalOffset, y + verticalOffset, foregroundColor);
        } else {
            font.draw(stack, getMessage(), x + horizontalOffset, y + verticalOffset, foregroundColor);
        }
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return false;
    }
}
