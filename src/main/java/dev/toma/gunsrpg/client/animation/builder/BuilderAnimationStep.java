package dev.toma.gunsrpg.client.animation.builder;

import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class BuilderAnimationStep implements Animation {

    public float length = 1.0F;
    public Map<BuilderData.Part, Data> map = new HashMap<>();
    protected float current, prev, smooth;

    public BuilderAnimationStep(BuilderAnimationStep other) {
        for(BuilderData.Part part : BuilderData.Part.values()) {
            Data prev = other.map.get(part);
            map.put(part, new Data(prev));
        }
    }

    public BuilderAnimationStep() {
        for(BuilderData.Part part : BuilderData.Part.values()) {
            map.put(part, new Data());
        }
    }

    public BuilderAnimationStep resetState() {
        this.current = 0.0F;
        this.prev = 0.0F;
        this.smooth = 0.0F;
        return this;
    }

    public Animation asTempAnimation() {
        int i = (int)(BuilderData.animationLength * length);
        MultiStepAnimation animation = new MultiStepAnimation(i) {
            @Override
            public void createAnimationSteps() {
                addStep(0.0F, 1.0F, BuilderAnimationStep.this.resetState());
            }
        };
        animation.init();
        return animation;
    }

    public int getIntValue() {
        return (int)(length * 100.0F);
    }

    public void updateValue(float value) {
        map.get(BuilderData.part).updateValue(BuilderData.context, BuilderData.axis, value);
    }

    @Override
    public void animateItemHands(float partialTicks) {
        map.get(BuilderData.Part.ITEM_AND_HANDS).apply(smooth);
    }

    @Override
    public void animateHands(float partialTicks) {
        map.get(BuilderData.Part.HANDS).apply(smooth);
    }

    @Override
    public void animateRightArm(float partialTicks) {
        map.get(BuilderData.Part.RIGHT_HAND).apply(smooth);
    }

    @Override
    public void animateLeftArm(float partialTicks) {
        map.get(BuilderData.Part.LEFT_HAND).apply(smooth);
    }

    @Override
    public void animateItem(float partialTicks) {
        map.get(BuilderData.Part.ITEM).apply(smooth);
    }

    @Override
    public void clientTick() {
        this.prev = this.current;
    }

    @Override
    public void renderTick(float partialTicks, TickEvent.Phase phase) {
        if(phase == TickEvent.Phase.START) this.interpolate(partialTicks);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean cancelsItemRender() {
        return false;
    }

    @Override
    public void setProgress(float progress) {
        this.current = progress;
    }

    private void interpolate(float partialTicks) {
        this.smooth = this.prev + (this.current - this.prev) * partialTicks;
    }

    public static class Data {

        public TranslationContext translationContext;
        public RotationContext rotationContext;

        public Data() {
            this.translationContext = new TranslationContext();
            this.rotationContext = new RotationContext();
        }

        public Data(Data other) {
            this.translationContext = new TranslationContext(other.translationContext);
            this.rotationContext = new RotationContext(other.rotationContext);
        }

        public void updateValue(BuilderData.Context context, BuilderData.Axis axis, float value) {
            if(context == BuilderData.Context.TRANSLATION) {
                translationContext.updateValue(axis, value);
            } else rotationContext.updateValue(axis, value);
        }

        public void apply(float smooth) {
            translationContext.apply(smooth);
            rotationContext.apply(smooth);
        }

        public boolean isEmpty() {
            return translationContext.isEmpty() && rotationContext.isEmpty();
        }
    }

    public static class TranslationContext implements Context {

        public final float baseX, baseY, baseZ;
        public float newX, newY, newZ;

        public TranslationContext() {
            this(0.0F, 0.0F, 0.0F);
        }

        public TranslationContext(TranslationContext ctx) {
            this(ctx.baseX + ctx.newX, ctx.baseY + ctx.newY, ctx.baseZ + ctx.newZ);
        }

        public TranslationContext(float baseX, float baseY, float baseZ) {
            this.baseX = baseX;
            this.baseY = baseY;
            this.baseZ = baseZ;
        }

        @Override
        public void updateValue(BuilderData.Axis axis, float value) {
            switch (axis) {
                case X: newX += value; break;
                case Y: newY += value; break;
                case Z: newZ += value; break;
            }
        }

        @Override
        public void apply(float smooth) {
            GlStateManager.translate(baseX + newX * smooth, baseY + newY * smooth, baseZ + newZ * smooth);
        }

        @Override
        public boolean isEmpty() {
            return baseX == 0 && baseY == 0 && baseZ == 0 && newX == 0 && newY == 0 && newZ == 0;
        }
    }

    public static class RotationContext implements Context {

        public Map<BuilderData.Axis, Pair<Float, Float>> rotations = new HashMap<>();

        public RotationContext() {

        }

        @SuppressWarnings("SuspiciousNameCombination")
        public RotationContext(float rotX, float rotY, float rotZ) {
            if(rotX != 0) rotations.put(BuilderData.Axis.X, Pair.of(rotX, 0.0F));
            if(rotY != 0) rotations.put(BuilderData.Axis.Y, Pair.of(rotY, 0.0F));
            if(rotZ != 0) rotations.put(BuilderData.Axis.Z, Pair.of(rotZ, 0.0F));
        }

        public RotationContext(RotationContext ctx) {
            Pair<Float, Float> x = ctx.rotations.get(BuilderData.Axis.X);
            if(x != null) {
                rotations.put(BuilderData.Axis.X, Pair.of(this.sum(x), 0.0F));
            }
            Pair<Float, Float> y = ctx.rotations.get(BuilderData.Axis.Y);
            if(y != null) {
                rotations.put(BuilderData.Axis.Y, Pair.of(this.sum(y), 0.0F));
            }
            Pair<Float, Float> z = ctx.rotations.get(BuilderData.Axis.Z);
            if(z != null) {
                rotations.put(BuilderData.Axis.Z, Pair.of(this.sum(z), 0.0F));
            }
        }

        @Override
        public void updateValue(BuilderData.Axis axis, float f) {
            Pair<Float, Float> pair = rotations.computeIfAbsent(axis, k -> Pair.of(0.0F, f));
            pair.setRight(pair.getRight() + f);
            if(pair.getLeft() == 0.0F && pair.getRight() == 0.0F) rotations.remove(axis);
        }

        @Override
        public void apply(float smooth) {
            Pair<Float, Float> v = rotations.get(BuilderData.Axis.Y);
            if(v != null) {
                GlStateManager.rotate(v.getLeft() + v.getRight() * smooth, 0.0F, 1.0F, 0.0F);
            }
            v = rotations.get(BuilderData.Axis.X);
            if(v != null) {
                GlStateManager.rotate(v.getLeft() + v.getRight() * smooth, 1.0F, 0.0F, 0.0F);
            }
            v = rotations.get(BuilderData.Axis.Z);
            if(v != null) {
                GlStateManager.rotate(v.getLeft() + v.getRight() * smooth, 0.0F, 0.0F, 1.0F);
            }
        }

        @Override
        public boolean isEmpty() {
            return rotations.isEmpty();
        }

        private float sum(Pair<Float, Float> pair) {
            return pair.getLeft() + pair.getRight();
        }
    }

    public interface Context {

        void apply(float smooth);

        void updateValue(BuilderData.Axis axis, float value);

        boolean isEmpty();
    }
}
