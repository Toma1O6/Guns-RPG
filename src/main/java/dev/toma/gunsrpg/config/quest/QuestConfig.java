package dev.toma.gunsrpg.config.quest;

import dev.toma.configuration.config.Configurable;

public final class QuestConfig {

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
