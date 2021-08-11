package lib.toma.animations.pipeline.event;

import com.google.gson.*;
import lib.toma.animations.pipeline.IAnimation;
import lib.toma.animations.screen.animator.dialog.EventCreateDialog;
import lib.toma.animations.screen.animator.dialog.EventDialogContext;
import lib.toma.animations.screen.animator.dialog.SuggestionResponder;
import lib.toma.animations.screen.animator.widget.ListView;
import lib.toma.animations.serialization.IAnimationEventSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SoundAnimationEvent extends AbstractAnimationEvent {

    private final SoundEvent sound;
    private final float volume;
    private final float pitch;

    protected SoundAnimationEvent(AnimationEventType<? extends SoundAnimationEvent> type, float target, SoundEvent sound, float volume, float pitch) {
        super(type, target);
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundAnimationEvent(float target, SoundEvent sound, float volume, float pitch) {
        this(AnimationEventType.SOUND, target, sound, volume, pitch);
    }

    @Override
    public void dispatch(Minecraft client, IAnimation fromAnimation) {
        client.player.playSound(sound, volume, pitch);
    }

    @Override
    public IAnimationEvent copyAt(float target) {
        return new SoundAnimationEvent(target, sound, volume, pitch);
    }

    public static final class Serializer implements IAnimationEventSerializer<SoundAnimationEvent> {

        @Override
        public JsonElement serialize(SoundAnimationEvent event, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("sound", event.sound.getRegistryName().toString());
            object.addProperty("volume", event.volume);
            object.addProperty("pitch", event.pitch);
            return object;
        }

        @Override
        public SoundAnimationEvent deserialize(float target, JsonElement src, JsonDeserializationContext context) throws JsonParseException {
            if (!src.isJsonObject())
                throw new JsonSyntaxException("Not a Json object!");
            JsonObject object = src.getAsJsonObject();
            ResourceLocation location = new ResourceLocation(JSONUtils.getAsString(object, "sound"));
            SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(location);
            if (event == null)
                throw new JsonSyntaxException("Unknown sound: " + location);
            float volume = JSONUtils.getAsFloat(object, "volume", 1.0F);
            float pitch = JSONUtils.getAsFloat(object, "pitch", 1.0F);
            return new SoundAnimationEvent(target, event, volume, pitch);
        }
    }

    public static class AddSoundEventDialog extends EventCreateDialog<SoundAnimationEvent> {

        private Slider volumeSlider;
        private Slider pitchSlider;
        private SoundEvent selectedSound;
        private ListView<SoundEvent> soundSelector;

        public AddSoundEventDialog(EventDialogContext<SoundAnimationEvent> context) {
            super(context);
        }

        @Override
        protected SoundAnimationEvent construct() {
            EventDialogContext<SoundAnimationEvent> context = getContext();
            float volume = (float) volumeSlider.getValue();
            float pitch = (float) pitchSlider.getValue();
            return new SoundAnimationEvent(context.getTarget(), selectedSound, volume, pitch);
        }

        @Override
        protected void preInit() {
            setDimensions(width - 20, 240);
        }

        @Override
        protected void addWidgets() {
            int elWidth = dWidth() - 10;
            int btWidth = (elWidth - 5) / 2;
            TextFieldWidget soundFilter = addButton(new TextFieldWidget(font, left() + 5, top() + 15, elWidth, 20, StringTextComponent.EMPTY));
            soundFilter.setResponder(new SuggestionResponder("Find sound", soundFilter, this::soundFilter_change));
            List<SoundEvent> sortedSoundList = ForgeRegistries.SOUND_EVENTS.getValues().stream().sorted(Comparator.comparing(SoundEvent::getLocation, ResourceLocation::compareNamespaced)).collect(Collectors.toList());
            soundSelector = addButton(new ListView<>(left() + 5, top() + 40, elWidth, 120, sortedSoundList));
            soundSelector.setResponder(this::sound_select);
            soundSelector.setFormatter(sound -> sound.getRegistryName().toString());
            volumeSlider = addButton(new Slider(left() + 5, top() + 165, elWidth, 20, new StringTextComponent("Volume: "), StringTextComponent.EMPTY, 0.0, 1.0, 1.0, true, true, btn -> {}));
            pitchSlider = addButton(new Slider(left() + 5, top() + 190, elWidth, 20, new StringTextComponent("Pitch: "), StringTextComponent.EMPTY, 0.0, 1.0, 1.0, true, true, btn -> {}));
            cancel = addButton(new Button(left() + 5, top() + 215, btWidth, 20, CANCEL, this::cancel_clicked));
            confirm = addButton(new Button(left() + 10 + btWidth, top() + 215, btWidth, 20, CONFIRM, this::confirm_clicked));

            updateConfirmButton();
        }

        private void soundFilter_change(String value) {
            soundSelector.setFilter(sound -> value == null || value.isEmpty() || sound.getRegistryName().toString().contains(value));
        }

        private void sound_select(SoundEvent event) {
            this.selectedSound = event;
            updateConfirmButton();
        }

        private void updateConfirmButton() {
            if (confirm != null)
                confirm.active = selectedSound != null;
        }
    }
}
