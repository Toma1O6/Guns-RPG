package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.AbstractWeaponModel;
import dev.toma.gunsrpg.client.model.Ump45Model;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;

public class Ump45Renderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel createModelInstance() {
        return new Ump45Model();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/ump45.png");
    }

    @Override
    public void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.5, 0.4);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.19, 0.7, 0.2);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            return 0.65F;
        }
        return super.scaleForTransform(transform);
    }
}
