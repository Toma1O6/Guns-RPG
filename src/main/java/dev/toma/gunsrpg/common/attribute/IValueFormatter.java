package dev.toma.gunsrpg.common.attribute;

@FunctionalInterface
public interface IValueFormatter {

    IValueFormatter IDENTITY = in -> (int) Math.round(in);
    IValueFormatter SECONDS = in -> (int) Math.round(in / 20);
    IValueFormatter PERCENT = in -> (int) Math.round(in * 100);

    int formatAttributeValue(double in);
}
