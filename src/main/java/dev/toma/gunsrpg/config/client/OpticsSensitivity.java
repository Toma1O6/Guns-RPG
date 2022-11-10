package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.config.Configurable;

public class OpticsSensitivity {

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 1.0)
    @Configurable.Comment("Sensitivity multiplier for 2.5x scope")
    @Configurable.Gui.NumberFormat("0.00")
    public float scope25x = 0.4f;

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 1.0)
    @Configurable.Comment("Sensitivity multiplier for 3.0x scope")
    @Configurable.Gui.NumberFormat("0.00")
    public float scope30x = 0.45f;

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 1.0)
    @Configurable.Comment("Sensitivity multiplier for 3.5x scope")
    @Configurable.Gui.NumberFormat("0.00")
    public float scope35x = 0.4f;

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 1.0)
    @Configurable.Comment("Sensitivity multiplier for 4.0x scope")
    @Configurable.Gui.NumberFormat("0.00")
    public float scope40x = 0.4f;

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 1.0)
    @Configurable.Comment("Sensitivity multiplier for 6.0x scope")
    @Configurable.Gui.NumberFormat("0.00")
    public float scope60x = 0.15f;
}
