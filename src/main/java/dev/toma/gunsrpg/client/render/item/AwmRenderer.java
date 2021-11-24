package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class AwmRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.AWM;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.05, 0.5);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.25, 0.05, 0.4);
                break;
        }
    }

    @Override
    protected void scaleModel(MatrixStack matrixStack, ItemCameraTransforms.TransformType transform) {
        float multiplier = transform.firstPerson() ? 0.55F : 0.75F;
        float factorXY = scaleForTransform(transform);
        float factorZ  = factorXY * multiplier;
        matrixStack.scale(factorXY, factorXY, factorZ);
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return transform == ItemCameraTransforms.TransformType.GUI ? 0.15F : 0.2F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(0.7, 0.9, 0.0);
    }
}
