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

    private final List<SpecialRenderer<?>> specialRenderers = new ArrayList<>();

    public void setSpecialRenderer(AnimationStage stage, ModelRenderer cubeRenderer) {
        setSpecialRenderer(new SpecialRenderer<>(stage, cubeRenderer, (renderObj, data, poseStack, vertexBuilder, light, overlay) -> renderObj.render(poseStack, vertexBuilder, light, overlay)));
    }

    public void setSpecialRenderer(AnimationStage stage, Function<IPlayerData, ModelRenderer> selector) {
        setSpecialRenderer(new SpecialRenderer<>(stage, selector, (renderObj, data, poseStack, vertexBuilder, light, overlay) -> renderObj.apply(data).render(poseStack, vertexBuilder, light, overlay)));
    }

    public void setSpecialRenderer(SpecialRenderer<?> cubeRenderer) {
        specialRenderers.add(cubeRenderer);
    }

    @Override
    public final void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
    }

    public final void render(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay, boolean heldFp) {
        renderWeapon(stack, data, matrix, builder, light, overlay);
        if (heldFp) {
            renderAnimated(data, matrix, builder, light, overlay);
        } else {
            renderStatic(data, matrix, builder, light, overlay);
        }
    }

    protected abstract void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay);

    private void renderStatic(IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        for (SpecialRenderer<?> specialRenderer : specialRenderers) {
            specialRenderer.renderCubes(data, matrix, builder, light, overlay);
        }
    }

    private void renderAnimated(IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        for (SpecialRenderer<?> specialRenderer : specialRenderers) {
            matrix.pushPose();
            {
                pipeline.animateStage(specialRenderer.targetStage, matrix);
                specialRenderer.renderCubes(data, matrix, builder, light, overlay);
            }
            matrix.popPose();
        }
    }

    public static class SpecialRenderer<T> {

        private final AnimationStage targetStage;
        private final T renderObj;
        private final IRenderer<T> objRenderer;

        SpecialRenderer(AnimationStage targetStage, T renderObj, IRenderer<T> objRenderer) {
            this.targetStage = targetStage;
            this.renderObj = renderObj;
            this.objRenderer = objRenderer;
        }

        public void renderCubes(IPlayerData data, MatrixStack poseStack, IVertexBuilder vertexBuilder, int light, int overlay) {
            objRenderer.renderAnimated(renderObj, data, poseStack, vertexBuilder, light, overlay);
        }
    }

    interface IRenderer<T> {
        void renderAnimated(T renderObj, IPlayerData data, MatrixStack poseStack, IVertexBuilder vertexBuilder, int light, int overlay);
    }
}
