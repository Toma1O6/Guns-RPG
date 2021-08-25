package lib.toma.animations.api;

import lib.toma.animations.api.lifecycle.IRegistryEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Animation stage defines bounds for animating.
 *
 * @author Toma
 */
public final class AnimationStage implements Comparable<AnimationStage>, IRegistryEntry {

    private static int indexOffset;

    /** Animates both right and left hand and held item at the same time. You can still assign independent animation to all parts animated */
    public static final AnimationStage ITEM_AND_HANDS = create("hands.item");
    /** Animates right hand */
    public static final AnimationStage RIGHT_HAND = create("hands.right");
    /** Animates left hand */
    public static final AnimationStage LEFT_HAND = create("hands.left");
    /** Animates both right and left hand */
    public static final AnimationStage HANDS = create("hands");
    /** Animates held item */
    public static final AnimationStage HELD_ITEM = create("item.heldfp");

    private final int index;
    private final ResourceLocation key;
    private final ITextComponent name;

    private AnimationStage(ResourceLocation key) {
        this.index = indexOffset++;
        this.key = key;
        this.name = new TranslationTextComponent("animation.stage." + key.toString());
    }

    /**
     * Creates new animation stage in minecraft namespace
     * @param name Stage name
     * @return New animation stage
     */
    public static AnimationStage create(String name) {
        return create(new ResourceLocation(name));
    }

    /**
     * Creates new animation stage
     * @param namespace Mod ID
     * @param name Stage name
     * @return New animation stage
     */
    public static AnimationStage create(String namespace, String name) {
        return create(new ResourceLocation(namespace, name));
    }

    /**
     * Creates new animation stage
     * @param resourceLocation Unique ID
     * @return New animation stage
     */
    public static AnimationStage create(ResourceLocation resourceLocation) {
        return new AnimationStage(resourceLocation);
    }

    public int getIndex() {
        return index;
    }

    @Override
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
}
