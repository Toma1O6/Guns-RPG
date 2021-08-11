package lib.toma.animations.screen.animator.dialog;

import lib.toma.animations.ByteFlags;
import lib.toma.animations.pipeline.event.AnimationEventType;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.screen.animator.AnimatorScreen;
import lib.toma.animations.screen.animator.widget.ListView;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Locale;
import java.util.regex.Pattern;

public class AddEventDialog extends DialogScreen {

    private static final ITextComponent NEXT = new TranslationTextComponent("screen.animator.next");
    private ListView<ResourceLocation> typeSelector;
    private TextFieldWidget position;
    private final ByteFlags errorFlags = new ByteFlags(0);
    private final float pos;
    private final EventCreateDialog.ICreator<?> creator;
    private ResourceLocation selectedValue;

    private final Pattern posPattern = Pattern.compile("(1(\\.0)?)|(0(\\.[0-9]+)?)");

    public AddEventDialog(AnimatorScreen screen, float pos, EventCreateDialog.ICreator<?> creator) {
        super(new TranslationTextComponent("screen.animator.dialog.add_event"), screen);
        this.pos = pos;
        this.creator = creator;

        setDimensions(160, 145);
    }

    @Override
    protected void init() {
        super.init();

        errorFlags.set(0);
        int btnWidthP = dWidth() - 10;
        int btnWidth = (btnWidthP - 5) / 2;
        typeSelector = addButton(new ListView<>(left() + 5, top() + 15, btnWidthP, 75, AnimationEventType.allKeys()));
        typeSelector.setResponder(this::selection_change);
        position = addButton(new TextFieldWidget(font, left() + 5, top() + 95, btnWidthP, 20, StringTextComponent.EMPTY));
        position.setResponder(new SuggestionResponder("Target", position, this::invokeTarget_change));
        position.setValue(String.format(Locale.ROOT, "%.3f", pos));
        cancel = addButton(new Button(left() + 5, top() + 120, btnWidth, 20, CANCEL, this::cancel_clicked));
        confirm = addButton(new Button(left() + 10 + btnWidth, top() + 120, btnWidth, 20, NEXT, this::confirm_clicked));

        updateConfirmButton();
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @SuppressWarnings("unchecked")
    private <E extends IAnimationEvent> void confirm_clicked(Button button) {
        AnimationEventType<E> eventType = AnimationEventType.getType(selectedValue);
        if (eventType == null) showParent();
        minecraft.setScreen(eventType.createDialog(this, Float.parseFloat(position.getValue()), (EventCreateDialog.ICreator<E>) creator));
    }

    private void invokeTarget_change(String value) {
        if (posPattern.matcher(value).matches()) {
            position.setTextColor(0xE0E0E0);
            errorFlags.clear(1);
        } else {
            position.setTextColor(0xE0 << 16);
            errorFlags.set(1);
        }
    }

    private void selection_change(ResourceLocation value) {
        this.selectedValue = value;
        if (value != null) {
            errorFlags.clear(0);
        } else {
            errorFlags.set(0);
        }
        updateConfirmButton();
    }

    private void updateConfirmButton() {
        if (confirm != null)
            confirm.active = errorFlags.isEmpty();
    }
}
