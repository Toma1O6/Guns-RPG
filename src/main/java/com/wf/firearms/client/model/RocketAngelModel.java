package com.wf.firearms.client.model;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.entity.RocketAngelEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * 独立模型层 + {@link HierarchicalModel}，避免：
 * <ul>
 *   <li>动画模组对 {@link net.minecraft.client.model.VexModel} 的 Vex 强转</li>
 *   <li>整合包改写 {@link net.minecraft.client.model.geom.ModelLayers#VEX} 导致部件树不完整</li>
 * </ul>
 */
public class RocketAngelModel extends HierarchicalModel<RocketAngelEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "rocket_angel"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public RocketAngelModel(ModelPart bakedRoot) {
        this.root = bakedRoot;
        this.head = bakedRoot.getChild("head");
        this.rightArm = bakedRoot.getChild("right_arm");
        this.leftArm = bakedRoot.getChild("left_arm");
        this.rightLeg = bakedRoot.getChild("right_leg");
        this.leftLeg = bakedRoot.getChild("left_leg");
        this.leftWing = bakedRoot.getChild("left_wing");
        this.rightWing = bakedRoot.getChild("right_wing");
        this.leftLeg.visible = false;
        bakedRoot.getChild("hat").visible = false;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild(
                "left_wing",
                CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 2.0F));
        root.addOrReplaceChild(
                "right_wing",
                CubeListBuilder.create().texOffs(0, 32).addBox(-20.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 2.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(
            RocketAngelEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        head.xRot = headPitch * ((float) Math.PI / 180F);
        rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        rightArm.zRot = 0.0F;
        leftArm.zRot = 0.0F;
        rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        rightLeg.yRot = 0.0F;
        leftLeg.yRot = 0.0F;

        rightLeg.xRot += ((float) Math.PI / 5F);
        rightWing.z = 2.0F;
        leftWing.z = 2.0F;
        rightWing.y = 1.0F;
        leftWing.y = 1.0F;
        rightWing.yRot = 0.47123894F + Mth.cos(ageInTicks * 0.8F) * (float) Math.PI * 0.05F;
        leftWing.yRot = -rightWing.yRot;
        leftWing.zRot = -0.47123894F;
        leftWing.xRot = 0.47123894F;
        rightWing.xRot = 0.47123894F;
        rightWing.zRot = 0.47123894F;
    }
}
