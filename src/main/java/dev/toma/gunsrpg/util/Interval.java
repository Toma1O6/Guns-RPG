package dev.toma.gunsrpg.util;

import java.util.Arrays;

/**
 * Helper class which converts various time units to mc time
 *
 * @author Toma
 */
public final class Interval implements IIntervalProvider {

    private final Unit unit;
    private final int value;

    private Interval(Unit unit, int value) {
        this.unit = unit;
        this.value = value;
    }

    public Interval append(Interval other) {
        int value = convert(other.unit, other.value, unit);
        return new Interval(unit, this.value + value);
    }

    public int valueIn(Unit unit) {
        if (unit == this.unit)
            return value;
        int ticks = this.unit == Unit.TICK ? value : value * unit.tickValue;
        return ticks / unit.tickValue;
    }

    @Override
    public int getTicks() {
        return unit == Unit.TICK ? value : valueIn(Unit.TICK);
    }

    public String format(Unit... values) {
        StringBuilder builder = new StringBuilder();
        Unit[] sorted = Arrays.stream(values).sorted((o1, o2) -> o2.tickValue - o1.tickValue).toArray(Unit[]::new);
        int value = this.value;
        for (Unit unit : sorted) {
            int unitValue = value / unit.tickValue;
            if (unitValue > 0) {
                value = value % unit.tickValue;
            }
            builder.append(unitValue).append(unit.id);
        }
        return builder.toString();
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

    public static String format(int value, Unit unit, Unit... out) {
        Interval interval = new Interval(unit, value);
        return interval.format(out);
    }

    public enum Unit {

        TICK(1, 't'),
        SECOND(20, 's'),
        MINUTE(60 * 20, 'm'),
        HOUR(60 * 60 * 20, 'h'),
        MC_DAY(24000, 'd');

        final int tickValue;
        final char id;

        Unit(int tickValue, char id) {
            this.tickValue = tickValue;
            this.id = id;
        }
    }
}
