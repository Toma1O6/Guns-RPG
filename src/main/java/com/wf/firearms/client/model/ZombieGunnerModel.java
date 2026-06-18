package com.wf.firearms.client.model;

import com.wf.firearms.entity.ZombieGunnerEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/** 僵尸枪手：持枪时双臂前伸（参考弩瞄准 + 僵尸抬臂）。 */
public class ZombieGunnerModel extends HumanoidModel<ZombieGunnerEntity> {
    public ZombieGunnerModel(ModelPart root) {
        super(root);
    }

    @Override
    public void prepareMobModel(
            ZombieGunnerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks) {
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, ageInTicks);
        if (entity.shouldHoldGunPose()) {
            this.rightArmPose = ArmPose.CROSSBOW_HOLD;
            this.leftArmPose =
                    entity.holdsTwoHandedWeapon() ? ArmPose.CROSSBOW_HOLD : ArmPose.EMPTY;
        }
    }

    @Override
    public void setupAnim(
            ZombieGunnerEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (!entity.shouldHoldGunPose()) {
            return;
        }
        boolean twoHanded = entity.holdsTwoHandedWeapon();
        float aim = Mth.clamp(headPitch, -45f, 45f) * ((float) Math.PI / 180F);
        float headYawRad = netHeadYaw * ((float) Math.PI / 180F);

        this.rightArm.xRot = -((float) Math.PI / 2F) + aim * 0.85F;
        this.rightArm.yRot = -0.12F + headYawRad * 0.35F;
        this.rightArm.zRot = 0.05F;

        if (twoHanded) {
            this.leftArm.xRot = -((float) Math.PI / 2F) + aim * 0.75F + 0.15F;
            this.leftArm.yRot = 0.18F + headYawRad * 0.35F;
            this.leftArm.zRot = -0.08F;
        } else {
            this.leftArm.xRot = -0.8F;
            this.leftArm.yRot = 0.15F + headYawRad * 0.2F;
        }
    }
}
