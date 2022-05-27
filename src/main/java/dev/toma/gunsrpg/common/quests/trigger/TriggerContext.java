package dev.toma.gunsrpg.common.quests.trigger;

import dev.toma.gunsrpg.util.properties.IPropertyReader;

public final class TriggerContext {

    private final ITriggerResponder responder;
    private final ITriggerHandler handler;

    private TriggerContext(ITriggerResponder responder, ITriggerHandler handler) {
        this.responder = responder;
        this.handler = handler;
    }

    public static TriggerContext make(ITriggerResponder responder, ITriggerHandler handler) {
        return new TriggerContext(responder, handler);
    }

    public TriggerResponseStatus getResponse(Trigger trigger, IPropertyReader reader) {
        return responder.handleTriggerEvent(trigger, reader);
    }

    public void handleSuccess(Trigger trigger, IPropertyReader reader) {
        handler.handleTriggerSuccess(trigger, reader);
    }
}
