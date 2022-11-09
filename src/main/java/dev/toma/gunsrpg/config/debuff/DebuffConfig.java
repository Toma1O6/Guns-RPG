package dev.toma.gunsrpg.config.debuff;

import dev.toma.configuration.config.Configurable;

public final class DebuffConfig {

    @Configurable
    @Configurable.Comment("Disables poison debuff")
    public final boolean disablePoison = false;

    @Configurable
    @Configurable.Comment("Disables infection debuff")
    public final boolean disableInfection = false;

    @Configurable
    @Configurable.Comment("Disables fracture debuff")
    public final boolean disableFractures = false;

    @Configurable
    @Configurable.Comment("Disables bleeding debuff")
    public final boolean disableBleeding = false;

    @Configurable
    @Configurable.Comment("Disables reduced health debuff after respawn")
    public final boolean disableRespawnDebuff = false;
}
