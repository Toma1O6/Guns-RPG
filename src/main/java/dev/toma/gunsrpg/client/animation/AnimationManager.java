package dev.toma.gunsrpg.client.animation;

import dev.toma.gunsrpg.util.object.OptionalObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AnimationManager {

    private static final Map<Integer, Animation> ANIMATIONS = new HashMap<>();
    private static final OptionalObject<Animation> currentAnimation = OptionalObject.empty();

    public static void sendNewAnimation(int ID, Animation animation) {
        ANIMATIONS.put(ID, animation);
    }

    public static void animateItemHands(float partialTicks) {
        ANIMATIONS.forEach((id, animation) -> animation.animateItemHands(partialTicks));
    }

    public static void animateHands(float partialTicks) {
        ANIMATIONS.forEach((id, animation) -> animation.animateHands(partialTicks));
    }

    public static void animateRightArm(float partialTicks) {
        ANIMATIONS.forEach((id, animation) -> animation.animateRightArm(partialTicks));
    }

    public static void animateLeftArm(float partialTicks) {
        ANIMATIONS.forEach((id, animation) -> animation.animateLeftArm(partialTicks));
    }

    public static void animateItem(float partialTicks) {
        ANIMATIONS.forEach((id, animation) -> animation.animateItem(partialTicks));
    }

    public static void tick() {
        Iterator<Map.Entry<Integer, Animation>> iterator = ANIMATIONS.entrySet().iterator();
        while (iterator.hasNext()) {
            Animation animation = iterator.next().getValue();
            animation.clientTick();
            if(animation.isFinished()) {
                iterator.remove();
            }
        }
    }
}
