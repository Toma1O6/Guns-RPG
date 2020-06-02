package dev.toma.gunsrpg.client.animation;

import dev.toma.gunsrpg.client.animation.impl.SimpleAnimation;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AnimationManager {

    public static final Map<String, List<Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>>>> SCRIPT_ANIMATIONS = new HashMap<>();
    private static final Map<Integer, Animation> ANIMATIONS = new HashMap<>();

    public static void sendNewAnimation(int ID, Animation animation) {
        ANIMATIONS.put(ID, animation);
    }

    public static void cancelAnimation(int ID) {
        ANIMATIONS.remove(ID);
    }

    public static Animation getAnimationByID(int ID) {
        return ANIMATIONS.get(ID);
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

    public static void renderTick(float partialTicks, TickEvent.Phase phase) {
        ANIMATIONS.forEach((id, animation) -> animation.renderTick(partialTicks, phase));
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
