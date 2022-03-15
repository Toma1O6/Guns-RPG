package dev.toma.gunsrpg.client.animation;

import com.google.gson.*;
import dev.toma.gunsrpg.common.item.StashDetectorItem;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_RequestStashDetectorStatus;
import lib.toma.animations.api.IAnimation;
import lib.toma.animations.api.event.IAnimationEvent;
import lib.toma.animations.api.event.IAnimationEventSerializer;
import lib.toma.animations.engine.AbstractAnimationEvent;
import lib.toma.animations.engine.screen.SelectionButton;
import lib.toma.animations.engine.screen.animator.dialog.EventCreateDialog;
import lib.toma.animations.engine.screen.animator.dialog.EventDialogContext;
import lib.toma.animations.engine.screen.animator.widget.LabelWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class StashDetectorEvent extends AbstractAnimationEvent {

    private final StashDetectorItem.StatusEvent status;

    public StashDetectorEvent(float target, StashDetectorItem.StatusEvent status) {
        super(ModAnimations.STASH_DETECTOR_EVENT, target);
        this.status = status;
    }

    @Override
    public void dispatch(Minecraft client, IAnimation fromAnimation) {
        NetworkManager.sendServerPacket(new C2S_RequestStashDetectorStatus(status));
    }

    @Override
    public IAnimationEvent copyAt(float target) {
        return new StashDetectorEvent(target, status);
    }

    public static final class Serializer implements IAnimationEventSerializer<StashDetectorEvent> {

        @Override
        public JsonElement serialize(StashDetectorEvent event, JsonSerializationContext context) {
            return new JsonPrimitive(event.status.ordinal());
        }

        @Override
        public StashDetectorEvent deserialize(float target, JsonElement src, JsonDeserializationContext context) throws JsonParseException {
            int index = src.getAsInt();
            StashDetectorItem.StatusEvent event = StashDetectorItem.StatusEvent.values()[index];
            return new StashDetectorEvent(target, event);
        }
    }

    public static final class StashDetectorEventDialog extends EventCreateDialog<StashDetectorEvent> {

        private SelectionButton<StashDetectorItem.StatusEvent> statusSelector;

        public StashDetectorEventDialog(EventDialogContext<StashDetectorEvent> context) {
            super(context);
        }

        @Override
        protected void preInit() {
            setDimensions(150, 65);
        }

        @Override
        protected StashDetectorEvent construct() {
            EventDialogContext<StashDetectorEvent> context = this.getContext();
            return new StashDetectorEvent(context.getTarget(), statusSelector.getValue());
        }

        @Override
        protected void addWidgets() {
            int elWidth = dWidth() - 10;
            int btWidth = (elWidth - 5) / 2;

            addButton(new LabelWidget(left() + 5, top() + 15, btWidth, 20, new StringTextComponent("Status"), font));
            statusSelector = addButton(new SelectionButton<>(left() + 10 + btWidth, top() + 15, btWidth, 20, StashDetectorItem.StatusEvent.values()));

            cancel = addButton(new Button(left() + 5, top() + 40, btWidth, 20, CANCEL, this::cancel_clicked));
            confirm = addButton(new Button(left() + 10 + btWidth, top() + 40, btWidth, 20, CONFIRM, this::confirm_clicked));
        }
    }
}
