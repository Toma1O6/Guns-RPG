package dev.toma.gunsrpg.util;

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

    public Interval merge(Interval other) {
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

    public enum Unit {

        TICK(1),
        SECOND(20),
        MINUTE(60 * 20),
        HOUR(60 * 60 * 20),
        MC_DAY(24000);

        final int tickValue;

        Unit(int tickValue) {
            this.tickValue = tickValue;
        }
    }
}
