package lib.toma.animations.screen.animator.dialog;

import net.minecraft.client.gui.widget.TextFieldWidget;

import java.util.function.Consumer;

public class SuggestionResponder implements Consumer<String> {

    private final String suggestion;
    private final TextFieldWidget widget;
    private final Consumer<String> responder;

    public SuggestionResponder(String suggestion, TextFieldWidget widget, Consumer<String> responder) {
        this.suggestion = suggestion;
        this.widget = widget;
        this.responder = responder;
    }

    @Override
    public void accept(String s) {
        if (s.isEmpty())
            widget.setSuggestion(suggestion);
        else
            widget.setSuggestion(null);
        responder.accept(s);
    }
}