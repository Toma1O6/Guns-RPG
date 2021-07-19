package dev.toma.gunsrpg.client.animation;

import dev.toma.gunsrpg.client.animation.impl.SimpleAnimation;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.sided.ClientSideManager;

import static net.minecraft.util.math.vector.Vector3f.*;

public class Animations {

    public static final int AIMING = 0x0;
    public static final int RECOIL = 0x1;
    public static final int SPRINT = 0x2;
    public static final int RELOAD = 0x3;
    public static final int REBOLT = 0x4;
    public static final int HEAL = 0x5;
    public static final int FIREMODE = 0x6;

    public static class SwitchFiremode extends MultiStepAnimation {
        public SwitchFiremode(int length) {
            super(length);
            this.init();
        }

        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.5F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.translate(0.05F * f, -0.05F * f, 0.0F)).create());
            addStep(0.5F, 1.0F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.translate(0.05F - 0.05F * f, -0.05F + 0.05F * f, 0.0F)).create());
        }
    }

    public static class ReboltSR extends MultiStepAnimation {
        public ReboltSR(int time) {
            super(time);
            this.init();
        }

        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.2F, SimpleAnimation.newSimpleAnimation().create());
            addStep(0.2F, 0.3F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(5.0f * f))).create());
            addStep(0.3F, 0.5F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(5.0F))).rightHand((stack, f) -> stack.translate(0.1F * f, 0.1f * f, 0.2F * f)).create());
            addStep(0.5F, 0.6F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(5.0F))).rightHand((stack, f) -> stack.translate(0.1F, 0.1f, 0.2F)).create());
            addStep(0.6F, 0.7F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(5.0F))).rightHand((stack, f) -> stack.translate(0.1F - 0.05F * f, 0.1f + 0.1F * f, 0.2F)).create());
            addStep(0.7F, 0.8F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(5.0F))).rightHand((stack, f) -> stack.translate(0.05F, 0.2F, 0.2F + 0.1F * f)).create());
            addStep(0.8F, 0.9F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(5.0F))).rightHand((stack, f) -> stack.translate(0.05F, 0.2F, 0.3F - 0.1F * f)).create());
            addStep(0.9F, 1.0F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(5.0F - 5.0F * f))).rightHand((stack, f) -> stack.translate(0.05F - 0.05F * f, 0.2F - 0.2F * f, 0.2F - 0.2F * f)).create());
        }
    }

    public static class ReboltSG extends MultiStepAnimation {
        public ReboltSG(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.2F, SimpleAnimation.newSimpleAnimation().create());
            addStep(0.2F, 0.4F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(5.0F * f))).create());
            addStep(0.4F, 0.6F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(5.0F))).leftHand((stack, f) -> stack.translate(0.0F, 0.0F, 0.2F * f)).create());
            addStep(0.6F, 0.8F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(5.0F))).leftHand((stack, f) -> stack.translate(0.0F, 0.0F, 0.2F - 0.2F * f)).create());
            addStep(0.0F, 1.0F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(5.0F - 5.0F * f))).create());
        }
    }

    public static class PistolReload extends MultiStepAnimation {
        public PistolReload(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.1F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(15 * f))).create());
            addStep(0.1F, 0.2F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(15))).leftHand((stack, f) -> stack.mulPose(XP.rotationDegrees(-60))).create());
            addStep(0.2F, 0.7F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(15))).leftHand((stack, f) -> stack.mulPose(XP.rotationDegrees(-60))).create());
            addStep(0.7F, 0.8F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(15))).leftHand((stack, f) -> stack.mulPose(XP.rotationDegrees(-60 + 60 * f))).create());
            addStep(0.8F, 0.85F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(15 + 5 * f))).create());
            addStep(0.85F, 1.0F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(20 - 20 * f))).create());
        }
    }

    public static class SmgReload extends MultiStepAnimation {
        public SmgReload(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {

        }
    }

    public static class ShotgunReload extends MultiStepAnimation {
        public ShotgunReload(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {

        }
    }

    public static class ArReload extends MultiStepAnimation {
        public ArReload(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {

        }
    }

    public static class SniperReload extends MultiStepAnimation {
        public SniperReload(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {

        }
    }

    public static class ReloadDual extends MultiStepAnimation {
        public ReloadDual(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {
            AnimationProcessor processor = ClientSideManager.instance().processor();
            addStep(0.0F, 0.05F, SimpleAnimation.newSimpleAnimation().item((stack, f) -> {
                if (processor.isRenderingDualWield()) {
                    stack.mulPose(XP.rotationDegrees(-90.0F * f));
                }
            }).leftHand((stack, f) -> stack.mulPose(XP.rotationDegrees(-90.0F * f))).create());
            addStep(0.05F, 0.1F, SimpleAnimation.newSimpleAnimation().item((stack, f) -> {
                if (processor.isRenderingDualWield()) {
                    stack.mulPose(XP.rotationDegrees(-90.0F));
                }
            }).leftHand((stack, f) -> {
                stack.translate(0.25F * f, 0.0F, 0.1F * f);
                stack.mulPose(XP.rotationDegrees(-90.0F + 90.0F * f));
                stack.mulPose(YP.rotationDegrees(-45.0F * f));
            }).create());
            addStep(0.1F, 0.2F, SimpleAnimation.newSimpleAnimation().item((stack, f) -> {
                if (processor.isRenderingDualWield()) {
                    stack.mulPose(XP.rotationDegrees(-90.0F));
                }
            }).leftHand((stack, f) -> {
                stack.translate(0.25F, 0.0F, 0.1F);
                stack.mulPose(YP.rotationDegrees(-45.0F));
            }).itemHand((stack, f) -> {
                stack.translate(0.0F, 0.0F, 0.1F * f);
                stack.mulPose(XP.rotationDegrees(10.0F * f));
            }).create());
            addStep(0.2F, 0.35F, SimpleAnimation.newSimpleAnimation().item((stack, f) -> {
                if (processor.isRenderingDualWield()) {
                    stack.mulPose(XP.rotationDegrees(-90.0F));
                }
            }).itemHand((stack, f) -> {
                stack.translate(0.0F, 0.0F, 0.1F);
                stack.mulPose(XP.rotationDegrees(10.0F));
            }).leftHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(-60.0F * f));
                stack.translate(0.25F, 0.0F, 0.1F);
                stack.mulPose(YP.rotationDegrees(-45.0F));
            }).create());
            addStep(0.45F, 0.55F, SimpleAnimation.newSimpleAnimation().item((stack, f) -> {
                if (processor.isRenderingDualWield()) {
                    stack.mulPose(XP.rotationDegrees(-90.0F));
                }
            }).itemHand((stack, f) -> {
                stack.translate(0.0F, 0.0F, 0.1F);
                stack.mulPose(XP.rotationDegrees(10.0F));
            }).leftHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(-60.0F + 60.0F * f));
                stack.translate(0.25F, 0.0F, 0.1F);
                stack.mulPose(YP.rotationDegrees(-45));
            }).create());
            addStep(0.55F, 0.6F, SimpleAnimation.newSimpleAnimation().item((stack, f) -> {
                if (processor.isRenderingDualWield()) {
                    stack.mulPose(XP.rotationDegrees(-90));
                }
            }).itemHand((stack, f) -> {
                stack.translate(0.0F, 0.0F, 0.1F);
                stack.mulPose(XP.rotationDegrees(10 - 90 * f));
            }).leftHand((stack, f) -> {
                stack.translate(0.25F, 0.0F, 0.1F);
                stack.mulPose(YP.rotationDegrees(-45.0f));
            }).create());
            addStep(0.6F, 0.7F, SimpleAnimation.newSimpleAnimation().item((stack, f) -> {
                if (processor.isRenderingDualWield()) {
                    stack.mulPose(XP.rotationDegrees(-90));
                }
            }).itemHand((stack, f) -> {
                stack.translate(0.0F, 0.0F, 0.1F);
                stack.mulPose(XP.rotationDegrees(-80 + 90 * f));
            }).leftHand((stack, f) -> {
                stack.translate(0.25F, 0.0F, 0.1F);
                stack.mulPose(XP.rotationDegrees(-90 * f));
                stack.mulPose(YP.rotationDegrees(-45));
            }).create());
            addStep(0.7F, 0.8F, SimpleAnimation.newSimpleAnimation().item((stack, f) -> {
                if (processor.isRenderingDualWield()) {
                    stack.mulPose(XP.rotationDegrees(-90));
                }
            }).itemHand((stack, f) -> {
                stack.translate(0.0F, 0.0F, 0.1F);
                stack.mulPose(XP.rotationDegrees(10));
            }).leftHand((stack, f) -> {
                stack.translate(0.25F, 0.0F, 0.1F);
                stack.mulPose(XP.rotationDegrees(-90 + 90 * f));
                stack.mulPose(YP.rotationDegrees(-45));
            }).create());
            addStep(0.8F, 0.9F, SimpleAnimation.newSimpleAnimation().item((stack, f) -> {
                if (processor.isRenderingDualWield()) {
                    stack.mulPose(XP.rotationDegrees(-90));
                }
            }).itemHand((stack, f) -> {
                stack.translate(0.0F, 0.0F, 0.1F);
                stack.mulPose(XP.rotationDegrees(10 - 10 * f));
            }).leftHand((stack, f) -> {
                stack.translate(0.25F - 0.25F * f, 0.0F, 0.1F - 0.1F * f);
                stack.mulPose(XP.rotationDegrees(-90 * f));
                stack.mulPose(YP.rotationDegrees(-45 + 45 * f));
            }).create());
            addStep(0.9F, 1.0F, SimpleAnimation.newSimpleAnimation().item((stack, f) -> {
                if (processor.isRenderingDualWield()) {
                    stack.mulPose(XP.rotationDegrees(-90 + 90 * f));
                }
            }).leftHand((stack, f) -> stack.mulPose(XP.rotationDegrees(-90 + 90 * f))).create());
        }

        @Override
        public void onStepChanged() {
            if (index == 4) {
                this.getPlayer().playSound(ModItems.PISTOL.getReloadSound(this.getPlayer()), 1.0F, 1.0F);
            }
        }
    }

    public static class ReloadCrossbow extends MultiStepAnimation {

        public ReloadCrossbow(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {
            addStep(0F, 0.2F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(-10 * f))).leftHand((stack, f) -> {
                stack.translate(0.0F, 0.0F, +0.4F * f);
                stack.mulPose(YP.rotationDegrees(30 * f));
            }).create());
            addStep(0.2F, 0.5F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(-10))).leftHand((stack, f) -> {
                stack.translate(0.0F, +0.05F * f, 0.4F - 0.3F * f);
                stack.mulPose(YP.rotationDegrees(30 - 20 * f));
                stack.mulPose(XP.rotationDegrees(10 * f));
            }).create());
            addStep(0.5F, 0.8F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(-10))).leftHand((stack, f) -> {
                stack.translate(-0.3F * f, 0.05F, 0.1F + 0.2F * f);
                stack.mulPose(YP.rotationDegrees(10 - 20 * f));
                stack.mulPose(XP.rotationDegrees(10));
            }).create());
            addStep(0.8F, 1F, SimpleAnimation.newSimpleAnimation().itemHand((stack, f) -> stack.mulPose(XP.rotationDegrees(-10 + 10 * f))).leftHand((stack, f) -> {
                stack.translate(-0.3F + 0.3F * f, 0.05F + 0.05F * f, 0.3F - 0.3F * f);
                stack.mulPose(YP.rotationDegrees(-10 + 10 * f));
                stack.mulPose(XP.rotationDegrees(10 - 10 * f));
            }).create());
        }
    }

    public static class Pills extends MultiStepAnimation {

        public Pills(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.5F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.mulPose(YP.rotationDegrees(30 * f));
                stack.translate(0.3F, 0.0F, 0.3F);
                stack.mulPose(XP.rotationDegrees(80));
            }).leftHand((stack, f) -> {
                stack.translate(-0.2F * f, -0.1F * f, 0.1F * f);
                stack.mulPose(YP.rotationDegrees(-30 * f));
                stack.mulPose(XP.rotationDegrees(80 * f));
            }).item((stack, f) -> {
                stack.translate(-0.2F * f, -0.3F * f, 0.0F);
                stack.mulPose(ZP.rotationDegrees(60 * f));
                stack.mulPose(YP.rotationDegrees(30 * f));
            }).create());
            addStep(0.5F, 0.75F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.mulPose(YP.rotationDegrees(30 - 30 * f));
                stack.translate(0.3F, 0.0F, 0.3F);
                stack.mulPose(XP.rotationDegrees(80));
            }).leftHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(60 * f));
                stack.translate(0.0F, 0.0F, 1.0F * f);
                stack.translate(-0.2F, -0.1F, 0.1F);
                stack.mulPose(YP.rotationDegrees(-30));
                stack.mulPose(XP.rotationDegrees(80));
            }).item((stack, f) -> {
                stack.translate(-0.2F + 0.2F * f, -0.3F + 0.3F * f, 0.0F);
                stack.mulPose(ZP.rotationDegrees(60 - 60 * f));
                stack.mulPose(YP.rotationDegrees(30 - 30 * f));
            }).create());
            addStep(0.75F, 1.0F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.translate(0.3F, 0.0F - 0.2F * f, 0.3F + 0.3F * f);
                stack.mulPose(XP.rotationDegrees(80));
            }).leftHand((stack, f) -> {
                stack.translate(0.0F, -0.4F * f, 0.0F);
                stack.mulPose(XP.rotationDegrees(60 - 60 * f));
                stack.translate(0.0F, 0.0F, 1.0F - 1.0F * f);
                stack.translate(-0.2F + 0.2F * f, -0.1F + 0.1F * f, 0.1F - 0.1F * f);
                stack.mulPose(YP.rotationDegrees(-30));
                stack.mulPose(XP.rotationDegrees(80));
            }).create());
        }
    }

    public static class Injection extends MultiStepAnimation {

        public Injection(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.3F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.translate(0.5f * f, 0.0F, 0.25f * f);
                stack.mulPose(YP.rotationDegrees(70 * f));
                stack.mulPose(XP.rotationDegrees(80));
            }).create());
            addStep(0.3F, 0.8F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.translate(0.5F, 0.0F, 0.25F);
                stack.mulPose(YP.rotationDegrees(70));
                stack.mulPose(XP.rotationDegrees(80));
            }).create());
            addStep(0.8F, 1.0F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.translate(0.5F - 0.5F * f, 0.0F, 0.25F - 0.25F * f);
                stack.mulPose(YP.rotationDegrees(70 - 70 * f));
                stack.mulPose(XP.rotationDegrees(80 - 30 * f));
            }).create());
        }

        @Override
        public boolean cancelsItemRender() {
            return true;
        }
    }

    public static class Bandage extends MultiStepAnimation {

        public Bandage(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.10F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.translate(0.5F * f, 0.15F * f, 0.1F * f);
                stack.mulPose(YP.rotationDegrees(45 * f));
                stack.mulPose(XP.rotationDegrees(80 * f));
            }).leftHand((stack, f) -> {
                stack.translate(-0.5F * f, -0.15F * f, 0.0F);
                stack.mulPose(YP.rotationDegrees(-60 * f));
                stack.mulPose(XP.rotationDegrees(80 * f));
            }).create());
            addStep(0.1F, 0.2F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(-15 * f));
                stack.mulPose(YP.rotationDegrees(-15 * f));
                stack.translate(0.5F, 0.15F, 0.1F);
                stack.mulPose(YP.rotationDegrees(45));
                stack.mulPose(XP.rotationDegrees(80));
            }).leftHand((stack, f) -> {
                stack.translate(-0.5F, -0.15F, 0.0F);
                stack.mulPose(YP.rotationDegrees(-60));
                stack.mulPose(XP.rotationDegrees(80));
            }).create());
            addStep(0.2F, 0.3F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(-15 - 15 * f));
                stack.mulPose(YP.rotationDegrees(-15 + 15 * f));
                stack.translate(0.5F, 0.15F, 0.1F);
                stack.mulPose(YP.rotationDegrees(45));
                stack.mulPose(XP.rotationDegrees(80));
            }).leftHand((stack, f) -> {
                stack.translate(-0.5F, -0.15F, 0.0F);
                stack.mulPose(YP.rotationDegrees(-60));
                stack.mulPose(XP.rotationDegrees(80));
            }).create());
            addStep(0.3F, 0.4F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(-30 + 15 * f));
                stack.mulPose(YP.rotationDegrees(15 * f));
                stack.translate(0.5F, 0.15F, 0.1F);
                stack.mulPose(YP.rotationDegrees(45));
                stack.mulPose(XP.rotationDegrees(80));
            }).leftHand((stack, f) -> {
                stack.translate(-0.5F, -0.15F, 0.0F);
                stack.mulPose(YP.rotationDegrees(-60));
                stack.mulPose(XP.rotationDegrees(80));
            }).create());
            addStep(0.4F, 0.5F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(-15 + 15 * f));
                stack.mulPose(YP.rotationDegrees(15 - 15 * f));
                stack.translate(0.5F, 0.15F, 0.1F);
                stack.mulPose(YP.rotationDegrees(45));
                stack.mulPose(XP.rotationDegrees(80));
            }).leftHand((stack, f) -> {
                stack.translate(-0.5F, -0.15F, 0.0F);
                stack.mulPose(YP.rotationDegrees(-60));
                stack.mulPose(XP.rotationDegrees(80));
            }).create());
            addStep(0.5F, 0.6F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(-15 * f));
                stack.mulPose(YP.rotationDegrees(-15 * f));
                stack.translate(0.5F, 0.15F, 0.1F);
                stack.mulPose(YP.rotationDegrees(45));
                stack.mulPose(XP.rotationDegrees(80));
            }).leftHand((stack, f) -> {
                stack.translate(-0.5F, -0.15F, 0.0F);
                stack.mulPose(YP.rotationDegrees(-60));
                stack.mulPose(XP.rotationDegrees(80));
            }).create());
            addStep(0.6F, 0.7F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(-15 - 15 * f));
                stack.mulPose(YP.rotationDegrees(-15 + 15 * f));
                stack.translate(0.5F, 0.15F, 0.1F);
                stack.mulPose(YP.rotationDegrees(45));
                stack.mulPose(XP.rotationDegrees(80));
            }).leftHand((stack, f) -> {
                stack.translate(-0.5F, -0.15F, 0.0F);
                stack.mulPose(YP.rotationDegrees(-60));
                stack.mulPose(XP.rotationDegrees(80));
            }).create());
            addStep(0.7F, 0.8F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(-30 + 15 * f));
                stack.mulPose(YP.rotationDegrees(15 * f));
                stack.translate(0.5F, 0.15F, 0.1F);
                stack.mulPose(YP.rotationDegrees(45));
                stack.mulPose(XP.rotationDegrees(80));
            }).leftHand((stack, f) -> {
                stack.translate(-0.5F, -0.15F, 0.0F);
                stack.mulPose(YP.rotationDegrees(-60));
                stack.mulPose(XP.rotationDegrees(80));
            }).create());
            addStep(0.8F, 0.9F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(-15 + 15 * f));
                stack.mulPose(YP.rotationDegrees(15 - 15 * f));
                stack.translate(0.5F, 0.15F, 0.1F);
                stack.mulPose(YP.rotationDegrees(45));
                stack.mulPose(XP.rotationDegrees(80));
            }).leftHand((stack, f) -> {
                stack.translate(-0.5F, -0.15F, 0.0F);
                stack.mulPose(YP.rotationDegrees(-60));
                stack.mulPose(XP.rotationDegrees(80));
            }).create());
            addStep(0.9F, 1.0F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.translate(0.5F - 0.5F * f, 0.15F - 0.15F * f, 0.1F - 0.1F * f);
                stack.mulPose(YP.rotationDegrees(45 - 45 * f));
                stack.mulPose(XP.rotationDegrees(80 - 80 * f));
            }).leftHand((stack, f) -> {
                stack.translate(-0.5F + 0.5F, -0.15F + 0.15F, 0.0F);
                stack.mulPose(YP.rotationDegrees(-60 + 60 * f));
                stack.mulPose(XP.rotationDegrees(80 - 80 * f));
            }).create());
        }

        @Override
        public boolean cancelsItemRender() {
            return true;
        }
    }

    public static class Splint extends MultiStepAnimation {

        public Splint(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.35F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.translate(0.2F, 0.0F, 0.5F);
                stack.mulPose(XP.rotationDegrees(80));
            }).itemHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(-10 * f));
                stack.mulPose(YP.rotationDegrees(30 * f));
            }).create());
            addStep(0.35F, 1.0F, SimpleAnimation.newSimpleAnimation().rightHand((stack, f) -> {
                stack.translate(0.2F, 0.0F, 0.5F);
                stack.mulPose(XP.rotationDegrees(80));
            }).itemHand((stack, f) -> {
                stack.mulPose(XP.rotationDegrees(-10 - 50 * f));
                stack.mulPose(YP.rotationDegrees(30 + 30 * f));
            }).create());
        }
    }
}
