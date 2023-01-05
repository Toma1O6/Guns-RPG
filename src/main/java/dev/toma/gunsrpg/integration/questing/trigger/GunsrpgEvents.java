package dev.toma.gunsrpg.integration.questing.trigger;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.integration.questing.trigger.event.ItemGivenEvent;
import dev.toma.questing.common.component.trigger.EventType;

public final class GunsrpgEvents {

    public static final EventType<ItemGivenEvent> ITEM_GIVEN = EventType.create(GunsRPG.makeResource("item_given"));
}
