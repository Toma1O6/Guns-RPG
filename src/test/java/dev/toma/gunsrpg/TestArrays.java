package dev.toma.gunsrpg;

import dev.toma.gunsrpg.util.ModUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class TestArrays {

    @Test
    void testArrayShifts() {
        Integer[] in1 = {2, null, null};
        ModUtils.shift(1, in1);
        ModUtils.shift(0, in1);
        Assertions.assertArrayEquals(new Integer[] {0, 1, 2}, in1);
    }
}
