package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import lib.toma.animations.api.AnimationStage;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class WoodenCrossbowModel extends AbstractWeaponModel {

    private final ModelRenderer crossbow;
    private final ModelRenderer stock;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer cube_r11;
    private final ModelRenderer cube_r12;
    private final ModelRenderer cube_r13;
    private final ModelRenderer cube_r14;
    private final ModelRenderer cube_r15;
    private final ModelRenderer grip;
    private final ModelRenderer cube_r16;
    private final ModelRenderer cube_r17;
    private final ModelRenderer cube_r18;
    private final ModelRenderer cube_r19;
    private final ModelRenderer cube_r20;
    private final ModelRenderer cube_r21;
    private final ModelRenderer cube_r22;
    private final ModelRenderer cube_r23;
    private final ModelRenderer grip1;
    private final ModelRenderer cube_r24;
    private final ModelRenderer cube_r25;
    private final ModelRenderer cube_r26;
    private final ModelRenderer cube_r27;
    private final ModelRenderer receiver;
    private final ModelRenderer cube_r28;
    private final ModelRenderer cube_r29;
    private final ModelRenderer cube_r30;
    private final ModelRenderer cube_r31;
    private final ModelRenderer cube_r32;
    private final ModelRenderer cube_r33;
    private final ModelRenderer cube_r34;
    private final ModelRenderer cube_r35;
    private final ModelRenderer cube_r36;
    private final ModelRenderer cube_r37;
    private final ModelRenderer cube_r38;
    private final ModelRenderer cube_r39;
    private final ModelRenderer cube_r40;
    private final ModelRenderer cube_r41;
    private final ModelRenderer cube_r42;
    private final ModelRenderer cube_r43;
    private final ModelRenderer cube_r44;
    private final ModelRenderer cube_r45;
    private final ModelRenderer cube_r46;
    private final ModelRenderer cube_r47;
    private final ModelRenderer cube_r48;
    private final ModelRenderer toprail;
    private final ModelRenderer cube_r51;
    private final ModelRenderer cube_r52;
    private final ModelRenderer arms_charged;
    private final ModelRenderer left;
    private final ModelRenderer left_base;
    private final ModelRenderer cube_r57;
    private final ModelRenderer cube_r58;
    private final ModelRenderer left_1;
    private final ModelRenderer left_2;
    private final ModelRenderer left_3;
    private final ModelRenderer cube_r59;
    private final ModelRenderer cube_r60;
    private final ModelRenderer left_cable;
    private final ModelRenderer cube_r61;
    private final ModelRenderer cube_r62;
    private final ModelRenderer right3;
    private final ModelRenderer right_base3;
    private final ModelRenderer cube_r63;
    private final ModelRenderer cube_r64;
    private final ModelRenderer arms;
    private final ModelRenderer left2;
    private final ModelRenderer left_base2;
    private final ModelRenderer cube_r69;
    private final ModelRenderer cube_r70;
    private final ModelRenderer left_4;
    private final ModelRenderer left_5;
    private final ModelRenderer left_6;
    private final ModelRenderer cube_r71;
    private final ModelRenderer cube_r72;
    private final ModelRenderer left_cable2;
    private final ModelRenderer cube_r73;
    private final ModelRenderer right2;
    private final ModelRenderer right_base2;
    private final ModelRenderer cube_r74;
    private final ModelRenderer cube_r75;
    private final ModelRenderer right_2;
    private final ModelRenderer right_3;
    private final ModelRenderer right_7;
    private final ModelRenderer right_8;
    private final ModelRenderer right_9;
    private final ModelRenderer cube_r65;
    private final ModelRenderer cube_r66;
    private final ModelRenderer right_cable3;
    private final ModelRenderer cube_r67;
    private final ModelRenderer cube_r68;
    private final ModelRenderer cube_r76;
    private final ModelRenderer cube_r77;
    private final ModelRenderer right_cable2;
    private final ModelRenderer cube_r78;
    private final ModelRenderer arrow;
    private final ModelRenderer fletch;

    public WoodenCrossbowModel() {
        texWidth = 512;
        texHeight = 512;

        crossbow = new ModelRenderer(this);
        crossbow.setPos(0.0F, 24.0F, 0.0F);


        stock = new ModelRenderer(this);
        stock.setPos(2.2071F, -4.5858F, -26.0F);
        crossbow.addChild(stock);
        stock.texOffs(162, 71).addBox(-3.7071F, -0.4142F, 5.0F, 3.0F, 3.0F, 11.0F, 0.0F, false);
        stock.texOffs(145, 159).addBox(-4.2071F, -0.9142F, -7.0F, 4.0F, 4.0F, 12.0F, 0.0F, false);
        stock.texOffs(147, 157).addBox(-3.7071F, -1.4142F, -8.0F, 3.0F, 1.0F, 12.0F, 0.0F, false);
        stock.texOffs(157, 169).addBox(-3.7071F, 7.3789F, -8.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        stock.texOffs(167, 154).addBox(-4.6037F, -0.5176F, -8.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        stock.texOffs(157, 159).addBox(-3.7071F, -0.5176F, -7.8F, 3.0F, 8.0F, 1.0F, 0.0F, false);
        stock.texOffs(154, 157).addBox(-3.2071F, 2.4824F, -4.8F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        stock.texOffs(157, 157).addBox(-0.8105F, -0.5176F, -8.0F, 1.0F, 8.0F, 1.0F, 0.0F, true);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0F, 0.0F, 0.0F);
        stock.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, 0.7854F);
        cube_r1.texOffs(137, 149).addBox(-1.5F, -0.5F, -7.0F, 3.0F, 1.0F, 10.0F, 0.0F, true);
        cube_r1.texOffs(162, 152).addBox(-1.5F, -0.5F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r1.texOffs(154, 152).addBox(-1.5F, -0.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(0.5777F, 0.5777F, -6.817F);
        stock.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, -0.5236F, 0.7854F);
        cube_r2.texOffs(162, 154).addBox(-1.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(0.5777F, 0.5777F, 2.817F);
        stock.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.5236F, 0.7854F);
        cube_r3.texOffs(145, 154).addBox(-1.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-4.9919F, 0.5777F, -6.817F);
        stock.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.5236F, -0.7854F);
        cube_r4.texOffs(154, 154).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(-4.9919F, 0.5777F, 2.817F);
        stock.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, -0.5236F, -0.7854F);
        cube_r5.texOffs(149, 159).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(0.1895F, 6.7753F, -4.0F);
        stock.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, -0.7854F);
        cube_r6.texOffs(167, 157).addBox(-1.5F, -0.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r7 = new ModelRenderer(this);
        cube_r7.setPos(0.1895F, 0.1895F, -4.0F);
        stock.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, 0.7854F);
        cube_r7.texOffs(174, 169).addBox(-1.5F, -0.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r7.texOffs(152, 149).addBox(-1.5F, -0.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r8 = new ModelRenderer(this);
        cube_r8.setPos(-4.6037F, 6.7753F, -4.0F);
        stock.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.0F, 0.7854F);
        cube_r8.texOffs(149, 147).addBox(0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r9 = new ModelRenderer(this);
        cube_r9.setPos(-4.6037F, 0.1895F, -4.0F);
        stock.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.0F, -0.7854F);
        cube_r9.texOffs(149, 162).addBox(0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r9.texOffs(162, 162).addBox(0.5F, -0.5F, 7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r10 = new ModelRenderer(this);
        cube_r10.setPos(0.0F, 6.9647F, -11.0F);
        stock.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.0F, -0.7854F);
        cube_r10.texOffs(149, 164).addBox(-1.5F, -0.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r11 = new ModelRenderer(this);
        cube_r11.setPos(-4.4142F, 6.9647F, -11.0F);
        stock.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, 0.0F, 0.7854F);
        cube_r11.texOffs(162, 149).addBox(0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r12 = new ModelRenderer(this);
        cube_r12.setPos(-4.4142F, 0.0F, -11.0F);
        stock.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, 0.0F, -0.7854F);
        cube_r12.texOffs(174, 157).addBox(0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r12.texOffs(171, 171).addBox(0.5F, -0.5F, 14.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r12.texOffs(134, 149).addBox(-1.5F, -0.5F, 4.0F, 3.0F, 1.0F, 10.0F, 0.0F, false);

        cube_r13 = new ModelRenderer(this);
        cube_r13.setPos(-2.7071F, 5.5309F, -5.933F);
        stock.addChild(cube_r13);
        setRotationAngle(cube_r13, -0.5236F, 0.0F, 0.0F);
        cube_r13.texOffs(152, 159).addBox(-0.5F, -2.8F, -2.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        cube_r13.texOffs(164, 145).addBox(-0.5F, -3.0F, -1.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        cube_r13.texOffs(164, 154).addBox(-0.5F, -3.0F, -0.5F, 2.0F, 6.0F, 1.0F, 0.0F, false);

        cube_r14 = new ModelRenderer(this);
        cube_r14.setPos(-2.2071F, 1.6539F, -2.5071F);
        stock.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.7854F, 0.0F, 0.0F);
        cube_r14.texOffs(157, 152).addBox(-1.0F, 1.5F, -1.5F, 2.0F, 1.0F, 3.0F, 0.0F, false);

        cube_r15 = new ModelRenderer(this);
        cube_r15.setPos(-2.2071F, 3.5858F, 2.5F);
        stock.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0F, -0.7854F, 0.0F);
        cube_r15.texOffs(154, 83).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);

        grip = new ModelRenderer(this);
        grip.setPos(-1.5F, 0.866F, 0.5F);
        crossbow.addChild(grip);
        grip.texOffs(162, 154).addBox(0.0F, -2.0F, -9.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);
        grip.texOffs(152, 164).addBox(0.0F, -2.0F, -10.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        grip.texOffs(159, 162).addBox(0.0F, -2.0F, -10.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);

        cube_r16 = new ModelRenderer(this);
        cube_r16.setPos(1.634F, -0.634F, 0.5F);
        grip.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.0F, 0.0F, 0.5236F);


        cube_r17 = new ModelRenderer(this);
        cube_r17.setPos(1.366F, -0.634F, 0.5F);
        grip.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.0F, 0.0F, -0.5236F);


        cube_r18 = new ModelRenderer(this);
        cube_r18.setPos(1.5F, 9.4657F, -4.965F);
        grip.addChild(cube_r18);
        setRotationAngle(cube_r18, -1.0472F, 0.0F, 0.0F);


        cube_r19 = new ModelRenderer(this);
        cube_r19.setPos(1.5F, 8.3729F, -3.4044F);
        grip.addChild(cube_r19);
        setRotationAngle(cube_r19, -0.8727F, 0.0F, 0.0F);


        cube_r20 = new ModelRenderer(this);
        cube_r20.setPos(1.5F, 7.0257F, -2.0571F);
        grip.addChild(cube_r20);
        setRotationAngle(cube_r20, -0.6981F, 0.0F, 0.0F);


        cube_r21 = new ModelRenderer(this);
        cube_r21.setPos(1.5F, 5.465F, -0.9643F);
        grip.addChild(cube_r21);
        setRotationAngle(cube_r21, -0.5236F, 0.0F, 0.0F);


        cube_r22 = new ModelRenderer(this);
        cube_r22.setPos(1.5F, 3.7383F, -0.1592F);
        grip.addChild(cube_r22);
        setRotationAngle(cube_r22, -0.3491F, 0.0F, 0.0F);


        cube_r23 = new ModelRenderer(this);
        cube_r23.setPos(1.5F, 1.898F, 0.3339F);
        grip.addChild(cube_r23);
        setRotationAngle(cube_r23, -0.1745F, 0.0F, 0.0F);


        grip1 = new ModelRenderer(this);
        grip1.setPos(1.5F, 1.0F, -8.366F);
        grip.addChild(grip1);
        setRotationAngle(grip1, -0.3054F, 0.0F, 0.0F);
        grip1.texOffs(164, 164).addBox(1.0F, -1.6652F, -1.9102F, 1.0F, 5.0F, 3.0F, 0.0F, true);
        grip1.texOffs(159, 162).addBox(-1.5F, -1.6652F, -2.7762F, 3.0F, 5.0F, 1.0F, 0.0F, false);
        grip1.texOffs(157, 152).addBox(-2.0F, -1.6652F, -1.9102F, 1.0F, 5.0F, 3.0F, 0.0F, false);
        grip1.texOffs(149, 167).addBox(-1.5F, -1.6652F, 0.9558F, 3.0F, 5.0F, 1.0F, 0.0F, false);

        cube_r24 = new ModelRenderer(this);
        cube_r24.setPos(0.451F, -0.6652F, 0.7728F);
        grip1.addChild(cube_r24);
        setRotationAngle(cube_r24, 0.0F, -0.5236F, 0.0F);
        cube_r24.texOffs(159, 164).addBox(0.5F, -1.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);

        cube_r25 = new ModelRenderer(this);
        cube_r25.setPos(-0.451F, -0.6652F, 0.7728F);
        grip1.addChild(cube_r25);
        setRotationAngle(cube_r25, 0.0F, 0.5236F, 0.0F);
        cube_r25.texOffs(140, 147).addBox(-1.5F, -1.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        cube_r26 = new ModelRenderer(this);
        cube_r26.setPos(-0.451F, -0.6652F, -1.5932F);
        grip1.addChild(cube_r26);
        setRotationAngle(cube_r26, 0.0F, -0.5236F, 0.0F);
        cube_r26.texOffs(159, 152).addBox(-1.5F, -1.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        cube_r27 = new ModelRenderer(this);
        cube_r27.setPos(0.451F, -0.6652F, -1.5932F);
        grip1.addChild(cube_r27);
        setRotationAngle(cube_r27, 0.0F, 0.5236F, 0.0F);
        cube_r27.texOffs(157, 162).addBox(0.5F, -1.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);

        receiver = new ModelRenderer(this);
        receiver.setPos(0.0F, 0.0F, 0.0F);
        crossbow.addChild(receiver);
        receiver.texOffs(147, 154).addBox(-2.5F, -4.0F, -8.0F, 1.0F, 2.0F, 16.0F, 0.0F, false);
        receiver.texOffs(157, 159).addBox(-2.5F, -4.0F, 8.0F, 1.0F, 2.0F, 7.0F, 0.0F, false);
        receiver.texOffs(167, 169).addBox(-2.5F, -3.0F, 15.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        receiver.texOffs(152, 157).addBox(-2.5F, -5.0F, -11.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        receiver.texOffs(137, 157).addBox(-2.5F, -6.0F, -11.0F, 5.0F, 1.0F, 4.0F, 0.0F, false);
        receiver.texOffs(132, 142).addBox(1.5F, -4.0F, -8.0F, 1.0F, 2.0F, 16.0F, 0.0F, false);
        receiver.texOffs(147, 159).addBox(1.5F, -4.0F, 8.0F, 1.0F, 2.0F, 7.0F, 0.0F, false);
        receiver.texOffs(137, 162).addBox(1.5F, -3.0F, 15.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        receiver.texOffs(147, 142).addBox(1.5F, -5.0F, -11.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        receiver.texOffs(135, 157).addBox(-2.0F, -2.134F, -11.0F, 4.0F, 1.0F, 19.0F, 0.0F, false);
        receiver.texOffs(162, 157).addBox(-2.0F, -2.134F, 8.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);
        receiver.texOffs(193, 9).addBox(-0.5F, -3.134F, -10.0F, 1.0F, 1.0F, 30.0F, 0.0F, false);

        cube_r28 = new ModelRenderer(this);
        cube_r28.setPos(-0.5179F, -2.567F, 12.5F);
        receiver.addChild(cube_r28);
        setRotationAngle(cube_r28, 0.0F, 0.0F, -0.5236F);
        cube_r28.texOffs(154, 142).addBox(-2.0F, -0.5F, -4.5F, 1.0F, 1.0F, 8.0F, 0.0F, true);
        cube_r28.texOffs(135, 149).addBox(-2.0F, -0.5F, -23.5F, 1.0F, 1.0F, 19.0F, 0.0F, true);

        cube_r29 = new ModelRenderer(this);
        cube_r29.setPos(0.5179F, -2.567F, 12.5F);
        receiver.addChild(cube_r29);
        setRotationAngle(cube_r29, 0.0F, 0.0F, 0.5236F);
        cube_r29.texOffs(154, 162).addBox(1.0F, -0.5F, -4.5F, 1.0F, 1.0F, 8.0F, 0.0F, false);
        cube_r29.texOffs(142, 147).addBox(1.0F, -0.5F, -23.5F, 1.0F, 1.0F, 19.0F, 0.0F, false);

        cube_r30 = new ModelRenderer(this);
        cube_r30.setPos(0.0F, -0.951F, 26.317F);
        receiver.addChild(cube_r30);
        setRotationAngle(cube_r30, -0.5236F, 0.0F, 0.0F);


        cube_r31 = new ModelRenderer(this);
        cube_r31.setPos(0.0F, -3.317F, 26.317F);
        receiver.addChild(cube_r31);
        setRotationAngle(cube_r31, 0.5236F, 0.0F, 0.0F);


        cube_r32 = new ModelRenderer(this);
        cube_r32.setPos(-0.683F, -2.817F, 18.0F);
        receiver.addChild(cube_r32);
        setRotationAngle(cube_r32, 0.0F, 0.0F, 0.5236F);
        cube_r32.texOffs(194, 10).addBox(-0.5F, -0.5F, -28.0F, 1.0F, 1.0F, 30.0F, 0.0F, true);

        cube_r33 = new ModelRenderer(this);
        cube_r33.setPos(0.683F, -2.817F, 18.0F);
        receiver.addChild(cube_r33);
        setRotationAngle(cube_r33, 0.0F, 0.0F, -0.5236F);
        cube_r33.texOffs(191, 5).addBox(-0.5F, -0.5F, -27.0F, 1.0F, 1.0F, 29.0F, 0.0F, false);

        cube_r34 = new ModelRenderer(this);
        cube_r34.setPos(0.0133F, -1.1823F, 19.0668F);
        receiver.addChild(cube_r34);
        setRotationAngle(cube_r34, -0.3054F, 0.0F, 0.0F);


        cube_r35 = new ModelRenderer(this);
        cube_r35.setPos(1.744F, -7.3F, 0.4469F);
        receiver.addChild(cube_r35);
        setRotationAngle(cube_r35, 0.0F, -0.0873F, 0.0F);


        cube_r36 = new ModelRenderer(this);
        cube_r36.setPos(-1.744F, -7.3F, 0.4469F);
        receiver.addChild(cube_r36);
        setRotationAngle(cube_r36, 0.0F, 0.0873F, 0.0F);


        cube_r37 = new ModelRenderer(this);
        cube_r37.setPos(3.0F, -3.683F, 9.9869F);
        receiver.addChild(cube_r37);
        setRotationAngle(cube_r37, 0.5236F, 0.0F, 0.0F);


        cube_r38 = new ModelRenderer(this);
        cube_r38.setPos(3.9999F, -3.683F, -10.2811F);
        receiver.addChild(cube_r38);
        setRotationAngle(cube_r38, 0.5236F, 0.0F, 0.0F);


        cube_r39 = new ModelRenderer(this);
        cube_r39.setPos(3.9999F, -3.683F, -1.0131F);
        receiver.addChild(cube_r39);
        setRotationAngle(cube_r39, 0.5236F, 0.0F, 0.0F);


        cube_r40 = new ModelRenderer(this);
        cube_r40.setPos(0.0F, -6.9821F, -8.701F);
        receiver.addChild(cube_r40);
        setRotationAngle(cube_r40, -0.5236F, 0.0F, 0.0F);


        cube_r41 = new ModelRenderer(this);
        cube_r41.setPos(-1.909F, -6.0336F, 9.0F);
        receiver.addChild(cube_r41);
        setRotationAngle(cube_r41, 0.0F, 0.0F, -0.1309F);


        cube_r42 = new ModelRenderer(this);
        cube_r42.setPos(1.909F, -6.0336F, 9.0F);
        receiver.addChild(cube_r42);
        setRotationAngle(cube_r42, 0.0F, 0.0F, 0.1309F);


        cube_r43 = new ModelRenderer(this);
        cube_r43.setPos(-1.909F, -7.8946F, 9.0F);
        receiver.addChild(cube_r43);
        setRotationAngle(cube_r43, 0.0F, 0.0F, 0.1309F);


        cube_r44 = new ModelRenderer(this);
        cube_r44.setPos(1.909F, -7.8946F, 9.0F);
        receiver.addChild(cube_r44);
        setRotationAngle(cube_r44, 0.0F, 0.0F, -0.1309F);


        cube_r45 = new ModelRenderer(this);
        cube_r45.setPos(-1.683F, -8.7811F, 0.5F);
        receiver.addChild(cube_r45);
        setRotationAngle(cube_r45, 0.0F, 0.0F, -0.5236F);


        cube_r46 = new ModelRenderer(this);
        cube_r46.setPos(1.317F, -8.7811F, 4.5F);
        receiver.addChild(cube_r46);
        setRotationAngle(cube_r46, 0.0F, 0.0F, -0.5236F);


        cube_r47 = new ModelRenderer(this);
        cube_r47.setPos(1.683F, -8.7811F, 0.5F);
        receiver.addChild(cube_r47);
        setRotationAngle(cube_r47, 0.0F, 0.0F, 0.5236F);


        cube_r48 = new ModelRenderer(this);
        cube_r48.setPos(-1.317F, -8.7811F, 4.5F);
        receiver.addChild(cube_r48);
        setRotationAngle(cube_r48, 0.0F, 0.0F, 0.5236F);


        toprail = new ModelRenderer(this);
        toprail.setPos(-0.5F, -9.366F, -7.0F);
        crossbow.addChild(toprail);
        toprail.texOffs(89, 102).addBox(-0.5F, 1.4019F, 3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        toprail.texOffs(89, 102).addBox(-0.5F, 1.4019F, 1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        toprail.texOffs(89, 102).addBox(-0.5F, 1.4019F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        toprail.texOffs(89, 102).addBox(-0.5F, 1.4019F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        toprail.texOffs(89, 102).addBox(-0.5F, 1.4019F, -5.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        toprail.texOffs(89, 102).addBox(-0.5F, 1.4019F, -7.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        toprail.texOffs(89, 102).addBox(0.3652F, 1.9014F, 2.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        toprail.texOffs(89, 102).addBox(0.3652F, 1.9014F, 0.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        toprail.texOffs(89, 102).addBox(0.3652F, 1.9014F, -2.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        toprail.texOffs(89, 102).addBox(0.3652F, 1.9014F, -4.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        toprail.texOffs(89, 102).addBox(0.3652F, 1.9014F, -6.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        toprail.texOffs(89, 102).addBox(-1.3652F, 1.9014F, 2.0F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        toprail.texOffs(89, 102).addBox(-1.3652F, 1.9014F, 0.0F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        toprail.texOffs(89, 102).addBox(-1.3652F, 1.9014F, -2.0F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        toprail.texOffs(89, 102).addBox(-1.3652F, 1.9014F, -4.0F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        toprail.texOffs(89, 102).addBox(-1.3652F, 1.9014F, -6.0F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        toprail.texOffs(89, 102).addBox(-1.0F, 2.4019F, -7.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);

        cube_r51 = new ModelRenderer(this);
        cube_r51.setPos(-1.116F, 2.3349F, -5.5F);
        toprail.addChild(cube_r51);
        setRotationAngle(cube_r51, 0.0F, 0.0F, -0.5236F);
        cube_r51.texOffs(89, 102).addBox(0.0F, -0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r51.texOffs(89, 102).addBox(0.0F, -0.5F, 1.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r51.texOffs(89, 102).addBox(0.0F, -0.5F, 3.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r51.texOffs(89, 102).addBox(0.0F, -0.5F, 5.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r51.texOffs(89, 102).addBox(0.0F, -0.5F, 7.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r51.texOffs(89, 102).addBox(0.0F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r51.texOffs(89, 102).addBox(0.0F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r51.texOffs(89, 102).addBox(0.0F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r51.texOffs(89, 102).addBox(0.0F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r51.texOffs(89, 102).addBox(0.0F, -0.5F, 6.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r51.texOffs(89, 102).addBox(0.0F, -0.5F, 8.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r52 = new ModelRenderer(this);
        cube_r52.setPos(2.116F, 2.3349F, -5.5F);
        toprail.addChild(cube_r52);
        setRotationAngle(cube_r52, 0.0F, 0.0F, 0.5236F);
        cube_r52.texOffs(89, 102).addBox(0.0F, -0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r52.texOffs(89, 102).addBox(0.0F, -0.5F, 1.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r52.texOffs(89, 102).addBox(0.0F, -0.5F, 3.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r52.texOffs(89, 102).addBox(0.0F, -0.5F, 5.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r52.texOffs(89, 102).addBox(0.0F, -0.5F, 7.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r52.texOffs(89, 102).addBox(-1.0F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r52.texOffs(89, 102).addBox(-1.0F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r52.texOffs(89, 102).addBox(-1.0F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r52.texOffs(89, 102).addBox(-1.0F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r52.texOffs(89, 102).addBox(-1.0F, -0.5F, 6.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r52.texOffs(89, 102).addBox(-1.0F, -0.5F, 8.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        arms_charged = new ModelRenderer(this);
        arms_charged.setPos(0.0F, 24.0F, 0.0F);


        left = new ModelRenderer(this);
        left.setPos(0.0F, 0.0F, 0.0F);
        arms_charged.addChild(left);


        left_base = new ModelRenderer(this);
        left_base.setPos(-2.183F, -2.683F, 15.5F);
        left.addChild(left_base);


        cube_r57 = new ModelRenderer(this);
        cube_r57.setPos(0.0F, 0.0F, 0.0F);
        left_base.addChild(cube_r57);
        setRotationAngle(cube_r57, 0.0F, 0.0F, -0.5236F);


        cube_r58 = new ModelRenderer(this);
        cube_r58.setPos(0.0F, -1.634F, 0.0F);
        left_base.addChild(cube_r58);
        setRotationAngle(cube_r58, 0.0F, 0.0F, 0.5236F);


        left_1 = new ModelRenderer(this);
        left_1.setPos(-4.183F, -0.817F, 0.5F);
        left_base.addChild(left_1);
        setRotationAngle(left_1, 0.0F, -0.2182F, 0.0F);
        left_1.texOffs(159, 162).addBox(-4.0F, -1.5F, -1.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        left_1.texOffs(154, 169).addBox(-4.0F, 0.5F, -1.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        left_2 = new ModelRenderer(this);
        left_2.setPos(-4.0F, 0.0F, 0.0F);
        left_1.addChild(left_2);
        setRotationAngle(left_2, 0.0F, -0.2182F, 0.0F);
        left_2.texOffs(149, 152).addBox(-4.0F, -1.5F, -1.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        left_2.texOffs(169, 145).addBox(-4.0F, 0.5F, -1.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        left_3 = new ModelRenderer(this);
        left_3.setPos(-4.0F, 0.0F, 0.0F);
        left_2.addChild(left_3);
        setRotationAngle(left_3, 0.0F, -0.2182F, 0.0F);
        left_3.texOffs(147, 149).addBox(-4.0F, -1.5F, -1.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        left_3.texOffs(157, 162).addBox(-4.866F, -1.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        left_3.texOffs(154, 179).addBox(-4.0F, 0.5F, -1.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r59 = new ModelRenderer(this);
        cube_r59.setPos(-2.884F, -1.567F, -0.5F);
        left_3.addChild(cube_r59);
        setRotationAngle(cube_r59, 0.0F, 0.0F, -0.5236F);
        cube_r59.texOffs(145, 174).addBox(-2.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r60 = new ModelRenderer(this);
        cube_r60.setPos(-2.884F, 1.567F, -0.5F);
        left_3.addChild(cube_r60);
        setRotationAngle(cube_r60, 0.0F, 0.0F, 0.5236F);
        cube_r60.texOffs(174, 159).addBox(-2.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        left_cable = new ModelRenderer(this);
        left_cable.setPos(-4.366F, 0.0F, -1.0F);
        left_3.addChild(left_cable);
        setRotationAngle(left_cable, 0.0F, -0.0873F, 0.0F);


        cube_r61 = new ModelRenderer(this);
        cube_r61.setPos(0.5F, -0.5F, -2.0F);
        left_cable.addChild(cube_r61);
        setRotationAngle(cube_r61, 0.0F, 0.0436F, 0.0F);
        cube_r61.texOffs(197, 149).addBox(-1.0F, -1.0F, -22.0F, 1.0F, 1.0F, 23.0F, 0.0F, false);

        cube_r62 = new ModelRenderer(this);
        cube_r62.setPos(0.0F, -0.5F, -0.5F);
        left_cable.addChild(cube_r62);
        setRotationAngle(cube_r62, 0.0F, -0.7854F, 0.0F);
        cube_r62.texOffs(157, 103).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        right3 = new ModelRenderer(this);
        right3.setPos(0.0F, 0.0F, 0.0F);
        arms_charged.addChild(right3);


        right_base3 = new ModelRenderer(this);
        right_base3.setPos(2.183F, -2.683F, 15.5F);
        right3.addChild(right_base3);


        cube_r63 = new ModelRenderer(this);
        cube_r63.setPos(0.0F, 0.0F, 0.0F);
        right_base3.addChild(cube_r63);
        setRotationAngle(cube_r63, 0.0F, 0.0F, 0.5236F);


        cube_r64 = new ModelRenderer(this);
        cube_r64.setPos(0.0F, -1.634F, 0.0F);
        right_base3.addChild(cube_r64);
        setRotationAngle(cube_r64, 0.0F, 0.0F, -0.5236F);


        arms = new ModelRenderer(this);
        arms.setPos(0.0F, 24.0F, 0.0F);


        left2 = new ModelRenderer(this);
        left2.setPos(0.0F, 0.0F, 0.0F);
        arms.addChild(left2);


        left_base2 = new ModelRenderer(this);
        left_base2.setPos(-2.183F, -2.683F, 15.5F);
        left2.addChild(left_base2);
        left_base2.texOffs(162, 154).addBox(-0.317F, -1.817F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        left_base2.texOffs(162, 149).addBox(-4.183F, -2.317F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        left_base2.texOffs(157, 154).addBox(-4.183F, -0.317F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        left_base2.texOffs(164, 154).addBox(-1.683F, -1.317F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r69 = new ModelRenderer(this);
        cube_r69.setPos(0.0F, 0.0F, 0.0F);
        left_base2.addChild(cube_r69);
        setRotationAngle(cube_r69, 0.0F, 0.0F, -0.5236F);
        cube_r69.texOffs(152, 145).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r70 = new ModelRenderer(this);
        cube_r70.setPos(0.0F, -1.634F, 0.0F);
        left_base2.addChild(cube_r70);
        setRotationAngle(cube_r70, 0.0F, 0.0F, 0.5236F);
        cube_r70.texOffs(169, 154).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        left_4 = new ModelRenderer(this);
        left_4.setPos(-4.183F, -0.817F, 0.5F);
        left_base2.addChild(left_4);
        setRotationAngle(left_4, 0.0F, -0.0436F, 0.0F);


        left_5 = new ModelRenderer(this);
        left_5.setPos(-4.0F, 0.0F, 0.0F);
        left_4.addChild(left_5);
        setRotationAngle(left_5, 0.0F, -0.0436F, 0.0F);


        left_6 = new ModelRenderer(this);
        left_6.setPos(-4.0F, 0.0F, 0.0F);
        left_5.addChild(left_6);
        setRotationAngle(left_6, 0.0F, -0.0436F, 0.0F);


        cube_r71 = new ModelRenderer(this);
        cube_r71.setPos(-2.884F, -1.567F, -0.5F);
        left_6.addChild(cube_r71);
        setRotationAngle(cube_r71, 0.0F, 0.0F, -0.5236F);


        cube_r72 = new ModelRenderer(this);
        cube_r72.setPos(-2.884F, 1.567F, -0.5F);
        left_6.addChild(cube_r72);
        setRotationAngle(cube_r72, 0.0F, 0.0F, 0.5236F);


        left_cable2 = new ModelRenderer(this);
        left_cable2.setPos(-4.366F, 0.0F, -1.0F);
        left_6.addChild(left_cable2);
        setRotationAngle(left_cable2, 0.0F, -1.0472F, 0.0F);


        cube_r73 = new ModelRenderer(this);
        cube_r73.setPos(0.0F, -0.5F, -0.5F);
        left_cable2.addChild(cube_r73);
        setRotationAngle(cube_r73, 0.0F, -0.7854F, 0.0F);


        right2 = new ModelRenderer(this);
        right2.setPos(0.0F, 0.0F, 0.0F);
        arms.addChild(right2);


        right_base2 = new ModelRenderer(this);
        right_base2.setPos(2.183F, -2.683F, 15.5F);
        right2.addChild(right_base2);
        right_base2.texOffs(149, 142).addBox(-0.683F, -1.817F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        right_base2.texOffs(149, 145).addBox(0.183F, -2.317F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, true);
        right_base2.texOffs(162, 179).addBox(0.183F, -0.317F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, true);
        right_base2.texOffs(162, 152).addBox(0.683F, -1.317F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r74 = new ModelRenderer(this);
        cube_r74.setPos(0.0F, 0.0F, 0.0F);
        right_base2.addChild(cube_r74);
        setRotationAngle(cube_r74, 0.0F, 0.0F, 0.5236F);
        cube_r74.texOffs(169, 157).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r75 = new ModelRenderer(this);
        cube_r75.setPos(0.0F, -1.634F, 0.0F);
        right_base2.addChild(cube_r75);
        setRotationAngle(cube_r75, 0.0F, 0.0F, -0.5236F);
        cube_r75.texOffs(169, 169).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        right_2 = new ModelRenderer(this);
        right_2.setPos(4.183F, -0.817F, 0.5F);
        right_base2.addChild(right_2);
        setRotationAngle(right_2, 0.0F, 0.0436F, 0.0F);
        right_2.texOffs(162, 157).addBox(0.0F, -1.5F, -1.0F, 4.0F, 1.0F, 1.0F, 0.0F, true);
        right_2.texOffs(147, 147).addBox(0.0F, 0.5F, -1.0F, 4.0F, 1.0F, 1.0F, 0.0F, true);

        right_3 = new ModelRenderer(this);
        right_3.setPos(4.0F, 0.0F, 0.0F);
        right_2.addChild(right_3);
        setRotationAngle(right_3, 0.0F, 0.0436F, 0.0F);


        right_7 = new ModelRenderer(this);
        right_7.setPos(4.0F, 0.0F, 0.0F);
        right_3.addChild(right_7);
        setRotationAngle(right_7, 0.0F, 0.0436F, 0.0F);


        right_8 = new ModelRenderer(this);
        right_8.setPos(-4.0F, 0.0F, 0.0F);
        right_7.addChild(right_8);
        setRotationAngle(right_8, 0.0F, 0.2182F, 0.0F);
        right_8.texOffs(159, 169).addBox(0.0415F, -1.5F, -1.1694F, 4.0F, 1.0F, 1.0F, 0.0F, true);
        right_8.texOffs(169, 171).addBox(0.0415F, 0.5F, -1.1694F, 4.0F, 1.0F, 1.0F, 0.0F, true);

        right_9 = new ModelRenderer(this);
        right_9.setPos(4.0F, 0.0F, 0.0F);
        right_8.addChild(right_9);
        setRotationAngle(right_9, 0.0F, 0.2182F, 0.0F);
        right_9.texOffs(147, 162).addBox(0.0771F, -1.5F, -1.1564F, 4.0F, 1.0F, 1.0F, 0.0F, true);
        right_9.texOffs(169, 167).addBox(3.866F, -1.0F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        right_9.texOffs(152, 152).addBox(0.0771F, 0.5F, -1.1564F, 4.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r65 = new ModelRenderer(this);
        cube_r65.setPos(2.884F, -1.567F, -0.5F);
        right_9.addChild(cube_r65);
        setRotationAngle(cube_r65, 0.0F, 0.0F, 0.5236F);
        cube_r65.texOffs(164, 174).addBox(1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r66 = new ModelRenderer(this);
        cube_r66.setPos(2.884F, 1.567F, -0.5F);
        right_9.addChild(cube_r66);
        setRotationAngle(cube_r66, 0.0F, 0.0F, -0.5236F);
        cube_r66.texOffs(159, 162).addBox(1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        right_cable3 = new ModelRenderer(this);
        right_cable3.setPos(4.366F, 0.0F, -1.0F);
        right_9.addChild(right_cable3);
        setRotationAngle(right_cable3, 0.0F, 0.0873F, 0.0F);


        cube_r67 = new ModelRenderer(this);
        cube_r67.setPos(-0.5F, -0.5F, -2.0F);
        right_cable3.addChild(cube_r67);
        cube_r67.texOffs(200, 144).addBox(0.0F, -1.0F, -23.0F, 1.0F, 1.0F, 24.0F, 0.0F, true);

        cube_r68 = new ModelRenderer(this);
        cube_r68.setPos(0.0F, -0.5F, -0.5F);
        right_cable3.addChild(cube_r68);
        setRotationAngle(cube_r68, 0.0F, 0.7854F, 0.0F);
        cube_r68.texOffs(149, 78).addBox(-0.9F, -1.5F, -0.85F, 2.0F, 3.0F, 2.0F, 0.0F, true);

        cube_r76 = new ModelRenderer(this);
        cube_r76.setPos(2.884F, -1.567F, -0.5F);
        right_7.addChild(cube_r76);
        setRotationAngle(cube_r76, 0.0F, 0.0F, 0.5236F);


        cube_r77 = new ModelRenderer(this);
        cube_r77.setPos(2.884F, 1.567F, -0.5F);
        right_7.addChild(cube_r77);
        setRotationAngle(cube_r77, 0.0F, 0.0F, -0.5236F);


        right_cable2 = new ModelRenderer(this);
        right_cable2.setPos(4.366F, 0.0F, -1.0F);
        right_7.addChild(right_cable2);
        setRotationAngle(right_cable2, 0.0F, 1.0472F, 0.0F);


        cube_r78 = new ModelRenderer(this);
        cube_r78.setPos(0.0F, -0.5F, -0.5F);
        right_cable2.addChild(cube_r78);
        setRotationAngle(cube_r78, 0.0F, 0.7854F, 0.0F);


        arrow = new ModelRenderer(this);
        arrow.setPos(-2.65F, 23.15F, -8.0F);
        setRotationAngle(arrow, 0.0F, 0.0F, 0.7854F);
        arrow.texOffs(64, 70).addBox(-0.5F, -4.25F, 0.0F, 1.0F, 1.0F, 29.0F, 0.0F, false);
        arrow.texOffs(89, 81).addBox(-0.5F, -4.25F, 29.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        fletch = new ModelRenderer(this);
        fletch.setPos(3.0F, -2.0F, 0.0F);
        arrow.addChild(fletch);
        setRotationAngle(fletch, 0.0F, 0.0F, -0.7854F);
        fletch.texOffs(83, 147).addBox(-0.8839F, -5.0659F, 0.0F, 0.0F, 1.0F, 5.0F, 0.0F, false);
        fletch.texOffs(83, 147).addBox(-2.591F, -3.3578F, 0.0F, 1.0F, 0.0F, 5.0F, 0.0F, false);
        fletch.texOffs(83, 147).addBox(-0.1768F, -3.3578F, 0.0F, 1.0F, 0.0F, 5.0F, 0.0F, false);
        fletch.texOffs(85, 147).addBox(-0.8839F, -2.6517F, 0.0F, 0.0F, 1.0F, 5.0F, 0.0F, false);

        setSpecialRenderer(ModAnimations.BULLET, stack -> AbstractGun.getAmmoCount(stack) > 0, arrow);
    }

    @Override
    public void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        matrix.pushPose();
        matrix.mulPose(Vector3f.YN.rotation((float) Math.PI));
        crossbow.render(matrix, builder, light, overlay);
        arms.render(matrix, builder, light, overlay);
        arms_charged.render(matrix, builder, light, overlay);
        arrow.render(matrix, builder, light, overlay);
        matrix.popPose();
    }
}
