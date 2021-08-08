package lib.toma.animations.screen.animator.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SelectionWidget<T> extends Widget {

    private final FontRenderer font;
    private final List<T> displayList = new ArrayList<>();
    private final List<T> selection = new ArrayList<>();
    private ISelect<T> selectCallback;
    private Function<T, String> formatter;

    private int scroll;
    private int displayLimit;
    private boolean selected;

    public SelectionWidget(int x, int y, int width, int height, Iterable<T> entries, FontRenderer font, int selectionIndex) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.font = font;
        entries.forEach(displayList::add);
        setDisplayLimit(5);
        setFormatter(Object::toString);
        T t = displayList.get(selectionIndex);
        selection.add(displayList.get(selectionIndex));
        selectionChanged(t);
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, x - 1, y - 1, x + width + 1, y + height + 1, 0xFFE5E5E5); // frame
        fill(stack, x, y, x + width, y + height, 0xFF << 24);
        if (selected) {
            fill(stack, x - 1, y + height, x + width + 1, y + height + displayLimit * 15 + 1, 0xFFE5E5E5);
            for (int i = scroll; i < scroll + displayLimit; i++) {

            }
        }
        if (!selection.isEmpty()) {
            T t = selection.get(0);
            font.drawShadow(stack, formatter.apply(t), x + 2, y, 0xEAEA00);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        int index = scroll + (int) (mouseY - y) / 15;
        if (index >= 0 && index < displayList.size()) {
            trySelect(index);
        } else {
            trySelect(-1);
        }
    }

    @Override
    public boolean isMouseOver(double mx, double my) {
        boolean hor = mx >= x && mx <= x + width;
        boolean ver = my >= y && (selected ? my <= y + height : my <= y + height + displayLimit * 15);
        return hor && ver;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (amount != 0.0) {
            if (isMouseOver(mouseX, mouseY)) {
                onScroll((int) amount);
                return true;
            }
        }
        return false;
    }

    public void onScroll(int amount) {
        int i = -amount;
        int j = scroll + i;
        if (j >= 0 && j < displayList.size() - displayLimit) {
            scroll = j;
        }
    }

    public void onSelect(ISelect<T> callback) {
        this.selectCallback = callback;
    }

    public void setFormatter(Function<T, String> formatter) {
        this.formatter = formatter;
    }

    public void setDisplayLimit(int displayLimit) {
        this.displayLimit = Math.min(displayList.size(), displayLimit);
    }

    @SuppressWarnings("unchecked")
    public T[] getSelectedValues() {
        return (T[]) selection.toArray(new Object[0]);
    }

    private void trySelect(int index) {
        if (index < 0) {
            selection.clear();
            selectionChanged(null);
        } else {
            T t = displayList.get(index);
            int elIndex = selection.indexOf(t);
            if (elIndex == -1) {
                selection.add(t);
            } else {
                selection.remove(t);
            }
            selectionChanged(t);
        }
    }

    private void selectionChanged(T t) {
        if (selectCallback != null)
            selectCallback.onSelected(t, getSelectedValues());
    }

    @FunctionalInterface
    public interface ISelect<T> {
        void onSelected(T selected, T[] selection);
    }
}
