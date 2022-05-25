package dev.toma.gunsrpg.util.helper;

public final class NumberHelper {

    public static int parseInt(String string, int fallback) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private NumberHelper() {}
}
