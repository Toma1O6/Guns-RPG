package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.client.render.item.AbstractWeaponRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class Mk14EbrRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.MK14;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.1, 0.25);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.3, 0.2, 0.2);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return 0.15F;
    }

    @Override
    protected void scaleModel(MatrixStack matrixStack, ItemCameraTransforms.TransformType transform) {
        if (transform.firstPerson()) {
            matrixStack.scale(0.25F, 0.25F, 0.2F);
            return;
        }
        super.scaleModel(matrixStack, transform);
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(1.2, 0.4, 0.0);
    }
}
