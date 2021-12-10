package dev.toma.gunsrpg.client.screen.skill.placement;

public class UiPosition {

    private static final int SQUARE_SIZE = 4;
    private final int x;
    private final int y;

    public UiPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static UiPosition offsetSquares(int squares, int x, int y) {
        int offset = offsetSquares(squares);
        return new UiPosition(
                offset + x,
                offset + y
        );
    }

    public static int offsetSquares(int squares) {
        return squares * SQUARE_SIZE;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
