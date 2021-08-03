package lib.toma.animations.screen.animator;

import lib.toma.animations.screen.animator.widget.LabelWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.regex.Pattern;

public class SaveAsDialog extends DialogScreen {

    private final Pattern namePattern = Pattern.compile("[a-z][a-z0-9_]*");
    private TextFieldWidget filename;
    private boolean errored;

    public SaveAsDialog(AnimatorScreen screen) {
        super(new TranslationTextComponent("screen.animator.dialog.save_as"), screen);
    }

    @Override
    protected void init() {
        super.init();
        int btnWidthP = dWidth() - 10;
        int btnWidth = (btnWidthP - 5) / 2;
        addButton(new LabelWidget(left() + 5, top() + 15, btnWidthP, 15, new StringTextComponent("Filename"), font));
        filename = addButton(new TextFieldWidget(font, left() + 5, top() + 30, btnWidthP, 20, StringTextComponent.EMPTY));
        filename.setResponder(new SuggestionResponder("Filename", filename, this::filename_changed));

        cancel = addButton(new Button(left() + 5, top() + 55, btnWidth, 20, new StringTextComponent("Cancel"), this::cancel_clicked));
        confirm = addButton(new Button(left() + 10 + btnWidth, top() + 55, btnWidth, 20, new StringTextComponent("Save"), this::save_clicked));
    }

    private void cancel_clicked(Button button) {
        showParent();
    }

    private void save_clicked(Button button) {
        Animator.get().getLatestProject().saveProjectAs(filename.getValue());
        showParent();
    }

    private void filename_changed(String value) {
        if (namePattern.matcher(value).matches()) {
            filename.setTextColor(0xE0E0E0);
            errored = false;
        } else {
            filename.setTextColor(0xE0 << 16);
            errored = true;
        }
        updateSaveButtonState();
    }

    private void updateSaveButtonState() {
        if (confirm != null)
            confirm.active = !errored;
    }
}
