package dev.toma.gunsrpg.api.common.attribute;

import java.util.function.Supplier;

@FunctionalInterface
public interface IAttributeModifierFactory {

    IAttributeModifier make();

    static IAttributeModifierFactory of(Supplier<IAttributeModifier> supplier) {
        return supplier::get;
    }
}
