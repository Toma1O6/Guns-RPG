package dev.toma.gunsrpg.client.render.infobar;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Function;

public class DataRowElement<T> extends DataSourcedElement<T> {

    private final Function<T, ITextComponent> column1;
    private final Function<T, ITextComponent> column2Provider;
    private int width;

    public DataRowElement(T dataSource, Function<T, ITextComponent> column1, Function<T, ITextComponent> column2Provider) {
        super(dataSource);
        this.column1 = column1;
        this.column2Provider = column2Provider;
    }

    @Override
    public void draw(MatrixStack matrix, FontRenderer font, int x, int y, int width, int height) {
        calculateDimensions(font);
        T src = this.getDataSource();
        font.draw(matrix, column1.apply(src), x, y, 0xFFFFFF);
        ITextComponent col2 = column2Provider.apply(src);
        int col2Width = font.width(col2);
        font.draw(matrix, col2, x + width - col2Width - 3, y, 0xFFFFFF);
    }

    @Override
    public void recalculate(FontRenderer font, int width, int height) {
        this.calculateDimensions(font);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    private void calculateDimensions(FontRenderer font) {
        T source = this.getDataSource();
        ITextComponent column2 = column2Provider.apply(source);
        int leftWidth = font.width(column1.apply(source));
        int spacing = 15;
        int rightWidth = font.width(column2);
        this.width = leftWidth + spacing + rightWidth;
    }
}
