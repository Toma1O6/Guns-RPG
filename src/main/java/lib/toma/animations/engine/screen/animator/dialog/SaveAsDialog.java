package lib.toma.animations.engine.screen.animator.dialog;

import lib.toma.animations.Keyframes;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.engine.frame.MutableKeyframe;
import lib.toma.animations.engine.screen.animator.*;
import lib.toma.animations.engine.screen.animator.widget.LabelWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class SaveAsDialog extends DialogScreen {

    private final Pattern namePattern = Pattern.compile("[a-zA-Z][A-Za-z0-9_]*");
    private TextFieldWidget filename;
    private CheckboxButton cleanFirstFrames;
    private boolean errored;

    public SaveAsDialog(AnimatorScreen screen) {
        super(new TranslationTextComponent("screen.animator.dialog.save_as"), screen);
        setDimensions(140, 105);
    }

    @Override
    protected void init() {
        super.init();
        int btnWidthP = dWidth() - 10;
        int btnWidth = (btnWidthP - 5) / 2;
        addButton(new LabelWidget(left() + 5, top() + 15, btnWidthP, 15, new StringTextComponent("Filename"), font));
        filename = addButton(new TextFieldWidget(font, left() + 5, top() + 30, btnWidthP, 20, StringTextComponent.EMPTY));
        filename.setResponder(new SuggestionResponder("Filename", filename, this::filename_changed));
        cleanFirstFrames = addButton(new CheckboxButton(left() + 5, top() + 55, btnWidthP, 20, new StringTextComponent("Clean first frames"), false));
        errored = true;

        cancel = addButton(new Button(left() + 5, top() + 80, btnWidth, 20, new StringTextComponent("Cancel"), this::cancel_clicked));
        confirm = addButton(new Button(left() + 10 + btnWidth, top() + 80, btnWidth, 20, new StringTextComponent("Save"), this::save_clicked));

        updateSaveButtonState();
    }

    private void save_clicked(Button button) {
        Animator animator = Animator.get();
        AnimationProject project = animator.getProject();
        if (cleanFirstFrames.selected()) {
            FrameProviderWrapper wrapper = project.getFrameControl();
            AnimatorFrameProvider provider = wrapper.getProvider();
            cleanParentFrames(provider);
        }
        project.saveProjectAs(filename.getValue());
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

    private void cleanParentFrames(AnimatorFrameProvider provider) {
        Set<AnimationStage> stagesToClear = provider.getParentStages();
        if (stagesToClear != null) {
            for (AnimationStage stage : stagesToClear) {
                List<MutableKeyframe> frames = provider.getFrames().get(stage);
                if (frames == null || frames.isEmpty()) continue;
                MutableKeyframe mkf = frames.get(0);
                mkf.setEndpoint(0);
                mkf.setPosition(Vector3d.ZERO);
                mkf.setRotation(Vector3d.ZERO);
                mkf.baseOn(Keyframes.none());
            }
        }
    }
}
