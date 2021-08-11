package lib.toma.animations.screen.animator.dialog;

import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.screen.animator.AnimatorScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Locale;
import java.util.regex.Pattern;

public class EditEventDialog extends DialogScreen {

    private final IAnimationEvent clickedEvent;
    private final IPositionChanger positionChanger;

    private TextFieldWidget position;
    private final Pattern posPattern = Pattern.compile("(1(\\.0)?)|(0(\\.[0-9]+)?)");
    private boolean valid;

    public EditEventDialog(AnimatorScreen screen, IAnimationEvent clickedEvent, IPositionChanger positionChanger) {
        super(new TranslationTextComponent("screen.animator.dialog.edit_event"), screen);
        this.clickedEvent = clickedEvent;
        this.positionChanger = positionChanger;

        setDimensions(180, 65);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    protected void init() {
        super.init();

        int btnWidthFull = dWidth() - 10;
        int btnWidthPartial = (dWidth() - 20) / 3;
        position = addButton(new TextFieldWidget(font, left() + 5, top() + 15, btnWidthFull, 20, StringTextComponent.EMPTY));
        position.setResponder(new SuggestionResponder("Position", position, this::position_changed));
        position.setValue(String.format(Locale.ROOT, "%.3f", clickedEvent.invokeAt()));
        cancel = addButton(new Button(left() + 5, top() + 40, btnWidthPartial, 20, CANCEL, this::cancel_clicked));
        addButton(new Button(left() + 10 + btnWidthPartial, top() + 40, btnWidthPartial, 20, new StringTextComponent("Delete"), this::delete_clicked));
        confirm = addButton(new Button(left() + dWidth() - 5 - btnWidthPartial, top() + 40, btnWidthPartial, 20, CONFIRM, this::confirm_clicked));
        updateConfirmButton();
    }

    private void position_changed(String value) {
        if (posPattern.matcher(value).matches()) {
            position.setTextColor(0xE0E0E0);
            valid = true;
        } else {
            position.setTextColor(0xE0 << 16);
            valid = false;
        }
        updateConfirmButton();
    }

    private void confirm_clicked(Button button) {
        positionChanger.change(clickedEvent, clickedEvent.copyAt(Float.parseFloat(position.getValue())));
        showParent();
    }

    private void delete_clicked(Button button) {
        positionChanger.change(clickedEvent, null);
        showParent();
    }

    private void updateConfirmButton() {
        if (confirm != null)
            confirm.active = valid;
    }

    public interface IPositionChanger {
        void change(IAnimationEvent oldEvent, IAnimationEvent newEvent);
    }
}
