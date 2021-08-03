package lib.toma.animations.screen.animator;

import lib.toma.animations.screen.animator.widget.LabelWidget;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.regex.Pattern;

public class NewProjectDialog extends DialogScreen {

    private final Pattern namePattern = Pattern.compile("[a-z][a-z0-9_]*");
    private final Pattern cyclePattern = Pattern.compile("[1-9][0-9]{0,2}");
    private TextFieldWidget nameField;
    private TextFieldWidget cycleField;
    private CheckboxButton onRepeat;
    private byte errorFlags = 0;

    public NewProjectDialog(AnimatorScreen animator) {
        super(new TranslationTextComponent("screen.animator.dialog.new_project"), animator);
        setDimensions(155, 145);
    }

    @Override
    protected void init() {
        super.init();
        addButton(new LabelWidget(left() + 5, top() + 15, 50, 15, new StringTextComponent("Project name"), font));
        nameField = addButton(new TextFieldWidget(font, left() + 5, top() + 30, dWidth() - 10, 20, StringTextComponent.EMPTY));
        nameField.setResponder(this::nameField_changed);
        nameField.setValue("animation_" + LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)).replaceAll(":", ""));
        addButton(new LabelWidget(left() + 5, top() + 55, 50, 15, new StringTextComponent("Initial animation cycle [ticks]"), font));
        cycleField = addButton(new TextFieldWidget(font, left() + 5, top() + 70, dWidth() - 10, 20, StringTextComponent.EMPTY));
        cycleField.setResponder(this::cycleField_changed);
        cycleField.setValue("50");
        onRepeat = addButton(new CheckboxButton(left() + 5, top() + 95, dWidth() - 10, 20, new StringTextComponent("On repeat"), false));

        int btnWidthP = dWidth() - 10;
        int btnWidth = (btnWidthP - 5) / 2;
        cancel = addButton(new Button(left() + 5, top() + 120, btnWidth, 20, new StringTextComponent("Cancel"), this::cancel_clicked));
        confirm = addButton(new Button(left() + 10 + btnWidth, top() + 120, btnWidth, 20, new StringTextComponent("Confirm"), this::confirm_clicked));
        updateConfirmButtonState();
    }

    private void nameField_changed(String text) {
        if (namePattern.matcher(text).matches()) {
            nameField.setTextColor(0xE0E0E0);
            setErrorFlags(0, false);
        } else {
            nameField.setTextColor(0xE0 << 16);
            setErrorFlags(0, true);
        }
    }

    private void cycleField_changed(String text) {
        if (cyclePattern.matcher(text).matches()) {
            cycleField.setTextColor(0xE0E0E0);
            setErrorFlags(1, false);
        } else {
            cycleField.setTextColor(0xE0 << 16);
            setErrorFlags(1, true);
        }
    }

    private void setErrorFlags(int offset, boolean value) {
        if (value) {
            errorFlags |= 1 << offset;
        } else {
            errorFlags &= ~(1 << offset);
        }
        updateConfirmButtonState();
    }

    private void confirm_clicked(Button button) {
        Animator animator = Animator.get();
        AnimationProject lastProject = animator.getLatestProject();
        if (!lastProject.isSaved()) {
            ConfirmScreen warning = new ConfirmScreen(proceed -> handleUnsavedProject(proceed, this::createNewProject), new StringTextComponent("Warning"), new StringTextComponent("Your project is not saved. Do you wish to proceed anyway?"));
            warning.setDelay(25);
            minecraft.setScreen(warning);
        } else {
            createNewProject();
        }
    }

    private void handleUnsavedProject(boolean proceed, Runnable event) {
        if (proceed) {
            event.run();
        } else {
            minecraft.setScreen(this);
        }
    }

    private void createNewProject() {
        String name = nameField.getValue();
        int value = parse(cycleField.getValue(), 50);
        boolean repeat = onRepeat.selected();
        AnimationProject project = new AnimationProject(name, value);
        project.getAnimationControl().setOnRepeat(repeat);
        Animator.get().setUsingProject(project);
        showParent();
    }

    private void cancel_clicked(Button button) {
        showParent();
    }

    private void updateConfirmButtonState() {
        if (confirm != null)
            confirm.active = errorFlags == 0;
    }
}
