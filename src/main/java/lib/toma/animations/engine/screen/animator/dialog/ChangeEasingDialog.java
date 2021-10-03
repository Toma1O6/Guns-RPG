package lib.toma.animations.engine.screen.animator.dialog;

import lib.toma.animations.Easing;
import lib.toma.animations.engine.screen.animator.AnimatorScreen;
import lib.toma.animations.engine.screen.animator.widget.ListView;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Arrays;

public class ChangeEasingDialog extends DialogScreen {

    private final IEasingSelected easingSelected;
    private Easing easing;

    public ChangeEasingDialog(AnimatorScreen screen, IEasingSelected easingSelected) {
        super(new TranslationTextComponent("screen.animator.dialog.change_easing"), screen);
        this.easingSelected = easingSelected;

        setDimensions(176, 180);
    }

    @Override
    protected void init() {
        super.init();

        ListView<Easing> easingListView = addButton(new ListView<>(left() + 5, top() + 15, dWidth() - 10, 135, Arrays.asList(Easing.values())));
        easingListView.setResponder(this::easingSelected);
        easingListView.setFormatter(e -> e.getDisplayComponent().getString());

        int totalWidth = dWidth() - 10;
        int buttonWidth = (totalWidth - 5) / 2;

        cancel = addButton(new Button(left() + 5, top() + 155, buttonWidth, 20, CANCEL, this::cancel_clicked));
        confirm = addButton(new Button(left() + 10 + buttonWidth, top() + 155, buttonWidth, 20, CONFIRM, this::confirm_clicked));
        updateConfirmButton();
    }

    private void easingSelected(@Nullable Easing easing) {
        this.easing = easing;
        updateConfirmButton();
    }

    private void updateConfirmButton() {
        confirm.active = easing != null;
    }

    private void confirm_clicked(Button button) {
        easingSelected.onEasingConfirmSelect(easing);
        showParent();
    }

    @FunctionalInterface
    public interface IEasingSelected {
        void onEasingConfirmSelect(Easing easing);
    }
}
