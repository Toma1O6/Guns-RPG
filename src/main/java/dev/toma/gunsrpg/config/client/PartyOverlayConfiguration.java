package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.client.OverlayPlacement;

public class PartyOverlayConfiguration extends ConfigurableOverlay {

    @Configurable
    @Configurable.Comment("Will enable this overlay only when you have quest assigned")
    public boolean requireActiveQuest = true;

    public PartyOverlayConfiguration(boolean enabled, OverlayPlacement.HorizontalAlignment horizontal, OverlayPlacement.VerticalAlignment vertical, int xOffset, int yOffset) {
        super(enabled, horizontal, vertical, xOffset, yOffset);
    }
}
