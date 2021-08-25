package lib.toma.animations.engine;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Objects;
import java.util.function.Function;

public final class MainRenderPipeline implements IRenderPipeline {

    private IAnimator animator = this::animate;
    private IHandAnimator handAnimator = this::animateHands;
    private IHandRenderer handRenderer = this::renderHand;
    private IItemRenderer itemRenderer = this::renderItem;
    private IAnimateCallback preAnimate = this::empty_callback;
    private IAnimateCallback postAnimate = this::empty_callback;

    @Override
    public void register(IEventBus bus) {
        bus.addListener(this::onHandRender);
    }

    @Override
    public void setAnimator(IAnimator animator) {
        this.animator = Objects.requireNonNull(animator);
    }

    @Override
    public void setHandAnimator(IHandAnimator animator) {
        this.handAnimator = Objects.requireNonNull(animator);
    }

    @Override
    public void setHandRenderer(IHandRenderer handRenderer) {
        this.handRenderer = Objects.requireNonNull(handRenderer);
    }

    @Override
    public void setItemRenderer(IItemRenderer itemRenderer) {
        this.itemRenderer = Objects.requireNonNull(itemRenderer);
    }

    @Override
    public void setPreAnimateCallback(IAnimateCallback callback) {
        this.preAnimate = Objects.requireNonNull(callback);
    }

    @Override
    public void setPostAnimateCallback(IAnimateCallback callback) {
        this.postAnimate = Objects.requireNonNull(callback);
    }

    @Override
    public IAnimator getAnimator() {
        return animator;
    }

    @Override
    public IHandAnimator getHandAnimator() {
        return handAnimator;
    }

    @Override
    public IHandRenderer getHandRenderer() {
        return handRenderer;
    }

    @Override
    public IItemRenderer getItemRenderer() {
        return itemRenderer;
    }

