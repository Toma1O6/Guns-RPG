package lib.toma.animations.api.event;

import com.google.gson.*;
import lib.toma.animations.api.IAnimation;
import lib.toma.animations.engine.AbstractAnimationEvent;
import lib.toma.animations.engine.screen.SelectionButton;
import lib.toma.animations.engine.screen.animator.dialog.EventCreateDialog;
import lib.toma.animations.engine.screen.animator.dialog.EventDialogContext;
import lib.toma.animations.engine.screen.animator.dialog.SuggestionResponder;
import lib.toma.animations.engine.screen.animator.widget.ListView;
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
    private final FlowDirection direction;

    protected SoundAnimationEvent(AnimationEventType<? extends SoundAnimationEvent> type, float target, SoundEvent sound, float volume, float pitch, FlowDirection direction) {
        super(type, target);
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
        this.direction = direction;
    }

    public SoundAnimationEvent(float target, SoundEvent sound, float volume, float pitch, FlowDirection direction) {
        this(AnimationEventType.SOUND, target, sound, volume, pitch, direction);
    }

    @Override
    public void dispatch(Minecraft client, IAnimation fromAnimation) {
        if (fromAnimation instanceof IAnimationDirectionProvider) {
            FlowDirection direction = ((IAnimationDirectionProvider) fromAnimation).getDirection();
            if (this.direction.isMatch(direction)) {
                play(client);
            }
        } else {
            play(client);
        }
    }

    private void play(Minecraft client) {
        client.player.playSound(sound, volume, pitch);
    }

    @Override
    public IAnimationEvent copyAt(float target) {
        return new SoundAnimationEvent(target, sound, volume, pitch, direction);
    }

    public static final class Serializer implements IAnimationEventSerializer<SoundAnimationEvent> {

        @Override
        public JsonElement serialize(SoundAnimationEvent event, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("sound", event.sound.getRegistryName().toString());
            object.addProperty("volume", event.volume);
            object.addProperty("pitch", event.pitch);
            object.addProperty("direction", event.direction.ordinal());
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
            FlowDirection direction = FlowDirection.byId(JSONUtils.getAsInt(object, "direction", 0));
            return new SoundAnimationEvent(target, event, volume, pitch, direction);
        }
    }

    public static class AddSoundEventDialog extends EventCreateDialog<SoundAnimationEvent> {

        private Slider volumeSlider;
        private Slider pitchSlider;
        private SoundEvent selectedSound;
        private ListView<SoundEvent> soundSelector;
        private SelectionButton<FlowDirection> directionSelector;

        public AddSoundEventDialog(EventDialogContext<SoundAnimationEvent> context) {
            super(context);
        }

        @Override
        protected SoundAnimationEvent construct() {
            EventDialogContext<SoundAnimationEvent> context = getContext();
            float volume = (float) volumeSlider.getValue();
            float pitch = (float) pitchSlider.getValue();
            FlowDirection direction = directionSelector.getValue();
            return new SoundAnimationEvent(context.getTarget(), selectedSound, volume, pitch, direction);
        }

        @Override
        protected void preInit() {
            setDimensions(width - 20, 265);
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
            directionSelector = addButton(new SelectionButton<>(left() + 5, top() + 215, elWidth, 20, FlowDirection.values())).formatter(FlowDirection::format);
            cancel = addButton(new Button(left() + 5, top() + 240, btWidth, 20, CANCEL, this::cancel_clicked));
            confirm = addButton(new Button(left() + 10 + btWidth, top() + 240, btWidth, 20, CONFIRM, this::confirm_clicked));

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
