package dev.toma.gunsrpg.common.attribute;

@FunctionalInterface
public interface IModifierOperator {

    double combine(double base, double value);
}
