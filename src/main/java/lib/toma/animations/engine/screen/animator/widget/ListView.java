package lib.toma.animations.engine.screen.animator.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListView<T> extends Widget {

    private final FontRenderer font;
    private final Iterable<T> entries;
    private final List<T> displayList = new ArrayList<>();
    private Predicate<T> filter;
    private ISelected<T> onSelect;
    private Function<T, String> formatter;

    private int scrollOffset;
    private final int elementDisplayLimit;
    private int selectedIndex;

    public ListView(int x, int y, int width, int height, Iterable<T> entries) {
        this(x, y, width, height, entries, t -> true);
    }

    public ListView(int x, int y, int width, int height, Iterable<T> entries, Predicate<T> filter) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.font = Minecraft.getInstance().font;
        this.entries = entries;
        this.formatter = Object::toString;
        this.elementDisplayLimit = height / 15;
        setFilter(filter);
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, x, y, x + width, y + height, 0x98 << 24);
        for (int i = scrollOffset; i < scrollOffset + elementDisplayLimit; i++) {
            if (i >= displayList.size()) break;
            int renderIndex = i - scrollOffset;
            T t = displayList.get(i);
            String name = formatter.apply(t);
            int y1 = y + renderIndex * 15;
            if (i == selectedIndex) {
                fill(stack, x, y1, x + width - 4, y1 + 15, 0x98FFFFFF);
            } else if (isHovered(renderIndex, mouseX, mouseY)) {
                fill(stack, x, y1, x + width - 4, y1 + 15, 0x67FFFFFF);
            }
            font.draw(stack, name, x + 2, y1 + 3, 0xFFFFFF);
        }
        drawScrollbar(stack);
    }

    @Override
    public void onClick(double xMouse, double yMouse) {
        int index = (int) (yMouse - y) / 15;
        int offsetIndex = scrollOffset + index;
        if (index >= 0 && index < displayList.size()) {
            trySelect(displayList.get(offsetIndex), offsetIndex);
        } else {
            trySelect(null, -1);
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
        int j = scrollOffset + i;
        if (j >= 0 && j <= displayList.size() - elementDisplayLimit) {
            scrollOffset = j;
        }
    }

    public void setFilter(Predicate<T> entryFilter) {
        this.filter = Objects.requireNonNull(entryFilter);
        this.scrollOffset = 0;
        updateDisplayList();
    }

    public void setFormatter(Function<T, String> formatter) {
        this.formatter = Objects.requireNonNull(formatter);
    }

    public void setResponder(ISelected<T> responder) {
        this.onSelect = responder;
    }

    private void drawScrollbar(MatrixStack stack) {
        int left = x + width - 2;
        int right = left + 2;
        int top = y;
        int bottom = top + height;
        double size = 1.0 / displayList.size() * height;
        int scrollbarY = (int) (scrollOffset * size);
        int scrollbarHeight = (int) (Math.ceil(Math.min(elementDisplayLimit, displayList.size()) * size));

        fill(stack, left, top, right, bottom, 0xFF << 24);
        fill(stack, left, top + scrollbarY, right, top + scrollbarY + scrollbarHeight, 0xFFFFFFFF);
    }

    private void trySelect(T t, int index) {
        if (onSelect != null)
            onSelect.onSelect(t);
        this.selectedIndex = index;
    }

    private void updateDisplayList() {
        displayList.clear();
        for (T t : entries) {
            if (filter.test(t))
                displayList.add(t);
        }
        selectedIndex = -1;
    }

    private boolean isHovered(int renderIndex, int mouseX, int mouseY) {
        int x1 = x;
        int x2 = x + width - 4;
        int y1 = y + renderIndex * 15;
        int y2 = y1 + 15;
        return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY < y2;
    }

    public interface ISelected<T> {
        void onSelect(@Nullable T value);
    }
}
