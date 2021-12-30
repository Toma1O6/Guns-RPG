package dev.toma.gunsrpg.common.skills.core;

import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public final class DescriptionContainer {

    private final SkillType<?> type;
    private final List<Property> properties = new ArrayList<>();

    public DescriptionContainer(SkillType<?> type) {
        this.type = type;
    }

    public void addProperty(String propertyTag, Object... props) {
        properties.add(new Property(propertyTag, props));
    }

    public ITextComponent[] getLines() {
        ITextComponent[] components = new ITextComponent[properties.size()];
        int index = 0;
        for (Property property : properties) {
            components[index++] = SkillUtil.Localizations.translation(type, property.tag, property.props);
        }
        return components;
    }

    private static class Property {
        private final String tag;
        private final Object[] props;

        public Property(String tag, Object[] props) {
            this.tag = tag;
            this.props = props;
        }
    }
}
