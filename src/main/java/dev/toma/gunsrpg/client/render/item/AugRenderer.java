package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class AugRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.AUG;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.15, 0.8);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.2, 0.85);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return transform.firstPerson() ? 0.4F : 0.25F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(0.0, 0.5, 0.0);
    }
}
