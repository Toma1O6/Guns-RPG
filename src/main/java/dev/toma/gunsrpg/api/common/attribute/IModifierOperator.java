package dev.toma.gunsrpg.api.common.attribute;

@FunctionalInterface
public interface IModifierOperator {

    double combine(double base, double value);
}
