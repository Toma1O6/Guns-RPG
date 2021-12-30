package lib.toma.animations.api.event;

import java.util.Locale;
import java.util.function.Predicate;

public enum FlowDirection {

    ALL(dir -> true),
    FORWARD,
    BACKWARD;

    final Predicate<FlowDirection> matcher;

    FlowDirection() {
        this.matcher = dir -> dir.equals(this);
    }

    FlowDirection(Predicate<FlowDirection> matcher) {
        this.matcher = matcher;
    }

    public boolean isMatch(FlowDirection direction) {
        return matcher.test(direction);
    }

    public String format() {
        return name().toUpperCase(Locale.ROOT).replaceAll("_", " ");
    }

    public static FlowDirection byId(int id) {
        FlowDirection[] arr = values();
        int l = arr.length;
        return arr[id % l];
    }
}
