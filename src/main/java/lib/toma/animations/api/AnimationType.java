package lib.toma.animations.api;

import lib.toma.animations.api.lifecycle.IRegistryEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Animation type acts as common identifier for specific group of animations.
 *
 * @param <A> Type of animation
 */
public final class AnimationType<A extends IAnimation> implements IRegistryEntry, Comparable<AnimationType<?>> {

    private static int indexPool;

    // internal id used mainly for sorting
    private final int internalId = indexPool++;
    // unique id
    private final ResourceLocation name;
    // animation instance creator, can be null
    private final IAnimationCreator<A> creator;
    // whether this animation should be process via IAnimationList#animateSpecial method
    private boolean renderSpecial;

    private AnimationType(ResourceLocation name, IAnimationCreator<A> creator) {
        this.name = name;
        this.creator = creator;
    }

    /**
     * Creates new animation type with specified ID and instance creator
     * @param uniqueID Unique id for this type
     * @param instanceCreator Animation instance creator
     * @param <A> Animation type
     * @return New animation type instance
     */
    public static <A extends IAnimation> AnimationType<A> create(ResourceLocation uniqueID, IAnimationCreator<A> instanceCreator) {
        return new AnimationType<>(uniqueID, instanceCreator);
    }

    /**
     * Creates new animation type with specified ID but without custom instance creator
     * @param uniqueID Unique id for this type
     * @param <A> Animation type
     * @return New animation type instance
     */
    public static <A extends IAnimation> AnimationType<A> create(ResourceLocation uniqueID) {
        return create(uniqueID, null);
    }

    public AnimationType<A> setSpecial() {
        this.renderSpecial = true;
        return this;
    }

    public boolean hasCreator() {
        return creator != null;
    }

    public boolean isSpecial() {
        return renderSpecial;
    }

    public A create(PlayerEntity client) {
        return creator.create(client);
    }

    @Override
    public ResourceLocation getKey() {
        return name;
    }

    @Override
    public String toString() {
        return "AnimationType{" +
                "name=" + name +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimationType<?> that = (AnimationType<?>) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(AnimationType<?> o) {
        return internalId - o.internalId;
    }
}
