package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.client.render.item.AbstractWeaponRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class S686Renderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.S686;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                break;
            case FIRST_PERSON_RIGHT_HAND:
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return super.scaleForTransform(transform);
    }

    @Override
    protected void transformUI(MatrixStack matrix) {

    }
}
