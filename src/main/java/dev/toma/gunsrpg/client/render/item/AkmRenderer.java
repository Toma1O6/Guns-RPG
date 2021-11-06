package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class AkmRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.AKM;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, -0.2, 0.35);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.2, -0.2, 0.4);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return transform.firstPerson() ? 0.18F : 0.15F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {

    }
}
