package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.config.Configurable;

public class OpticsSensitivity {

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 1.0)
    @Configurable.Gui.NumberFormat("0.00")
    public final float scope25x = 0.4f;

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 1.0)
    @Configurable.Gui.NumberFormat("0.00")
    public final float scope30x = 0.45f;

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 1.0)
    @Configurable.Gui.NumberFormat("0.00")
    public final float scope35x = 0.4f;

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 1.0)
    @Configurable.Gui.NumberFormat("0.00")
    public final float scope40x = 0.4f;

    @Configurable
    @Configurable.DecimalRange(min = 0, max = 1.0)
    @Configurable.Gui.NumberFormat("0.00")
    public final float scope60x = 0.15f;
}
