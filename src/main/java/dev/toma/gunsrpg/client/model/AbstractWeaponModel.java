package dev.toma.gunsrpg.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public abstract class AbstractWeaponModel extends AbstractSolidEntityModel {

    @Override
    public final void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
    }

    public abstract void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay, float r, float g, float b, float a);

    protected <T> void renderWithCondition(ModelRenderer renderer, MatrixStack stack, IVertexBuilder builder, int light, int overlay, T src, Predicate<T> condition) {
        if (condition.test(src))
            renderer.render(stack, builder, light, overlay);
    }
}
