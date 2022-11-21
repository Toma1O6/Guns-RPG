package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.util.ITickable;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class NavigatorWidget<T> extends ContainerWidget {

    private final T[] values;
    private final NavEntry<T>[] buttons;
    private IClickResponder<T> responder;
    private int selectedIndex;

    public NavigatorWidget(int x, int y, int width, int height, T[] values) {
        super(x, y, width, height);
        this.values = values;
        this.buttons = buildEntries(values, x, y, width, height);
        this.setTextFormatter(Object::toString);
    }

    public T getSelectedValue() {
        return values[selectedIndex];
    }

    public void setTextFormatter(ITextFormatter<T> formatter) {
        Objects.requireNonNull(formatter);
        for (NavEntry<T> entry : buttons) {
            entry.updateMessage(formatter);
        }
    }

    public void setClickResponder(IClickResponder<T> responder) {
        this.responder = Objects.requireNonNull(responder);
        for (NavEntry<T> entry : buttons) {
            entry.setResponder(this::handleValueClicked);
        }
    }

    public void setColorProvider(Function<T, ITextColorProvider<T>> provider) {
        for (NavEntry<T> entry : buttons) {
            entry.setColorProvider(provider.apply(entry.value));
        }
    }

    private void handleValueClicked(T value, int index) {
        this.selectedIndex = index;
        if (responder != null) {
            responder.onElementClicked(value);
        }
    }

    @SuppressWarnings("unchecked")
    private NavEntry<T>[] buildEntries(T[] values, int x, int y, int width, int height) {
        int total = values.length;
        int elementWidth = width / total;
        int widthCorrection = width;
        NavEntry<T>[] array = new NavEntry[values.length];
        for (int i = 0; i < values.length - 1; i++) {
            NavEntry<T> entry = new NavEntry<>(values[i], idx -> idx == selectedIndex, i, x + i * elementWidth, y, elementWidth, height);
            widthCorrection -= elementWidth;
            addWidget(entry);
            array[i] = entry;
        }
        int index = total - 1;
        T value = values[index];
        widthCorrection -= elementWidth;
        NavEntry<T> last = new NavEntry<>(value, idx -> idx == selectedIndex, index, x + index * elementWidth, y, elementWidth + widthCorrection, height);
        addWidget(last);
        array[index] = last;
        return array;
    }

    private static class NavEntry<V> extends Widget implements ITickable {

        final int index;
        final V value;
        final Predicate<Integer> isSameIndex;
        IEntryClickResponder<V> responder = (value1, index1) -> {};
        ITextColorProvider<V> colorProvider = (v, partialTicks) -> 0xFFFFFF;

        NavEntry(V value, Predicate<Integer> isSameIndex, int index, int x, int y, int width, int height) {
            super(x, y, width, height, StringTextComponent.EMPTY);
            this.value = value;
            this.isSameIndex = isSameIndex;
            this.index = index;
        }

        @Override
        public void tick() {
            this.colorProvider.tick(value);
        }

        void updateMessage(ITextFormatter<V> formatter) {
            this.setMessage(new TranslationTextComponent("nav.entry." + formatter.getFormatted(value)));
        }

        void setResponder(IEntryClickResponder<V> responder) {
            this.responder = responder;
        }

        void setColorProvider(ITextColorProvider<V> provider) {
            this.colorProvider = provider;
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            if (responder != null) {
                responder.onClicked(value, index);
            }
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            ITextComponent msg = this.getMessage();
            ITextComponent text = isHovered || isSameIndex.test(index) ? new StringTextComponent(TextFormatting.BOLD + msg.getString()) : msg;
            FontRenderer renderer = Minecraft.getInstance().font;
            RenderUtils.drawGradient(matrix.last().pose(), x, y, x + width, y + height, 0x44 << 24, 0x07 << 24);
            RenderUtils.drawCenteredShadowText(matrix, text, renderer, this, this.colorProvider.getColor(value, partialTicks));
        }
    }

    @FunctionalInterface
    private interface IEntryClickResponder<T> {

        void onClicked(T value, int index);
    }

    @FunctionalInterface
    public interface ITextColorProvider<T> {

        default void tick(T t) {
        }

        int getColor(T t, float partialTicks);
    }
}
