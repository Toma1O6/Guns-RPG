package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class Hk416Renderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.HK416;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.3, 0.1);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.1, 0.4, 0.0);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return transform == ItemCameraTransforms.TransformType.GUI ? 0.27F : 0.2F;
    }

    @Override
    protected void scaleModel(MatrixStack matrixStack, ItemCameraTransforms.TransformType transform) {
        if (!transform.firstPerson()) {
            super.scaleModel(matrixStack, transform);
        } else {
            matrixStack.scale(0.3F, 0.3F, 0.22F);
        }
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(0.9, -0.8, 0.0);
    }
}
