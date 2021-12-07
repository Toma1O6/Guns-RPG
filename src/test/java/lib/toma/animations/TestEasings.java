package lib.toma.animations;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testable
public class TestEasings {

    @Test
    public void testAllBeginAtZero() {
        Arrays.stream(Easings.values()).mapToDouble(easing -> easing.ease(0.0F)).forEach(value -> assertEquals(0.0F, value, 0.0001));
    }

    @Test
    public void testAllEndAtOne() {
        Arrays.stream(Easings.values()).mapToDouble(easing -> easing.ease(1.0F)).forEach(value -> assertEquals(1.0F, value, 0.0001));
    }
}
