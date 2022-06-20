package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.NumberDisplayType;
import dev.toma.configuration.api.type.DoubleType;
import dev.toma.configuration.api.type.ObjectType;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class OpticsSensitivity extends ObjectType {

    public final DoubleType scope25x;
    public final DoubleType scope30x;
    public final DoubleType scope35x;
    public final DoubleType scope40x;
    public final DoubleType scope60x;

    public OpticsSensitivity(IObjectSpec spec) {
        super(spec);

        IConfigWriter writer = spec.getWriter();
        DecimalFormatSymbols decSeparator = new DecimalFormatSymbols();
        decSeparator.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("0.00", decSeparator);
        this.scope25x = writer.writeBoundedDouble("Sensitivity 2.5x", 0.40F, 0.0F, 1.0F, "Sensitivity for 2.5x optics").setFormatting(format).setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER);
        this.scope30x = writer.writeBoundedDouble("Sensitivity 3.0x", 0.45F, 0.0F, 1.0F, "Sensitivity for 3.0x optics").setFormatting(format).setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER);
        this.scope35x = writer.writeBoundedDouble("Sensitivity 3.5x", 0.40F, 0.0F, 1.0F, "Sensitivity for 3.5x optics").setFormatting(format).setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER);
        this.scope40x = writer.writeBoundedDouble("Sensitivity 4.0x", 0.40F, 0.0F, 1.0F, "Sensitivity for 4.0x optics").setFormatting(format).setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER);
        this.scope60x = writer.writeBoundedDouble("Sensitivity 6.0x", 0.15F, 0.0F, 1.0F, "Sensitivity for 6.0x optics").setFormatting(format).setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER);
    }
}
