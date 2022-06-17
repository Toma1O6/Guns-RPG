package dev.toma.gunsrpg.config;

import dev.toma.configuration.api.*;
import dev.toma.configuration.api.type.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SkillsConfig extends ObjectType {

    private final IntType wellFedTriggerValue;
    private final CollectionType<StringType> boundSkills;

    public SkillsConfig(IObjectSpec spec) {
        super(spec);
        IConfigWriter writer = spec.getWriter();

        wellFedTriggerValue = writer.writeBoundedInt("WellFed min nutrition", 14, 1, 20, "Specify minimum nutrition value for Well Fed skill to be triggered").setDisplay(NumberDisplayType.SLIDER);
        IRestriction<String> patternLimit = Restrictions.restrictStringByPattern(Pattern.compile("[a-z0-9]+:[a-z0-9/\\-_]+"), false, "Invalid resource location format");
        Supplier<StringType> factory = () -> new StringType("", "namespace:path", patternLimit);
        List<StringType> list = new ArrayList<>();
        list.add(new StringType("", "gunsrpg:like_a_cat_i", patternLimit));
        list.add(new StringType("", "gunsrpg:iron_buddy_i", patternLimit));
        list.add(new StringType("", "gunsrpg:god_help_us", patternLimit));
        boundSkills = writer.writeFillList("Bound skills", list, factory);
    }

    public int getWellFedMinNutrition() {
        return wellFedTriggerValue.get();
    }

    public List<String> getBoundSkills() {
        return boundSkills.get().stream().map(AbstractConfigType::get).collect(Collectors.toList());
    }
}
