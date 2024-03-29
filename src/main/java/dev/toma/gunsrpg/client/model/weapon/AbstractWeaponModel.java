package dev.toma.gunsrpg.client.model.weapon;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.model.AbstractSolidEntityModel;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractWeaponModel extends AbstractSolidEntityModel {

    private final List<SpecialRenderer<?>> specialRenderers = new ArrayList<>();
    private IRenderCallback bulletRenderer;

    public SpecialRenderer<?> setSpecialRenderer(AnimationStage stage, ModelRenderer renderer) {
        return setSpecialRenderer(new SpecialRenderer<>(stage, renderer, (renderObj, data, poseStack, vertexBuilder, light, overlay) -> renderObj.render(poseStack, vertexBuilder, light, overlay)));
    }

    public SpecialRenderer<?> setSpecialRenderer(AnimationStage stage, Function<IPlayerData, ModelRenderer> selector) {
        return setSpecialRenderer(new SpecialRenderer<>(stage, selector, (renderObj, data, poseStack, vertexBuilder, light, overlay) -> renderObj.apply(data).render(poseStack, vertexBuilder, light, overlay)));
    }

    public SpecialRenderer<?> setSpecialRenderer(AnimationStage stage, Predicate<ItemStack> condition, ModelRenderer renderer) {
        return setSpecialRenderer(new SpecialRenderer<>(stage, condition, (renderObj, data, poseStack, vertexBuilder, light, overlay) -> {
            ItemStack held = Minecraft.getInstance().player.getMainHandItem();
            if (condition.test(held)) {
                renderer.render(poseStack, vertexBuilder, light, overlay);
            }
        }));
    }

    public SpecialRenderer<?> setSpecialRenderer(SpecialRenderer<?> cubeRenderer) {
        specialRenderers.add(cubeRenderer);
        if (cubeRenderer.builtInAnimations()) {
            bulletRenderer = cubeRenderer::renderCubes;
        }
        return cubeRenderer;
    }

    @Override
    public final void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
    }

    public final void render(ItemStack stack, IPlayerData data, MatrixStack matrix, IRenderTypeBuffer typeBuffer, IVertexBuilder builder, int light, int overlay, boolean heldFp) {
        renderWeapon(stack, data, matrix, builder, light, overlay);
        if (heldFp) {
            renderAnimated(data, matrix, typeBuffer, builder, light, overlay);
        } else {
            renderStatic(data, matrix, typeBuffer, builder, light, overlay);
        }
    }

    public final IRenderCallback getBulletRenderer() {
        return bulletRenderer;
    }

    public List<SpecialRenderer<?>> getSpecialRenderers() {
        return ImmutableList.copyOf(specialRenderers);
    }

    protected abstract void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay);

    protected SpecialRenderer<?> setBulletRenderer(SpecialRenderer<?> renderer) {
        this.bulletRenderer = renderer::renderCubes;
        renderer.setSpecial();
        return renderer;
    }

    private void renderStatic(IPlayerData data, MatrixStack matrix, IRenderTypeBuffer typeBuffer, IVertexBuilder builder, int light, int overlay) {
        for (SpecialRenderer<?> specialRenderer : specialRenderers) {
            specialRenderer.renderCubes(data, matrix, builder, light, overlay);
        }
    }

    private void renderAnimated(IPlayerData data, MatrixStack matrix, IRenderTypeBuffer typeBuffer, IVertexBuilder builder, int light, int overlay) {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        for (SpecialRenderer<?> specialRenderer : specialRenderers) {
            matrix.pushPose();
            {
                pipeline.animateStage(specialRenderer.targetStage, matrix, typeBuffer, light, overlay);
                specialRenderer.renderCubes(data, matrix, builder, light, overlay);
            }
            matrix.popPose();
        }
    }

    public static class SpecialRenderer<T> {

        private final AnimationStage targetStage;
        private final T renderObj;
        private final IRenderer<T> objRenderer;
        private boolean specialAnimations;

        SpecialRenderer(AnimationStage targetStage, T renderObj, IRenderer<T> objRenderer) {
            this.targetStage = targetStage;
            this.renderObj = renderObj;
            this.objRenderer = objRenderer;
        }

        public void setSpecial() {
            this.specialAnimations = true;
        }

        public void renderCubes(IPlayerData data, MatrixStack poseStack, IVertexBuilder vertexBuilder, int light, int overlay) {
            objRenderer.renderAnimated(renderObj, data, poseStack, vertexBuilder, light, overlay);
        }

        public boolean builtInAnimations() {
            return specialAnimations;
        }

        public AnimationStage target() {
            return targetStage;
        }
    }

    interface IRenderer<T> {
        void renderAnimated(T renderObj, IPlayerData data, MatrixStack poseStack, IVertexBuilder vertexBuilder, int light, int overlay);
    }

    public interface IRenderCallback {
        void renderSpecial(IPlayerData data, MatrixStack poses, IVertexBuilder vertexBuilder, int light, int overlay);
    }
}
