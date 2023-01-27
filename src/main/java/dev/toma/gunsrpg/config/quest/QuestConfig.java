package dev.toma.gunsrpg.config.quest;

import dev.toma.configuration.config.Configurable;

public final class QuestConfig {

    @Configurable
    @Configurable.Comment("Amount of reputation gained for every tier")
    @Configurable.DecimalRange(min = 0.0F, max = 100.0F)
    @Configurable.Gui.NumberFormat("0.0##")
    public float reputationGainPerTier = 0.02F;

    @Configurable
    @Configurable.Comment("Amount of reputation lost for every tier")
    @Configurable.DecimalRange(min = 0.0F, max = 100.0F)
    @Configurable.Gui.NumberFormat("0.0##")
    public float reputationLostPerTier = 0.03F;

    @Configurable
    @Configurable.Comment("Reputation multiplier for other party members")
    @Configurable.DecimalRange(min = 0.0F, max = 1.0F)
    @Configurable.Gui.NumberFormat("0.0##")
    public float otherPartyMemberReputationMultiplier = 0.5F;

    @Configurable
    @Configurable.Comment("Quest refresh timer for mayors")
    @Configurable.StringPattern(value = "(\\d+[tsmhd])+", errorDescriptor = "text.config.validation.error.format.interval") // TODO localize
    public String questRefreshTimer = "1h";

    @Configurable
    @Configurable.Comment("Level 1 quest area configuration")
    public AreaConfig area1 = new AreaConfig(200, 500, 32);

    @Configurable
    @Configurable.Comment("Level 2 quest area configuration")
    public AreaConfig area2 = new AreaConfig(300, 750, 32);

    @Configurable
    @Configurable.Comment("Level 3 quest area configuration")
    public AreaConfig area3 = new AreaConfig(400, 1000, 32);
}
