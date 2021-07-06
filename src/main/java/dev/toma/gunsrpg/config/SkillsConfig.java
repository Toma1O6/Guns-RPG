package dev.toma.gunsrpg.config;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.NumberDisplayType;
import dev.toma.configuration.api.type.IntType;
import dev.toma.configuration.api.type.ObjectType;

public class SkillsConfig extends ObjectType {

    private final IntType wellFedTriggerValue;

    public SkillsConfig(IObjectSpec spec) {
        super(spec);
        IConfigWriter writer = spec.getWriter();

        wellFedTriggerValue = writer.writeBoundedInt("WellFed min nutrition", 14, 1, 20, "Specify minimum nutrition value for Well Fed skill to be triggered").setDisplay(NumberDisplayType.SLIDER);
    }

    public int getWellFedMinNutrition() {
        return wellFedTriggerValue.get();
    }
}
