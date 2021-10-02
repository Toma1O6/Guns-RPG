package lib.toma.animations.engine.screen.animator;

import lib.toma.animations.engine.ByteFlags;
import lib.toma.animations.engine.screen.animator.dialog.DialogScreen;
import lib.toma.animations.engine.screen.animator.dialog.SuggestionResponder;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class MergeAnimationScreen extends AbstractImportScreen {

    private static final ITextComponent MERGE = new StringTextComponent("Merge into current");
    private final ByteFlags errors = new ByteFlags(0, 1, 2);
    private final Pattern pattern01Dec = Pattern.compile("(1(\\.0+)?)|(0(\\.[0-9]+)?)");
    private TextFieldWidget rangeMin;
    private TextFieldWidget rangeMax;

    public MergeAnimationScreen(AnimatorScreen screen) {
        super(screen);
    }

    @Override
    protected void placeControlButtons(int totalButtonWidth, int buttonWidth) {
        int controlY = height - 25;
        int rangeY = height - 50;

        rangeMin = addButton(new TextFieldWidget(font, 5, rangeY, buttonWidth, 20, StringTextComponent.EMPTY));
        rangeMin.setResponder(new SuggestionResponder("Merge start", rangeMin, this::rangeMinChanged));
        rangeMax = addButton(new TextFieldWidget(font, 10 + buttonWidth, rangeY, buttonWidth, 20, StringTextComponent.EMPTY));
        rangeMax.setResponder(new SuggestionResponder("Merge end", rangeMax, this::rangeMaxChanged));

        cancel = addButton(new Button(5, controlY, buttonWidth, 20, DialogScreen.CANCEL, this::cancelClicked));
        confirm = addButton(new Button(10 + buttonWidth, controlY, buttonWidth, 20, MERGE, this::confirmClicked));
    }

    @Override
    protected int getPathViewHeight() {
        return 90;
    }

    @Override
    protected void confirmClicked(Button button) {
        Animator animator = Animator.get();
        FrameProviderWrapper wrapper = animator.getWrapper(getSelectedPath());
        if (wrapper != null) {
            AnimationProject project = animator.getProject();
            project.getFrameControl().merge(wrapper, getMergeStart(), getMergeEnd());
        }
        showParent();
    }

    @Override
    protected void updatePathDependents(@Nullable String path) {
        if (path == null || path.isEmpty()) {
            errors.set(0);
        } else {
            errors.clear(0);
        }
        updateDependents();
    }

    private void rangeMinChanged(String value) {
        rangeChanged(value, rangeMin, 1);
    }

    private void rangeMaxChanged(String value) {
        rangeChanged(value, rangeMax, 2);
    }

    private void rangeChanged(String value, TextFieldWidget widget, int index) {
        boolean errored = true;
        if (pattern01Dec.matcher(value).matches()) {
            float min = getMergeStart();
            float max = getMergeEnd();
            if (min < max) {
                errored = false;
            }
        }
        if (errored) {
            errors.set(index);
            widget.setTextColor(0xE0 << 16);
        } else {
            errors.clear(index);
            widget.setTextColor(0xE0E0E0);
        }
        updateDependents();
    }

    private void updateDependents() {
        confirm.active = errors.isEmpty();
    }

    private float getMergeStart() {
        return getWidgetValue(rangeMin);
    }

    private float getMergeEnd() {
        return getWidgetValue(rangeMax);
    }

    private float getWidgetValue(TextFieldWidget widget) {
        return widget != null ? parseFloat(widget.getValue()) : 0.0F;
    }

    private float parseFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException nfe) {
            return 0.0F;
        }
    }
}
