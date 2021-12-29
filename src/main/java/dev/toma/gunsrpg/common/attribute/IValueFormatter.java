package dev.toma.gunsrpg.common.attribute;

@FunctionalInterface
public interface IValueFormatter {

    IValueFormatter IDENTITY = in -> (int) Math.round(in);
    IValueFormatter SECONDS = in -> (int) Math.round(in / 20);
    IValueFormatter PERCENT = in -> (int) (in * 100);
    IValueFormatter INV_PERCENT = in -> 100 - PERCENT.formatAttributeValue(in);

    int formatAttributeValue(double in);
}
