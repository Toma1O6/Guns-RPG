package dev.toma.gunsrpg;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import static dev.toma.gunsrpg.util.math.Mth.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testable
public class TestMth {

    @Test
    public void testInvert() {
        assertEquals(1.0F, invert(0.0F));
        assertEquals(0.0F, invert(1.0F));
        assertEquals(0.5F, invert(0.5F));
        assertEquals(0.25F, invert(0.75F));
    }

    @Test
    public void testLinearRise() {
        assertEquals(0.5F, linearLineRising(0.5F, 0.25F, 0.75F));
        assertEquals(0.5F, linearLineRising(0.75F, 0.5F, 1.0F));
        assertEquals(1.0F, linearLineRising(0.75F, 0.0F, 0.75F));
        assertEquals(0.5F, linearLineRising(0.5F, 0.0F, 1.0F));
    }

    @Test
    public void testLinearFall() {
        assertEquals(0.5F, linearLineFalling(0.5F, 0.25F, 0.75F));
        assertEquals(1.0F, linearLineFalling(0.75F, 0.75F, 1.0F));
        assertEquals(0.0F, linearLineFalling(0.75F, 0.33F, 0.75F));
        assertEquals(0.5F, linearLineFalling(0.5F, 0.0F, 1.0F));
    }

    @Test
    public void testTriangleFunction() {
        assertEquals(0.0F, triangleFunc(0.00F));
        assertEquals(1.0F, triangleFunc(0.50F));
        assertEquals(0.0F, triangleFunc(1.00F));
        assertEquals(0.5F, triangleFunc(0.25F));
        assertEquals(0.5F, triangleFunc(0.75F));
    }
}
