package dev.toma.gunsrpg.client.model;

import lib.toma.animations.AnimationUtils;
import net.minecraft.client.renderer.model.ModelRenderer;

public abstract class AbstractTurretModel extends AbstractSolidEntityModel {

    public abstract void setTurretRotations(float xRot, float xRotO, float yRot, float yRotO, float partialTicks);

    protected static void rotateModelAccordingToEntity(ModelRenderer model, float xRot, float xRotO, float yRot, float yRotO, float partialTicks) {
        model.xRot = (float) Math.toRadians(AnimationUtils.linearInterpolate(xRot, xRotO, partialTicks));
        model.yRot = (float) Math.toRadians(AnimationUtils.linearInterpolate(yRot, yRotO, partialTicks));
    }
}
