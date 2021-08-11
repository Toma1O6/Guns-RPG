package lib.toma.animations.screen.animator.dialog;

import lib.toma.animations.pipeline.event.IAnimationEvent;
import net.minecraft.client.gui.widget.button.Button;

public abstract class EventCreateDialog<E extends IAnimationEvent> extends DialogScreen {

    private final EventDialogContext<E> context;

    public EventCreateDialog(EventDialogContext<E> context) {
        super(context.getTitle(), context.getParentDialog().getParent());
        this.context = context;
    }

    protected abstract E construct();

    protected abstract void addWidgets();

    protected void preInit() {}

    @Override
    protected final void init() {
        preInit();
        super.init();
        addWidgets();
    }

    @Override
    public void showParent() {
        minecraft.setScreen(context.getParentDialog());
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    protected final void confirm_clicked(Button button) {
        context.getCreator().createInsert(this.construct());
        context.getParentDialog().showParent();
    }

    public EventDialogContext<E> getContext() {
        return context;
    }

    public interface ICreator<E extends IAnimationEvent> {
        void createInsert(E event);
    }
}
