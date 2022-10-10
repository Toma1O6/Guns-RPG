package dev.toma.gunsrpg.util.helper;

import java.util.TreeMap;

public final class NumberHelper {

    private final static TreeMap<Integer, String> ROMAN_NUMBER_MAP = new TreeMap<>();

    public static int parseInt(String string, int fallback) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static String toRomanLiteral(int number) {
        int l = ROMAN_NUMBER_MAP.floorKey(number);
        if (number == l) {
            return ROMAN_NUMBER_MAP.get(number);
        }
        return ROMAN_NUMBER_MAP.get(l) + toRomanLiteral(number - l);
    }

    private NumberHelper() {
    }

    static {
        ROMAN_NUMBER_MAP.put(1000, "M");
        ROMAN_NUMBER_MAP.put(900, "CM");
        ROMAN_NUMBER_MAP.put(500, "D");
        ROMAN_NUMBER_MAP.put(400, "CD");
        ROMAN_NUMBER_MAP.put(100, "C");
        ROMAN_NUMBER_MAP.put(90, "XC");
        ROMAN_NUMBER_MAP.put(50, "L");
        ROMAN_NUMBER_MAP.put(40, "XL");
        ROMAN_NUMBER_MAP.put(10, "X");
        ROMAN_NUMBER_MAP.put(9, "IX");
        ROMAN_NUMBER_MAP.put(5, "V");
        ROMAN_NUMBER_MAP.put(4, "IV");
        ROMAN_NUMBER_MAP.put(1, "I");
    }
}
