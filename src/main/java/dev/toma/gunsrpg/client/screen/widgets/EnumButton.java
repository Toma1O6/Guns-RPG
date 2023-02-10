package dev.toma.gunsrpg.client.screen.widgets;

import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.Collection;
import java.util.EnumSet;
import java.util.function.Function;

public class EnumButton<E extends Enum<E>> extends AbstractButton {

    private final EnumSet<E> values;
    private final ClickResponder<E> clickResponder;
    private Function<E, ITextComponent> toComponent = e -> new StringTextComponent(e.name());
    private E value;

    public EnumButton(int x, int y, int width, int height, E value, ClickResponder<E> clickResponder) {
        this(x, y, width, height, value, EnumSet.allOf(value.getDeclaringClass()), clickResponder);
    }

    public EnumButton(int x, int y, int width, int height, E value, Collection<E> validValues, ClickResponder<E> clickResponder) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.clickResponder = clickResponder;
        this.value = value;
        this.setMessage(this.toComponent.apply(value));
        this.values = EnumSet.noneOf(value.getDeclaringClass());
        this.values.addAll(validValues);
    }

    @Override
    public void onPress() {
        this.value = ModUtils.getEnumByIdSafely(value.ordinal() + 1, value.getDeclaringClass());
        this.clickResponder.onClicked(value);
    }

    public void setToComponentTransformer(Function<E, ITextComponent> toComponent) {
        this.toComponent = toComponent;
    }

    public static <E extends Enum<E>> ITextComponent upperCaseFormat(E value) {
        String name = value.name();
        String formatted = name.replaceAll("_+", " ").toUpperCase();
        return new StringTextComponent(formatted);
    }

    @FunctionalInterface
    public interface ClickResponder<E extends Enum<E>> {

        void onClicked(E value);
    }
}
