package dev.toma.gunsrpg.client.model;

import net.minecraft.client.renderer.model.ModelRenderer;

public interface IRotatableRenderer {

    void setRotationAngle(ModelRenderer renderer, float x, float y, float z);
}