    private void onHandRender(RenderHandEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof IAnimationEntry) {
            AnimationEngine engine = AnimationEngine.get();
            IHandRenderAPI handSetup = engine.getHandRenderAPI();
            IAnimationEntry animatedRender = (IAnimationEntry) stack.getItem();
            IAnimationPipeline pipeline = engine.pipeline();
            boolean isDeveloperMode = handSetup.isDevMode();
            IHandTransformer transformer = isDeveloperMode ? handSetup : animatedRender;
            Function<HandSide, IRenderConfig> configForHand = hand -> hand == HandSide.RIGHT ? transformer.right() : transformer.left();
            event.setCanceled(true);
            Minecraft mc = Minecraft.getInstance();
            FirstPersonRenderer fpRenderer = mc.getItemInHandRenderer();
            boolean mainhand = event.getHand() == Hand.MAIN_HAND;
            ItemCameraTransforms.TransformType transformType = mainhand ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
            boolean disableVanillaAnimations = animatedRender.disableVanillaAnimations();
            float equipProgress = disableVanillaAnimations ? 0.0F : event.getEquipProgress();
            float swingProgress = disableVanillaAnimations ? 0.0F : event.getSwingProgress();
            MatrixStack poseStack = event.getMatrixStack();
            IRenderTypeBuffer buffer = event.getBuffers();
            int light = event.getLight();
            animator.animate(poseStack, buffer, light, swingProgress, equipProgress, configForHand, pipeline, fpRenderer, mc.player, stack, transformType, mainhand);
        }
    }

    // implementations

    private void animate(MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float swing, float equip, Function<HandSide, IRenderConfig> selector,
                         IAnimationPipeline pipeline, FirstPersonRenderer fpRenderer, PlayerEntity player, ItemStack stack,
                         ItemCameraTransforms.TransformType type, boolean mainHand) {

        preAnimate.call(poseStack, buffer, light, swing, equip, selector, pipeline, fpRenderer, player, stack, type, mainHand);
        poseStack.pushPose();
        {
            pipeline.animateStage(AnimationStage.ITEM_AND_HANDS, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            poseStack.pushPose();
            {
                pipeline.animateStage(AnimationStage.HANDS, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
                handAnimator.animateHands(poseStack, buffer, light, equip, selector, pipeline);
            }
            poseStack.popPose();
            pipeline.animateStage(AnimationStage.HELD_ITEM, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
            itemRenderer.renderItem(fpRenderer, player, stack, type, !mainHand, poseStack, buffer, light, swing, equip);
        }
        poseStack.popPose();
        postAnimate.call(poseStack, buffer, light, swing, equip, selector, pipeline, fpRenderer, player, stack, type, mainHand);
    }

    private void animateHands(MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float equip, Function<HandSide, IRenderConfig> selector, IAnimationPipeline pipeline) {
        float offsetY = -0.5F * equip;
        RenderSystem.disableCull();
        poseStack.pushPose();
        {
            poseStack.translate(0, offsetY, 0);
            poseStack.pushPose();
            {
                pipeline.animateStage(AnimationStage.RIGHT_HAND, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
                handRenderer.renderHand(poseStack, HandSide.RIGHT, selector, buffer, light);
            }
            poseStack.popPose();
            poseStack.pushPose();
            {
                pipeline.animateStage(AnimationStage.LEFT_HAND, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
                handRenderer.renderHand(poseStack, HandSide.LEFT, selector, buffer, light);
            }
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    private void renderHand(MatrixStack stack, HandSide side, Function<HandSide, IRenderConfig> selector, IRenderTypeBuffer buffer, int light) {
        boolean rightArm = side == HandSide.RIGHT;
        IRenderConfig config = selector.apply(side);
        config.applyTo(stack);
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bind(mc.player.getSkinTextureLocation());
        EntityRenderer<? super ClientPlayerEntity> renderer = mc.getEntityRenderDispatcher().getRenderer(mc.player);
        PlayerRenderer playerRenderer = (PlayerRenderer) renderer;

        stack.pushPose();
        {
            stack.mulPose(Vector3f.YP.rotationDegrees(40.0F));
            stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            if (rightArm) {
                stack.translate(0.8F, -0.3F, -0.4F);
                playerRenderer.renderRightHand(stack, buffer, light, mc.player);
            } else {
                stack.translate(-0.5F, 0.6F, -0.36F);
                playerRenderer.renderLeftHand(stack, buffer, light, mc.player);
            }
            stack.mulPose(Vector3f.ZP.rotationDegrees(-41.0F)); // ??? what is this doing
        }
        stack.popPose();
    }

    private void renderItem(FirstPersonRenderer fpRenderer, PlayerEntity player, ItemStack stack, ItemCameraTransforms.TransformType type,
                            boolean offHand, MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float swing, float equip) {

        HandSide side = offHand ? HandSide.LEFT : HandSide.RIGHT;
        float swingSqrt = MathHelper.sqrt(swing);
        float swingX = -0.4F * MathHelper.sin(swingSqrt *  (float) Math.PI);
        float swingY =  0.2F * MathHelper.sin(swingSqrt * ((float) Math.PI * 2.0F));
        float swingZ = -0.2F * MathHelper.sin(swing * (float) Math.PI);
        int xMultiplier = !offHand ? 1 : -1;
        poseStack.translate(xMultiplier * swingX, swingY, swingZ);
        int multiplier = side == HandSide.RIGHT ? 1 : -1;
        poseStack.translate(multiplier * 0.56F, -0.52F + equip * -0.6F, -0.72F);
        if (swing != 0.0F) {
            float attackSwingY = MathHelper.sin(swing * swing * (float) Math.PI);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(multiplier * (45.0F + attackSwingY * -20.0F)));
            float attackSwingXZ = MathHelper.sin(swingSqrt * (float) Math.PI);
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(multiplier * attackSwingXZ * -20.0F));
            poseStack.mulPose(Vector3f.XP.rotationDegrees(attackSwingXZ * -80.0F));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(multiplier * -45.0F));
        }
        fpRenderer.renderItem(player, stack, type, offHand, poseStack, buffer, light);
    }

    private void empty_callback(MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float swing, float equip, Function<HandSide, IRenderConfig> selector,
                                IAnimationPipeline pipeline, FirstPersonRenderer fpRenderer, PlayerEntity player, ItemStack stack, ItemCameraTransforms.TransformType type,
                                boolean mainHand) {}
}
