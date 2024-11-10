package dev.toma.gunsrpg.client.render.infobar;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;

public class TextElement implements IDataElement {

    private final ITextComponent component;
    private int width;

    public TextElement(ITextComponent component) {
        this.component = component;
    }

    @Override
    public void recalculate(FontRenderer font, int width, int height) {
    }

    @Override
    public void draw(MatrixStack matrix, FontRenderer font, int x, int y, int width, int height) {
        this.width = font.width(component);
        font.draw(matrix, component, x, y, 0xFFFFFF);
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
