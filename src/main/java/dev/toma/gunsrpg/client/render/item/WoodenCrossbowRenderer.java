package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.AbstractWeaponModel;
import dev.toma.gunsrpg.client.model.WoodenCrossbowModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;

public class WoodenCrossbowRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel createModelInstance() {
        return new WoodenCrossbowModel();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/crossbow.png");
    }

    @Override
    public void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            stack.translate(0.2, 0.0, -0.2);
        } else if (transform == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
            stack.translate(-0.2, 0.2, 0.3);
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        if (transform != ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            return 0.2F;
        }
        return super.scaleForTransform(transform);
    }

    @Override
    protected void transformUI(MatrixStack matrix) {

    }
}
