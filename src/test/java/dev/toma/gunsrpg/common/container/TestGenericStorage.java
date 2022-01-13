package dev.toma.gunsrpg.common.container;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import static dev.toma.gunsrpg.common.container.GenericStorageContainer.getOffset;
import static dev.toma.gunsrpg.common.container.GenericStorageContainer.getOffsetModifier;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testable
public class TestGenericStorage {

    @Test
    public void testWidth9() {
        assertEquals(0.0, getOffsetModifier(9));
    }

    @Test
    public void testWidth8() {
        assertEquals(0.5, getOffsetModifier(8));
    }

    @Test
    public void testWidth7() {
        assertEquals(1.0, getOffsetModifier(7));
    }

    @Test
    public void testWidth6() {
        assertEquals(1.5, getOffsetModifier(6));
    }

    @Test
    public void testFirstOffsets() {
        assertEquals( 0, getOffset(0, 0.0));
        assertEquals( 9, getOffset(0, 0.5));
        assertEquals(18, getOffset(0, 1.0));
        assertEquals(27, getOffset(0, 1.5));
    }

    @Test
    public void testPositions() {
        assertEquals(36, getOffset(2, 0.0));
        assertEquals(27, getOffset(1, 0.5));
        assertEquals(36, getOffset(1, 1.0));
        assertEquals(81, getOffset(3, 1.5));
    }
}
