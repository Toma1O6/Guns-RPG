package lib.toma.animations.engine.screen.animator.dialog;

import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.lifecycle.Registries;
import lib.toma.animations.engine.ByteFlags;
import lib.toma.animations.engine.screen.animator.AnimatorScreen;
import lib.toma.animations.engine.screen.animator.widget.LabelWidget;
import lib.toma.animations.engine.screen.animator.widget.MultiSelectListView;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class AddFrameDialog extends DialogScreen {

    private final float pos;
    private final Pattern posPattern = Pattern.compile("^(1(\\.0+)?)|(0(\\.[0-9]+))$");
    private final IInsertion insertion;
    private MultiSelectListView<AnimationStage> stageSelector;
    private TextFieldWidget position;
    private final ByteFlags errorFlags = new ByteFlags();

    public AddFrameDialog(AnimatorScreen screen, float pos, IInsertion insertionCallback) {
        super(new TranslationTextComponent("screen.animator.dialog.add_frame"), screen);
        this.pos = pos;
        this.insertion = insertionCallback;
        setDimensions(175, 160);
    }

    @Override
    protected void init() {
        super.init();

        int btnWidthP = dWidth() - 10;
        int btnWidth = (btnWidthP - 5) / 2;
        Collection<AnimationStage> stages = Registries.ANIMATION_STAGES.values();
        stageSelector = addButton(new MultiSelectListView<>(font, left() + 5, top() + 15, btnWidthP, 75, new ArrayList<>(stages)));
        stageSelector.setFormatter(stage -> stage.getName().getString());
        stageSelector.setSelectionResponder(this::selection_change);
        addButton(new LabelWidget(left() + 5, top() + 95, btnWidthP, 15, new StringTextComponent("Position"), font));
        position = addButton(new TextFieldWidget(font, left() + 5, top() + 110, btnWidthP, 20, StringTextComponent.EMPTY));
        position.setResponder(new SuggestionResponder("Frame position", position, this::pos_changed));
        position.setValue(AnimatorScreen.POSITION_FORMAT.format(pos));
        cancel = addButton(new Button(left() + 5, top() + 135, btnWidth, 20, CANCEL, this::cancel_clicked));
        confirm = addButton(new Button(left() + 10 + btnWidth, top() + 135, btnWidth, 20, CONFIRM, this::confirm_clicked));
        errorFlags.set(0);
        updateConfirmButtonState();
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    private void pos_changed(String value) {
        if (posPattern.matcher(value).matches()) {
            errorFlags.clear(1);
            position.setTextColor(0xE0E0E0);
        } else {
            position.setTextColor(0xE0 << 16);
            errorFlags.set(1);
        }
        updateConfirmButtonState();
    }

    private void updateConfirmButtonState() {
        if (confirm != null) confirm.active = errorFlags.isEmpty();
    }

    private void selection_change(AnimationStage stage, List<AnimationStage> stages) {
        if (stages.isEmpty()) {
            errorFlags.set(0);
        } else errorFlags.clear(0);
        updateConfirmButtonState();
    }

    private void confirm_clicked(Button button) {
        float progress;
        try {
            progress = Float.parseFloat(position.getValue());
        } catch (NumberFormatException nfe) {
            progress = pos;
        }
        insertion.doInsertion(progress, stageSelector.getSelection());
        showParent();
    }

    public interface IInsertion {
        void doInsertion(float progress, List<AnimationStage> stages);
    }
}
