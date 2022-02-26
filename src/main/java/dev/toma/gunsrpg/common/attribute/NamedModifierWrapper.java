package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.api.common.attribute.*;

import java.util.UUID;

public class NamedModifierWrapper implements IAttributeModifier, IDisplayableModifier {

    private final IAttributeModifier modifier;
    private final IValueFormatter formatter;
    private final String tag;

    public NamedModifierWrapper(IAttributeModifier modifier, IValueFormatter valueFormatter, String tag) {
        this.modifier = modifier;
        this.formatter = valueFormatter;
        this.tag = tag;
    }

    @Override
    public UUID getUid() {
        return modifier.getUid();
    }

    @Override
    public IModifierOp getOperation() {
        return modifier.getOperation();
    }

    @Override
    public double getModifierValue() {
        return modifier.getModifierValue();
    }

    @Override
    public IModifierSerializer<?> getSerializer() {
        return modifier.getSerializer();
    }

    @Override
    public IAttributeModifier instance() {
        return modifier.instance();
    }

    @Override
    public String getTagId() {
        return tag;
    }

    @Override
    public IValueFormatter getFormatter() {
        return formatter;
    }

    @Override
    public String toString() {
        return modifier.toString();
    }
}
