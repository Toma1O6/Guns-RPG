package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class WoodenCrossbowModel extends AbstractWeaponModel {

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

    public WoodenCrossbowModel() {
        texWidth = 128;
        texHeight = 128;

        crossbow = new ModelRenderer(this);
        crossbow.setPos(0.0F, 24.0F, 0.0F);
        crossbow.texOffs(0, 0).addBox(-3.0F, -14.0F, -34.0F, 6.0F, 5.0F, 58.0F, 0.0F, false);
        crossbow.texOffs(0, 72).addBox(-3.0F, -14.0F, -39.0F, 6.0F, 5.0F, 5.0F, 0.0F, false);
        crossbow.texOffs(0, 93).addBox(-8.0F, -14.0F, -63.0F, 21.0F, 2.0F, 4.0F, 0.0F, false);
        crossbow.texOffs(0, 93).addBox(-2.75F, -15.25F, -37.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        crossbow.texOffs(0, 93).addBox(-2.75F, -15.25F, 4.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        crossbow.texOffs(0, 93).addBox(-2.95F, -18.25F, -62.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);
        crossbow.texOffs(0, 93).addBox(0.75F, -15.25F, -37.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        crossbow.texOffs(0, 93).addBox(0.75F, -15.25F, 4.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        crossbow.texOffs(0, 93).addBox(0.95F, -18.25F, -62.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);
        crossbow.texOffs(0, 93).addBox(-13.0F, -14.0F, -63.0F, 5.0F, 2.0F, 4.0F, 0.0F, false);
        crossbow.texOffs(0, 70).addBox(-3.0F, -15.3997F, 16.5142F, 6.0F, 1.0F, 7.0F, 0.0F, false);
        crossbow.texOffs(0, 70).addBox(-2.5F, -16.3997F, 6.5142F, 5.0F, 1.0F, 17.0F, 0.0F, false);
        crossbow.texOffs(0, 70).addBox(-2.5F, -16.8997F, 21.5142F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        crossbow.texOffs(0, 70).addBox(-2.5F, -16.8997F, 19.5142F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        crossbow.texOffs(0, 70).addBox(-2.5F, -16.8997F, 17.5142F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        crossbow.texOffs(0, 70).addBox(-2.5F, -16.8997F, 15.5142F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        crossbow.texOffs(0, 70).addBox(-2.5F, -16.8997F, 13.5142F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        crossbow.texOffs(0, 70).addBox(-2.5F, -16.8997F, 11.5142F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        crossbow.texOffs(0, 70).addBox(-2.5F, -16.8997F, 9.5142F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        crossbow.texOffs(0, 70).addBox(-2.5F, -16.8997F, 7.5142F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        crossbow.texOffs(72, 83).addBox(-4.0F, -12.0F, -1.0F, 8.0F, 4.0F, 20.0F, 0.0F, false);
        crossbow.texOffs(72, 88).addBox(-4.0F, -12.0F, 19.0F, 8.0F, 4.0F, 5.0F, 0.0F, false);
        crossbow.texOffs(73, 88).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 1.0F, 3.0F, 0.0F, false);
        crossbow.texOffs(84, 88).addBox(-4.0F, -14.0F, 24.0F, 8.0F, 6.0F, 1.0F, 0.0F, false);
        crossbow.texOffs(0, 70).addBox(-4.0F, -14.0F, 10.0F, 1.0F, 2.0F, 14.0F, 0.0F, false);
        crossbow.texOffs(74, 81).addBox(3.0F, -14.0F, 10.0F, 1.0F, 2.0F, 14.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 1.2F, -3.1F);
        crossbow.addChild(bone);
        setRotationAngle(bone, -0.7854F, 0.0F, 0.0F);
        bone.texOffs(58, 84).addBox(-4.0F, -9.0F, -9.1414F, 8.0F, 1.0F, 4.0F, 0.0F, false);
        bone.texOffs(94, 93).addBox(-4.0F, -10.1594F, -8.3045F, 8.0F, 2.0F, 3.0F, 0.0F, false);
        bone.texOffs(0, 70).addBox(-3.0F, -30.5997F, 7.1142F, 6.0F, 1.0F, 2.0F, 0.0F, false);

        right1 = new ModelRenderer(this);
        right1.setPos(-23.7F, -13.0F, -54.7F);
        crossbow.addChild(right1);
        setRotationAngle(right1, 0.0F, 0.5236F, 0.0F);
        right1.texOffs(0, 71).addBox(2.3759F, -3.0F, 2.1368F, 5.0F, 6.0F, 2.0F, 0.0F, false);
        right1.texOffs(7, 90).addBox(-1.6241F, -1.0F, -1.8632F, 15.0F, 2.0F, 4.0F, 0.0F, false);
        right1.texOffs(62, 71).addBox(-13.6241F, -6.0F, -2.8632F, 27.0F, 5.0F, 6.0F, 0.0F, false);
        right1.texOffs(62, 77).addBox(-13.6241F, 1.0F, -2.8632F, 27.0F, 5.0F, 6.0F, 0.0F, false);

        right2 = new ModelRenderer(this);
        right2.setPos(-30.5193F, 0.0F, 5.4931F);
        right1.addChild(right2);
        setRotationAngle(right2, 0.0F, 0.5236F, 0.0F);
        right2.texOffs(56, 70).addBox(-5.2109F, -6.0F, 1.1129F, 24.0F, 5.0F, 6.0F, 0.0F, false);
        right2.texOffs(56, 76).addBox(-5.2109F, 1.0F, 1.1129F, 24.0F, 5.0F, 6.0F, 0.0F, false);

        left1 = new ModelRenderer(this);
        left1.setPos(16.6F, -13.0F, -59.7F);
        crossbow.addChild(left1);
        setRotationAngle(left1, 0.0F, -0.5236F, 0.0F);
        left1.texOffs(4, 78).addBox(1.2939F, -3.0F, 2.9316F, 5.0F, 6.0F, 2.0F, 0.0F, false);
        left1.texOffs(0, 90).addBox(-4.7061F, -1.0F, -1.0684F, 15.0F, 2.0F, 4.0F, 0.0F, false);
        left1.texOffs(62, 70).addBox(-4.7061F, -6.0F, -2.0684F, 27.0F, 5.0F, 6.0F, 0.0F, false);
        left1.texOffs(62, 85).addBox(-4.7061F, 1.0F, -2.0684F, 27.0F, 5.0F, 6.0F, 0.0F, false);

        left2 = new ModelRenderer(this);
        left2.setPos(0.0F, 0.0F, 0.0F);
        left1.addChild(left2);
        setRotationAngle(left2, 0.0F, -0.5236F, 0.0F);
        left2.texOffs(56, 70).addBox(18.3013F, -6.0F, -12.9183F, 24.0F, 5.0F, 6.0F, 0.0F, false);
        left2.texOffs(56, 96).addBox(18.3013F, 1.0F, -12.9183F, 24.0F, 5.0F, 6.0F, 0.0F, false);

        support_r1 = new ModelRenderer(this);
        support_r1.setPos(-34.0F, -1.0F, -12.0F);
        crossbow.addChild(support_r1);
        setRotationAngle(support_r1, 0.0F, -0.7854F, 0.0F);
        support_r1.texOffs(0, 72).addBox(-19.4697F, -12.5F, -40.1768F, 26.0F, 2.0F, 2.0F, 0.0F, false);

        sr2 = new ModelRenderer(this);
        sr2.setPos(-6.364F, 0.0F, -2.1213F);
        support_r1.addChild(sr2);
        setRotationAngle(sr2, 0.0F, -0.1745F, 0.0F);
        sr2.texOffs(0, 72).addBox(-19.4697F, -12.5F, -40.1768F, 26.0F, 2.0F, 2.0F, 0.0F, false);

        support_l1 = new ModelRenderer(this);
        support_l1.setPos(0.0F, 0.0F, 0.0F);
        crossbow.addChild(support_l1);
        setRotationAngle(support_l1, 0.0F, 0.7854F, 0.0F);
        support_l1.texOffs(0, 72).addBox(26.4334F, -13.5F, -24.5996F, 26.0F, 2.0F, 2.0F, 0.0F, false);

        sl2 = new ModelRenderer(this);
        sl2.setPos(93.3381F, 0.0F, 18.3848F);
        support_l1.addChild(sl2);
        setRotationAngle(sl2, 0.0F, 0.1745F, 0.0F);
        sl2.texOffs(0, 72).addBox(-59.8336F, -13.5F, -54.2981F, 26.0F, 2.0F, 2.0F, 0.0F, false);

        stock = new ModelRenderer(this);
        stock.setPos(-0.5F, -2.0F, 2.0F);
        crossbow.addChild(stock);
        setRotationAngle(stock, -0.0873F, 0.0F, 0.0F);
        stock.texOffs(64, 77).addBox(-3.0F, -10.0F, 19.0F, 7.0F, 2.0F, 24.0F, 0.0F, false);
        stock.texOffs(64, 77).addBox(-3.0F, -8.0076F, 25.1743F, 7.0F, 2.0F, 16.0F, 0.0F, false);
        stock.texOffs(64, 77).addBox(-3.0F, -6.0152F, 28.3486F, 7.0F, 2.0F, 13.0F, 0.0F, false);
        stock.texOffs(64, 77).addBox(-3.0F, -4.0228F, 31.5229F, 7.0F, 2.0F, 10.0F, 0.0F, false);
        stock.texOffs(64, 77).addBox(-3.0F, -2.0479F, 34.8965F, 7.0F, 2.0F, 7.0F, 0.0F, false);
        stock.texOffs(64, 77).addBox(-3.0F, -0.0381F, 38.8716F, 7.0F, 2.0F, 2.0F, 0.0F, false);
        stock.texOffs(64, 77).addBox(-3.0F, -7.9902F, 40.9751F, 7.0F, 12.0F, 2.0F, 0.0F, false);

        angle = new ModelRenderer(this);
        angle.setPos(0.0F, -6.9734F, -0.6101F);
        stock.addChild(angle);
        setRotationAngle(angle, -0.5236F, 0.0F, 0.0F);
        angle.texOffs(64, 77).addBox(-3.0F, -13.7588F, 17.6319F, 7.0F, 2.0F, 24.0F, 0.0F, false);

        trigger = new ModelRenderer(this);
        trigger.setPos(0.0F, 1.0F, 2.0F);
        crossbow.addChild(trigger);
        trigger.texOffs(72, 83).addBox(-3.5F, -6.3F, 7.2F, 7.0F, 1.0F, 6.0F, 0.0F, false);
        trigger.texOffs(14, 95).addBox(-1.5F, -9.3F, 10.2F, 3.0F, 2.0F, 1.0F, 0.0F, false);

        angle2 = new ModelRenderer(this);
        angle2.setPos(0.0F, 0.0F, 0.0F);
        trigger.addChild(angle2);
        setRotationAngle(angle2, 0.1745F, 0.0F, 0.0F);
        angle2.texOffs(72, 83).addBox(-3.5F, -8.0F, 8.0F, 7.0F, 4.0F, 1.0F, 0.0F, false);
        angle2.texOffs(72, 83).addBox(-3.5F, -6.9755F, 13.8104F, 7.0F, 4.0F, 1.0F, 0.0F, false);
        angle2.texOffs(6, 82).addBox(-3.5F, -6.8018F, 14.7952F, 7.0F, 13.0F, 5.0F, 0.0F, false);

        arrow = new ModelRenderer(this);
        arrow.setPos(0.0F, 24.0F, 0.0F);
        arrow.texOffs(0, 0).addBox(-0.5F, -15.25F, -72.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        arrow.texOffs(0, 0).addBox(-1.0F, -15.75F, -70.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        arrow.texOffs(6, 69).addBox(-1.0F, -15.75F, -67.0F, 2.0F, 2.0F, 22.0F, 0.0F, false);
        arrow.texOffs(6, 69).addBox(-1.0F, -15.75F, -45.0F, 2.0F, 2.0F, 22.0F, 0.0F, false);
        arrow.texOffs(6, 69).addBox(-1.0F, -15.75F, -23.0F, 2.0F, 2.0F, 22.0F, 0.0F, false);
        arrow.texOffs(6, 69).addBox(-1.0F, -15.75F, -1.0F, 2.0F, 2.0F, 11.0F, 0.0F, false);
    }

    @Override
    public void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        crossbow.render(matrix, builder, light, overlay);
        if (stack.hasTag() && stack.getTag().getInt("ammo") > 0) arrow.render(matrix, builder, light, overlay);
    }
}
