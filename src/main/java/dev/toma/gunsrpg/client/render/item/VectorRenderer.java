package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class VectorRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.VECTOR;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.28, 0.3);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.1, 0.4, 0.0);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return transform.firstPerson() ? 0.35F : 0.25F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(0.6, -0.2, 0.0);
    }
}
