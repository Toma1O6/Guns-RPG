package dev.toma.gunsrpg;

import dev.toma.gunsrpg.util.RenderUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class TestString2Color {

    @Test
    public void testConvertions() {
        Assertions.assertEquals(0x123456, RenderUtils.string2colorRgb("123456"));
        Assertions.assertEquals(0x34F, RenderUtils.string2colorRgb("34F"));
        Assertions.assertEquals(0xFFFFFF, RenderUtils.string2colorRgb("FFfffF"));
    }
}
