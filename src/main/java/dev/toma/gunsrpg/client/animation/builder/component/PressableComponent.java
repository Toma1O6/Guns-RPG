package dev.toma.gunsrpg.client.animation.builder.component;

import java.util.function.Consumer;

public class PressableComponent extends UIComponent {

    public PressableComponent(int x, int y, int w, int h, String text, Consumer<UIComponent> pressable) {
        super(x, y, w, h, text, pressable);
    }

    @Override
    public void onClick() {
        this.onPress.accept(this);
    }
}
