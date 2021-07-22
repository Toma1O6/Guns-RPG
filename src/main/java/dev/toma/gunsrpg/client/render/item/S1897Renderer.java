package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.S1897Model;
import dev.toma.gunsrpg.client.model.AbstractWeaponModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;

public class S1897Renderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel createModelInstance() {
        return new S1897Model();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/sawedoff.png");
    }

    @Override
    public void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.25, 0.35);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.495, 0.6, 0.3);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            return 0.4F;
        } else if (transform == ItemCameraTransforms.TransformType.GUI) {
            return 0.28F;
        }
        return 0.2F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {

    }
}
