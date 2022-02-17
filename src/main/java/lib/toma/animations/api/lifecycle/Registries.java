package lib.toma.animations.api.lifecycle;

import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.AnimationType;
import lib.toma.animations.api.event.AnimationEventType;
import lib.toma.animations.engine.Registry;
import lib.toma.animations.engine.screen.animator.Animator;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 * Collection of common registries
 */
public class Registries {

    public static final IRegistry<AnimationType<?>> ANIMATION_TYPES = new Registry.RegistryBuilder<AnimationType<?>>().map(new TreeMap<>(ResourceLocation::compareNamespaced)).vanillaListener(Registries::registerAnimationTypes).buildRegistry();
    public static final IRegistry<AnimationStage> ANIMATION_STAGES = new Registry.RegistryBuilder<AnimationStage>().map(new LinkedHashMap<>()).vanillaListener(devMode -> registerAnimationStages()).buildRegistry();
    public static final IRegistry<AnimationEventType<?>> EVENTS = new Registry.RegistryBuilder<AnimationEventType<?>>().map(new TreeMap<>(ResourceLocation::compareNamespaced)).vanillaListener(devMode -> registerEvents()).buildRegistry();

    // Vanilla registration =============================
    private static AnimationType<?>[] registerAnimationTypes(boolean devMode) {
        if (devMode)
            return new AnimationType[]{Animator.ANIMATOR_TYPE};
        else return new AnimationType[0];
    }

    private static AnimationStage[] registerAnimationStages() {
        return new AnimationStage[]{
                AnimationStage.HELD_ITEM,
                AnimationStage.RIGHT_HAND,
                AnimationStage.LEFT_HAND,
                AnimationStage.HANDS,
                AnimationStage.ITEM_AND_HANDS
        };
    }

    private static AnimationEventType<?>[] registerEvents() {
        return new AnimationEventType<?>[]{
                AnimationEventType.SOUND,
                AnimationEventType.PLAY_ANIMATION,
                AnimationEventType.STOP_ANIMATION
        };
    }
}
