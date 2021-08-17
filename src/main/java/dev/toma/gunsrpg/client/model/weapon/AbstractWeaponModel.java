package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.client.model.AbstractSolidEntityModel;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractWeaponModel extends AbstractSolidEntityModel {

    private final List<AnimatedCubeRenderer<?>> animatedCubeRenderers = new ArrayList<>();

    public void addSingleAnimated(AnimationStage stage, ModelRenderer cubeRenderer) {
        addAnimated(new AnimatedCubeRenderer<>(stage, cubeRenderer, (renderObj, data, poseStack, vertexBuilder, light, overlay) -> renderObj.render(poseStack, vertexBuilder, light, overlay)));
    }

    public void addSingleAnimatedSelective(AnimationStage stage, Function<IPlayerData, ModelRenderer> selector) {
        addAnimated(new AnimatedCubeRenderer<>(stage, selector, (renderObj, data, poseStack, vertexBuilder, light, overlay) -> renderObj.apply(data).render(poseStack, vertexBuilder, light, overlay)));
    }

    public void addAnimated(AnimatedCubeRenderer<?> cubeRenderer) {
        animatedCubeRenderers.add(cubeRenderer);
    }

    @Override
    public final void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
    }

    public final void render(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay, boolean animate) {
        renderWeapon(stack, data, matrix, builder, light, overlay);
        if (animate) {
            animateAll(data, matrix, builder, light, overlay);
        }
    }

    protected abstract void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay);

    private void animateAll(IPlayerData data, MatrixStack stack, IVertexBuilder vertexBuilder, int light, int overlay) {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        for (AnimatedCubeRenderer<?> cubeRenderer : animatedCubeRenderers) {
            animate(cubeRenderer, pipeline, data, stack, vertexBuilder, light, overlay);
        }
    }

    private void animate(AnimatedCubeRenderer<?> renderer, IAnimationPipeline pipeline, IPlayerData data, MatrixStack stack, IVertexBuilder vertexBuilder, int light, int overlay) {
        stack.pushPose();
        pipeline.animateStage(renderer.targetStage, stack);
        renderer.renderAnimated(data, stack, vertexBuilder, light, overlay);
        stack.popPose();
    }

    public static class AnimatedCubeRenderer<T> {

        private final AnimationStage targetStage;
        private final T renderObj;
        private final IRenderer<T> objRenderer;

        AnimatedCubeRenderer(AnimationStage targetStage, T renderObj, IRenderer<T> objRenderer) {
            this.targetStage = targetStage;
            this.renderObj = renderObj;
            this.objRenderer = objRenderer;
        }

        public void renderAnimated(IPlayerData data, MatrixStack poseStack, IVertexBuilder vertexBuilder, int light, int overlay) {
            objRenderer.renderAnimated(renderObj, data, poseStack, vertexBuilder, light, overlay);
        }
    }

    interface IRenderer<T> {
        void renderAnimated(T renderObj, IPlayerData data, MatrixStack poseStack, IVertexBuilder vertexBuilder, int light, int overlay);
    }
}
