package lib.toma.animations.engine.screen.animator.dialog;

import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.engine.screen.animator.AnimatorScreen;
import lib.toma.animations.engine.screen.animator.widget.MultiSelectListView;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.stream.Collectors;

public class CopyFrameDialog extends DialogScreen {

    private final ICopyCallback copyCallback;
    private final AnimationStage selectedStage;
    private MultiSelectListView<AnimationStage> selector;
    private boolean selected;

    public CopyFrameDialog(AnimatorScreen screen, ICopyCallback callback, AnimationStage selected) {
        super(new TranslationTextComponent("screen.animator.dialog.copy_frame"), screen);
        this.copyCallback = callback;
        this.selectedStage = selected;
        setDimensions(150, 120);
    }

    @Override
    protected void init() {
        super.init();
        int btnWidthP = dWidth() - 10;
        int btnWidth = (btnWidthP - 5) / 2;
        List<AnimationStage> list = AnimationStage.values().stream().filter(stage -> !stage.equals(selectedStage)).collect(Collectors.toList());
        selector = addButton(new MultiSelectListView<>(font, left() + 5, top() + 15, btnWidthP, 75, list));
        selector.setSelectionResponder(this::selection_change);
        selector.setFormatter(stage -> stage.getName().getString());
        cancel = addButton(new Button(left() + 5, top() + 95, btnWidth, 20, CANCEL, this::cancel_clicked));
        confirm = addButton(new Button(left() + 10 + btnWidth, top() + 95, btnWidth, 20, CONFIRM, this::confirm_clicked));
        updateConfirmButton();
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    private void selection_change(AnimationStage stage, List<AnimationStage> selection) {
        selected = !selection.isEmpty();
        updateConfirmButton();
    }

    private void confirm_clicked(Button button) {
        if (copyCallback != null) {
            copyCallback.onCopied(selector.getSelection());
        }
        showParent();
    }

    private void updateConfirmButton() {
        if (confirm != null) {
            confirm.active = selected;
        }
    }

    public interface ICopyCallback {
        void onCopied(List<AnimationStage> targetStages);
    }
}
