package lib.toma.animations.pipeline.event;

import com.google.gson.*;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.pipeline.AnimationType;
import lib.toma.animations.pipeline.IAnimation;
import lib.toma.animations.pipeline.IAnimationPipeline;
import lib.toma.animations.screen.animator.dialog.EventCreateDialog;
import lib.toma.animations.screen.animator.dialog.EventDialogContext;
import lib.toma.animations.screen.animator.widget.ListView;
import lib.toma.animations.serialization.IAnimationEventSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class StopAnimationEvent extends AbstractAnimationEvent {

    private final AnimationType<?> targetType;

    public StopAnimationEvent(AnimationEventType<? extends StopAnimationEvent> type, float target, AnimationType<?> targetType) {
        super(type, target);
        this.targetType = targetType;
    }

    public StopAnimationEvent(float target, AnimationType<?> targetType) {
        this(AnimationEventType.STOP_ANIMATION, target, targetType);
    }

    @Override
    public void dispatch(Minecraft client, IAnimation fromAnimation) {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        pipeline.remove(targetType);
    }

    @Override
    public IAnimationEvent copyAt(float target) {
        return new StopAnimationEvent(target, targetType);
    }

    public static class Serializer implements IAnimationEventSerializer<StopAnimationEvent> {

        @Override
        public JsonElement serialize(StopAnimationEvent event, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("key", event.targetType.getName().toString());
            return object;
        }

        @Override
        public StopAnimationEvent deserialize(float target, JsonElement src, JsonDeserializationContext context) throws JsonParseException {
            if (!src.isJsonObject())
                throw new JsonSyntaxException("Not a Json object!");
            JsonObject object = src.getAsJsonObject();
            ResourceLocation keyPath = new ResourceLocation(JSONUtils.getAsString(object, "key"));
            AnimationType<?> type = AnimationType.getTypeFromID(keyPath);
            if (type == null)
                throw new JsonSyntaxException("Unknown animation type: " + keyPath);
            return new StopAnimationEvent(target, type);
        }
    }

    public static class AddStopAnimationEventDialog extends EventCreateDialog<StopAnimationEvent> {

        private AnimationType<?> selected;

        public AddStopAnimationEventDialog(EventDialogContext<StopAnimationEvent> context) {
            super(context);
            setDimensions(175, 120);
        }

        @Override
        protected void addWidgets() {
            int elWidth = dWidth() - 10;
            int btWidth = (elWidth - 5) / 2;
            ListView<AnimationType<?>> view = addButton(new ListView<>(left() + 5, top() + 15, elWidth, 75, AnimationType.values()));
            view.setResponder(this::onSelect);
            view.setFormatter(type -> type.getName().toString());

            cancel = addButton(new Button(left() + 5, top() + 95, btWidth, 20, CANCEL, this::cancel_clicked));
            confirm = addButton(new Button(left() + 10 + btWidth, top() + 95, btWidth, 20, CONFIRM, this::confirm_clicked));

            updateConfirmButton();
        }

        @Override
        protected StopAnimationEvent construct() {
            return new StopAnimationEvent(getContext().getTarget(), selected);
        }

        private void onSelect(AnimationType<?> value) {
            this.selected = value;
            updateConfirmButton();
        }

        private void updateConfirmButton() {
            if (confirm != null)
                confirm.active = selected != null;
        }
    }
}
