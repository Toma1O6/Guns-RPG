package com.wf.firearms.client.model;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.entity.BloodmoonGolemEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** 独立模型层，避免 Better Animations Collection 将实体强转为 {@link net.minecraft.world.entity.animal.IronGolem}。 */
public class BloodmoonGolemModel extends HierarchicalModel<BloodmoonGolemEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "bloodmoon_golem"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public BloodmoonGolemModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        return IronGolemModel.createBodyLayer();
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(
            BloodmoonGolemEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        head.xRot = headPitch * ((float) Math.PI / 180F);
        rightLeg.xRot = -1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        leftLeg.xRot = 1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        rightLeg.yRot = 0.0F;
        leftLeg.yRot = 0.0F;

        int attack = entity.getAttackTimer();
        if (attack > 0) {
            rightArm.xRot = -2.0F + 1.5F * Mth.triangleWave(attack - ageInTicks, 10.0F);
            leftArm.xRot = -2.0F + 1.5F * Mth.triangleWave(attack - ageInTicks, 10.0F);
        } else {
            rightArm.xRot =
                    (-0.2F + 1.5F * Mth.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
            leftArm.xRot =
                    (-0.2F - 1.5F * Mth.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
        }
    }
}
