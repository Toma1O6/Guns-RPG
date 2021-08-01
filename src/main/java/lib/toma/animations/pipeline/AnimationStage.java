package lib.toma.animations.pipeline;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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

    private static final Map<ResourceLocation, AnimationStage> ID_STAGE_MAP = new IdentityHashMap<>();
    private static final Set<AnimationStage> VANILLA_TYPES = new HashSet<>();
    private static int indexOffset;

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

    private static AnimationStage vanilla(String name) {
        AnimationStage stage = new AnimationStage(new ResourceLocation(name));
        VANILLA_TYPES.add(stage);
        return stage;
    }
}
