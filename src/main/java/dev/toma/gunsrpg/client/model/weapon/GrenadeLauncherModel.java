package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class GrenadeLauncherModel extends AbstractWeaponModel {

    private final ModelRenderer magazine;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer drum;
    private final ModelRenderer magslot1;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer cube_r11;
    private final ModelRenderer cube_r12;
    private final ModelRenderer magslot2;
    private final ModelRenderer cube_r13;
    private final ModelRenderer cube_r14;
    private final ModelRenderer cube_r15;
    private final ModelRenderer cube_r16;
    private final ModelRenderer cube_r17;
    private final ModelRenderer cube_r18;
    private final ModelRenderer cube_r19;
    private final ModelRenderer magslot3;
    private final ModelRenderer cube_r20;
    private final ModelRenderer cube_r21;
    private final ModelRenderer cube_r22;
    private final ModelRenderer cube_r23;
    private final ModelRenderer cube_r24;
    private final ModelRenderer cube_r25;
    private final ModelRenderer cube_r26;
    private final ModelRenderer magslot4;
    private final ModelRenderer cube_r27;
    private final ModelRenderer cube_r28;
    private final ModelRenderer cube_r29;
    private final ModelRenderer cube_r30;
    private final ModelRenderer cube_r31;
    private final ModelRenderer cube_r32;
    private final ModelRenderer cube_r33;
    private final ModelRenderer magslot5;
    private final ModelRenderer cube_r34;
    private final ModelRenderer cube_r35;
    private final ModelRenderer cube_r36;
    private final ModelRenderer cube_r37;
    private final ModelRenderer cube_r38;
    private final ModelRenderer cube_r39;
    private final ModelRenderer cube_r40;
    private final ModelRenderer magslot6;
    private final ModelRenderer cube_r41;
    private final ModelRenderer cube_r42;
    private final ModelRenderer cube_r43;
    private final ModelRenderer cube_r44;
    private final ModelRenderer cube_r45;
    private final ModelRenderer cube_r46;
    private final ModelRenderer cube_r47;
    private final ModelRenderer gun;
    private final ModelRenderer cube_r48;
    private final ModelRenderer cube_r49;
    private final ModelRenderer cube_r50;
    private final ModelRenderer cube_r51;
    private final ModelRenderer cube_r52;
    private final ModelRenderer cube_r53;
    private final ModelRenderer cube_r54;
    private final ModelRenderer cube_r55;
    private final ModelRenderer cube_r56;
    private final ModelRenderer cube_r57;
    private final ModelRenderer cube_r58;
    private final ModelRenderer cube_r59;
    private final ModelRenderer cube_r60;
    private final ModelRenderer cube_r61;
    private final ModelRenderer cube_r62;
    private final ModelRenderer cube_r63;
    private final ModelRenderer cube_r64;
    private final ModelRenderer cube_r65;
    private final ModelRenderer cube_r66;
    private final ModelRenderer cube_r67;
    private final ModelRenderer cube_r68;
    private final ModelRenderer cube_r69;
    private final ModelRenderer cube_r70;
    private final ModelRenderer cube_r71;
    private final ModelRenderer cube_r72;
    private final ModelRenderer cube_r73;
    private final ModelRenderer cube_r74;
    private final ModelRenderer cube_r75;
    private final ModelRenderer cube_r76;
    private final ModelRenderer cube_r77;
    private final ModelRenderer cube_r78;
    private final ModelRenderer cube_r79;
    private final ModelRenderer cube_r80;
    private final ModelRenderer cube_r81;
    private final ModelRenderer cube_r82;
    private final ModelRenderer cover_back;
    private final ModelRenderer cube_r83;
    private final ModelRenderer cube_r84;
    private final ModelRenderer cube_r85;
    private final ModelRenderer cube_r86;
    private final ModelRenderer cube_r87;
    private final ModelRenderer cube_r88;
    private final ModelRenderer cube_r89;
    private final ModelRenderer cube_r90;
    private final ModelRenderer cube_r91;
    private final ModelRenderer grip1;
    private final ModelRenderer cube_r92;
    private final ModelRenderer cube_r93;
    private final ModelRenderer cube_r94;
    private final ModelRenderer cube_r95;
    private final ModelRenderer cube_r96;
    private final ModelRenderer cube_r97;
    private final ModelRenderer cube_r98;
    private final ModelRenderer cube_r99;
    private final ModelRenderer cube_r100;
    private final ModelRenderer cover_front;
    private final ModelRenderer frontcoverp1;
    private final ModelRenderer cube_r101;
    private final ModelRenderer frontcoverp2;
    private final ModelRenderer cube_r102;
    private final ModelRenderer frontcoverp3;
    private final ModelRenderer cube_r103;
    private final ModelRenderer frontcoverp4;
    private final ModelRenderer cube_r104;
    private final ModelRenderer frontcoverp5;
    private final ModelRenderer cube_r105;
    private final ModelRenderer frontcoverp6;
    private final ModelRenderer cube_r106;
    private final ModelRenderer frontcoverp7;
    private final ModelRenderer cube_r107;
    private final ModelRenderer frontcoverp8;
    private final ModelRenderer cube_r108;
    private final ModelRenderer frontcoverp9;
    private final ModelRenderer cube_r109;
    private final ModelRenderer frontcoverp10;
    private final ModelRenderer cube_r110;
    private final ModelRenderer frontcoverp11;
    private final ModelRenderer cube_r111;
    private final ModelRenderer frontcoverp12;
    private final ModelRenderer cube_r112;
    private final ModelRenderer stock_piece;
    private final ModelRenderer cube_r113;
    private final ModelRenderer stock;
    private final ModelRenderer cube_r114;
    private final ModelRenderer cube_r115;
    private final ModelRenderer cube_r116;
    private final ModelRenderer cube_r117;
    private final ModelRenderer cube_r118;
    private final ModelRenderer cube_r119;
    private final ModelRenderer cube_r120;
    private final ModelRenderer cube_r121;
    private final ModelRenderer cube_r122;
    private final ModelRenderer ironsights;
    private final ModelRenderer cube_r123;
    private final ModelRenderer cube_r124;
    private final ModelRenderer cube_r125;
    private final ModelRenderer cube_r126;
    private final ModelRenderer cube_r127;
    private final ModelRenderer cube_r128;
    private final ModelRenderer cube_r129;
    private final ModelRenderer cube_r130;
    private final ModelRenderer cube_r131;

    @Override
    protected void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        gun.render(matrix, builder, light, overlay);
        ironsights.render(matrix, builder, light, overlay);
    }

    public GrenadeLauncherModel() {
        texWidth = 512;
        texHeight = 512;

        magazine = new ModelRenderer(this);
        magazine.setPos(3.5F, 24.0F, -2.0F);
        magazine.texOffs(94, 21).addBox(-4.5F, -1.5F, -5.0F, 2.0F, 3.0F, 10.0F, 0.0F, false);
        magazine.texOffs(94, 21).addBox(-5.0F, -1.0F, -5.0F, 3.0F, 2.0F, 10.0F, 0.0F, false);
        magazine.texOffs(94, 21).addBox(-4.5F, -1.0F, -5.866F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        magazine.texOffs(94, 21).addBox(-1.5F, -1.5F, -5.866F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        magazine.texOffs(94, 21).addBox(-4.5F, -1.0F, 4.6F, 6.0F, 2.0F, 1.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-1.5F, 0.384F, -4.933F);
        magazine.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.5236F, 0.0F, 0.0F);
        cube_r1.texOffs(94, 21).addBox(-3.0F, 0.0F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(-1.5F, -0.384F, -4.933F);
        magazine.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.5236F, 0.0F, 0.0F);
        cube_r2.texOffs(94, 21).addBox(-3.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(-2.049F, -0.549F, -5.366F);
        magazine.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, 0.5236F);
        cube_r3.texOffs(94, 21).addBox(0.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-2.049F, 0.549F, -5.366F);
        magazine.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.5236F);
        cube_r4.texOffs(94, 21).addBox(0.5F, -1.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(-3.451F, 0.0F, -4.683F);
        magazine.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, -0.5236F, 0.0F);
        cube_r5.texOffs(94, 21).addBox(-1.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        drum = new ModelRenderer(this);
        drum.setPos(-3.5F, 0.0F, 2.0F);
        magazine.addChild(drum);


        magslot1 = new ModelRenderer(this);
        magslot1.setPos(2.2071F, -4.5858F, 1.5F);
        drum.addChild(magslot1);
        magslot1.texOffs(225, 150).addBox(-3.7071F, -1.4142F, -8.5F, 3.0F, 1.0F, 10.0F, 0.0F, false);
        magslot1.texOffs(94, 21).addBox(-3.2071F, -0.9142F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        magslot1.texOffs(94, 21).addBox(-3.2071F, 1.5499F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(0.0F, 0.0F, 0.0F);
        magslot1.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, 0.7854F);
        cube_r6.texOffs(225, 150).addBox(-1.5F, -0.5F, -8.5F, 2.0F, 1.0F, 10.0F, 0.0F, true);

        cube_r7 = new ModelRenderer(this);
        cube_r7.setPos(-4.4142F, 0.0F, 0.0F);
        magslot1.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, -0.7854F);
        cube_r7.texOffs(225, 150).addBox(-0.5F, -0.5F, -8.5F, 2.0F, 1.0F, 10.0F, 0.0F, false);

        cube_r8 = new ModelRenderer(this);
        cube_r8.setPos(-3.2741F, 1.4339F, 0.0F);
        magslot1.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.0F, 1.0472F);
        cube_r8.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        cube_r9 = new ModelRenderer(this);
        cube_r9.setPos(-3.2741F, 0.2018F, 0.0F);
        magslot1.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.0F, -1.0472F);
        cube_r9.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        cube_r10 = new ModelRenderer(this);
        cube_r10.setPos(-3.4812F, 1.3231F, -0.5F);
        magslot1.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.0F, 1.0472F);
        cube_r10.texOffs(225, 150).addBox(-2.0F, -0.5F, -7.0F, 4.0F, 2.0F, 8.0F, 0.0F, true);

        cube_r11 = new ModelRenderer(this);
        cube_r11.setPos(-1.1401F, 1.4339F, 0.0F);
        magslot1.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, 0.0F, -1.0472F);
        cube_r11.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        cube_r12 = new ModelRenderer(this);
        cube_r12.setPos(-1.1401F, 0.2018F, 0.0F);
        magslot1.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, 0.0F, 1.0472F);
        cube_r12.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        magslot2 = new ModelRenderer(this);
        magslot2.setPos(0.0F, -3.9822F, 1.5F);
        drum.addChild(magslot2);
        setRotationAngle(magslot2, 0.0F, 0.0F, -1.0472F);
        magslot2.texOffs(225, 150).addBox(-4.9003F, -3.981F, -8.5F, 3.0F, 1.0F, 10.0F, 0.0F, false);
        magslot2.texOffs(94, 21).addBox(-4.4003F, -3.481F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        magslot2.texOffs(94, 21).addBox(-4.4003F, -1.0169F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        cube_r13 = new ModelRenderer(this);
        cube_r13.setPos(-1.1932F, -2.5667F, 0.0F);
        magslot2.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.0F, 0.0F, 0.7854F);
        cube_r13.texOffs(225, 150).addBox(-1.5F, -0.5F, -8.5F, 2.0F, 1.0F, 10.0F, 0.0F, true);

        cube_r14 = new ModelRenderer(this);
        cube_r14.setPos(-5.6074F, -2.5667F, 0.0F);
        magslot2.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.0F, 0.0F, -0.7854F);
        cube_r14.texOffs(225, 150).addBox(-0.5F, -0.5F, -8.5F, 2.0F, 1.0F, 10.0F, 0.0F, false);

        cube_r15 = new ModelRenderer(this);
        cube_r15.setPos(-4.4673F, -1.1329F, 0.0F);
        magslot2.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0F, 0.0F, 1.0472F);
        cube_r15.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        cube_r16 = new ModelRenderer(this);
        cube_r16.setPos(-4.4673F, -2.3649F, 0.0F);
        magslot2.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.0F, 0.0F, -1.0472F);
        cube_r16.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        cube_r17 = new ModelRenderer(this);
        cube_r17.setPos(-4.6744F, -1.2436F, -0.5F);
        magslot2.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.0F, 0.0F, 1.0472F);
        cube_r17.texOffs(225, 150).addBox(-2.0F, -0.5F, -7.0F, 4.0F, 2.0F, 8.0F, 0.0F, true);

        cube_r18 = new ModelRenderer(this);
        cube_r18.setPos(-2.3334F, -1.1329F, 0.0F);
        magslot2.addChild(cube_r18);
        setRotationAngle(cube_r18, 0.0F, 0.0F, -1.0472F);
        cube_r18.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        cube_r19 = new ModelRenderer(this);
        cube_r19.setPos(-2.3334F, -2.3649F, 0.0F);
        magslot2.addChild(cube_r19);
        setRotationAngle(cube_r19, 0.0F, 0.0F, 1.0472F);
        cube_r19.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        magslot3 = new ModelRenderer(this);
        magslot3.setPos(-3.4003F, -5.9454F, 1.5F);
        drum.addChild(magslot3);
        setRotationAngle(magslot3, 0.0F, 0.0F, -2.0944F);
        magslot3.texOffs(225, 150).addBox(-8.3007F, -5.9441F, -8.5F, 3.0F, 1.0F, 10.0F, 0.0F, false);
        magslot3.texOffs(94, 21).addBox(-7.8007F, -5.4441F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        magslot3.texOffs(94, 21).addBox(-7.8007F, -2.98F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        cube_r20 = new ModelRenderer(this);
        cube_r20.setPos(-4.5936F, -4.5299F, 0.0F);
        magslot3.addChild(cube_r20);
        setRotationAngle(cube_r20, 0.0F, 0.0F, 0.7854F);
        cube_r20.texOffs(225, 150).addBox(-1.5F, -0.5F, -8.5F, 2.0F, 1.0F, 10.0F, 0.0F, true);

        cube_r21 = new ModelRenderer(this);
        cube_r21.setPos(-9.0078F, -4.5299F, 0.0F);
        magslot3.addChild(cube_r21);
        setRotationAngle(cube_r21, 0.0F, 0.0F, -0.7854F);
        cube_r21.texOffs(225, 150).addBox(-0.5F, -0.5F, -8.5F, 2.0F, 1.0F, 10.0F, 0.0F, false);

        cube_r22 = new ModelRenderer(this);
        cube_r22.setPos(-7.8677F, -3.0961F, 0.0F);
        magslot3.addChild(cube_r22);
        setRotationAngle(cube_r22, 0.0F, 0.0F, 1.0472F);
        cube_r22.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        cube_r23 = new ModelRenderer(this);
        cube_r23.setPos(-7.8677F, -4.3281F, 0.0F);
        magslot3.addChild(cube_r23);
        setRotationAngle(cube_r23, 0.0F, 0.0F, -1.0472F);
        cube_r23.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        cube_r24 = new ModelRenderer(this);
        cube_r24.setPos(-8.0748F, -3.2068F, -0.5F);
        magslot3.addChild(cube_r24);
        setRotationAngle(cube_r24, 0.0F, 0.0F, 1.0472F);
        cube_r24.texOffs(225, 150).addBox(-2.0F, -0.5F, -7.0F, 4.0F, 2.0F, 8.0F, 0.0F, true);

        cube_r25 = new ModelRenderer(this);
        cube_r25.setPos(-5.7337F, -3.0961F, 0.0F);
        magslot3.addChild(cube_r25);
        setRotationAngle(cube_r25, 0.0F, 0.0F, -1.0472F);
        cube_r25.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        cube_r26 = new ModelRenderer(this);
        cube_r26.setPos(-5.7337F, -4.3281F, 0.0F);
        magslot3.addChild(cube_r26);
        setRotationAngle(cube_r26, 0.0F, 0.0F, 1.0472F);
        cube_r26.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        magslot4 = new ModelRenderer(this);
        magslot4.setPos(2.2071F, -4.5858F, 1.5F);
        drum.addChild(magslot4);
        setRotationAngle(magslot4, 0.0F, 0.0F, 1.0472F);
        magslot4.texOffs(225, 150).addBox(1.3343F, -1.7948F, -8.5F, 3.0F, 1.0F, 10.0F, 0.0F, false);
        magslot4.texOffs(94, 21).addBox(1.8343F, -1.2948F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        magslot4.texOffs(94, 21).addBox(1.8343F, 1.1693F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        cube_r27 = new ModelRenderer(this);
        cube_r27.setPos(5.0414F, -0.3806F, 0.0F);
        magslot4.addChild(cube_r27);
        setRotationAngle(cube_r27, 0.0F, 0.0F, 0.7854F);
        cube_r27.texOffs(225, 150).addBox(-1.5F, -0.5F, -8.5F, 2.0F, 1.0F, 10.0F, 0.0F, true);

        cube_r28 = new ModelRenderer(this);
        cube_r28.setPos(0.6272F, -0.3806F, 0.0F);
        magslot4.addChild(cube_r28);
        setRotationAngle(cube_r28, 0.0F, 0.0F, -0.7854F);
        cube_r28.texOffs(225, 150).addBox(-0.5F, -0.5F, -8.5F, 2.0F, 1.0F, 10.0F, 0.0F, false);

        cube_r29 = new ModelRenderer(this);
        cube_r29.setPos(1.7673F, 1.0533F, 0.0F);
        magslot4.addChild(cube_r29);
        setRotationAngle(cube_r29, 0.0F, 0.0F, 1.0472F);
        cube_r29.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        cube_r30 = new ModelRenderer(this);
        cube_r30.setPos(1.7673F, -0.1788F, 0.0F);
        magslot4.addChild(cube_r30);
        setRotationAngle(cube_r30, 0.0F, 0.0F, -1.0472F);
        cube_r30.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        cube_r31 = new ModelRenderer(this);
        cube_r31.setPos(1.5602F, 0.9425F, -0.5F);
        magslot4.addChild(cube_r31);
        setRotationAngle(cube_r31, 0.0F, 0.0F, 1.0472F);
        cube_r31.texOffs(225, 150).addBox(-2.0F, -0.5F, -7.0F, 4.0F, 2.0F, 8.0F, 0.0F, true);

        cube_r32 = new ModelRenderer(this);
        cube_r32.setPos(3.9013F, 1.0533F, 0.0F);
        magslot4.addChild(cube_r32);
        setRotationAngle(cube_r32, 0.0F, 0.0F, -1.0472F);
        cube_r32.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        cube_r33 = new ModelRenderer(this);
        cube_r33.setPos(3.9013F, -0.1788F, 0.0F);
        magslot4.addChild(cube_r33);
        setRotationAngle(cube_r33, 0.0F, 0.0F, 1.0472F);
        cube_r33.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        magslot5 = new ModelRenderer(this);
        magslot5.setPos(4.914F, -4.3848F, 1.5F);
        drum.addChild(magslot5);
        setRotationAngle(magslot5, 0.0F, 0.0F, 2.0944F);
        magslot5.texOffs(225, 150).addBox(4.69F, -3.8793F, -8.5F, 3.0F, 1.0F, 10.0F, 0.0F, false);
        magslot5.texOffs(94, 21).addBox(5.19F, -3.3793F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        magslot5.texOffs(94, 21).addBox(5.19F, -0.9152F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        cube_r34 = new ModelRenderer(this);
        cube_r34.setPos(8.3971F, -2.4651F, 0.0F);
        magslot5.addChild(cube_r34);
        setRotationAngle(cube_r34, 0.0F, 0.0F, 0.7854F);
        cube_r34.texOffs(225, 150).addBox(-1.5F, -0.5F, -8.5F, 2.0F, 1.0F, 10.0F, 0.0F, true);

        cube_r35 = new ModelRenderer(this);
        cube_r35.setPos(3.9829F, -2.4651F, 0.0F);
        magslot5.addChild(cube_r35);
        setRotationAngle(cube_r35, 0.0F, 0.0F, -0.7854F);
        cube_r35.texOffs(225, 150).addBox(-0.5F, -0.5F, -8.5F, 2.0F, 1.0F, 10.0F, 0.0F, false);

        cube_r36 = new ModelRenderer(this);
        cube_r36.setPos(5.123F, -1.0312F, 0.0F);
        magslot5.addChild(cube_r36);
        setRotationAngle(cube_r36, 0.0F, 0.0F, 1.0472F);
        cube_r36.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        cube_r37 = new ModelRenderer(this);
        cube_r37.setPos(5.123F, -2.2633F, 0.0F);
        magslot5.addChild(cube_r37);
        setRotationAngle(cube_r37, 0.0F, 0.0F, -1.0472F);
        cube_r37.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        cube_r38 = new ModelRenderer(this);
        cube_r38.setPos(4.9159F, -1.142F, -0.5F);
        magslot5.addChild(cube_r38);
        setRotationAngle(cube_r38, 0.0F, 0.0F, 1.0472F);
        cube_r38.texOffs(225, 150).addBox(-2.0F, -0.5F, -7.0F, 4.0F, 2.0F, 8.0F, 0.0F, true);

        cube_r39 = new ModelRenderer(this);
        cube_r39.setPos(7.257F, -1.0312F, 0.0F);
        magslot5.addChild(cube_r39);
        setRotationAngle(cube_r39, 0.0F, 0.0F, -1.0472F);
        cube_r39.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        cube_r40 = new ModelRenderer(this);
        cube_r40.setPos(7.257F, -2.2633F, 0.0F);
        magslot5.addChild(cube_r40);
        setRotationAngle(cube_r40, 0.0F, 0.0F, 1.0472F);
        cube_r40.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        magslot6 = new ModelRenderer(this);
        magslot6.setPos(11.1572F, -6.142F, 1.5F);
        drum.addChild(magslot6);
        setRotationAngle(magslot6, 0.0F, 0.0F, -3.1416F);
        magslot6.texOffs(225, 150).addBox(9.6572F, -12.0303F, -8.5F, 3.0F, 1.0F, 10.0F, 0.0F, false);
        magslot6.texOffs(94, 21).addBox(10.1572F, -11.5303F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        magslot6.texOffs(94, 21).addBox(10.1572F, -9.0662F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        cube_r41 = new ModelRenderer(this);
        cube_r41.setPos(13.3643F, -10.6161F, 0.0F);
        magslot6.addChild(cube_r41);
        setRotationAngle(cube_r41, 0.0F, 0.0F, 0.7854F);
        cube_r41.texOffs(225, 150).addBox(-1.5F, -0.5F, -8.5F, 2.0F, 1.0F, 10.0F, 0.0F, true);

        cube_r42 = new ModelRenderer(this);
        cube_r42.setPos(8.9501F, -10.6161F, 0.0F);
        magslot6.addChild(cube_r42);
        setRotationAngle(cube_r42, 0.0F, 0.0F, -0.7854F);
        cube_r42.texOffs(225, 150).addBox(-0.5F, -0.5F, -8.5F, 2.0F, 1.0F, 10.0F, 0.0F, false);

        cube_r43 = new ModelRenderer(this);
        cube_r43.setPos(10.0902F, -9.1822F, 0.0F);
        magslot6.addChild(cube_r43);
        setRotationAngle(cube_r43, 0.0F, 0.0F, 1.0472F);
        cube_r43.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        cube_r44 = new ModelRenderer(this);
        cube_r44.setPos(10.0902F, -10.4143F, 0.0F);
        magslot6.addChild(cube_r44);
        setRotationAngle(cube_r44, 0.0F, 0.0F, -1.0472F);
        cube_r44.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);

        cube_r45 = new ModelRenderer(this);
        cube_r45.setPos(9.8831F, -9.293F, -0.5F);
        magslot6.addChild(cube_r45);
        setRotationAngle(cube_r45, 0.0F, 0.0F, 1.0472F);
        cube_r45.texOffs(225, 150).addBox(-2.0F, -0.5F, -7.0F, 4.0F, 2.0F, 8.0F, 0.0F, true);

        cube_r46 = new ModelRenderer(this);
        cube_r46.setPos(12.2242F, -9.1822F, 0.0F);
        magslot6.addChild(cube_r46);
        setRotationAngle(cube_r46, 0.0F, 0.0F, -1.0472F);
        cube_r46.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        cube_r47 = new ModelRenderer(this);
        cube_r47.setPos(12.2242F, -10.4143F, 0.0F);
        magslot6.addChild(cube_r47);
        setRotationAngle(cube_r47, 0.0F, 0.0F, 1.0472F);
        cube_r47.texOffs(94, 21).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);

        gun = new ModelRenderer(this);
        gun.setPos(0.0F, 24.0F, 0.0F);
        gun.texOffs(213, 158).addBox(-1.5F, -1.5F, -10.099F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        gun.texOffs(213, 158).addBox(-2.5F, -5.5F, -10.099F, 5.0F, 4.0F, 1.0F, 0.0F, false);
        gun.texOffs(213, 158).addBox(-2.0F, -7.5F, -10.099F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(213, 158).addBox(-1.5F, -8.366F, -10.099F, 3.0F, 1.0F, 15.0F, 0.0F, false);
        gun.texOffs(213, 158).addBox(-1.5F, -6.5981F, 3.1F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(213, 158).addBox(-0.5F, -7.5981F, 3.1F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-1.5F, -9.366F, -9.0F, 3.0F, 1.0F, 13.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-1.0F, -10.366F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-0.134F, -9.866F, 0.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-0.134F, -9.866F, 2.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-1.866F, -9.866F, 0.0F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(81, 92).addBox(-1.866F, -9.866F, 2.0F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(81, 92).addBox(-1.0F, -10.366F, 1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-1.0F, -10.366F, 3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-1.0F, -10.366F, -5.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-0.134F, -9.866F, -4.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-1.866F, -9.866F, -4.0F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(81, 92).addBox(-1.0F, -10.366F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-0.134F, -9.866F, -2.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-1.866F, -9.866F, -2.0F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(81, 92).addBox(-1.0F, -10.366F, -7.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-0.134F, -9.866F, -6.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-1.866F, -9.866F, -6.0F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(81, 92).addBox(-1.0F, -10.366F, -9.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-0.134F, -9.866F, -8.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 92).addBox(-1.866F, -9.866F, -8.0F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(23, 105).addBox(0.7F, -8.866F, -8.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        gun.texOffs(23, 105).addBox(-1.7F, -8.866F, -8.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        gun.texOffs(23, 105).addBox(0.7F, -8.866F, 1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        gun.texOffs(23, 105).addBox(-1.7F, -8.866F, 1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        gun.texOffs(19, 97).addBox(0.1F, -7.5F, -9.099F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        gun.texOffs(19, 97).addBox(-2.1F, -7.5F, -9.099F, 2.0F, 3.0F, 1.0F, 0.0F, true);
        gun.texOffs(210, 150).addBox(-1.5F, -5.5F, -24.099F, 3.0F, 1.0F, 14.0F, 0.0F, false);
        gun.texOffs(210, 150).addBox(-1.5F, -5.5F, -27.099F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        gun.texOffs(210, 150).addBox(-1.5F, -5.4F, -25.099F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.5F, -6.5F, -20.099F, 3.0F, 1.0F, 9.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.0F, -7.5F, -20.099F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.866F, -7.0F, -19.099F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.866F, 0.4142F, -19.099F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-0.134F, -7.0F, -19.099F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(82, 86).addBox(-0.134F, 0.4142F, -19.099F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(82, 86).addBox(-1.0F, -7.5F, -18.099F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.866F, -7.0F, -17.099F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.866F, 0.4142F, -17.099F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-0.134F, -7.0F, -17.099F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(82, 86).addBox(-0.134F, 0.4142F, -17.099F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(82, 86).addBox(-1.0F, -7.5F, -16.099F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.866F, -7.0F, -15.099F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.866F, 0.4142F, -15.099F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-0.134F, -7.0F, -15.099F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(82, 86).addBox(-0.134F, 0.4142F, -15.099F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(82, 86).addBox(-1.0F, -7.5F, -14.099F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.866F, -7.0F, -13.099F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.866F, 0.4142F, -13.099F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-0.134F, -7.0F, -13.099F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(82, 86).addBox(-0.134F, 0.4142F, -13.099F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        gun.texOffs(82, 86).addBox(-1.0F, -7.5F, -12.099F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.5F, -1.0858F, -20.099F, 3.0F, 1.0F, 9.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.0F, -0.0858F, -12.099F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.0F, -0.0858F, -16.099F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.0F, -0.0858F, -14.099F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.0F, -0.0858F, -18.099F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(82, 86).addBox(-1.0F, -0.0858F, -20.099F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(210, 150).addBox(-1.5F, -2.0858F, -24.099F, 3.0F, 1.0F, 14.0F, 0.0F, false);
        gun.texOffs(210, 150).addBox(-1.5F, -2.0858F, -27.099F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        gun.texOffs(210, 150).addBox(-1.5F, -2.1858F, -25.099F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(210, 150).addBox(1.2071F, -4.7929F, -24.099F, 1.0F, 3.0F, 14.0F, 0.0F, false);
        gun.texOffs(210, 150).addBox(1.2071F, -4.7929F, -27.099F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        gun.texOffs(210, 150).addBox(1.1071F, -4.7929F, -25.099F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(2.2071F, -4.7929F, -20.099F, 1.0F, 3.0F, 9.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(3.2071F, -4.2929F, -12.099F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(3.2071F, -4.2929F, -14.099F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(3.2071F, -4.2929F, -16.099F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(3.2071F, -4.2929F, -18.099F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(3.2071F, -4.2929F, -20.099F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(-3.2071F, -4.7929F, -20.099F, 1.0F, 3.0F, 9.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(-4.2071F, -4.2929F, -12.099F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(-3.7061F, -5.1589F, -13.099F, 0.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(3.7061F, -5.1589F, -13.099F, 0.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(-3.7061F, -3.4269F, -13.099F, 0.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(3.7061F, -3.4269F, -13.099F, 0.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(-4.2071F, -4.2929F, -14.099F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(-3.7061F, -5.1589F, -15.099F, 0.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(3.7061F, -5.1589F, -15.099F, 0.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(-3.7061F, -3.4269F, -15.099F, 0.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(3.7061F, -3.4269F, -15.099F, 0.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(-4.2071F, -4.2929F, -16.099F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(-3.7061F, -5.1589F, -17.099F, 0.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(3.7061F, -5.1589F, -17.099F, 0.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(-3.7061F, -3.4269F, -17.099F, 0.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(3.7061F, -3.4269F, -17.099F, 0.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(-4.2071F, -4.2929F, -18.099F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(-3.7061F, -5.1589F, -19.099F, 0.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(3.7061F, -5.1589F, -19.099F, 0.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(-3.7061F, -3.4269F, -19.099F, 0.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(89, 95).addBox(3.7061F, -3.4269F, -19.099F, 0.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(89, 95).addBox(-4.2071F, -4.2929F, -20.099F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(210, 150).addBox(-2.2071F, -4.7929F, -24.099F, 1.0F, 3.0F, 14.0F, 0.0F, true);
        gun.texOffs(210, 150).addBox(-2.2071F, -4.7929F, -27.099F, 1.0F, 3.0F, 2.0F, 0.0F, true);
        gun.texOffs(210, 150).addBox(-2.1071F, -4.7929F, -25.099F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        cube_r48 = new ModelRenderer(this);
        cube_r48.setPos(-2.2071F, -1.0858F, -24.599F);
        gun.addChild(cube_r48);
        setRotationAngle(cube_r48, 0.0F, 0.0F, -0.7854F);
        cube_r48.texOffs(210, 150).addBox(0.6F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r48.texOffs(210, 150).addBox(0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        cube_r48.texOffs(210, 150).addBox(0.5F, -0.5F, 0.5F, 1.0F, 1.0F, 14.0F, 0.0F, true);

        cube_r49 = new ModelRenderer(this);
        cube_r49.setPos(-2.2071F, -4.0858F, -24.599F);
        gun.addChild(cube_r49);
        setRotationAngle(cube_r49, 0.0F, 0.0F, -0.7854F);
        cube_r49.texOffs(210, 150).addBox(0.5F, -0.4F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r49.texOffs(210, 150).addBox(0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        cube_r49.texOffs(210, 150).addBox(0.5F, -0.5F, 0.5F, 1.0F, 1.0F, 14.0F, 0.0F, true);

        cube_r50 = new ModelRenderer(this);
        cube_r50.setPos(2.2071F, -1.0858F, -24.599F);
        gun.addChild(cube_r50);
        setRotationAngle(cube_r50, 0.0F, 0.0F, 0.7854F);
        cube_r50.texOffs(210, 150).addBox(-1.6F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r50.texOffs(210, 150).addBox(-1.5F, -0.5F, -2.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        cube_r50.texOffs(210, 150).addBox(-1.5F, -0.5F, 0.5F, 1.0F, 1.0F, 14.0F, 0.0F, false);

        cube_r51 = new ModelRenderer(this);
        cube_r51.setPos(2.2071F, -4.0858F, -24.599F);
        gun.addChild(cube_r51);
        setRotationAngle(cube_r51, 0.0F, 0.0F, 0.7854F);
        cube_r51.texOffs(210, 150).addBox(-1.5F, -0.4F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r51.texOffs(210, 150).addBox(-1.5F, -0.5F, -2.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        cube_r51.texOffs(210, 150).addBox(-1.5F, -0.5F, 0.5F, 1.0F, 1.0F, 14.0F, 0.0F, false);

        cube_r52 = new ModelRenderer(this);
        cube_r52.setPos(-1.2585F, -9.1891F, -17.1F);
        gun.addChild(cube_r52);
        setRotationAngle(cube_r52, 0.0F, 0.0F, -0.5236F);
        cube_r52.texOffs(89, 95).addBox(-6.0F, 5.5F, -1.999F, 1.0F, 0.0F, 1.0F, 0.0F, true);
        cube_r52.texOffs(89, 95).addBox(-6.0F, 5.5F, 0.001F, 1.0F, 0.0F, 1.0F, 0.0F, true);
        cube_r52.texOffs(89, 95).addBox(-6.0F, 5.5F, 2.001F, 1.0F, 0.0F, 1.0F, 0.0F, true);
        cube_r52.texOffs(89, 95).addBox(-6.0F, 5.5F, 4.001F, 1.0F, 0.0F, 1.0F, 0.0F, true);

        cube_r53 = new ModelRenderer(this);
        cube_r53.setPos(1.2585F, -9.1891F, -17.1F);
        gun.addChild(cube_r53);
        setRotationAngle(cube_r53, 0.0F, 0.0F, 0.5236F);
        cube_r53.texOffs(89, 95).addBox(5.0F, 5.5F, -1.999F, 1.0F, 0.0F, 1.0F, 0.0F, false);
        cube_r53.texOffs(89, 95).addBox(5.0F, 5.5F, 0.001F, 1.0F, 0.0F, 1.0F, 0.0F, false);
        cube_r53.texOffs(89, 95).addBox(5.0F, 5.5F, 2.001F, 1.0F, 0.0F, 1.0F, 0.0F, false);
        cube_r53.texOffs(89, 95).addBox(5.0F, 5.5F, 4.001F, 1.0F, 0.0F, 1.0F, 0.0F, false);

        cube_r54 = new ModelRenderer(this);
        cube_r54.setPos(1.261F, 2.6042F, -17.1F);
        gun.addChild(cube_r54);
        setRotationAngle(cube_r54, 0.0F, 0.0F, -0.5236F);
        cube_r54.texOffs(89, 95).addBox(5.0F, -5.5F, -1.999F, 1.0F, 0.0F, 1.0F, 0.0F, false);
        cube_r54.texOffs(89, 95).addBox(5.0F, -5.5F, 0.001F, 1.0F, 0.0F, 1.0F, 0.0F, false);
        cube_r54.texOffs(89, 95).addBox(5.0F, -5.5F, 2.001F, 1.0F, 0.0F, 1.0F, 0.0F, false);
        cube_r54.texOffs(89, 95).addBox(5.0F, -5.5F, 4.001F, 1.0F, 0.0F, 1.0F, 0.0F, false);

        cube_r55 = new ModelRenderer(this);
        cube_r55.setPos(-1.261F, 2.6042F, -17.1F);
        gun.addChild(cube_r55);
        setRotationAngle(cube_r55, 0.0F, 0.0F, 0.5236F);
        cube_r55.texOffs(89, 95).addBox(-6.0F, -5.5F, -1.999F, 1.0F, 0.0F, 1.0F, 0.0F, true);
        cube_r55.texOffs(89, 95).addBox(-6.0F, -5.5F, 0.001F, 1.0F, 0.0F, 1.0F, 0.0F, true);
        cube_r55.texOffs(89, 95).addBox(-6.0F, -5.5F, 2.001F, 1.0F, 0.0F, 1.0F, 0.0F, true);
        cube_r55.texOffs(89, 95).addBox(-6.0F, -5.5F, 4.001F, 1.0F, 0.0F, 1.0F, 0.0F, true);

        cube_r56 = new ModelRenderer(this);
        cube_r56.setPos(1.507F, -0.3958F, -15.599F);
        gun.addChild(cube_r56);
        setRotationAngle(cube_r56, 0.0F, 0.0F, -0.5236F);
        cube_r56.texOffs(82, 86).addBox(-4.0F, -4.5F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r56.texOffs(82, 86).addBox(-4.0F, -4.5F, 1.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r56.texOffs(82, 86).addBox(-4.0F, -4.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r56.texOffs(82, 86).addBox(-4.0F, -4.5F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r56.texOffs(82, 86).addBox(-4.0F, -4.5F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r57 = new ModelRenderer(this);
        cube_r57.setPos(-1.507F, -0.3958F, -15.599F);
        gun.addChild(cube_r57);
        setRotationAngle(cube_r57, 0.0F, 0.0F, 0.5236F);
        cube_r57.texOffs(82, 86).addBox(3.0F, -4.5F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r57.texOffs(82, 86).addBox(3.0F, -4.5F, 1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r57.texOffs(82, 86).addBox(3.0F, -4.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r57.texOffs(82, 86).addBox(3.0F, -4.5F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r57.texOffs(82, 86).addBox(3.0F, -4.5F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r58 = new ModelRenderer(this);
        cube_r58.setPos(-1.507F, -6.19F, -15.599F);
        gun.addChild(cube_r58);
        setRotationAngle(cube_r58, 0.0F, 0.0F, -0.5236F);
        cube_r58.texOffs(82, 86).addBox(3.0F, 3.5F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(82, 86).addBox(3.0F, 3.5F, 1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(82, 86).addBox(3.0F, 3.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(82, 86).addBox(3.0F, 3.5F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(82, 86).addBox(3.0F, 3.5F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r59 = new ModelRenderer(this);
        cube_r59.setPos(1.507F, -6.19F, -15.599F);
        gun.addChild(cube_r59);
        setRotationAngle(cube_r59, 0.0F, 0.0F, 0.5236F);
        cube_r59.texOffs(82, 86).addBox(-4.0F, 3.5F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r59.texOffs(82, 86).addBox(-4.0F, 3.5F, 1.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r59.texOffs(82, 86).addBox(-4.0F, 3.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r59.texOffs(82, 86).addBox(-4.0F, 3.5F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r59.texOffs(82, 86).addBox(-4.0F, 3.5F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r60 = new ModelRenderer(this);
        cube_r60.setPos(1.616F, -0.0188F, -15.599F);
        gun.addChild(cube_r60);
        setRotationAngle(cube_r60, 0.0F, 0.0F, -0.5236F);
        cube_r60.texOffs(82, 86).addBox(-1.0F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r60.texOffs(82, 86).addBox(-1.0F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r60.texOffs(82, 86).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r60.texOffs(82, 86).addBox(-1.0F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r60.texOffs(82, 86).addBox(-1.0F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r61 = new ModelRenderer(this);
        cube_r61.setPos(1.616F, -6.567F, -15.599F);
        gun.addChild(cube_r61);
        setRotationAngle(cube_r61, 0.0F, 0.0F, 0.5236F);
        cube_r61.texOffs(82, 86).addBox(-1.0F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r61.texOffs(82, 86).addBox(-1.0F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r61.texOffs(82, 86).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r61.texOffs(82, 86).addBox(-1.0F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r61.texOffs(82, 86).addBox(-1.0F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r62 = new ModelRenderer(this);
        cube_r62.setPos(-1.616F, -0.0188F, -15.599F);
        gun.addChild(cube_r62);
        setRotationAngle(cube_r62, 0.0F, 0.0F, 0.5236F);
        cube_r62.texOffs(82, 86).addBox(0.0F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r62.texOffs(82, 86).addBox(0.0F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r62.texOffs(82, 86).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r62.texOffs(82, 86).addBox(0.0F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r62.texOffs(82, 86).addBox(0.0F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r63 = new ModelRenderer(this);
        cube_r63.setPos(-1.616F, -6.567F, -15.599F);
        gun.addChild(cube_r63);
        setRotationAngle(cube_r63, 0.0F, 0.0F, -0.5236F);
        cube_r63.texOffs(82, 86).addBox(0.0F, -0.5F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r63.texOffs(82, 86).addBox(0.0F, -0.5F, 1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r63.texOffs(82, 86).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r63.texOffs(82, 86).addBox(0.0F, -0.5F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r63.texOffs(82, 86).addBox(0.0F, -0.5F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r64 = new ModelRenderer(this);
        cube_r64.setPos(-1.6169F, -0.0183F, -15.599F);
        gun.addChild(cube_r64);
        setRotationAngle(cube_r64, 0.0F, 0.0F, 0.5236F);
        cube_r64.texOffs(82, 86).addBox(0.0F, -0.5F, 2.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r64.texOffs(82, 86).addBox(0.0F, -0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r64.texOffs(82, 86).addBox(0.0F, -0.5F, -1.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r64.texOffs(82, 86).addBox(0.0F, -0.5F, -3.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r65 = new ModelRenderer(this);
        cube_r65.setPos(-1.6169F, -6.5675F, -15.599F);
        gun.addChild(cube_r65);
        setRotationAngle(cube_r65, 0.0F, 0.0F, -0.5236F);
        cube_r65.texOffs(82, 86).addBox(0.0F, -0.5F, 2.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r65.texOffs(82, 86).addBox(0.0F, -0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r65.texOffs(82, 86).addBox(0.0F, -0.5F, -1.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r65.texOffs(82, 86).addBox(0.0F, -0.5F, -3.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r66 = new ModelRenderer(this);
        cube_r66.setPos(1.6169F, -0.0183F, -15.599F);
        gun.addChild(cube_r66);
        setRotationAngle(cube_r66, 0.0F, 0.0F, -0.5236F);
        cube_r66.texOffs(82, 86).addBox(0.0F, -0.5F, 2.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r66.texOffs(82, 86).addBox(0.0F, -0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r66.texOffs(82, 86).addBox(0.0F, -0.5F, -1.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r66.texOffs(82, 86).addBox(0.0F, -0.5F, -3.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r67 = new ModelRenderer(this);
        cube_r67.setPos(1.6169F, -6.5675F, -15.599F);
        gun.addChild(cube_r67);
        setRotationAngle(cube_r67, 0.0F, 0.0F, 0.5236F);
        cube_r67.texOffs(82, 86).addBox(0.0F, -0.5F, 2.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r67.texOffs(82, 86).addBox(0.0F, -0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r67.texOffs(82, 86).addBox(0.0F, -0.5F, -1.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r67.texOffs(82, 86).addBox(0.0F, -0.5F, -3.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r68 = new ModelRenderer(this);
        cube_r68.setPos(0.4151F, -5.549F, -9.599F);
        gun.addChild(cube_r68);
        setRotationAngle(cube_r68, 0.0F, 0.0F, 0.5236F);
        cube_r68.texOffs(213, 158).addBox(-2.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r69 = new ModelRenderer(this);
        cube_r69.setPos(0.4151F, -1.451F, -9.599F);
        gun.addChild(cube_r69);
        setRotationAngle(cube_r69, 0.0F, 0.0F, -0.5236F);
        cube_r69.texOffs(213, 158).addBox(-2.5F, -1.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        cube_r70 = new ModelRenderer(this);
        cube_r70.setPos(0.9151F, -7.549F, -9.599F);
        gun.addChild(cube_r70);
        setRotationAngle(cube_r70, 0.0F, 0.0F, 0.5236F);
        cube_r70.texOffs(213, 158).addBox(-2.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r71 = new ModelRenderer(this);
        cube_r71.setPos(-0.9151F, -7.549F, -9.599F);
        gun.addChild(cube_r71);
        setRotationAngle(cube_r71, 0.0F, 0.0F, -0.5236F);
        cube_r71.texOffs(213, 158).addBox(1.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r72 = new ModelRenderer(this);
        cube_r72.setPos(-0.4151F, -5.549F, -9.599F);
        gun.addChild(cube_r72);
        setRotationAngle(cube_r72, 0.0F, 0.0F, -0.5236F);
        cube_r72.texOffs(213, 158).addBox(1.5F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r73 = new ModelRenderer(this);
        cube_r73.setPos(-0.4151F, -1.451F, -9.599F);
        gun.addChild(cube_r73);
        setRotationAngle(cube_r73, 0.0F, 0.0F, 0.5236F);
        cube_r73.texOffs(213, 158).addBox(1.5F, -1.5F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        cube_r74 = new ModelRenderer(this);
        cube_r74.setPos(-1.2F, -8.366F, 1.0F);
        gun.addChild(cube_r74);
        setRotationAngle(cube_r74, -0.7854F, 0.0F, 0.0F);
        cube_r74.texOffs(29, 152).addBox(-0.7F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r74.texOffs(29, 152).addBox(2.1F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r75 = new ModelRenderer(this);
        cube_r75.setPos(-1.2F, -8.366F, -8.0F);
        gun.addChild(cube_r75);
        setRotationAngle(cube_r75, -0.7854F, 0.0F, 0.0F);
        cube_r75.texOffs(29, 152).addBox(-0.7F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r75.texOffs(29, 152).addBox(2.1F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r76 = new ModelRenderer(this);
        cube_r76.setPos(-1.2F, -8.366F, 3.0F);
        gun.addChild(cube_r76);
        setRotationAngle(cube_r76, -0.7854F, 0.0F, 0.0F);
        cube_r76.texOffs(29, 152).addBox(-0.7F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r76.texOffs(29, 152).addBox(2.1F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r77 = new ModelRenderer(this);
        cube_r77.setPos(-1.2F, -8.366F, -6.0F);
        gun.addChild(cube_r77);
        setRotationAngle(cube_r77, -0.7854F, 0.0F, 0.0F);
        cube_r77.texOffs(29, 152).addBox(-0.7F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r77.texOffs(29, 152).addBox(2.1F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r78 = new ModelRenderer(this);
        cube_r78.setPos(-1.616F, -9.433F, -3.5F);
        gun.addChild(cube_r78);
        setRotationAngle(cube_r78, 0.0F, 0.0F, -0.5236F);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, -4.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, -2.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, 5.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, 3.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, 6.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, 1.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r78.texOffs(81, 92).addBox(0.0F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r79 = new ModelRenderer(this);
        cube_r79.setPos(1.616F, -9.433F, -3.5F);
        gun.addChild(cube_r79);
        setRotationAngle(cube_r79, 0.0F, 0.0F, 0.5236F);
        cube_r79.texOffs(81, 92).addBox(-1.0F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r79.texOffs(81, 92).addBox(0.0F, -0.5F, -4.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r79.texOffs(81, 92).addBox(-1.0F, -0.5F, -3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r79.texOffs(81, 92).addBox(0.0F, -0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r79.texOffs(81, 92).addBox(-1.0F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r79.texOffs(81, 92).addBox(0.0F, -0.5F, -2.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r79.texOffs(81, 92).addBox(-1.0F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r79.texOffs(81, 92).addBox(0.0F, -0.5F, 5.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r79.texOffs(81, 92).addBox(0.0F, -0.5F, 3.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r79.texOffs(81, 92).addBox(-1.0F, -0.5F, 6.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r79.texOffs(81, 92).addBox(-1.0F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r79.texOffs(81, 92).addBox(0.0F, -0.5F, 1.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r79.texOffs(81, 92).addBox(-1.0F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r80 = new ModelRenderer(this);
        cube_r80.setPos(-0.4433F, -6.9633F, 3.6F);
        gun.addChild(cube_r80);
        setRotationAngle(cube_r80, 0.0F, 0.0F, 0.1309F);
        cube_r80.texOffs(213, 158).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r81 = new ModelRenderer(this);
        cube_r81.setPos(0.4433F, -6.9633F, 3.6F);
        gun.addChild(cube_r81);
        setRotationAngle(cube_r81, 0.0F, 0.0F, -0.1309F);
        cube_r81.texOffs(213, 158).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r82 = new ModelRenderer(this);
        cube_r82.setPos(3.5F, 0.0F, -9.0F);
        gun.addChild(cube_r82);
        setRotationAngle(cube_r82, 0.0F, 0.0F, 0.7854F);
        cube_r82.texOffs(94, 21).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        cover_back = new ModelRenderer(this);
        cover_back.setPos(0.0F, 0.0F, 12.1F);
        gun.addChild(cover_back);
        cover_back.texOffs(213, 158).addBox(3.5981F, -1.5F, -9.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        cover_back.texOffs(213, 158).addBox(-4.0F, -4.0F, -9.001F, 8.0F, 8.0F, 1.0F, 0.0F, false);
        cover_back.texOffs(213, 158).addBox(-1.5F, -3.0F, -8.001F, 3.0F, 4.0F, 4.0F, 0.0F, false);
        cover_back.texOffs(213, 158).addBox(-1.5F, -3.0F, -4.4651F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        cover_back.texOffs(213, 158).addBox(-1.5F, -1.0F, -4.001F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        cover_back.texOffs(213, 158).addBox(-5.5981F, -1.5F, -9.0F, 2.0F, 3.0F, 1.0F, 0.0F, true);
        cover_back.texOffs(213, 158).addBox(-1.5F, -5.5981F, -9.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        cover_back.texOffs(213, 158).addBox(-1.5F, 3.5981F, -9.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);

        cube_r83 = new ModelRenderer(this);
        cube_r83.setPos(-2.549F, 4.4151F, -8.5F);
        cover_back.addChild(cube_r83);
        setRotationAngle(cube_r83, 0.0F, 0.0F, -1.0472F);
        cube_r83.texOffs(213, 158).addBox(-0.5F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, true);

        cube_r84 = new ModelRenderer(this);
        cube_r84.setPos(-2.549F, -4.4151F, -8.5F);
        cover_back.addChild(cube_r84);
        setRotationAngle(cube_r84, 0.0F, 0.0F, 1.0472F);
        cube_r84.texOffs(213, 158).addBox(-0.5F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, true);

        cube_r85 = new ModelRenderer(this);
        cube_r85.setPos(2.549F, 4.4151F, -8.5F);
        cover_back.addChild(cube_r85);
        setRotationAngle(cube_r85, 0.0F, 0.0F, 1.0472F);
        cube_r85.texOffs(213, 158).addBox(-1.5F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r86 = new ModelRenderer(this);
        cube_r86.setPos(2.549F, -4.4151F, -8.5F);
        cover_back.addChild(cube_r86);
        setRotationAngle(cube_r86, 0.0F, 0.0F, -1.0472F);
        cube_r86.texOffs(213, 158).addBox(-1.5F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r87 = new ModelRenderer(this);
        cube_r87.setPos(-4.1651F, 2.9821F, -8.5F);
        cover_back.addChild(cube_r87);
        setRotationAngle(cube_r87, 0.0F, 0.0F, -0.5236F);
        cube_r87.texOffs(213, 158).addBox(-0.5F, -2.0F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, true);

        cube_r88 = new ModelRenderer(this);
        cube_r88.setPos(-4.1651F, -2.9821F, -8.5F);
        cover_back.addChild(cube_r88);
        setRotationAngle(cube_r88, 0.0F, 0.0F, 0.5236F);
        cube_r88.texOffs(213, 158).addBox(-0.5F, -1.0F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, true);

        cube_r89 = new ModelRenderer(this);
        cube_r89.setPos(4.1651F, 2.9821F, -8.5F);
        cover_back.addChild(cube_r89);
        setRotationAngle(cube_r89, 0.0F, 0.0F, 0.5236F);
        cube_r89.texOffs(213, 158).addBox(-1.5F, -2.0F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r90 = new ModelRenderer(this);
        cube_r90.setPos(4.1651F, -2.9821F, -8.5F);
        cover_back.addChild(cube_r90);
        setRotationAngle(cube_r90, 0.0F, 0.0F, -0.5236F);
        cube_r90.texOffs(213, 158).addBox(-1.5F, -1.0F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r91 = new ModelRenderer(this);
        cube_r91.setPos(0.5F, -1.134F, -2.2331F);
        cover_back.addChild(cube_r91);
        setRotationAngle(cube_r91, -0.5236F, 0.0F, 0.0F);
        cube_r91.texOffs(213, 158).addBox(-2.0F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);

        grip1 = new ModelRenderer(this);
        grip1.setPos(0.5F, 2.0F, -5.001F);
        cover_back.addChild(grip1);
        grip1.texOffs(213, 158).addBox(-1.5F, -1.0F, 4.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        grip1.texOffs(213, 158).addBox(-1.5F, -1.0F, 0.2679F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        grip1.texOffs(213, 158).addBox(0.0F, -1.0F, 1.134F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        grip1.texOffs(213, 158).addBox(-2.0F, -1.0F, 1.134F, 1.0F, 2.0F, 3.0F, 0.0F, true);

        cube_r92 = new ModelRenderer(this);
        cube_r92.setPos(-0.5F, 1.3015F, 2.3635F);
        grip1.addChild(cube_r92);
        setRotationAngle(cube_r92, 0.3491F, 0.0F, 0.0F);
        cube_r92.texOffs(213, 158).addBox(-1.5F, -1.0F, -1.0F, 1.0F, 6.0F, 3.0F, 0.0F, true);
        cube_r92.texOffs(213, 158).addBox(0.5F, -1.0F, -1.0F, 1.0F, 6.0F, 3.0F, 0.0F, false);
        cube_r92.texOffs(213, 158).addBox(-1.0F, -1.0F, -1.866F, 2.0F, 6.0F, 1.0F, 0.0F, false);
        cube_r92.texOffs(213, 158).addBox(-1.0F, -1.0F, 1.866F, 2.0F, 6.0F, 1.0F, 0.0F, false);

        cube_r93 = new ModelRenderer(this);
        cube_r93.setPos(-0.5F, 1.3015F, 2.3635F);
        grip1.addChild(cube_r93);
        setRotationAngle(cube_r93, 0.3979F, 0.4891F, 0.195F);
        cube_r93.texOffs(213, 158).addBox(0.799F, -1.0F, -1.116F, 1.0F, 6.0F, 1.0F, 0.0F, true);

        cube_r94 = new ModelRenderer(this);
        cube_r94.setPos(0.75F, 0.0F, 0.701F);
        grip1.addChild(cube_r94);
        setRotationAngle(cube_r94, 0.0F, 0.5236F, 0.0F);
        cube_r94.texOffs(213, 158).addBox(-1.0F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        cube_r95 = new ModelRenderer(this);
        cube_r95.setPos(-0.5F, 0.9595F, 3.3032F);
        grip1.addChild(cube_r95);
        setRotationAngle(cube_r95, 0.3979F, 0.4891F, 0.195F);
        cube_r95.texOffs(213, 158).addBox(-1.799F, -1.0F, 0.116F, 1.0F, 6.0F, 1.0F, 0.0F, true);

        cube_r96 = new ModelRenderer(this);
        cube_r96.setPos(-0.884F, 0.0F, 4.067F);
        grip1.addChild(cube_r96);
        setRotationAngle(cube_r96, 0.0F, 0.5236F, 0.0F);
        cube_r96.texOffs(213, 158).addBox(-1.0F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        cube_r97 = new ModelRenderer(this);
        cube_r97.setPos(-0.5F, 1.3015F, 2.3635F);
        grip1.addChild(cube_r97);
        setRotationAngle(cube_r97, 0.3979F, -0.4891F, -0.195F);
        cube_r97.texOffs(213, 158).addBox(-1.799F, -1.0F, -1.116F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        cube_r98 = new ModelRenderer(this);
        cube_r98.setPos(-1.75F, 0.0F, 0.701F);
        grip1.addChild(cube_r98);
        setRotationAngle(cube_r98, 0.0F, -0.5236F, 0.0F);
        cube_r98.texOffs(213, 158).addBox(0.0F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        cube_r99 = new ModelRenderer(this);
        cube_r99.setPos(-0.5F, 0.9595F, 3.3032F);
        grip1.addChild(cube_r99);
        setRotationAngle(cube_r99, 0.3979F, -0.4891F, -0.195F);
        cube_r99.texOffs(213, 158).addBox(0.799F, -1.0F, 0.116F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        cube_r100 = new ModelRenderer(this);
        cube_r100.setPos(-0.116F, 0.0F, 4.067F);
        grip1.addChild(cube_r100);
        setRotationAngle(cube_r100, 0.0F, -0.5236F, 0.0F);
        cube_r100.texOffs(213, 158).addBox(0.0F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        cover_front = new ModelRenderer(this);
        cover_front.setPos(0.0F, 0.0F, -17.1F);
        gun.addChild(cover_front);
        cover_front.texOffs(213, 158).addBox(-4.0F, -4.0F, 8.001F, 8.0F, 8.0F, 1.0F, 0.0F, false);

        frontcoverp1 = new ModelRenderer(this);
        frontcoverp1.setPos(4.9821F, 0.0F, 8.933F);
        cover_front.addChild(frontcoverp1);
        frontcoverp1.texOffs(213, 158).addBox(-1.384F, -1.5F, -0.933F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        frontcoverp1.texOffs(213, 158).addBox(0.116F, -1.5F, -0.067F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r101 = new ModelRenderer(this);
        cube_r101.setPos(0.0F, 0.0F, 0.0F);
        frontcoverp1.addChild(cube_r101);
        setRotationAngle(cube_r101, 0.0F, 0.5236F, 0.0F);
        cube_r101.texOffs(213, 158).addBox(0.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        frontcoverp2 = new ModelRenderer(this);
        frontcoverp2.setPos(5.2261F, 0.0F, 8.933F);
        cover_front.addChild(frontcoverp2);
        setRotationAngle(frontcoverp2, 0.0F, 0.0F, -0.5236F);
        frontcoverp2.texOffs(213, 158).addBox(-0.9278F, -4.113F, -0.933F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        frontcoverp2.texOffs(213, 158).addBox(0.5722F, -4.113F, -0.067F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r102 = new ModelRenderer(this);
        cube_r102.setPos(0.4561F, -2.613F, 0.0F);
        frontcoverp2.addChild(cube_r102);
        setRotationAngle(cube_r102, 0.0F, 0.5236F, 0.0F);
        cube_r102.texOffs(213, 158).addBox(0.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        frontcoverp3 = new ModelRenderer(this);
        frontcoverp3.setPos(5.2261F, 0.0F, 8.933F);
        cover_front.addChild(frontcoverp3);
        setRotationAngle(frontcoverp3, 0.0F, 0.0F, 0.5236F);
        frontcoverp3.texOffs(213, 158).addBox(-0.9278F, 1.113F, -0.933F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        frontcoverp3.texOffs(213, 158).addBox(0.5722F, 1.113F, -0.067F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r103 = new ModelRenderer(this);
        cube_r103.setPos(0.4561F, 2.613F, 0.0F);
        frontcoverp3.addChild(cube_r103);
        setRotationAngle(cube_r103, 0.0F, 0.5236F, 0.0F);
        cube_r103.texOffs(213, 158).addBox(0.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        frontcoverp4 = new ModelRenderer(this);
        frontcoverp4.setPos(-4.9821F, 0.0F, 8.933F);
        cover_front.addChild(frontcoverp4);
        frontcoverp4.texOffs(213, 158).addBox(-0.616F, -1.5F, -0.933F, 2.0F, 3.0F, 1.0F, 0.0F, true);
        frontcoverp4.texOffs(213, 158).addBox(-1.116F, -1.5F, -0.067F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        cube_r104 = new ModelRenderer(this);
        cube_r104.setPos(0.0F, 0.0F, 0.0F);
        frontcoverp4.addChild(cube_r104);
        setRotationAngle(cube_r104, 0.0F, -0.5236F, 0.0F);
        cube_r104.texOffs(213, 158).addBox(-1.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        frontcoverp5 = new ModelRenderer(this);
        frontcoverp5.setPos(-5.2261F, 0.0F, 8.933F);
        cover_front.addChild(frontcoverp5);
        setRotationAngle(frontcoverp5, 0.0F, 0.0F, 0.5236F);
        frontcoverp5.texOffs(213, 158).addBox(-1.0722F, -4.113F, -0.933F, 2.0F, 3.0F, 1.0F, 0.0F, true);
        frontcoverp5.texOffs(213, 158).addBox(-1.5722F, -4.113F, -0.067F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        cube_r105 = new ModelRenderer(this);
        cube_r105.setPos(-0.4561F, -2.613F, 0.0F);
        frontcoverp5.addChild(cube_r105);
        setRotationAngle(cube_r105, 0.0F, -0.5236F, 0.0F);
        cube_r105.texOffs(213, 158).addBox(-1.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        frontcoverp6 = new ModelRenderer(this);
        frontcoverp6.setPos(-5.2261F, 0.0F, 8.933F);
        cover_front.addChild(frontcoverp6);
        setRotationAngle(frontcoverp6, 0.0F, 0.0F, -0.5236F);
        frontcoverp6.texOffs(213, 158).addBox(-1.0722F, 1.113F, -0.933F, 2.0F, 3.0F, 1.0F, 0.0F, true);
        frontcoverp6.texOffs(213, 158).addBox(-1.5722F, 1.113F, -0.067F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        cube_r106 = new ModelRenderer(this);
        cube_r106.setPos(-0.4561F, 2.613F, 0.0F);
        frontcoverp6.addChild(cube_r106);
        setRotationAngle(cube_r106, 0.0F, -0.5236F, 0.0F);
        cube_r106.texOffs(213, 158).addBox(-1.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        frontcoverp7 = new ModelRenderer(this);
        frontcoverp7.setPos(-5.9262F, 2.613F, 8.933F);
        cover_front.addChild(frontcoverp7);
        setRotationAngle(frontcoverp7, 0.0F, 0.0F, -1.0472F);
        frontcoverp7.texOffs(213, 158).addBox(-0.372F, 2.3257F, -0.933F, 2.0F, 3.0F, 1.0F, 0.0F, true);
        frontcoverp7.texOffs(213, 158).addBox(-0.872F, 2.3257F, -0.067F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        cube_r107 = new ModelRenderer(this);
        cube_r107.setPos(0.244F, 3.8257F, 0.0F);
        frontcoverp7.addChild(cube_r107);
        setRotationAngle(cube_r107, 0.0F, -0.5236F, 0.0F);
        cube_r107.texOffs(213, 158).addBox(-1.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        frontcoverp8 = new ModelRenderer(this);
        frontcoverp8.setPos(5.9262F, 2.613F, 8.933F);
        cover_front.addChild(frontcoverp8);
        setRotationAngle(frontcoverp8, 0.0F, 0.0F, 1.0472F);
        frontcoverp8.texOffs(213, 158).addBox(-1.628F, 2.3257F, -0.933F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        frontcoverp8.texOffs(213, 158).addBox(-0.128F, 2.3257F, -0.067F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r108 = new ModelRenderer(this);
        cube_r108.setPos(-0.244F, 3.8257F, 0.0F);
        frontcoverp8.addChild(cube_r108);
        setRotationAngle(cube_r108, 0.0F, 0.5236F, 0.0F);
        cube_r108.texOffs(213, 158).addBox(0.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        frontcoverp9 = new ModelRenderer(this);
        frontcoverp9.setPos(-5.9262F, -2.613F, 8.933F);
        cover_front.addChild(frontcoverp9);
        setRotationAngle(frontcoverp9, 0.0F, 0.0F, 1.0472F);
        frontcoverp9.texOffs(213, 158).addBox(-0.372F, -5.3257F, -0.933F, 2.0F, 3.0F, 1.0F, 0.0F, true);
        frontcoverp9.texOffs(213, 158).addBox(-0.872F, -5.3257F, -0.067F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        cube_r109 = new ModelRenderer(this);
        cube_r109.setPos(0.244F, -3.8257F, 0.0F);
        frontcoverp9.addChild(cube_r109);
        setRotationAngle(cube_r109, 0.0F, -0.5236F, 0.0F);
        cube_r109.texOffs(213, 158).addBox(-1.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        frontcoverp10 = new ModelRenderer(this);
        frontcoverp10.setPos(5.9262F, -2.613F, 8.933F);
        cover_front.addChild(frontcoverp10);
        setRotationAngle(frontcoverp10, 0.0F, 0.0F, -1.0472F);
        frontcoverp10.texOffs(213, 158).addBox(-1.628F, -5.3257F, -0.933F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        frontcoverp10.texOffs(213, 158).addBox(-0.128F, -5.3257F, -0.067F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r110 = new ModelRenderer(this);
        cube_r110.setPos(-0.244F, -3.8257F, 0.0F);
        frontcoverp10.addChild(cube_r110);
        setRotationAngle(cube_r110, 0.0F, 0.5236F, 0.0F);
        cube_r110.texOffs(213, 158).addBox(0.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        frontcoverp11 = new ModelRenderer(this);
        frontcoverp11.setPos(4.9821F, 0.0F, 8.933F);
        cover_front.addChild(frontcoverp11);
        setRotationAngle(frontcoverp11, 0.0F, 0.0F, -1.5708F);
        frontcoverp11.texOffs(213, 158).addBox(3.5981F, -6.4821F, -0.933F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        frontcoverp11.texOffs(213, 158).addBox(5.0981F, -6.4821F, -0.067F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r111 = new ModelRenderer(this);
        cube_r111.setPos(4.9821F, -4.9821F, 0.0F);
        frontcoverp11.addChild(cube_r111);
        setRotationAngle(cube_r111, 0.0F, 0.5236F, 0.0F);
        cube_r111.texOffs(213, 158).addBox(0.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        frontcoverp12 = new ModelRenderer(this);
        frontcoverp12.setPos(4.9821F, 0.0F, 8.933F);
        cover_front.addChild(frontcoverp12);
        setRotationAngle(frontcoverp12, 0.0F, 0.0F, 1.5708F);
        frontcoverp12.texOffs(213, 158).addBox(3.5981F, 3.4821F, -0.933F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        frontcoverp12.texOffs(213, 158).addBox(5.0981F, 3.4821F, -0.067F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r112 = new ModelRenderer(this);
        cube_r112.setPos(4.9821F, 4.9821F, 0.0F);
        frontcoverp12.addChild(cube_r112);
        setRotationAngle(cube_r112, 0.0F, 0.5236F, 0.0F);
        cube_r112.texOffs(213, 158).addBox(0.0F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        stock_piece = new ModelRenderer(this);
        stock_piece.setPos(0.0F, -7.0981F, 4.1F);
        gun.addChild(stock_piece);
        setRotationAngle(stock_piece, -0.0873F, 0.0F, 0.0F);
        stock_piece.texOffs(213, 158).addBox(-1.5F, 0.0F, 2.0F, 3.0F, 3.0F, 10.0F, 0.0F, false);
        stock_piece.texOffs(213, 158).addBox(-1.5F, 2.0F, 0.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        stock_piece.texOffs(213, 158).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        stock_piece.texOffs(215, 153).addBox(-1.5F, 0.5F, 0.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        stock_piece.texOffs(19, 168).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r113 = new ModelRenderer(this);
        cube_r113.setPos(0.0F, 0.7418F, 1.1635F);
        stock_piece.addChild(cube_r113);
        setRotationAngle(cube_r113, 0.2618F, 0.0F, 0.0F);
        cube_r113.texOffs(215, 153).addBox(-1.5F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        stock = new ModelRenderer(this);
        stock.setPos(-0.5179F, 0.067F, 19.5F);
        stock_piece.addChild(stock);
        stock.texOffs(220, 157).addBox(-1.4821F, -0.567F, -9.5F, 4.0F, 4.0F, 9.0F, 0.0F, false);
        stock.texOffs(229, 162).addBox(-0.4821F, 3.299F, -8.5F, 2.0F, 2.0F, 8.0F, 0.0F, false);
        stock.texOffs(229, 162).addBox(0.0179F, 5.299F, -8.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        stock.texOffs(229, 162).addBox(0.0179F, 6.299F, -8.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);
        stock.texOffs(229, 162).addBox(0.0179F, 5.299F, -6.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        stock.texOffs(229, 162).addBox(0.0179F, 5.299F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        stock.texOffs(229, 162).addBox(0.0179F, 5.299F, -3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        stock.texOffs(229, 162).addBox(0.0179F, 5.299F, -2.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        stock.texOffs(229, 162).addBox(-1.4821F, 3.299F, -0.5F, 4.0F, 5.0F, 2.0F, 0.0F, false);
        stock.texOffs(229, 162).addBox(-1.9821F, -0.567F, -0.5F, 5.0F, 4.0F, 2.0F, 0.0F, false);
        stock.texOffs(229, 162).addBox(-1.4821F, -1.433F, -0.5F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        stock.texOffs(229, 162).addBox(-0.4821F, 8.0311F, -0.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        cube_r114 = new ModelRenderer(this);
        cube_r114.setPos(0.0F, 0.0F, 0.0F);
        stock.addChild(cube_r114);
        setRotationAngle(cube_r114, 0.0F, 0.0F, 0.5236F);
        cube_r114.texOffs(229, 162).addBox(-2.0F, -0.5F, -0.5F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        cube_r115 = new ModelRenderer(this);
        cube_r115.setPos(-0.0678F, 3.433F, -4.0F);
        stock.addChild(cube_r115);
        setRotationAngle(cube_r115, 0.0F, 0.0F, -0.7854F);
        cube_r115.texOffs(229, 162).addBox(-1.0F, -1.0F, -3.5F, 1.0F, 2.0F, 7.0F, 0.0F, true);

        cube_r116 = new ModelRenderer(this);
        cube_r116.setPos(0.0F, 2.866F, 0.0F);
        stock.addChild(cube_r116);
        setRotationAngle(cube_r116, 0.0F, 0.0F, -0.5236F);
        cube_r116.texOffs(229, 162).addBox(-2.0F, -0.5F, -0.5F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        cube_r117 = new ModelRenderer(this);
        cube_r117.setPos(0.5F, 7.7321F, 0.0F);
        stock.addChild(cube_r117);
        setRotationAngle(cube_r117, 0.0F, 0.0F, -0.5236F);
        cube_r117.texOffs(229, 162).addBox(-2.0F, -0.5F, -0.5F, 1.0F, 2.0F, 2.0F, 0.0F, true);

        cube_r118 = new ModelRenderer(this);
        cube_r118.setPos(1.0359F, 0.0F, 0.0F);
        stock.addChild(cube_r118);
        setRotationAngle(cube_r118, 0.0F, 0.0F, -0.5236F);
        cube_r118.texOffs(229, 162).addBox(1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r119 = new ModelRenderer(this);
        cube_r119.setPos(1.0359F, 2.866F, 0.0F);
        stock.addChild(cube_r119);
        setRotationAngle(cube_r119, 0.0F, 0.0F, 0.5236F);
        cube_r119.texOffs(229, 162).addBox(1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r120 = new ModelRenderer(this);
        cube_r120.setPos(0.5359F, 7.7321F, 0.0F);
        stock.addChild(cube_r120);
        setRotationAngle(cube_r120, 0.0F, 0.0F, 0.5236F);
        cube_r120.texOffs(229, 162).addBox(1.0F, -0.5F, -0.5F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        cube_r121 = new ModelRenderer(this);
        cube_r121.setPos(0.5179F, 6.9024F, -3.2956F);
        stock.addChild(cube_r121);
        setRotationAngle(cube_r121, -0.6545F, 0.0F, 0.0F);
        cube_r121.texOffs(229, 162).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        cube_r122 = new ModelRenderer(this);
        cube_r122.setPos(1.1037F, 3.433F, -4.0F);
        stock.addChild(cube_r122);
        setRotationAngle(cube_r122, 0.0F, 0.0F, 0.7854F);
        cube_r122.texOffs(229, 162).addBox(0.0F, -1.0F, -3.5F, 1.0F, 2.0F, 7.0F, 0.0F, false);

        ironsights = new ModelRenderer(this);
        ironsights.setPos(0.0F, 11.0F, 0.0F);
        ironsights.texOffs(0, 0).addBox(-1.5F, 2.5F, 1.5F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        ironsights.texOffs(0, 0).addBox(-1.5F, 2.5F, -8.5F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r123 = new ModelRenderer(this);
        cube_r123.setPos(-2.116F, 3.433F, -7.5F);
        ironsights.addChild(cube_r123);
        setRotationAngle(cube_r123, 0.0F, 0.0F, -0.5236F);
        cube_r123.texOffs(0, 0).addBox(0.0F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        cube_r123.texOffs(0, 0).addBox(0.0F, -0.5F, 9.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        cube_r124 = new ModelRenderer(this);
        cube_r124.setPos(2.116F, 3.433F, -7.5F);
        ironsights.addChild(cube_r124);
        setRotationAngle(cube_r124, 0.0F, 0.0F, 0.5236F);
        cube_r124.texOffs(0, 0).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        cube_r124.texOffs(0, 0).addBox(-1.0F, -0.5F, 9.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r125 = new ModelRenderer(this);
        cube_r125.setPos(0.0F, 2.1F, -7.4F);
        ironsights.addChild(cube_r125);
        setRotationAngle(cube_r125, -0.1309F, 0.0F, 0.0F);
        cube_r125.texOffs(48, 173).addBox(-1.4F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r125.texOffs(48, 173).addBox(0.4F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r126 = new ModelRenderer(this);
        cube_r126.setPos(0.0F, 2.1F, 2.6F);
        ironsights.addChild(cube_r126);
        setRotationAngle(cube_r126, -0.1309F, 0.0F, 0.0F);
        cube_r126.texOffs(48, 173).addBox(-1.4F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r126.texOffs(48, 173).addBox(0.4F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r127 = new ModelRenderer(this);
        cube_r127.setPos(0.0F, 2.1F, 3.0F);
        ironsights.addChild(cube_r127);
        setRotationAngle(cube_r127, 0.1745F, 0.0F, 0.0F);
        cube_r127.texOffs(48, 173).addBox(-2.0F, -3.8F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        cube_r127.texOffs(48, 173).addBox(-1.9F, -2.8F, -0.6F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r127.texOffs(48, 173).addBox(0.9F, -2.8F, -0.6F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r127.texOffs(48, 173).addBox(1.0F, -3.8F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        cube_r127.texOffs(48, 173).addBox(-2.0F, -4.8F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r127.texOffs(48, 173).addBox(-2.0F, -0.8F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r128 = new ModelRenderer(this);
        cube_r128.setPos(1.5792F, 2.2552F, -7.0F);
        ironsights.addChild(cube_r128);
        setRotationAngle(cube_r128, 0.1745F, 0.0F, 0.0F);
        cube_r128.texOffs(48, 173).addBox(0.0F, -1.8F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r128.texOffs(48, 173).addBox(-4.1585F, -1.8F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r129 = new ModelRenderer(this);
        cube_r129.setPos(-0.0652F, 1.735F, -6.8784F);
        ironsights.addChild(cube_r129);
        setRotationAngle(cube_r129, 0.1745F, 0.0F, -0.5236F);
        cube_r129.texOffs(48, 173).addBox(-2.0F, -1.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r130 = new ModelRenderer(this);
        cube_r130.setPos(0.0652F, 1.735F, -6.8784F);
        ironsights.addChild(cube_r130);
        setRotationAngle(cube_r130, 0.1745F, 0.0F, 0.5236F);
        cube_r130.texOffs(48, 173).addBox(1.0F, -1.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r131 = new ModelRenderer(this);
        cube_r131.setPos(1.0F, 2.1F, -7.0F);
        ironsights.addChild(cube_r131);
        setRotationAngle(cube_r131, 0.1745F, 0.0F, 0.0F);
        cube_r131.texOffs(48, 173).addBox(-3.0F, -0.8F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        setSpecialRenderer(ModAnimations.MAGAZINE, magazine);
    }
}
