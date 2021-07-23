package lib.toma.animations.pipeline;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public final class AnimationStage {

    public static final AnimationStage ITEM_AND_HANDS = vanilla("hands.item");
    public static final AnimationStage RIGHT_HAND = vanilla("hands.right");
    public static final AnimationStage LEFT_HAND = vanilla("hands.left");
    public static final AnimationStage HANDS = vanilla("hands");
    public static final AnimationStage HELD_ITEM = vanilla("item.heldfp");

    private static final Map<Integer, AnimationStage> ID_STAGE_MAP = new IdentityHashMap<>();
    private static final Set<AnimationStage> VANILLA_TYPES = new HashSet<>();
    private static int indexOffset;

    private final int index;
    private final String name;

    public AnimationStage(String name) {
        this.index = indexOffset++;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public Set<AnimationStage> vanillaTypes() {
        return VANILLA_TYPES;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimationStage stage = (AnimationStage) o;
        return index == stage.index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String toString() {
        return "AnimationStage{" +
                "index=" + index +
                ", name='" + name + '\'' +
                '}';
    }

    private static AnimationStage vanilla(String name) {
        AnimationStage stage = new AnimationStage(name);
        VANILLA_TYPES.add(stage);
        return stage;
    }
}
