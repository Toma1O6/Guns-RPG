package dev.toma.gunsrpg.config.gun;

import net.minecraftforge.common.config.Config;

public class GeneralSettings {

    @Config.Name("Carbon barrel recoil multipler")
    @Config.RangeDouble(min = 0.0, max = 1.0)
    public float carbonBarrel = 0.65F;

    @Config.Name("Vertical grip recoil multipler")
    @Config.RangeDouble(min = 0.0, max = 1.0)
    public float verticalGrip = 0.7F;

    @Config.Name("Cheekpad recoil multipler")
    @Config.RangeDouble(min = 0.0, max = 1.0)
    public float cheekpad = 0.75F;
}
