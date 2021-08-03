package lib.toma.animations.screen.animator;

import lib.toma.animations.screen.animator.widget.LabelWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class SettingsDialog extends DialogScreen {

    private final Pattern cyclePattern = Pattern.compile("[1-9][0-9]{0,2}");
    private TextFieldWidget cycleField;
    private CheckboxButton onRepeat;

    private boolean errored;

    public SettingsDialog(AnimatorScreen screen) {
        super(new TranslationTextComponent("screen.animator.dialog.settings"), screen);
        setDimensions(135, 105);
    }

    @Override
    protected void init() {
        super.init();
        AnimationProject project = Animator.get().getLatestProject();
        AnimationProject.AnimationController controller = project.getAnimationControl();
        addButton(new LabelWidget(left() + 5, top() + 15, dWidth() - 10, 15, new StringTextComponent("Animation cycle [ticks]"), font));
        cycleField = addButton(new TextFieldWidget(font, left() + 5, top() + 30, dWidth() - 10, 20, StringTextComponent.EMPTY));
        cycleField.setResponder(new SuggestionResponder("Ticks", cycleField, this::cycleField_changed));
        cycleField.setValue(String.valueOf(controller.getAnimationTime()));
        onRepeat = addButton(new CheckboxButton(left() + 5, top() + 55, dWidth() - 10, 20, new StringTextComponent("On repeat"), controller.isOnRepeat()));

        int btnWidthP = dWidth() - 10;
        int btnWidth = (btnWidthP - 5) / 2;
        cancel = addButton(new Button(left() + 5, top() + 80, btnWidth, 20, new StringTextComponent("Cancel"), this::cancel_clicked));
        confirm = addButton(new Button(left() + 10 + btnWidth, top() + 80, btnWidth, 20, new StringTextComponent("Save"), this::save_clicked));
        updateConfirmState();
    }

    private void save_clicked(Button button) {
        AnimationProject.AnimationController controller = Animator.get().getLatestProject().getAnimationControl();
        int len = parse(cycleField.getValue(), controller.getAnimationTime());
        boolean repeat = onRepeat.selected();
        controller.setAnimationTime(len);
        controller.setOnRepeat(repeat);
        showParent();
    }

    private void cancel_clicked(Button button) {
        showParent();
    }

    private void cycleField_changed(String value) {
        if (cyclePattern.matcher(value).matches()) {
            cycleField.setTextColor(0xE0E0E0);
            errored = false;
        } else {
            cycleField.setTextColor(0xE0 << 16);
            errored = true;
        }
        updateConfirmState();
    }

    private void updateConfirmState() {
        if (confirm != null)
            confirm.active = !errored;
    }
}
