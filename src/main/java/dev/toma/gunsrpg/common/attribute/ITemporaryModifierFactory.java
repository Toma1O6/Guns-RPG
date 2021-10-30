package dev.toma.gunsrpg.common.attribute;

@FunctionalInterface
public interface ITemporaryModifierFactory extends IModifierProvider {

    TemporaryModifier createTempModifier();

    default IAttributeModifier getModifier() {
        return createTempModifier();
    }
}
