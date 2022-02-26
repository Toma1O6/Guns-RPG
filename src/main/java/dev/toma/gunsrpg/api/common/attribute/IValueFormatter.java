package dev.toma.gunsrpg.api.common.attribute;

@FunctionalInterface
public interface IValueFormatter {

    IValueFormatter IDENTITY = in -> (int) Math.round(in);
    IValueFormatter SECONDS = in -> (int) Math.round(in / 20);
    IValueFormatter PERCENT = in -> (int) Math.round(in * 100);
    IValueFormatter BASE_PERCENT = in -> (int) Math.round((in - 1.0F) * 100);
    IValueFormatter INV_PERCENT = in -> PERCENT.formatAttributeValue(1.0F - in);

    int formatAttributeValue(double in);
}
