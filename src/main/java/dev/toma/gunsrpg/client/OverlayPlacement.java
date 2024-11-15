package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.config.client.ConfigurableOverlay;
import dev.toma.gunsrpg.util.math.IVec2i;
import dev.toma.gunsrpg.util.math.Vec2i;

import java.util.function.IntUnaryOperator;

public final class OverlayPlacement {

    public static IVec2i getPlacement(ConfigurableOverlay overlay, int x, int y, int screenWidth, int screenHeight, int itemWidth, int itemHeight) {
        return getPlacement(overlay.horizontalAlignment, overlay.verticalAlignment, x, y, screenWidth, screenHeight, itemWidth, itemHeight, overlay.xOffset, overlay.yOffset);
    }

    public static IVec2i getPlacement(HorizontalAlignment horizontal, VerticalAlignment vertical, int x, int y, int screenWidth, int screenHeight, int itemWidth, int itemHeight, int xOffset, int yOffset) {
        int leftPos = horizontal.placement(x, x + screenWidth, itemWidth) + horizontal.applyOffset(xOffset);
        int topPos = vertical.placement(y, y + screenHeight, itemHeight) + vertical.applyOffset(yOffset);
        return new Vec2i(leftPos, topPos);
    }

    @FunctionalInterface
    public interface Placement {
        int placement(int start, int end, int size);
    }

    public interface Alignment extends Placement {

        int applyOffset(int offset);

        static Alignment create(Placement placement, IntUnaryOperator operator) {
            return new Alignment() {
                @Override
                public int applyOffset(int offset) {
                    return operator.applyAsInt(offset);
                }
                @Override
                public int placement(int start, int end, int size) {
                    return placement.placement(start, end, size);
                }
            };
        }
    }

    public enum HorizontalAlignment implements Alignment {

        LEFT(Alignment.create((start, end, size) -> start, IntUnaryOperator.identity())),
        CENTER(Alignment.create((start, end, size) -> start + (end - size) / 2, IntUnaryOperator.identity())),
        RIGHT(Alignment.create((start, end, size) -> end - size, f -> -f));

        private final Alignment function;

        HorizontalAlignment(Alignment function) {
            this.function = function;
        }

        @Override
        public int placement(int start, int end, int size) {
            return this.function.placement(start, end, size);
        }

        @Override
        public int applyOffset(int offset) {
            return this.function.applyOffset(offset);
        }
    }

    public enum VerticalAlignment implements Alignment {

        TOP(Alignment.create((start, end, size) -> start, IntUnaryOperator.identity())),
        CENTER(Alignment.create((start, end, size) -> start + (end - size) / 2, IntUnaryOperator.identity())),
        BOTTOM(Alignment.create((start, end, size) -> end - size, f -> -f));

        private final Alignment function;

        VerticalAlignment(Alignment function) {
            this.function = function;
        }

        @Override
        public int placement(int start, int end, int size) {
            return this.function.placement(start, end, size);
        }

        @Override
        public int applyOffset(int offset) {
            return this.function.applyOffset(offset);
        }
    }
}
