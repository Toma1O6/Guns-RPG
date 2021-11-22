package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.client.render.item.AbstractWeaponRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
/*
//Hand render config
//right

//left

 */
public class S686Renderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.S686;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.2, 0.3);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.1, 0.4, 0.3);
                break;
        }
    }

    @Override
    protected void scaleModel(MatrixStack matrixStack, ItemCameraTransforms.TransformType transform) {
        if (transform.firstPerson()) {
            matrixStack.scale(0.3F, 0.3F, 0.25F);
            return;
        }
        super.scaleModel(matrixStack, transform);
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return 0.2F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(0.6, 0.3, 0.0);
    }
}
