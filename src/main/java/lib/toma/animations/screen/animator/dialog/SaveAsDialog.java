package lib.toma.animations.screen.animator.dialog;

import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.frame.MutableKeyframe;
import lib.toma.animations.screen.animator.*;
import lib.toma.animations.screen.animator.widget.LabelWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SaveAsDialog extends DialogScreen {

    private final Pattern namePattern = Pattern.compile("[a-z][a-z0-9_]*");
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

        cancel = addButton(new Button(left() + 5, top() + 80, btnWidth, 20, new StringTextComponent("Cancel"), this::cancel_clicked));
        confirm = addButton(new Button(left() + 10 + btnWidth, top() + 80, btnWidth, 20, new StringTextComponent("Save"), this::save_clicked));
    }

    private void save_clicked(Button button) {
        Animator animator = Animator.get();
        AnimationProject project = animator.getProject();
        if (cleanFirstFrames.selected()) {
            FrameProviderWrapper wrapper = project.getFrameControl();
            AnimatorFrameProvider provider = wrapper.getProvider();
            cleanFramesForConnector(provider);
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

    private void cleanFramesForConnector(AnimatorFrameProvider provider) {
        Map<AnimationStage, List<MutableKeyframe>> frames = provider.getFrames();
        for (List<MutableKeyframe> list : frames.values()) {
            if (list.isEmpty()) continue;
            MutableKeyframe first = list.get(0);
            first.setEndpoint(0.0F);
            first.setPosition(Vector3d.ZERO);
            first.setRotation(Quaternion.ONE.copy());
            first.setPos0(Vector3d.ZERO);
            first.setQuat0(Quaternion.ONE.copy());
            if (list.size() > 1) {
                for (int i = 1; i < list.size(); i++) {
                    list.get(i).baseOn(list.get(i - 1));
                }
            }
        }
    }
}
