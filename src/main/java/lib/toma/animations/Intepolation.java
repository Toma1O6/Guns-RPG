package lib.toma.animations;

public final class Intepolation {
    private Intepolation() {}

    public static float linear(float delta, float current, float old) {
        return old + (current - old) * delta;
    }

    public static double linear(double delta, double current, double old) {
        return old + (current - old) * delta;
    }
}
