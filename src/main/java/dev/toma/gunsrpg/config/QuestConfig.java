package dev.toma.gunsrpg.config;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.helper.ReputationHelper;

public final class QuestConfig {

    @Configurable
    @Configurable.DecimalRange(min = 0.0F, max = ReputationHelper.MAX_REPUTATION)
    @Configurable.Comment({"Amount of reputation awarded when quest is completed", "Value is multiplied by quest tier"})
    @Configurable.Gui.NumberFormat("0.0###")
    public float reputationAwardPerTier = 0.2F;

    @Configurable
    @Configurable.DecimalRange(min = 0.0F, max = ReputationHelper.MAX_REPUTATION)
    @Configurable.Comment({"Amount of reputation lost when quest is failed", "Value is multiplied by quest tier"})
    @Configurable.Gui.NumberFormat("0.0###")
    public float reputationLossPerTier = 0.3F;

    @Configurable
    @Configurable.Range(min = 6000L) // 5 minutes
    @Configurable.Synchronized
    @Configurable.Comment("Quest refresh interval for mayor [ticks]")
    public long questRefreshInterval = Interval.hours(1).valueIn(Interval.Unit.TICK);

    @Configurable
    @Configurable.Synchronized
    @Configurable.Comment("Will block all possible interaction types inside quest areas")
    public boolean disableQuestAreaInteractions = true;
}
