package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class GrenadeLauncherRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.GRENADE_LAUNCHER;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.4, 0.3);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(0.0, 0.5, 0.2);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return transform.firstPerson() ? 0.45F : 0.3F;
    }

    @Override
    protected void scaleModel(MatrixStack matrixStack, ItemCameraTransforms.TransformType transform) {
        super.scaleModel(matrixStack, transform);
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(0.2, 0.0, 0.0);
    }
}
