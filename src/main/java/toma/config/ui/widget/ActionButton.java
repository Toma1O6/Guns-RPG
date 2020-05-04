package toma.config.ui.widget;

import net.minecraft.client.gui.GuiButton;

import java.util.function.Consumer;

public class ActionButton extends GuiButton {

    private final Consumer<ActionButton> consumer;

    public ActionButton(int id, int x, int y, int w, int h, String text, Consumer<ActionButton> action) {
        super(id, x, y, w, h, text);
        this.consumer = action;
    }

    public void executeAction(ActionButton button) {
        this.consumer.accept(button);
    }
}
