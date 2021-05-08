package dev.toma.gunsrpg.config.debuff;

import net.minecraftforge.common.config.Config;

public class DebuffConfig {

    @Config.Name("Disable poison")
    public boolean disablePoison = false;

    @Config.Name("Disable infections")
    public boolean disableInfection = false;

    @Config.Name("Disable fractures")
    public boolean disableFractures = false;

    @Config.Name("Disable bleeds")
    public boolean disableBleeding = false;

    @Config.Name("Disable respawn health debuff")
    public boolean disableRespawnDebuff = false;
}
