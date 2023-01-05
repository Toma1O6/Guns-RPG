package dev.toma.gunsrpg.integration.questing.trigger;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.integration.questing.trigger.event.ItemGivenEvent;
import dev.toma.questing.common.component.trigger.Trigger;

public final class GunsrpgTriggers {

    public static final Trigger<HandoverItemTriggerData> HANDOVER_ITEMS = Trigger.create(GunsRPG.makeResource("handover_item"));

    static {
        HANDOVER_ITEMS.setEventMapping(GunsrpgEvents.ITEM_GIVEN, data -> new ItemGivenEvent(data.getStack(), data.getPlayer(), data.getEntity()));
    }
}
