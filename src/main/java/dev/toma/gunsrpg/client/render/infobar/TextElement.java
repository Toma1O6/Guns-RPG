package dev.toma.gunsrpg.client.render.infobar;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;

public class TextElement implements IDataElement {

    private final ITextComponent component;
    private final boolean rightAlignment;
    private int width;

    public TextElement(ITextComponent component) {
        this(component, false);
    }

    public TextElement(ITextComponent component, boolean rightAlignment) {
        this.component = component;
        this.rightAlignment = rightAlignment;
    }

    @Override
    public void draw(MatrixStack matrix, FontRenderer font, int x, int y, int width, int height) {
        this.width = font.width(component);
        if (this.rightAlignment) {
            font.draw(matrix, component, x + width - font.width(component), y, 0xFFFFFF);
        } else {
            font.draw(matrix, component, x, y, 0xFFFFFF);
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return 10;
    }
}
