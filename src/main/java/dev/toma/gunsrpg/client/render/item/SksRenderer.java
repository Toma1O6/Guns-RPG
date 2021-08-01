package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.AbstractWeaponModel;
import dev.toma.gunsrpg.client.model.SksModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;

public class SksRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel createModelInstance() {
        return new SksModel();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/sks.png");
    }

    @Override
    public void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.5, 0.34, 0.7);
                break;
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.2, 0.5);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
            return super.scaleForTransform(transform);
        return 0.2F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(-0.3, 0.8, 0.0);
    }
}
