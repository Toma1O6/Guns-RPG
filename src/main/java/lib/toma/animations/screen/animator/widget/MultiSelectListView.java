package lib.toma.animations.screen.animator.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MultiSelectListView<T> extends Widget {

    private final FontRenderer font;
    private final List<T> entries;
    private final List<T> selection;
    private Function<T, String> formatter;
    private ISelectHandler<T> selectHandler;

    private final int displayLimit;
    private int scroll;

    public MultiSelectListView(FontRenderer font, int x, int y, int width, int height, List<T> entries) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.font = font;
        this.entries = entries;
        this.selection = new ArrayList<>();
        this.displayLimit = height / 15;
        this.formatter = Object::toString;
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, x - 1, y - 1, x + width + 1, y + height + 1, 0xFFCDCDCD);
        fill(stack, x, y, x + width, y + height, 0xFF << 24);
        for (int i = scroll; i < Math.min(scroll + displayLimit, entries.size()); i++) {
            int j = i - scroll;
            T t = entries.get(i);
            String text = formatter.apply(t);
            boolean selected = isSelected(t);
            if (selected) {
                fill(stack, x, y + j * 15, x + width, y + (j + 1) * 15, 0x89FFFFFF);
            } else {
                int y1 = y + j * 15;
                int y2 = y1 + 15;
                if (mouseX >= x && mouseX <= x + width && mouseY >= y1 && mouseY < y2) { // hovered
                    fill(stack, x, y1, x + width, y2, 0x67FFFFFF);
                }
            }
            font.drawShadow(stack, text, x + 2, y + 3 + i * 15, 0xFFFFFF);
        }
        drawScrollbar(stack);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        int index = (int) (mouseY - y) / 15;
        int offsetIndex = scroll + index;
        if (index >= 0 && index < entries.size()) {
            T t = entries.get(offsetIndex);
            boolean ctrlDown = Screen.hasControlDown();
            if (isSelected(t)) {
                if (!ctrlDown)
                    selection.clear();
                else
                    selection.remove(t);
            } else {
                if (!ctrlDown)
                    selection.clear();
                selection.add(t);
            }
            selectionChanged(t);
        } else {
            selection.clear();
            selectionChanged(null);
        }
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
        if (j >= 0 && j < entries.size() - displayLimit) {
            scroll = j;
        }
    }

    public List<T> getSelection() {
        return selection;
    }

    public void setSelectionResponder(ISelectHandler<T> handler) {
        this.selectHandler = handler;
    }

    public void setFormatter(Function<T, String> formatter) {
        this.formatter = Objects.requireNonNull(formatter);
    }

    private boolean isSelected(T t) {
        return selection.contains(t);
    }

    private void drawScrollbar(MatrixStack stack) {
        int left = x + width - 2;
        int right = left + 2;
        int top = y;
        int bottom = top + height;
        fill(stack, left, top, right, bottom, 0xFF << 24);

        double size = 1.0 / entries.size() * height;
        int scrollbarY = (int) (scroll * size);
        int scrollbarHeight = (int) (Math.ceil(displayLimit * size));
        fill(stack, left, top + scrollbarY, right, top + scrollbarY + scrollbarHeight, 0xFFFFFFFF);
    }

    private void selectionChanged(T element) {
        if (selectHandler != null)
            selectHandler.onSelect(element, selection);
    }

    @FunctionalInterface
    public interface ISelectHandler<T> {
        void onSelect(T selectedElement, List<T> selection);
    }
}
