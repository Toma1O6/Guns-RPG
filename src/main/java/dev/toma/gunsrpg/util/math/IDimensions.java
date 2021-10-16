package dev.toma.gunsrpg.util.math;

public interface IDimensions {

    int getWidth();

    int getHeight();

    default boolean validate(int expectedLength) {
        return getWidth() * getHeight() == expectedLength;
    }

    static IDimensions of(int widthAndHeight) {
        return of(widthAndHeight, widthAndHeight);
    }

    static IDimensions of(int width, int height) {
        return new IDimensions() {
            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }
        };
    }
}
