package com.wf.firearms.client.model.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;

/** Guns RPG 1.16 模型基类（无动画管线）。 */
public abstract class LegacyModel extends Model {
    public int texWidth = 64;
    public int texHeight = 64;

    protected LegacyModel() {
        super(RenderType::entityCutoutNoCull);
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha) {
        // 由 LegacyModelRenderer 树渲染
    }

    public void setRotationAngle(LegacyModelRenderer part, float x, float y, float z) {
        part.xRot = x;
        part.yRot = y;
        part.zRot = z;
    }
}
