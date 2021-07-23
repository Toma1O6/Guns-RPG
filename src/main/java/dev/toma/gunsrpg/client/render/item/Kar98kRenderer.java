package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.Kar98kModel;
import dev.toma.gunsrpg.client.model.AbstractWeaponModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;

public class Kar98kRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel createModelInstance() {
        return new Kar98kModel();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/kar98k.png");
    }

    @Override
    public void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            stack.translate(-0.1, 0.2, 0.0);
        } else if(transform == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
            stack.translate(-0.2, 0.2, 0.3);
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            return 0.25F;
        }
        return 0.15F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        super.transformUI(matrix);
    }
}
