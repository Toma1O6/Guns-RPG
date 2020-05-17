package dev.toma.gunsrpg.client.baked;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;

public class PistolBakedModel extends GunBakedModel {

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        TRSRTransformation trsrTransformation = new TRSRTransformation(matrix4f);
        GlStateManager.translate(0, 0.8, -0.5);
        GlStateManager.scale(0.02, 0.02, 0.02);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.rotate(180, 0, 1, 0);
        switch (cameraTransformType) {
            case GUI: {
                GlStateManager.translate(-8, 15, 0);
                GlStateManager.rotate(90, 0, -1, 0);
                GlStateManager.rotate(30, -1, 0, 0);
            }
            default: break;
        }
        return Pair.of(this, trsrTransformation.getMatrix());
    }
}
