package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.config.Configurable;

public final class GeneralSettings {

    @Configurable
    @Configurable.DecimalRange(min = 0.0, max = 1.0)
    @Configurable.Comment("Recoil multiplier for carbon barrel attachment")
    @Configurable.Gui.NumberFormat("0.0##")
    public final double carbonBarrel = 0.65;

    @Configurable
    @Configurable.DecimalRange(min = 0.0, max = 1.0)
    @Configurable.Comment("Recoil multiplier for vertical grip attachment")
    @Configurable.Gui.NumberFormat("0.0##")
    public final double verticalGrip = 0.7;

    @Configurable
    @Configurable.DecimalRange(min = 0.0, max = 1.0)
    @Configurable.Comment("Recoil multiplier for cheekpad attachment")
    @Configurable.Gui.NumberFormat("0.0##")
    public final double cheekpad = 0.75;
}
