package dev.toma.gunsrpg.client.animation;

import dev.toma.gunsrpg.util.object.OptionalObject;

public class AnimationManager {

    private static final OptionalObject<Animation> currentAnimation = OptionalObject.empty();

    public static void sendNewAnimation(Animation animation) {
        currentAnimation.put(animation);
    }

    public static void animateItemHands(float partialTicks) {
        currentAnimation.ifPresent(an -> an.animateItemHands(partialTicks));
    }

    public static void animateHands(float partialTicks) {
        currentAnimation.ifPresent(an -> an.animateHands(partialTicks));
    }

    public static void animateRightArm(float partialTicks) {
        currentAnimation.ifPresent(an -> an.animateRightArm(partialTicks));
    }

    public static void animateLeftArm(float partialTicks) {
        currentAnimation.ifPresent(an -> an.animateLeftArm(partialTicks));
    }

    public static void animateItem(float partialTicks) {
        currentAnimation.ifPresent(an -> an.animateItem(partialTicks));
    }

    public static void tick() {
        currentAnimation.ifPresent(Animation::clientTick);
    }
}
