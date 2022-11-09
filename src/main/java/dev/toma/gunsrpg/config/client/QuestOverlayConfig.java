package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.config.Configurable;

public final class QuestOverlayConfig {

    @Configurable
    @Configurable.Comment("Quest overlay anchor on HUD")
    public final boolean rightAligned = true;

    @Configurable
    @Configurable.Range(min = 0, max = Short.MAX_VALUE)
    @Configurable.Comment("Height offset for quest overlay on HUD")
    public final int heightOffset = 60;
}
