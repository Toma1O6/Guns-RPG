package dev.toma.gunsrpg.common.attribute;

import java.util.UUID;

public final class AttributeModifierFactory {

    /**
     * Creates new persistent modifier instance
     * @param uid Unique ID
     * @param op Operation
     * @param value Base value
     * @return New persistent modifier instance
     */
    public static IAttributeModifier modifier(UUID uid, IModifierOp op, double value) {
        return new AttributeModifier(uid, op, value);
    }

    /**
     * Creates new temporary modifier for n ticks
     * @param uid Unique ID
     * @param op Operation
     * @param value Base value
     * @param ticks Specifies for how many ticks will this modifier be applied
     * @return New temporary modifier instance
     */
    public static ITickableModifier temporary(UUID uid, IModifierOp op, double value, int ticks) {
        return new TemporaryModifier(uid, op, value, ticks);
    }

    /**
     * Private constructor, this is just a utility class
     */
    private AttributeModifierFactory() {}
}
