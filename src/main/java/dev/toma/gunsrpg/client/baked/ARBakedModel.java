package dev.toma.gunsrpg.client.baked;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.math.vector.Vector3f;

public class ARBakedModel extends GunBakedModel {

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
        mat.translate(0, 0.35, 0.2);
        mat.scale(0.01f, 0.01f, 0.01f);
        mat.mulPose(Vector3f.XP.rotationDegrees(180));
        mat.mulPose(Vector3f.YP.rotationDegrees(180));
        switch (cameraTransformType) {
            case FIRST_PERSON_RIGHT_HAND:
                mat.translate(30.0, -5.0, 20.0);
                break;
            case THIRD_PERSON_RIGHT_HAND:
                mat.translate(0.0, 25.0, -5.0);
                break;
            case GUI:
                mat.translate(-25.0, 35.0, 0);
                mat.mulPose(Vector3f.YN.rotationDegrees(90.0F));
                mat.mulPose(Vector3f.XN.rotationDegrees(30.0F));
                break;
        }
        return super.handlePerspective(cameraTransformType, mat);
    }
}
