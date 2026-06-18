package com.wf.firearms.client.model;

import com.wf.firearms.entity.ExplosiveSkeletonEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/** 掷弹手：持榴弹发射器时双臂前伸。 */
public class ExplosiveSkeletonModel extends HumanoidModel<ExplosiveSkeletonEntity> {
    public ExplosiveSkeletonModel(ModelPart root) {
        super(root);
    }

    @Override
    public void prepareMobModel(
            ExplosiveSkeletonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks) {
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, ageInTicks);
        if (entity.shouldHoldGunPose()) {
            this.rightArmPose = ArmPose.CROSSBOW_HOLD;
            this.leftArmPose = ArmPose.CROSSBOW_HOLD;
        }
    }

    @Override
    public void setupAnim(
            ExplosiveSkeletonEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (!entity.shouldHoldGunPose()) {
            return;
        }
        float aim = Mth.clamp(headPitch, -45f, 45f) * ((float) Math.PI / 180F);
        float headYawRad = netHeadYaw * ((float) Math.PI / 180F);
        this.rightArm.xRot = -((float) Math.PI / 2F) + aim * 0.85F;
        this.rightArm.yRot = -0.12F + headYawRad * 0.35F;
        this.leftArm.xRot = -((float) Math.PI / 2F) + aim * 0.75F + 0.15F;
        this.leftArm.yRot = 0.18F + headYawRad * 0.35F;
    }
}
