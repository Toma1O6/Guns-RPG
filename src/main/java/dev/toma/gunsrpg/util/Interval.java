package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.util.helper.NumberHelper;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class which converts various time units to mc time
 *
 * @author Toma
 */
public final class Interval implements IIntervalProvider, Comparable<Interval> {

    public static final Pattern PATTERN = Pattern.compile("(\\d+[a-z])");
    private final Unit unit;
    private final int value;

    private Interval(Unit unit, int value) {
        this.unit = unit;
        this.value = value;
    }

    public Interval append(Interval other) {
        int ticks1 = valueIn(Unit.TICK);
        int ticks2 = other.valueIn(Unit.TICK);
        int total = ticks1 + ticks2;
        Unit target = this.unit;
        while (total % target.tickValue != 0 && target.ordinal() > 0) {
            target = Unit.values()[target.ordinal() - 1];
        }
        int value = total / target.tickValue;
        return new Interval(target, value);
    }

    public int valueIn(Unit unit) {
        if (unit == this.unit)
            return value;
        int ticks = value * this.unit.tickValue;
        return ticks / unit.tickValue;
    }

    @Override
    public int getTicks() {
        return unit == Unit.TICK ? value : valueIn(Unit.TICK);
    }

    public String format(boolean compact, Unit... values) {
        return this.format(compact, false, values);
    }

    public String format(boolean compact, boolean skipAllEmpty, Unit... values) {
        StringBuilder builder = new StringBuilder();
        Unit[] sorted = Arrays.stream(values).sorted((o1, o2) -> o2.tickValue - o1.tickValue).toArray(Unit[]::new);
        int value = this.value * this.unit.tickValue;
        boolean blockSkipping = !compact && !skipAllEmpty;
        for (Unit unit : sorted) {
            int unitValue = value / unit.tickValue;
            if (unitValue > 0 || blockSkipping) {
                value = value % unit.tickValue;
                builder.append(unitValue).append(compact ? "" : " ").append(unit.getName(compact)).append(!compact && unitValue > 1 ? "s" : "").append(compact ? "" : " ");
                blockSkipping = !skipAllEmpty;
            }
        }
        return builder.toString().trim();
    }

    @Override
    public int compareTo(Interval o) {
        return o.getTicks() - getTicks();
    }

    /**
     * Converts interval into another unit
     * @param unit Input unit
     * @param value Input value
     * @param out Output unit
     * @return Converted value
     */
    public static int convert(Unit unit, int value, Unit out) {
        return new Interval(unit, value).valueIn(out);
    }

    public static Interval ticks(int ticks) {
        return new Interval(Unit.TICK, ticks);
    }

    public static Interval seconds(int seconds) {
        return new Interval(Unit.SECOND, seconds);
    }

    public static Interval minutes(int minutes) {
        return new Interval(Unit.MINUTE, minutes);
    }

    public static Interval hours(int hours) {
        return new Interval(Unit.HOUR, hours);
    }

    public static Interval mcDays(int days) {
        return new Interval(Unit.MC_DAY, days);
    }

    public static String format(int value, IFormatFactory formatFactory) {
        Format format = formatFactory.configure(new Format());
        Interval interval = new Interval(format.source, value);
        return interval.format(format.compact, format.skipAllEmpty, format.output);
    }

    public static Interval parse(String input) {
        Matcher matcher = PATTERN.matcher(input);
        Interval interval = null;
        while (matcher.find()) {
            String component = matcher.group();
            Interval result = parseSimpleString(component);
            interval = interval == null ? result : interval.append(result);
        }
        return interval;
    }

    public static Interval parseSimpleString(String input) {
        String unitString = input.substring(input.length() - 1);
        String valueString = input.substring(0, input.length() - 1);
        Unit unit = Unit.getById(unitString);
        if (unit == null)
            throw new IllegalArgumentException("Invalid unit: " + unitString);
        int value = NumberHelper.parseInt(valueString, 0);
        return new Interval(unit, value);
    }

    public enum Unit {

        TICK(1, 't', "Tick"),
        SECOND(20, 's', "Second"),
        MINUTE(60 * 20, 'm', "Minute"),
        HOUR(60 * 60 * 20, 'h', "Hour"),
        MC_DAY(24000, 'd', "MC Day");

        final int tickValue;
        final char id;
        final String formattedName;

        Unit(int tickValue, char id, String formattedName) {
            this.tickValue = tickValue;
            this.id = id;
            this.formattedName = formattedName;
        }

        public String getName(boolean compact) {
            return compact ? String.valueOf(id) : formattedName.toLowerCase();
        }

        public static Unit getById(String id) {
            Unit[] units = Unit.values();
            for (Unit unit : units) {
                char character = unit.id;
                if (character == id.charAt(0)) {
                    return unit;
                }
            }
            return null;
        }
    }

    public static final class Format {

        private Unit source;
        private Unit[] output;
        private boolean compact;
        private boolean skipAllEmpty;

        public Format src(Unit unit) {
            this.source = unit;
            return this;
        }

        public Format out(Unit... units) {
            this.output = units;
            return this;
        }

        public Format compact() {
            this.compact = true;
            return this;
        }

        public Format skipAllEmptyValues() {
            this.skipAllEmpty = true;
            return this;
        }
    }

    @FunctionalInterface
    public interface IFormatFactory {
        Format configure(Format initial);
    }
}
