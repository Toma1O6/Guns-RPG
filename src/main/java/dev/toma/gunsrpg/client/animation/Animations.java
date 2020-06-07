package dev.toma.gunsrpg.client.animation;

import dev.toma.gunsrpg.client.animation.impl.SimpleAnimation;
import net.minecraft.client.renderer.GlStateManager;

public class Animations {

    public static final int AIMING = 0x0;
    public static final int RECOIL = 0x1;
    public static final int SPRINT = 0x2;
    public static final int RELOAD = 0x3;
    public static final int REBOLT = 0x4;
    public static final int HEAL = 0x5;

    public static class ReboltSR extends MultiStepAnimation {

        public ReboltSR(int time) {
            super(time);
            this.init();
        }

        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.2F, SimpleAnimation.newSimpleAnimation().create());
            addStep(0.2F, 0.3F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.rotate(5.0F * f, 1.0F, 0.0F, 0.0F)).create());
            addStep(0.3F, 0.5F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F)).rightHand(f -> GlStateManager.translate(0.1F * f, 0.1f * f, 0.2F * f)).create());
            addStep(0.5F, 0.6F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F)).rightHand(f -> GlStateManager.translate(0.1F, 0.1f, 0.2F)).create());
            addStep(0.6F, 0.7F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F)).rightHand(f -> GlStateManager.translate(0.1F - 0.05F * f, 0.1f + 0.1F * f, 0.2F)).create());
            addStep(0.7F, 0.8F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F)).rightHand(f -> GlStateManager.translate(0.05F, 0.2F, 0.2F + 0.1F * f)).create());
            addStep(0.8F, 0.9F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F)).rightHand(f -> GlStateManager.translate(0.05F, 0.2F, 0.3F - 0.1F * f)).create());
            addStep(0.9F, 1.0F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.rotate(5.0F - 5.0F * f, 1.0F, 0.0F, 0.0F)).rightHand(f -> GlStateManager.translate(0.05F - 0.05F * f, 0.2F - 0.2F * f, 0.2F - 0.2F * f)).create());
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
            addStep(0.2F, 0.4F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.rotate(5.0F * f, 1.0F, 0.0F, 0.0F)).create());
            addStep(0.4F, 0.6F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F)).leftHand(f -> GlStateManager.translate(0.0F, 0.0F, 0.2F * f)).create());
            addStep(0.6F, 0.8F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F)).leftHand(f -> GlStateManager.translate(0.0F, 0.0F, 0.2F - 0.2F * f)).create());
            addStep(0.0F, 1.0F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.rotate(5.0F - 5.0F * f, 1.0F, 0.0F, 0.0F)).create());
        }
    }

    public static class Antidotum extends MultiStepAnimation {

        public Antidotum(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.5F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.rotate(30.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.3F, 0.0F, 0.3F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translate(-0.2F * f, -0.1F * f, 0.1F * f);
                GlStateManager.rotate(-30.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F * f, 1.0F, 0.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translate(-0.2F * f, -0.3F * f, 0.0F);
                GlStateManager.rotate(60.0F * f, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(30.0F * f, 0.0F, 1.0F, 0.0F);
            }).create());
            addStep(0.5F, 0.75F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.rotate(30.0F - 30.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.3F, 0.0F, 0.3F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.rotate(60.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.0F, 0.0F, 1.0F * f);
                GlStateManager.translate(-0.2F, -0.1F, 0.1F);
                GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translate(-0.2F + 0.2F * f, -0.3F + 0.3F * f, 0.0F);
                GlStateManager.rotate(60.0F - 60.0F * f, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(30.0F - 30.0F * f, 0.0F, 1.0F, 0.0F);
            }).create());
            addStep(0.75F, 1.0F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.translate(0.3F, 0.0F - 0.2F * f, 0.3F + 0.3F * f);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translate(0.0F, -0.4F * f, 0.0F);
                GlStateManager.rotate(60.0F - 60.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.0F, 0.0F, 1.0F - 1.0F * f);
                GlStateManager.translate(-0.2F + 0.2F * f, -0.1F + 0.1F * f, 0.1F - 0.1F * f);
                GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).create());
        }
    }

    public static class Vaccine extends MultiStepAnimation {

        public Vaccine(int time) {
            super(time);
            init();
        }

        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.3F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.translate(0.5f * f, 0.0F, 0.25f * f);
                GlStateManager.rotate(70.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.3F, 0.8F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.translate(0.5F, 0.0F, 0.25F);
                GlStateManager.rotate(70.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.8F, 1.0F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.translate(0.5F - 0.5F * f, 0.0F, 0.25F - 0.25F * f);
                GlStateManager.rotate(70.0F - 70.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F - 30.0F * f, 1.0F, 0.0F, 0.0F);
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

        /*
        15...20
        25...30
        35...40
        45...50

        55...65
        40...75
        45...85
        50...95
         */
        @Override
        public void createAnimationSteps() {
            addStep(0.0F, 0.10F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.translate(0.5F * f, 0.15F * f, 0.1F * f);
                GlStateManager.rotate(45.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F * f, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translate(-0.5F * f, -0.15F * f, 0.0F);
                GlStateManager.rotate(-60.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F * f, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.1F, 0.2F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.rotate(-15.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-15.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.5F, 0.15F, 0.1F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translate(-0.5F, -0.15F, 0.0F);
                GlStateManager.rotate(-60.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.2F, 0.3F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.rotate(-15.0F - 15.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-15.0F + 15.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.5F, 0.15F, 0.1F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translate(-0.5F, -0.15F, 0.0F);
                GlStateManager.rotate(-60.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.3F, 0.4F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.rotate(-30.0F + 15.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(15.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.5F, 0.15F, 0.1F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translate(-0.5F, -0.15F, 0.0F);
                GlStateManager.rotate(-60.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.4F, 0.5F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.rotate(-15.0F + 15.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(15.0F - 15.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.5F, 0.15F, 0.1F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translate(-0.5F, -0.15F, 0.0F);
                GlStateManager.rotate(-60.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.5F, 0.6F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.rotate(-15.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-15.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.5F, 0.15F, 0.1F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translate(-0.5F, -0.15F, 0.0F);
                GlStateManager.rotate(-60.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.6F, 0.7F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.rotate(-15.0F - 15.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-15.0F + 15.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.5F, 0.15F, 0.1F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translate(-0.5F, -0.15F, 0.0F);
                GlStateManager.rotate(-60.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.7F, 0.8F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.rotate(-30.0F + 15.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(15.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.5F, 0.15F, 0.1F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translate(-0.5F, -0.15F, 0.0F);
                GlStateManager.rotate(-60.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.8F, 0.9F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.rotate(-15.0F + 15.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(15.0F - 15.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.5F, 0.15F, 0.1F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translate(-0.5F, -0.15F, 0.0F);
                GlStateManager.rotate(-60.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.9F, 1.0F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.translate(0.5F - 0.5F * f, 0.15F - 0.15F * f, 0.1F - 0.1F * f);
                GlStateManager.rotate(45.0F - 45.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F - 80.0F * f, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translate(-0.5F + 0.5F, -0.15F + 0.15F, 0.0F);
                GlStateManager.rotate(-60.0F + 60.0F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(80.0F - 80.0F * f, 1.0F, 0.0F, 0.0F);
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
            addStep(0.0F, 0.35F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.translate(0.2F, 0.0F, 0.5F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).itemHand(f -> {
                GlStateManager.rotate(-10.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(30.0F * f, 0.0F, 1.0F, 0.0F);
            }).create());
            addStep(0.35F, 1.0F, SimpleAnimation.newSimpleAnimation().rightHand(f -> {
                GlStateManager.translate(0.2F, 0.0F, 0.5F);
                GlStateManager.rotate(80.0F, 1.0F, 0.0F, 0.0F);
            }).itemHand(f -> {
                GlStateManager.rotate(-10.0F - 50.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(30.0F + 30.0F * f, 0.0F, 1.0F, 0.0F);
            }).create());
        }
    }
}
