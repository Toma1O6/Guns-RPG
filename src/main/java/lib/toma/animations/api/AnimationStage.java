package lib.toma.animations.api;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.*;

public final class AnimationStage implements Comparable<AnimationStage> {

    private static final Map<ResourceLocation, AnimationStage> ID_STAGE_MAP = new HashMap<>();
    private static final Set<AnimationStage> VANILLA_TYPES = new HashSet<>();
    private static int indexOffset;

    public static final AnimationStage ITEM_AND_HANDS = vanilla("hands.item");
    public static final AnimationStage RIGHT_HAND = vanilla("hands.right");
    public static final AnimationStage LEFT_HAND = vanilla("hands.left");
    public static final AnimationStage HANDS = vanilla("hands");
    public static final AnimationStage HELD_ITEM = vanilla("item.heldfp");

    private final int index;
    private final ResourceLocation key;
    private final ITextComponent name;

    public AnimationStage(ResourceLocation key) {
        this.index = indexOffset++;
        this.key = key;
        this.name = new TranslationTextComponent("animation.stage." + key.toString());
        ID_STAGE_MAP.put(key, this);
    }

    public int getIndex() {
        return index;
    }

    public ResourceLocation getKey() {
        return key;
    }

    public ITextComponent getName() {
        return name;
    }

    @Override
    public int compareTo(AnimationStage o) {
        return this.getIndex() - o.getIndex();
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

    /**
     * @return String version of {@link ResourceLocation} which is used for map lookups
     */
    @Override
    public String toString() {
        return key.toString();
    }

    public static AnimationStage byKey(ResourceLocation key) {
        return ID_STAGE_MAP.get(key);
    }

    public static Set<AnimationStage> vanillaTypes() {
        return VANILLA_TYPES;
    }

    public static Collection<AnimationStage> values() {
        return ID_STAGE_MAP.values();
    }

    private static AnimationStage vanilla(String name) {
        AnimationStage stage = new AnimationStage(new ResourceLocation(name));
        VANILLA_TYPES.add(stage);
        return stage;
    }
}
