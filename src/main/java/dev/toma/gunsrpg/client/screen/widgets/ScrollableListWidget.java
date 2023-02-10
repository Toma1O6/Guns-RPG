package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class ScrollableListWidget<T, W extends Widget> extends ContainerWidget {

    private final WidgetFactory<T, W> factory;
    private final List<T> data;
    private int scrollIndex;
    private int pageSize;

    private boolean showScrollbar;
    private int entryHeight = 20;

    public ScrollableListWidget(int x, int y, int width, int height, List<T> data, WidgetFactory<T, W> factory) {
        super(x, y, width, height);
        this.data = data;
        this.factory = factory;
        this.showScrollbar = true;
    }

    public void setScrollbarVisible(boolean visible) {
        this.showScrollbar = visible;
        this.init();
    }

    public void setEntryHeight(int height) {
        this.entryHeight = Math.max(1, height);
        this.init();
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderButton(stack, mouseX, mouseY, partialTicks);
        // Scrollbar render
        if (this.showScrollbar && this.data.size() > this.pageSize) {
            int dataSize = this.data.size();
            double step = this.height / (double) dataSize;
            int min = MathHelper.floor(this.scrollIndex * step);
            int max = MathHelper.floor((this.pageSize + this.scrollIndex) * step);
            int x1 = this.x + this.width - 5;
            int x2 = x1 + 5;
            int y1 = this.y + min;
            int y2 = this.y + max;
            fill(stack, x1, this.y, x2, this.y + this.height, 0xFF << 24);
            fill(stack, x1, y1, x2, y2, 0xFF888888);
            fill(stack, x1, y1, x2 - 1, y2 - 1, 0xFFEEEEEE);
            fill(stack, x1 + 1, y1 + 1, x2 - 1, y2 - 1, 0xFFCCCCCC);
        }
        ITextComponent text = this.getMessage();
        FontRenderer font = Minecraft.getInstance().font;
        if (this.data.isEmpty() && text != null) {
            int textFieldWidth = this.width - 10;
            List<IReorderingProcessor> lines = font.split(text, textFieldWidth);
            int lineCount = lines.size();
            int textHeight = lineCount * 10;
            float textY = this.y + (this.height - textHeight) / 2.0F;
            for (int i = 0; i < lineCount; i++) {
                IReorderingProcessor processor = lines.get(i);
                float textX = this.x + 5 + (textFieldWidth - font.width(processor)) / 2.0F;
                font.draw(stack, processor, textX, textY + i * 10, 0xFFFFFF);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.isMouseOver(mouseX, mouseY)) {
            this.scrollIndex -= amount;
            this.clampScrollIndex();
            this.init();
            return false;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    private void init() {
        this.clear();
        this.pageSize = height / entryHeight;
        this.clampScrollIndex();
        boolean scrollbarVisible = showScrollbar && this.data.size() > this.pageSize;
        for (int i = this.scrollIndex; i < this.scrollIndex + this.pageSize; i++) {
            if (i >= this.data.size())
                break;
            T element = this.data.get(i);
            int renderIndex = i - this.scrollIndex;
            W widget = this.factory.createWidget(element, this.x, this.y + renderIndex * this.entryHeight, scrollbarVisible ? this.width - 5 : this.width, this.entryHeight);
            this.addWidget(widget);
        }
    }

    private void clampScrollIndex() {
        this.scrollIndex = Math.max(MathHelper.clamp(this.scrollIndex, 0, this.data.size() - pageSize), 0);
    }

    @FunctionalInterface
    public interface WidgetFactory<T, W extends Widget> {
        W createWidget(T element, int x, int y, int width, int height);
    }
}
