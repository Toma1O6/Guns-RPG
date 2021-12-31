package dev.toma.gunsrpg;

import dev.toma.gunsrpg.util.Interval;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import static org.junit.jupiter.api.Assertions.*;

@Testable
public class TestIntervals {

    @Test
    public void testIdentity() {
        assertEquals(1, Interval.ticks(1).getTicks());
        assertEquals(111, Interval.ticks(111).getTicks());
    }

    //@Test
    public void testToTicks() {
        assertEquals(60, Interval.seconds(3).valueIn(Interval.Unit.TICK));
        assertEquals(4800, Interval.minutes(4800).valueIn(Interval.Unit.TICK));
        assertEquals(108000, Interval.hours(1).append(Interval.minutes(30)).valueIn(Interval.Unit.TICK));
        assertEquals(48000, Interval.mcDays(2).valueIn(Interval.Unit.TICK));
    }

    //@Test
    public void testToSeconds() {
        assertEquals(2, Interval.ticks(40).valueIn(Interval.Unit.SECOND));
        assertEquals(10, Interval.seconds(10).valueIn(Interval.Unit.SECOND));
        assertEquals(120, Interval.minutes(2).valueIn(Interval.Unit.SECOND));
        assertEquals(3600, Interval.hours(1).valueIn(Interval.Unit.SECOND));
    }

    //@Test
    public void testToMinutes() {
        assertEquals(1, Interval.ticks(1200).valueIn(Interval.Unit.MINUTE));
        assertEquals(5, Interval.seconds(300).valueIn(Interval.Unit.MINUTE));
        assertEquals(120, Interval.hours(2).valueIn(Interval.Unit.MINUTE));
    }
}
