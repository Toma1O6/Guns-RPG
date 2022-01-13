package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class RocketLauncherRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.ROCKET_LAUNCHER;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.7, 0.1);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.7, 0.0);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return transform == ItemCameraTransforms.TransformType.GUI ? 0.25F : 0.35F;
    }

    @Override
    protected void scaleModel(MatrixStack matrixStack, ItemCameraTransforms.TransformType transform) {
        super.scaleModel(matrixStack, transform);
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(0.7, -0.7, 0.0);
    }
}
