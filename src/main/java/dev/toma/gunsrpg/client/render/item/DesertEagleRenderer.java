package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class DesertEagleRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.DESERT_EAGLE;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.1, 0.6);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(0.0, 0.15, 0.0);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return transform.firstPerson() ? 0.3F : 0.2F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.scale(1.3F, 1.3F, 1.3F);
        matrix.translate(0.4, 0.4, 0.0);
    }
}
