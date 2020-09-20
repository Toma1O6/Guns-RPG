package dev.toma.gunsrpg.client.model;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.init.Skills;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class ModelCrossbow extends ModelWeapon {

    private final ModelRenderer crossbow;
    private final ModelRenderer bone;
    private final ModelRenderer right1;
    private final ModelRenderer right2;
    private final ModelRenderer left1;
    private final ModelRenderer left2;
    private final ModelRenderer support_r1;
    private final ModelRenderer sr2;
    private final ModelRenderer support_l1;
    private final ModelRenderer sl2;
    private final ModelRenderer stock;
    private final ModelRenderer angle;
    private final ModelRenderer trigger;
    private final ModelRenderer angle2;
    private final ModelRenderer arrow;
    private final ModelRenderer scope;

    public ModelCrossbow() {
        textureWidth = 128;
        textureHeight = 128;

        crossbow = new ModelRenderer(this);
        crossbow.setRotationPoint(0.0F, 24.0F, 0.0F);
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 0, -3.0F, -14.0F, -34.0F, 6, 5, 58, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 72, -3.0F, -14.0F, -39.0F, 6, 5, 5, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 93, -8.0F, -14.0F, -63.0F, 21, 2, 4, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 93, -2.75F, -15.25F, -37.0F, 2, 2, 4, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 93, -2.75F, -15.25F, 4.0F, 2, 2, 4, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 93, -2.95F, -18.25F, -62.0F, 2, 5, 2, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 93, 0.75F, -15.25F, -37.0F, 2, 2, 4, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 93, 0.75F, -15.25F, 4.0F, 2, 2, 4, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 93, 0.95F, -18.25F, -62.0F, 2, 5, 2, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 93, -13.0F, -14.0F, -63.0F, 5, 2, 4, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 70, -3.0F, -15.3997F, 16.5142F, 6, 1, 7, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 70, -2.5F, -16.3997F, 6.5142F, 5, 1, 17, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 70, -2.5F, -16.8997F, 21.5142F, 5, 1, 1, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 70, -2.5F, -16.8997F, 19.5142F, 5, 1, 1, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 70, -2.5F, -16.8997F, 17.5142F, 5, 1, 1, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 70, -2.5F, -16.8997F, 15.5142F, 5, 1, 1, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 70, -2.5F, -16.8997F, 13.5142F, 5, 1, 1, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 70, -2.5F, -16.8997F, 11.5142F, 5, 1, 1, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 70, -2.5F, -16.8997F, 9.5142F, 5, 1, 1, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 70, -2.5F, -16.8997F, 7.5142F, 5, 1, 1, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 72, 83, -4.0F, -12.0F, -1.0F, 8, 4, 20, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 72, 88, -4.0F, -12.0F, 19.0F, 8, 4, 5, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 73, 88, -4.0F, -12.0F, -4.0F, 8, 1, 3, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 84, 88, -4.0F, -14.0F, 24.0F, 8, 6, 1, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 0, 70, -4.0F, -14.0F, 10.0F, 1, 2, 14, 0.0F, false));
        crossbow.cubeList.add(new ModelBox(crossbow, 74, 81, 3.0F, -14.0F, 10.0F, 1, 2, 14, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 1.2F, -3.1F);
        crossbow.addChild(bone);
        setRotationAngle(bone, -0.7854F, 0.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 58, 84, -4.0F, -9.0F, -9.1414F, 8, 1, 4, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 94, 93, -4.0F, -10.1594F, -8.3045F, 8, 2, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 0, 70, -3.0F, -30.5997F, 7.1142F, 6, 1, 2, 0.0F, false));

        right1 = new ModelRenderer(this);
        right1.setRotationPoint(-23.7F, -13.0F, -54.7F);
        crossbow.addChild(right1);
        setRotationAngle(right1, 0.0F, 0.5236F, 0.0F);
        right1.cubeList.add(new ModelBox(right1, 0, 71, 2.3759F, -3.0F, 2.1368F, 5, 6, 2, 0.0F, false));
        right1.cubeList.add(new ModelBox(right1, 7, 90, -1.6241F, -1.0F, -1.8632F, 15, 2, 4, 0.0F, false));
        right1.cubeList.add(new ModelBox(right1, 62, 71, -13.6241F, -6.0F, -2.8632F, 27, 5, 6, 0.0F, false));
        right1.cubeList.add(new ModelBox(right1, 62, 77, -13.6241F, 1.0F, -2.8632F, 27, 5, 6, 0.0F, false));

        right2 = new ModelRenderer(this);
        right2.setRotationPoint(-30.5193F, 0.0F, 5.4931F);
        right1.addChild(right2);
        setRotationAngle(right2, 0.0F, 0.5236F, 0.0F);
        right2.cubeList.add(new ModelBox(right2, 56, 70, -5.2109F, -6.0F, 1.1129F, 24, 5, 6, 0.0F, false));
        right2.cubeList.add(new ModelBox(right2, 56, 76, -5.2109F, 1.0F, 1.1129F, 24, 5, 6, 0.0F, false));

        left1 = new ModelRenderer(this);
        left1.setRotationPoint(16.6F, -13.0F, -59.7F);
        crossbow.addChild(left1);
        setRotationAngle(left1, 0.0F, -0.5236F, 0.0F);
        left1.cubeList.add(new ModelBox(left1, 4, 78, 1.2939F, -3.0F, 2.9316F, 5, 6, 2, 0.0F, false));
        left1.cubeList.add(new ModelBox(left1, 0, 90, -4.7061F, -1.0F, -1.0684F, 15, 2, 4, 0.0F, false));
        left1.cubeList.add(new ModelBox(left1, 62, 70, -4.7061F, -6.0F, -2.0684F, 27, 5, 6, 0.0F, false));
        left1.cubeList.add(new ModelBox(left1, 62, 85, -4.7061F, 1.0F, -2.0684F, 27, 5, 6, 0.0F, false));

        left2 = new ModelRenderer(this);
        left2.setRotationPoint(0.0F, 0.0F, 0.0F);
        left1.addChild(left2);
        setRotationAngle(left2, 0.0F, -0.5236F, 0.0F);
        left2.cubeList.add(new ModelBox(left2, 56, 70, 18.3013F, -6.0F, -12.9183F, 24, 5, 6, 0.0F, false));
        left2.cubeList.add(new ModelBox(left2, 56, 96, 18.3013F, 1.0F, -12.9183F, 24, 5, 6, 0.0F, false));

        support_r1 = new ModelRenderer(this);
        support_r1.setRotationPoint(-34.0F, -1.0F, -12.0F);
        crossbow.addChild(support_r1);
        setRotationAngle(support_r1, 0.0F, -0.7854F, 0.0F);
        support_r1.cubeList.add(new ModelBox(support_r1, 0, 72, -19.4697F, -12.5F, -40.1768F, 26, 2, 2, 0.0F, false));

        sr2 = new ModelRenderer(this);
        sr2.setRotationPoint(-6.364F, 0.0F, -2.1213F);
        support_r1.addChild(sr2);
        setRotationAngle(sr2, 0.0F, -0.1745F, 0.0F);
        sr2.cubeList.add(new ModelBox(sr2, 0, 72, -19.4697F, -12.5F, -40.1768F, 26, 2, 2, 0.0F, false));

        support_l1 = new ModelRenderer(this);
        support_l1.setRotationPoint(0.0F, 0.0F, 0.0F);
        crossbow.addChild(support_l1);
        setRotationAngle(support_l1, 0.0F, 0.7854F, 0.0F);
        support_l1.cubeList.add(new ModelBox(support_l1, 0, 72, 26.4334F, -13.5F, -24.5996F, 26, 2, 2, 0.0F, false));

        sl2 = new ModelRenderer(this);
        sl2.setRotationPoint(93.3381F, 0.0F, 18.3848F);
        support_l1.addChild(sl2);
        setRotationAngle(sl2, 0.0F, 0.1745F, 0.0F);
        sl2.cubeList.add(new ModelBox(sl2, 0, 72, -59.8336F, -13.5F, -54.2981F, 26, 2, 2, 0.0F, false));

        stock = new ModelRenderer(this);
        stock.setRotationPoint(-0.5F, -2.0F, 2.0F);
        crossbow.addChild(stock);
        setRotationAngle(stock, -0.0873F, 0.0F, 0.0F);
        stock.cubeList.add(new ModelBox(stock, 64, 77, -3.0F, -10.0F, 19.0F, 7, 2, 24, 0.0F, false));
        stock.cubeList.add(new ModelBox(stock, 64, 77, -3.0F, -8.0076F, 25.1743F, 7, 2, 16, 0.0F, false));
        stock.cubeList.add(new ModelBox(stock, 64, 77, -3.0F, -6.0152F, 28.3486F, 7, 2, 13, 0.0F, false));
        stock.cubeList.add(new ModelBox(stock, 64, 77, -3.0F, -4.0228F, 31.5229F, 7, 2, 10, 0.0F, false));
        stock.cubeList.add(new ModelBox(stock, 64, 77, -3.0F, -2.0479F, 34.8965F, 7, 2, 7, 0.0F, false));
        stock.cubeList.add(new ModelBox(stock, 64, 77, -3.0F, -0.0381F, 38.8716F, 7, 2, 2, 0.0F, false));
        stock.cubeList.add(new ModelBox(stock, 64, 77, -3.0F, -7.9902F, 40.9751F, 7, 12, 2, 0.0F, false));

        angle = new ModelRenderer(this);
        angle.setRotationPoint(0.0F, -6.9734F, -0.6101F);
        stock.addChild(angle);
        setRotationAngle(angle, -0.5236F, 0.0F, 0.0F);
        angle.cubeList.add(new ModelBox(angle, 64, 77, -3.0F, -13.7588F, 17.6319F, 7, 2, 24, 0.0F, false));

        trigger = new ModelRenderer(this);
        trigger.setRotationPoint(0.0F, 1.0F, 2.0F);
        crossbow.addChild(trigger);
        trigger.cubeList.add(new ModelBox(trigger, 72, 83, -3.5F, -6.3F, 7.2F, 7, 1, 6, 0.0F, false));
        trigger.cubeList.add(new ModelBox(trigger, 14, 95, -1.5F, -9.3F, 10.2F, 3, 2, 1, 0.0F, false));

        angle2 = new ModelRenderer(this);
        angle2.setRotationPoint(0.0F, 0.0F, 0.0F);
        trigger.addChild(angle2);
        setRotationAngle(angle2, 0.1745F, 0.0F, 0.0F);
        angle2.cubeList.add(new ModelBox(angle2, 72, 83, -3.5F, -8.0F, 8.0F, 7, 4, 1, 0.0F, false));
        angle2.cubeList.add(new ModelBox(angle2, 72, 83, -3.5F, -6.9755F, 13.8104F, 7, 4, 1, 0.0F, false));
        angle2.cubeList.add(new ModelBox(angle2, 6, 82, -3.5F, -6.8018F, 14.7952F, 7, 13, 5, 0.0F, false));

        arrow = new ModelRenderer(this);
        arrow.setRotationPoint(0.0F, 24.0F, 0.0F);
        arrow.cubeList.add(new ModelBox(arrow, 0, 0, -0.5F, -15.25F, -72.0F, 1, 1, 2, 0.0F, false));
        arrow.cubeList.add(new ModelBox(arrow, 0, 0, -1.0F, -15.75F, -70.0F, 2, 2, 3, 0.0F, false));
        arrow.cubeList.add(new ModelBox(arrow, 6, 69, -1.0F, -15.75F, -67.0F, 2, 2, 22, 0.0F, false));
        arrow.cubeList.add(new ModelBox(arrow, 6, 69, -1.0F, -15.75F, -45.0F, 2, 2, 22, 0.0F, false));
        arrow.cubeList.add(new ModelBox(arrow, 6, 69, -1.0F, -15.75F, -23.0F, 2, 2, 22, 0.0F, false));
        arrow.cubeList.add(new ModelBox(arrow, 6, 69, -1.0F, -15.75F, -1.0F, 2, 2, 11, 0.0F, false));

        scope = new ModelRenderer(this);
        scope.setRotationPoint(0.0F, 24.0F, 0.0F);
        scope.cubeList.add(new ModelBox(scope, 6, 81, -2.0F, -19.25F, 11.0F, 4, 1, 9, 0.0F, false));
        scope.cubeList.add(new ModelBox(scope, 6, 81, -2.0F, -18.25F, 17.0F, 4, 3, 2, 0.0F, false));
        scope.cubeList.add(new ModelBox(scope, 6, 81, -2.0F, -18.25F, 12.0F, 4, 3, 2, 0.0F, false));
        scope.cubeList.add(new ModelBox(scope, 6, 81, -2.0F, -24.25F, 11.0F, 4, 1, 9, 0.0F, false));
        scope.cubeList.add(new ModelBox(scope, 6, 81, -3.0F, -23.25F, 11.0F, 1, 4, 9, 0.0F, false));
        scope.cubeList.add(new ModelBox(scope, 6, 81, 2.0F, -23.25F, 11.0F, 1, 4, 9, 0.0F, false));
    }

    @Override
    public void doRender(ItemStack stack) {
        crossbow.render(1f);
        if(stack.hasTagCompound() && stack.getTagCompound().getInteger("ammo") > 0) arrow.render(1f);
        if(PlayerDataFactory.hasActiveSkill(Minecraft.getMinecraft().player, Skills.CROSSBOW_SCOPE)) scope.render(1f);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
