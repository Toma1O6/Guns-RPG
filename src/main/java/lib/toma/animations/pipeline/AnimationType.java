package lib.toma.animations.pipeline;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class AnimationType<A extends IAnimation> {

    private static final Map<ResourceLocation, AnimationType<?>> TYPE_MAP = new HashMap<>();
    private static int indexOffset;
    private final ResourceLocation name;
    private final int index;
    private IAnimationCreator<A> creator;

    public AnimationType(ResourceLocation name, IAnimationCreator<A> creator) {
        this.name = name;
        this.index = indexOffset++;
        this.creator = creator;

        TYPE_MAP.put(name, this);
    }

    public AnimationType(ResourceLocation name) {
        this(name, null);
    }

    @SuppressWarnings("unchecked")
    public static <A extends IAnimation> AnimationType<A> getTypeFromID(ResourceLocation key) {
        return (AnimationType<A>) TYPE_MAP.get(key);
    }

    public static Collection<AnimationType<?>> values() {
        return TYPE_MAP.values();
    }

    public void setCreator(IAnimationCreator<A> creator) {
        this.creator = creator;
    }

    public boolean hasCreator() {
        return creator != null;
    }

    public A create(PlayerEntity client) {
        return creator.create(client);
    }

    public int getIndex() {
        return index;
    }

    public ResourceLocation getName() {
        return name;
    }

    @Override
    public String toString() {
        return "AnimationType{" +
                "name=" + name +
                ", index=" + index +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimationType<?> that = (AnimationType<?>) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return index;
    }
}
