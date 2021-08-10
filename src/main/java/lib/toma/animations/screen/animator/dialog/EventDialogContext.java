package lib.toma.animations.screen.animator.dialog;

import lib.toma.animations.pipeline.event.IAnimationEvent;
import net.minecraft.util.text.ITextComponent;

public final class EventDialogContext<E extends IAnimationEvent> {

    private final ITextComponent title;
    private final DialogScreen parentDialog;
    private final EventCreateDialog.ICreator<E> creator;
    private final float target;

    public EventDialogContext(ITextComponent title, DialogScreen parentDialog, EventCreateDialog.ICreator<E> creator, float target) {
        this.title = title;
        this.parentDialog = parentDialog;
        this.creator = creator;
        this.target = target;
    }

    public ITextComponent getTitle() {
        return title;
    }

    public DialogScreen getParentDialog() {
        return parentDialog;
    }

    public EventCreateDialog.ICreator<E> getCreator() {
        return creator;
    }

    public float getTarget() {
        return target;
    }
}
