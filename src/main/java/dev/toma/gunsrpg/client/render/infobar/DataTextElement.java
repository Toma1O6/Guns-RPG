package dev.toma.gunsrpg.client.render.infobar;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Function;

public class DataTextElement<S> implements IDataElement {

    private final S data;
    private final Function<S, ITextComponent> textProvider;
    private final boolean rightAlignment;
    private int width;

    public DataTextElement(S data, Function<S, ITextComponent> textProvider, boolean rightAlignment) {
        this.data = data;
        this.textProvider = textProvider;
        this.rightAlignment = rightAlignment;
    }

    public DataTextElement(S data, Function<S, ITextComponent> textProvider) {
        this(data, textProvider, false);
    }

    @Override
    public void draw(MatrixStack matrix, FontRenderer font, int x, int y, int width, int height) {
        ITextComponent text = textProvider.apply(data);
        this.width = font.width(text);
        if (this.rightAlignment) {
            font.draw(matrix, text, x + width - this.width, y, 0xFFFFFF);
        } else {
            font.draw(matrix, text, x, y, 0xFFFFFF);
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
