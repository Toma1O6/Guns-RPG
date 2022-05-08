package dev.toma.gunsrpg.client.model.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public interface IOpticsProvider {

    ResourceLocation getComponentTextureMap();

    Supplier<ResourceLocation> getReticleTextureProvider();

    ModelRenderer getGlassModel();

    ModelRenderer getOverlayModel();

    int getReticleTintARGB();

    void renderOptic(MatrixStack stack, IVertexBuilder builder, int light, int overlay);
}
