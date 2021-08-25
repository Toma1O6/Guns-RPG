package lib.toma.animations.api.event;

import lib.toma.animations.api.lifecycle.IRegistryEntry;
import lib.toma.animations.engine.screen.animator.dialog.DialogScreen;
import lib.toma.animations.engine.screen.animator.dialog.EventCreateDialog;
import lib.toma.animations.engine.screen.animator.dialog.EventDialogContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AnimationEventType<E extends IAnimationEvent> implements IRegistryEntry {

    public static final AnimationEventType<SoundAnimationEvent> SOUND = new AnimationEventType<>(new ResourceLocation("sound"), new SoundAnimationEvent.Serializer(), SoundAnimationEvent.AddSoundEventDialog::new);
    public static final AnimationEventType<PlayAnimationEvent> PLAY_ANIMATION = new AnimationEventType<>(new ResourceLocation("animation_play"), new PlayAnimationEvent.Serializer(), PlayAnimationEvent.AddPlayAnimationEventDialog::new);
    public static final AnimationEventType<StopAnimationEvent> STOP_ANIMATION = new AnimationEventType<>(new ResourceLocation("animation_stop"), new StopAnimationEvent.Serializer(), StopAnimationEvent.AddStopAnimationEventDialog::new);

    private final ResourceLocation key;
    private final IAnimationEventSerializer<E> serializer;
    private final ICreateDialog<E> dialogFactory;
    private final ITextComponent title;

    public AnimationEventType(ResourceLocation location, IAnimationEventSerializer<E> serializer, ICreateDialog<E> dialogFactory) {
        this.key = location;
        this.serializer = serializer;
        this.dialogFactory = dialogFactory;
        this.title = new TranslationTextComponent("event.type." + location.toString());
    }

    public IAnimationEventSerializer<E> serializer() {
        return serializer;
    }

    @Override
    public ResourceLocation getKey() {
        return key;
    }

    public EventCreateDialog<E> createDialog(DialogScreen parent, float target, EventCreateDialog.ICreator<E> creator) {
        EventDialogContext<E> context = new EventDialogContext<>(title, parent, creator, target);
        return dialogFactory.createDialog(context);
    }

    public interface ICreateDialog<E extends IAnimationEvent> {
        EventCreateDialog<E> createDialog(EventDialogContext<E> context);
    }
}
