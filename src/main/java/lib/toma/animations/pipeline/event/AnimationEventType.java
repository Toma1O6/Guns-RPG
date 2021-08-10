package lib.toma.animations.pipeline.event;

import lib.toma.animations.screen.animator.dialog.DialogScreen;
import lib.toma.animations.screen.animator.dialog.EventCreateDialog;
import lib.toma.animations.screen.animator.dialog.EventDialogContext;
import lib.toma.animations.serialization.IAnimationEventSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnimationEventType<E extends IAnimationEvent> {

    private static final Map<ResourceLocation, AnimationEventType<?>> TYPES = new HashMap<>();

    public static final AnimationEventType<SoundAnimationEvent> SOUND = new AnimationEventType<>(new ResourceLocation("sound"), new SoundAnimationEvent.Serializer(), SoundAnimationEvent.AddSoundEventDialog::new);
    public static final AnimationEventType<PlayAnimationEvent> ANIMATION = new AnimationEventType<>(new ResourceLocation("animation"), new PlayAnimationEvent.Serializer(), PlayAnimationEvent.AddPlayAnimationEventDialog::new);

    private final ResourceLocation key;
    private final IAnimationEventSerializer<E> serializer;
    private final ICreateDialog<E> dialogFactory;
    private final ITextComponent title;

    public AnimationEventType(ResourceLocation location, IAnimationEventSerializer<E> serializer, ICreateDialog<E> dialogFactory) {
        this.key = location;
        this.serializer = serializer;
        this.dialogFactory = dialogFactory;
        this.title = new TranslationTextComponent("event.type." + location.toString());
        TYPES.put(location, this);
    }

    public IAnimationEventSerializer<E> serializer() {
        return serializer;
    }

    public ResourceLocation getKey() {
        return key;
    }

    public EventCreateDialog<E> createDialog(DialogScreen parent, float target, EventCreateDialog.ICreator<E> creator) {
        EventDialogContext<E> context = new EventDialogContext<>(title, parent, creator, target);
        return dialogFactory.createDialog(context);
    }

    @SuppressWarnings("unchecked")
    public static <E extends IAnimationEvent> AnimationEventType<E> getType(ResourceLocation key) {
        return (AnimationEventType<E>) TYPES.get(key);
    }

    public static Set<ResourceLocation> allKeys() {
        return TYPES.keySet();
    }

    public interface ICreateDialog<E extends IAnimationEvent> {
        EventCreateDialog<E> createDialog(EventDialogContext<E> context);
    }
}
