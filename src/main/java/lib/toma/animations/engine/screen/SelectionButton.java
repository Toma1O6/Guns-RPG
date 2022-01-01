package lib.toma.animations.engine.screen;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class SelectionButton<T> extends Widget {

    private final T[] values;
    private int index;
    private IClickCallback<T> clickCallback;
    private IFormatter<T> formatter = Object::toString;

    public SelectionButton(int x, int y, int width, int height, T[] values, int value) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.values = values;
        this.index = value;
        this.setMessage(this.getText());
    }

    public SelectionButton(int x, int y, int width, int height, T[] values) {
        this(x, y, width, height, values, 0);
    }

    public SelectionButton<T> onClick(IClickCallback<T> callback) {
        this.clickCallback = callback;
        return this;
    }

    public SelectionButton<T> formatter(IFormatter<T> formatter) {
        this.formatter = formatter;
        return this;
    }

    public T getValue() {
        return values[index];
    }

    @Override
    public void onClick(double x, double y) {
        index = ++index % values.length;
        if (clickCallback != null) {
            clickCallback.onClick(this.getValue());
        }
        setMessage(this.getText());
    }

    private ITextComponent getText() {
        return new StringTextComponent(formatter.format(values[index]));
    }

    public interface IClickCallback<T> {
        void onClick(T t);
    }

    public interface IFormatter<T> {
        String format(T t);
    }
}
