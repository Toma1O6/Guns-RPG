package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.NumberDisplayType;
import dev.toma.configuration.api.type.DoubleType;
import dev.toma.configuration.api.type.ObjectType;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class GeneralSettings extends ObjectType {

    public final DoubleType carbonBarrel;
    public final DoubleType verticalGrip;
    public final DoubleType cheekpad;

    public GeneralSettings(IObjectSpec spec) {
        super(spec);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("#.###", symbols);
        IConfigWriter writer = spec.getWriter();
        carbonBarrel = writer.writeBoundedDouble("Carbon barrel recoil", 0.65, 0.0, 1.0).setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER).setFormatting(format);
        verticalGrip = writer.writeBoundedDouble("Verical grip recoil", 0.7, 0.0, 1.0).setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER).setFormatting(format);
        cheekpad = writer.writeBoundedDouble("Cheekpad recoil", 0.75, 0.0, 1.0).setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER).setFormatting(format);
    }

    @Override
    public int getSortIndex() {
        return -1;
    }
}
