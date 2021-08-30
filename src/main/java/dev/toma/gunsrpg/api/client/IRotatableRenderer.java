package dev.toma.gunsrpg.api.client;

import net.minecraft.client.renderer.model.ModelRenderer;

public interface IRotatableRenderer {

    void setRotationAngle(ModelRenderer renderer, float x, float y, float z);
}
