package lib.toma.animations.pipeline.event;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.ByteFlags;
import lib.toma.animations.pipeline.AnimationType;
import lib.toma.animations.pipeline.IAnimation;
import lib.toma.animations.pipeline.IAnimationPipeline;
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
import net.minecraft.util.text.StringTextComponent;

import java.util.regex.Pattern;

public class PlayAnimationEvent extends AbstractAnimationEvent {

    private final AnimationType<?> type;
    private final int playDelay;

    public PlayAnimationEvent(float target, AnimationType<?> type) {
        this(target, type, 0);
    }

    public PlayAnimationEvent(float target, AnimationType<?> type, int playDelay) {
        this(AnimationEventType.ANIMATION, target, type, playDelay);
    }

    protected PlayAnimationEvent(AnimationEventType<? extends PlayAnimationEvent> eventType, float target, AnimationType<?> type, int playDelay) {
        super(eventType, target);
        this.type = type;
        this.playDelay = playDelay;
        Preconditions.checkState(type.hasCreator(), "This type doesn't have custom animation creator!");
    }

    @Override
    public void dispatch(Minecraft client, IAnimation fromAnimation) {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        if (playDelay == 0)
            pipeline.insert(type);
        else
            pipeline.scheduleInsert(type, playDelay);
    }

    @Override
    public IAnimationEvent copyAt(float target) {
        return new PlayAnimationEvent(target, type, playDelay);
    }

    public static final class Serializer implements IAnimationEventSerializer<PlayAnimationEvent> {

        @Override
        public JsonElement serialize(PlayAnimationEvent event, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("key", event.type.getName().toString());
            if (event.playDelay > 0)
                object.addProperty("delay", event.playDelay);
            return object;
        }

        @Override
        public PlayAnimationEvent deserialize(float target, JsonElement src, JsonDeserializationContext context) {
            if (!src.isJsonObject())
                throw new JsonSyntaxException("Not a Json object!");
            JsonObject object = src.getAsJsonObject();
            ResourceLocation location = new ResourceLocation(JSONUtils.getAsString(object, "key"));
            AnimationType<?> type = AnimationType.getTypeFromID(location);
            if (type == null)
                throw new JsonSyntaxException("Unknown animation type: " + location);
            int delay = JSONUtils.getAsInt(object, "delay", 0);
            return new PlayAnimationEvent(target, type, delay);
        }
    }

    public static class AddPlayAnimationEventDialog extends EventCreateDialog<PlayAnimationEvent> {

        private final Pattern delayPattern = Pattern.compile("0|([1-9][0-9]{0,2})");
        private final ByteFlags errorHandler = new ByteFlags(0);
        private TextFieldWidget playDelay;

        private AnimationType<?> selectedType;

        public AddPlayAnimationEventDialog(EventDialogContext<PlayAnimationEvent> context) {
            super(context);
            setDimensions(175, 145);
        }

        @Override
        protected PlayAnimationEvent construct() {
            EventDialogContext<PlayAnimationEvent> context = getContext();
            return new PlayAnimationEvent(context.getTarget(), selectedType, Integer.parseInt(playDelay.getValue()));
        }

        @Override
        protected void addWidgets() {
            int elementWidth = dWidth() - 10;
            int buttonWidth = (elementWidth - 5) / 2;
            ListView<AnimationType<?>> availableTypes = addButton(new ListView<>(left() + 5, top() + 15, elementWidth, 75, AnimationType.values(), AnimationType::hasCreator));
            availableTypes.setResponder(this::select);
            availableTypes.setFormatter(type -> type.getName().toString());
            playDelay = addButton(new TextFieldWidget(font, left() + 5, top() + 95, elementWidth, 20, StringTextComponent.EMPTY));
            playDelay.setResponder(new SuggestionResponder("Delay [ticks]", playDelay, this::playDelay_changed));
            cancel = addButton(new Button(left() + 5, top() + 120, buttonWidth, 20, CANCEL, this::cancel_clicked));
            confirm = addButton(new Button(left() + 10 + buttonWidth, top() + 120, buttonWidth, 20, CONFIRM, this::confirm_clicked));
            updateConfirmButton();
        }

        private void select(AnimationType<?> type) {
            this.selectedType = type;
            if (type != null) errorHandler.clear(0);
            else errorHandler.set(0);
        }

        private void playDelay_changed(String value) {
            if (delayPattern.matcher(value).matches()) {
                errorHandler.clear(1);
                playDelay.setTextColor(0xE0E0E0);
            } else {
                errorHandler.set(1);
                playDelay.setTextColor(0xE0 << 16);
            }
            updateConfirmButton();
        }

        private void updateConfirmButton() {
            if (confirm != null)
                confirm.active = errorHandler.isEmpty();
        }
    }
}
