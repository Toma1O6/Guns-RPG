package dev.toma.gunsrpg.util.math;

public interface IDimensions {

    int getWidth();

    int getHeight();

    default int getArea() {
        return getWidth() * getHeight();
    }

    default boolean validate(int expectedLength) {
        return getArea() == expectedLength;
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
