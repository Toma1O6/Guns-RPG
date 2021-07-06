package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.impl.ConfigurableAnimation;
import dev.toma.gunsrpg.client.animation.impl.SimpleAnimation;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AnimationProcessor {

    private final Map<String, List<Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>>>> scriptAnimations = new HashMap<>();
    private final Map<Integer, IAnimation> animations = new HashMap<>();
    private boolean renderingDualWield = false;

    public boolean isRenderingDualWield() {
        return renderingDualWield;
    }

    public void setDualWieldRender(boolean render) {
        this.renderingDualWield = render;
    }

    public void registerScriptAnimation(String key, List<Pair<MultiStepAnimation.Range, Supplier<SimpleAnimation>>> animation) {
        scriptAnimations.put(key, animation);
    }

    public ConfigurableAnimation configurableFromFile(int time, String file) {
        ConfigurableAnimation.ILoader loader = animationSteps -> loadAnimationFromFile(file, animationSteps);
        return new ConfigurableAnimation(time, loader);
    }

    public void play(int ID, IAnimation animation) {
        if(animation == null) return;
        animations.put(ID, animation);
    }

    public void stop(int ID) {
        animations.remove(ID);
    }

    public IAnimation getByID(int ID) {
        return animations.get(ID);
    }

    public boolean blocksItemRender() {
        for(IAnimation animation : animations.values()) {
            if(animation.cancelsItemRender()) {
                return true;
            }
        }
        return false;
    }

    public void processItemAndHands(MatrixStack matrix, float partialTicks) {
        animations.forEach((id, animation) -> animation.animateItemHands(matrix, partialTicks));
    }

    public void processHands(MatrixStack matrix, float partialTicks) {
        animations.forEach((id, animation) -> animation.animateHands(matrix, partialTicks));
    }

    public void processRightHand(MatrixStack matrix, float partialTicks) {
        animations.forEach((id, animation) -> animation.animateRightArm(matrix, partialTicks));
    }

    public void processLeftHand(MatrixStack matrix, float partialTicks) {
        animations.forEach((id, animation) -> animation.animateLeftArm(matrix, partialTicks));
    }

    public void processItem(MatrixStack matrix, float partialTicks) {
        animations.forEach((id, animation) -> animation.animateItem(matrix, partialTicks));
    }

    public void processFrame(float partialTicks) {
        animations.forEach((id, animation) -> animation.frameTick(partialTicks));
    }

    public void tick() {
        if(Minecraft.getInstance().isPaused()) return;
        Iterator<Map.Entry<Integer, IAnimation>> iterator = animations.entrySet().iterator();
        while (iterator.hasNext()) {
            IAnimation animation = iterator.next().getValue();
            animation.clientTick();
            if(animation.isFinished()) {
                iterator.remove();
            }
        }
    }

    private void loadAnimationFromFile(String filename, List<Pair<MultiStepAnimation.Range, IAnimation>> steps) {

    }
}
