package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class RocketLauncherModel extends AbstractWeaponModel {

    private final ModelRenderer battery;
    private final ModelRenderer cube_r1;
    private final ModelRenderer volt_icon_rotation;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer launcher;
    private final ModelRenderer covers;
    private final ModelRenderer front_inner;
    private final ModelRenderer front_outer;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer loading_outer;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer cube_r11;
    private final ModelRenderer loading_inner;
    private final ModelRenderer tubes;
    private final ModelRenderer tl;
    private final ModelRenderer cube_r12;
    private final ModelRenderer cube_r13;
    private final ModelRenderer cube_r14;
    private final ModelRenderer cube_r15;
    private final ModelRenderer tr;
    private final ModelRenderer cube_r16;
    private final ModelRenderer cube_r17;
    private final ModelRenderer cube_r18;
    private final ModelRenderer cube_r19;
    private final ModelRenderer bl;
    private final ModelRenderer cube_r20;
    private final ModelRenderer cube_r21;
    private final ModelRenderer cube_r22;
    private final ModelRenderer cube_r23;
    private final ModelRenderer br;
    private final ModelRenderer cube_r24;
    private final ModelRenderer cube_r25;
    private final ModelRenderer cube_r26;
    private final ModelRenderer cube_r27;
    private final ModelRenderer hull;
    private final ModelRenderer cube_r28;
    private final ModelRenderer cube_r29;
    private final ModelRenderer cube_r30;
    private final ModelRenderer cube_r31;
    private final ModelRenderer cube_r32;
    private final ModelRenderer cube_r33;
    private final ModelRenderer cube_r34;
    private final ModelRenderer cube_r35;
    private final ModelRenderer cube_r36;
    private final ModelRenderer receiver;
    private final ModelRenderer grip;
    private final ModelRenderer cube_r37;
    private final ModelRenderer cube_r38;
    private final ModelRenderer cube_r39;
    private final ModelRenderer housing;
    private final ModelRenderer cube_r40;
    private final ModelRenderer cube_r41;
    private final ModelRenderer cube_r42;
    private final ModelRenderer cube_r43;
    private final ModelRenderer wiring;
    private final ModelRenderer cube_r44;
    private final ModelRenderer cube_r45;
    private final ModelRenderer cube_r46;
    private final ModelRenderer cube_r47;
    private final ModelRenderer cube_r48;
    private final ModelRenderer cube_r49;
    private final ModelRenderer cube_r50;
    private final ModelRenderer cube_r51;
    private final ModelRenderer cube_r52;
    private final ModelRenderer cube_r53;
    private final ModelRenderer cube_r54;
    private final ModelRenderer cube_r55;
    private final ModelRenderer laser;
    private final ModelRenderer cube_r56;
    private final ModelRenderer opticmount;
    private final ModelRenderer rail;
    private final ModelRenderer cube_r57;
    private final ModelRenderer cube_r58;
    private final ModelRenderer frame;
    private final ModelRenderer cube_r59;
    private final ModelRenderer cube_r60;
    private final ModelRenderer cube_r61;
    private final ModelRenderer cube_r62;
    private final ModelRenderer cube_r63;
    private final ModelRenderer cube_r64;
    private final ModelRenderer cube_r65;
    private final ModelRenderer loading_cover;
    private final ModelRenderer cube_r66;
    private final ModelRenderer cube_r67;
    private final ModelRenderer cube_r68;
    private final ModelRenderer cube_r69;
    private final ModelRenderer cube_r70;
    private final ModelRenderer cube_r71;
    private final ModelRenderer cube_r72;
    private final ModelRenderer cube_r73;
    private final ModelRenderer cube_r74;
    private final ModelRenderer ironsights;
    private final ModelRenderer rearsight;
    private final ModelRenderer cube_r75;
    private final ModelRenderer cube_r76;
    private final ModelRenderer cube_r77;
    private final ModelRenderer cube_r78;
    private final ModelRenderer cube_r79;
    private final ModelRenderer cube_r80;
    private final ModelRenderer cube_r81;
    private final ModelRenderer cube_r82;
    private final ModelRenderer cube_r83;
    private final ModelRenderer cube_r84;
    private final ModelRenderer frontsight;
    private final ModelRenderer cube_r85;
    private final ModelRenderer cube_r86;
    private final ModelRenderer cube_r87;
    private final ModelRenderer cube_r88;
    private final ModelRenderer cube_r89;
    private final ModelRenderer cube_r90;
    private final ModelRenderer cube_r91;
    private final ModelRenderer cube_r92;
    private final ModelRenderer cube_r93;
    private final ModelRenderer cube_r94;

    @Override
    protected void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        launcher.render(matrix, builder, light, overlay);
        loading_cover.render(matrix, builder, light, overlay);
        ironsights.render(matrix, builder, light, overlay);
        battery.render(matrix, builder, light, overlay);
    }

    public RocketLauncherModel() {
        texWidth = 512;
        texHeight = 512;

        battery = new ModelRenderer(this);
        battery.setPos(-5.0F, 35.7321F, -0.3F);
        battery.texOffs(97, 108).addBox(3.0F, -1.0F, 8.0F, 4.0F, 4.0F, 6.0F, 0.0F, false);
        battery.texOffs(86, 42).addBox(4.0F, 2.0009F, 9.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        battery.texOffs(6, 16).addBox(3.5F, -0.5F, 13.5F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        battery.texOffs(6, 16).addBox(5.5F, -0.5F, 7.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        battery.texOffs(6, 16).addBox(3.5F, -0.5F, 7.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(5.25F, 2.0F, 10.7F);
        battery.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.7854F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 493).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        cube_r1.texOffs(0, 493).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        volt_icon_rotation = new ModelRenderer(this);
        volt_icon_rotation.setPos(4.5F, 2.0F, 11.0F);
        battery.addChild(volt_icon_rotation);
        setRotationAngle(volt_icon_rotation, 0.0F, -0.5236F, 0.0F);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(-0.0397F, -1.0607F, -0.5688F);
        volt_icon_rotation.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, 0.7854F);
        cube_r2.texOffs(0, 493).addBox(0.3939F, 0.6061F, -1.2598F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(1.2593F, -1.0607F, 0.6812F);
        volt_icon_rotation.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, 0.7854F);
        cube_r3.texOffs(0, 493).addBox(0.3939F, 0.6061F, -1.2598F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        launcher = new ModelRenderer(this);
        launcher.setPos(0.0F, 24.0F, 0.0F);


        covers = new ModelRenderer(this);
        covers.setPos(-4.5F, 3.0F, 0.0F);
        launcher.addChild(covers);


        front_inner = new ModelRenderer(this);
        front_inner.setPos(6.0F, 0.0F, 0.0F);
        covers.addChild(front_inner);
        front_inner.texOffs(90, 102).addBox(-6.0F, -7.5F, 2.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        front_inner.texOffs(90, 102).addBox(-6.0F, -1.5F, 2.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        front_inner.texOffs(90, 102).addBox(0.0F, -1.5F, 2.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        front_inner.texOffs(90, 102).addBox(0.0F, -7.5F, 2.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        front_outer = new ModelRenderer(this);
        front_outer.setPos(2.5F, 3.0F, 0.0F);
        covers.addChild(front_outer);
        front_outer.texOffs(24, 86).addBox(6.0F, -8.0F, -13.0F, 1.0F, 4.0F, 1.0F, 0.0F, true);
        front_outer.texOffs(24, 86).addBox(1.0F, -10.0F, -13.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        front_outer.texOffs(24, 86).addBox(0.0F, -11.0F, -13.0F, 4.0F, 1.0F, 1.0F, 0.0F, true);
        front_outer.texOffs(24, 86).addBox(0.0F, -8.0F, -13.0F, 4.0F, 4.0F, 1.0F, 0.0F, true);
        front_outer.texOffs(24, 86).addBox(-3.0F, -8.0F, -13.0F, 1.0F, 4.0F, 1.0F, 0.0F, true);
        front_outer.texOffs(24, 86).addBox(-5.0F, -10.0F, -13.0F, 2.0F, 8.0F, 1.0F, 0.0F, true);
        front_outer.texOffs(24, 86).addBox(7.0F, -10.0F, -13.0F, 2.0F, 8.0F, 1.0F, 0.0F, true);
        front_outer.texOffs(24, 86).addBox(-2.0F, -13.0F, -13.0F, 8.0F, 2.0F, 1.0F, 0.0F, true);
        front_outer.texOffs(24, 86).addBox(-2.0F, -1.0F, -13.0F, 8.0F, 2.0F, 1.0F, 0.0F, true);
        front_outer.texOffs(24, 86).addBox(-2.0F, -7.0F, -13.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        front_outer.texOffs(24, 86).addBox(1.0F, -4.0F, -13.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        front_outer.texOffs(24, 86).addBox(0.0F, -2.0F, -13.0F, 4.0F, 1.0F, 1.0F, 0.0F, true);
        front_outer.texOffs(24, 86).addBox(4.0F, -7.0F, -13.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-2.5F, -1.5F, -12.5F);
        front_outer.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.7854F);
        cube_r4.texOffs(24, 86).addBox(-1.7F, -2.0F, -0.499F, 2.0F, 4.0F, 1.0F, 0.0F, true);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(6.5F, -1.5F, -12.5F);
        front_outer.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, 0.7854F);
        cube_r5.texOffs(24, 86).addBox(-0.3F, -2.0F, -0.499F, 2.0F, 4.0F, 1.0F, 0.0F, false);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setPos(6.5F, -10.5F, -12.5F);
        front_outer.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, -0.7854F);
        cube_r6.texOffs(24, 86).addBox(-0.3F, -2.0F, -0.499F, 2.0F, 4.0F, 1.0F, 0.0F, false);

        cube_r7 = new ModelRenderer(this);
        cube_r7.setPos(-2.5F, -10.5F, -12.5F);
        front_outer.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, 0.7854F);
        cube_r7.texOffs(24, 86).addBox(-1.7F, -2.0F, -0.499F, 2.0F, 4.0F, 1.0F, 0.0F, true);

        loading_outer = new ModelRenderer(this);
        loading_outer.setPos(2.5F, 3.0F, 61.0F);
        covers.addChild(loading_outer);
        loading_outer.texOffs(24, 86).addBox(6.0F, -8.0F, -13.0F, 1.0F, 4.0F, 1.0F, 0.0F, true);
        loading_outer.texOffs(24, 86).addBox(1.0F, -10.0F, -13.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        loading_outer.texOffs(24, 86).addBox(0.0F, -11.0F, -13.0F, 4.0F, 1.0F, 1.0F, 0.0F, true);
        loading_outer.texOffs(24, 86).addBox(0.0F, -8.0F, -13.0F, 4.0F, 4.0F, 1.0F, 0.0F, true);
        loading_outer.texOffs(24, 86).addBox(-3.0F, -8.0F, -13.0F, 1.0F, 4.0F, 1.0F, 0.0F, true);
        loading_outer.texOffs(24, 86).addBox(-5.0F, -10.0F, -13.0F, 2.0F, 8.0F, 1.0F, 0.0F, true);
        loading_outer.texOffs(24, 86).addBox(7.0F, -10.0F, -13.0F, 2.0F, 8.0F, 1.0F, 0.0F, true);
        loading_outer.texOffs(24, 86).addBox(-2.0F, -13.0F, -13.0F, 8.0F, 2.0F, 1.0F, 0.0F, true);
        loading_outer.texOffs(24, 86).addBox(-2.0F, -1.0F, -13.0F, 8.0F, 2.0F, 1.0F, 0.0F, true);
        loading_outer.texOffs(24, 86).addBox(-2.0F, -7.0F, -13.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        loading_outer.texOffs(24, 86).addBox(1.0F, -4.0F, -13.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        loading_outer.texOffs(24, 86).addBox(0.0F, -2.0F, -13.0F, 4.0F, 1.0F, 1.0F, 0.0F, true);
        loading_outer.texOffs(24, 86).addBox(4.0F, -7.0F, -13.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);

        cube_r8 = new ModelRenderer(this);
        cube_r8.setPos(-2.5F, -1.5F, -12.5F);
        loading_outer.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.0F, -0.7854F);
        cube_r8.texOffs(24, 86).addBox(-1.7F, -2.0F, -0.499F, 2.0F, 4.0F, 1.0F, 0.0F, true);

        cube_r9 = new ModelRenderer(this);
        cube_r9.setPos(6.5F, -1.5F, -12.5F);
        loading_outer.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.0F, 0.7854F);
        cube_r9.texOffs(24, 86).addBox(-0.3F, -2.0F, -0.499F, 2.0F, 4.0F, 1.0F, 0.0F, false);

        cube_r10 = new ModelRenderer(this);
        cube_r10.setPos(6.5F, -10.5F, -12.5F);
        loading_outer.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.0F, -0.7854F);
        cube_r10.texOffs(24, 86).addBox(-0.3F, -2.0F, -0.499F, 2.0F, 4.0F, 1.0F, 0.0F, false);

        cube_r11 = new ModelRenderer(this);
        cube_r11.setPos(-2.5F, -10.5F, -12.5F);
        loading_outer.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, 0.0F, 0.7854F);
        cube_r11.texOffs(24, 86).addBox(-1.7F, -2.0F, -0.499F, 2.0F, 4.0F, 1.0F, 0.0F, true);

        loading_inner = new ModelRenderer(this);
        loading_inner.setPos(6.0F, 0.0F, 38.0F);
        covers.addChild(loading_inner);
        loading_inner.texOffs(90, 102).addBox(-6.0F, -7.5F, 2.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        loading_inner.texOffs(90, 102).addBox(-6.0F, -1.5F, 2.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        loading_inner.texOffs(90, 102).addBox(0.0F, -1.5F, 2.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        loading_inner.texOffs(90, 102).addBox(0.0F, -7.5F, 2.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        tubes = new ModelRenderer(this);
        tubes.setPos(-4.3536F, -4.3536F, 0.5F);
        launcher.addChild(tubes);


        tl = new ModelRenderer(this);
        tl.setPos(2.7071F, 0.0F, 0.0F);
        tubes.addChild(tl);
        tl.texOffs(0, 0).addBox(-2.3536F, -1.0607F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        tl.texOffs(0, 0).addBox(-2.3536F, -1.0607F, 32.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        tl.texOffs(0, 0).addBox(-3.7678F, 0.3536F, -15.5F, 1.0F, 2.0F, 17.0F, 0.0F, false);
        tl.texOffs(0, 0).addBox(-3.7678F, 0.3536F, 32.5F, 1.0F, 2.0F, 17.0F, 0.0F, false);
        tl.texOffs(0, 0).addBox(-2.3536F, 2.7678F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        tl.texOffs(0, 0).addBox(-2.3536F, 2.7678F, 32.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        tl.texOffs(0, 0).addBox(0.0607F, 0.3536F, -15.5F, 1.0F, 2.0F, 17.0F, 0.0F, true);
        tl.texOffs(0, 0).addBox(0.0607F, 0.3536F, 32.5F, 1.0F, 2.0F, 17.0F, 0.0F, true);

        cube_r12 = new ModelRenderer(this);
        cube_r12.setPos(0.0F, 0.0F, 48.0F);
        tl.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.0F, 0.0F, 0.7854F);
        cube_r12.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        cube_r12.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);

        cube_r13 = new ModelRenderer(this);
        cube_r13.setPos(0.0F, 2.7071F, 48.0F);
        tl.addChild(cube_r13);
        setRotationAngle(cube_r13, 0.0F, 0.0F, -0.7854F);
        cube_r13.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        cube_r13.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);

        cube_r14 = new ModelRenderer(this);
        cube_r14.setPos(-2.7071F, 2.7071F, 48.0F);
        tl.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.0F, 0.0F, 0.7854F);
        cube_r14.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);
        cube_r14.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);

        cube_r15 = new ModelRenderer(this);
        cube_r15.setPos(-2.7071F, 0.0F, 48.0F);
        tl.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0F, 0.0F, -0.7854F);
        cube_r15.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);
        cube_r15.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);

        tr = new ModelRenderer(this);
        tr.setPos(7.3536F, 3.7678F, -0.5F);
        tubes.addChild(tr);
        tr.texOffs(0, 0).addBox(-2.4142F, -3.4142F, -15.0F, 1.0F, 2.0F, 17.0F, 0.0F, false);
        tr.texOffs(0, 0).addBox(-2.4142F, -3.4142F, 33.0F, 1.0F, 2.0F, 17.0F, 0.0F, false);
        tr.texOffs(0, 0).addBox(-1.0F, -4.8284F, -15.0F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        tr.texOffs(0, 0).addBox(-1.0F, -4.8284F, 33.0F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        tr.texOffs(0, 0).addBox(1.4142F, -3.4142F, -15.0F, 1.0F, 2.0F, 17.0F, 0.0F, true);
        tr.texOffs(0, 0).addBox(1.4142F, -3.4142F, 33.0F, 1.0F, 2.0F, 17.0F, 0.0F, true);
        tr.texOffs(0, 0).addBox(-1.0F, -1.0F, -15.0F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        tr.texOffs(0, 0).addBox(-1.0F, -1.0F, 33.0F, 2.0F, 1.0F, 17.0F, 0.0F, false);

        cube_r16 = new ModelRenderer(this);
        cube_r16.setPos(-1.3536F, -1.0607F, 48.5F);
        tr.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.0F, 0.0F, 0.7854F);
        cube_r16.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);
        cube_r16.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);

        cube_r17 = new ModelRenderer(this);
        cube_r17.setPos(1.3536F, -1.0607F, 48.5F);
        tr.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.0F, 0.0F, -0.7854F);
        cube_r17.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        cube_r17.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);

        cube_r18 = new ModelRenderer(this);
        cube_r18.setPos(1.3536F, -3.7678F, 48.5F);
        tr.addChild(cube_r18);
        setRotationAngle(cube_r18, 0.0F, 0.0F, 0.7854F);
        cube_r18.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        cube_r18.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);

        cube_r19 = new ModelRenderer(this);
        cube_r19.setPos(-1.3536F, -3.7678F, 48.5F);
        tr.addChild(cube_r19);
        setRotationAngle(cube_r19, 0.0F, 0.0F, -0.7854F);
        cube_r19.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);
        cube_r19.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);

        bl = new ModelRenderer(this);
        bl.setPos(0.0F, 6.0F, 0.0F);
        tubes.addChild(bl);
        bl.texOffs(0, 0).addBox(0.3536F, 2.7678F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        bl.texOffs(0, 0).addBox(0.3536F, 2.7678F, 32.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        bl.texOffs(0, 0).addBox(-1.0607F, 0.3536F, -15.5F, 1.0F, 2.0F, 17.0F, 0.0F, false);
        bl.texOffs(0, 0).addBox(-1.0607F, 0.3536F, 32.5F, 1.0F, 2.0F, 17.0F, 0.0F, false);
        bl.texOffs(0, 0).addBox(2.7678F, 0.3536F, -15.5F, 1.0F, 2.0F, 17.0F, 0.0F, true);
        bl.texOffs(0, 0).addBox(2.7678F, 0.3536F, 32.5F, 1.0F, 2.0F, 17.0F, 0.0F, true);
        bl.texOffs(0, 0).addBox(0.3536F, -1.0607F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        bl.texOffs(0, 0).addBox(0.3536F, -1.0607F, 32.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);

        cube_r20 = new ModelRenderer(this);
        cube_r20.setPos(0.0F, 0.0F, 48.0F);
        bl.addChild(cube_r20);
        setRotationAngle(cube_r20, 0.0F, 0.0F, -0.7854F);
        cube_r20.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);
        cube_r20.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);

        cube_r21 = new ModelRenderer(this);
        cube_r21.setPos(0.0F, 2.7071F, 48.0F);
        bl.addChild(cube_r21);
        setRotationAngle(cube_r21, 0.0F, 0.0F, 0.7854F);
        cube_r21.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);
        cube_r21.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);

        cube_r22 = new ModelRenderer(this);
        cube_r22.setPos(2.7071F, 0.0F, 48.0F);
        bl.addChild(cube_r22);
        setRotationAngle(cube_r22, 0.0F, 0.0F, 0.7854F);
        cube_r22.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        cube_r22.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);

        cube_r23 = new ModelRenderer(this);
        cube_r23.setPos(2.7071F, 2.7071F, 48.0F);
        bl.addChild(cube_r23);
        setRotationAngle(cube_r23, 0.0F, 0.0F, -0.7854F);
        cube_r23.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        cube_r23.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);

        br = new ModelRenderer(this);
        br.setPos(8.7071F, 8.7071F, 0.0F);
        tubes.addChild(br);
        br.texOffs(0, 0).addBox(-2.3536F, 0.0607F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        br.texOffs(0, 0).addBox(-2.3536F, 0.0607F, 32.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        br.texOffs(0, 0).addBox(-3.7678F, -2.3536F, -15.5F, 1.0F, 2.0F, 17.0F, 0.0F, false);
        br.texOffs(0, 0).addBox(-3.7678F, -2.3536F, 32.5F, 1.0F, 2.0F, 17.0F, 0.0F, false);
        br.texOffs(0, 0).addBox(-2.3536F, -3.7678F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        br.texOffs(0, 0).addBox(-2.3536F, -3.7678F, 32.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        br.texOffs(0, 0).addBox(0.0607F, -2.3536F, -15.5F, 1.0F, 2.0F, 17.0F, 0.0F, true);
        br.texOffs(0, 0).addBox(0.0607F, -2.3536F, 32.5F, 1.0F, 2.0F, 17.0F, 0.0F, true);

        cube_r24 = new ModelRenderer(this);
        cube_r24.setPos(0.0F, 0.0F, 48.0F);
        br.addChild(cube_r24);
        setRotationAngle(cube_r24, 0.0F, 0.0F, -0.7854F);
        cube_r24.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        cube_r24.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);

        cube_r25 = new ModelRenderer(this);
        cube_r25.setPos(0.0F, -2.7071F, 48.0F);
        br.addChild(cube_r25);
        setRotationAngle(cube_r25, 0.0F, 0.0F, 0.7854F);
        cube_r25.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);
        cube_r25.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, false);

        cube_r26 = new ModelRenderer(this);
        cube_r26.setPos(-2.7071F, -2.7071F, 48.0F);
        br.addChild(cube_r26);
        setRotationAngle(cube_r26, 0.0F, 0.0F, -0.7854F);
        cube_r26.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);
        cube_r26.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);

        cube_r27 = new ModelRenderer(this);
        cube_r27.setPos(-2.7071F, 0.0F, 48.0F);
        br.addChild(cube_r27);
        setRotationAngle(cube_r27, 0.0F, 0.0F, 0.7854F);
        cube_r27.texOffs(0, 0).addBox(-1.0F, -0.5F, -15.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);
        cube_r27.texOffs(0, 0).addBox(-1.0F, -0.5F, -63.5F, 2.0F, 1.0F, 17.0F, 0.0F, true);

        hull = new ModelRenderer(this);
        hull.setPos(0.0F, 0.0F, -2.0F);
        launcher.addChild(hull);
        hull.texOffs(6, 399).addBox(-7.2321F, -4.5F, -14.0F, 1.0F, 9.0F, 24.0F, 0.0F, false);
        hull.texOffs(6, 399).addBox(-4.5F, -7.2321F, -14.0F, 9.0F, 1.0F, 24.0F, 0.0F, false);
        hull.texOffs(6, 399).addBox(-4.5F, 6.2321F, -14.0F, 9.0F, 1.0F, 24.0F, 0.0F, false);
        hull.texOffs(6, 399).addBox(6.2321F, -4.5F, -14.0F, 1.0F, 9.0F, 24.0F, 0.0F, true);
        hull.texOffs(6, 399).addBox(-7.2321F, -4.5F, 10.0F, 1.0F, 9.0F, 24.0F, 0.0F, false);
        hull.texOffs(6, 399).addBox(-7.2321F, -4.5F, 34.0F, 1.0F, 9.0F, 18.0F, 0.0F, false);
        hull.texOffs(6, 399).addBox(-4.5F, -7.2321F, 10.0F, 9.0F, 1.0F, 24.0F, 0.0F, false);
        hull.texOffs(6, 399).addBox(-4.5F, -7.2321F, 34.0F, 9.0F, 1.0F, 18.0F, 0.0F, false);
        hull.texOffs(6, 399).addBox(-4.5F, 6.2321F, 10.0F, 9.0F, 1.0F, 24.0F, 0.0F, false);
        hull.texOffs(6, 399).addBox(-4.5F, 6.2321F, 34.0F, 9.0F, 1.0F, 18.0F, 0.0F, false);
        hull.texOffs(6, 399).addBox(6.2321F, -4.5F, 10.0F, 1.0F, 9.0F, 24.0F, 0.0F, true);
        hull.texOffs(6, 399).addBox(6.2321F, -4.5F, 34.0F, 1.0F, 9.0F, 18.0F, 0.0F, true);
        hull.texOffs(27, 155).addBox(-8.2321F, -3.5F, 50.001F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        hull.texOffs(74, 26).addBox(-7.7321F, 2.0F, 53.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r28 = new ModelRenderer(this);
        cube_r28.setPos(-7.2321F, 2.5F, 51.0F);
        hull.addChild(cube_r28);
        setRotationAngle(cube_r28, 0.0F, 0.0F, -0.7854F);
        cube_r28.texOffs(27, 155).addBox(-1.0F, -1.0F, -0.999F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        cube_r29 = new ModelRenderer(this);
        cube_r29.setPos(-4.799F, 7.7141F, 41.5F);
        hull.addChild(cube_r29);
        setRotationAngle(cube_r29, 0.0F, 0.0F, -0.5236F);
        cube_r29.texOffs(6, 399).addBox(-0.5F, -4.0F, -7.5F, 1.0F, 2.0F, 18.0F, 0.0F, false);
        cube_r29.texOffs(6, 399).addBox(-0.5F, -4.0F, -31.5F, 1.0F, 2.0F, 24.0F, 0.0F, false);
        cube_r29.texOffs(6, 399).addBox(-0.5F, -4.0F, -55.5F, 1.0F, 2.0F, 24.0F, 0.0F, false);

        cube_r30 = new ModelRenderer(this);
        cube_r30.setPos(-4.799F, -7.7141F, 41.5F);
        hull.addChild(cube_r30);
        setRotationAngle(cube_r30, 0.0F, 0.0F, 0.5236F);
        cube_r30.texOffs(6, 399).addBox(-0.5F, 2.0F, -7.5F, 1.0F, 2.0F, 18.0F, 0.0F, false);
        cube_r30.texOffs(6, 399).addBox(-0.5F, 2.0F, -31.5F, 1.0F, 2.0F, 24.0F, 0.0F, false);
        cube_r30.texOffs(6, 399).addBox(-0.5F, 2.0F, -55.5F, 1.0F, 2.0F, 24.0F, 0.0F, false);

        cube_r31 = new ModelRenderer(this);
        cube_r31.setPos(4.799F, 7.7141F, 41.5F);
        hull.addChild(cube_r31);
        setRotationAngle(cube_r31, 0.0F, 0.0F, 0.5236F);
        cube_r31.texOffs(6, 399).addBox(-0.5F, -4.0F, -7.5F, 1.0F, 2.0F, 18.0F, 0.0F, true);
        cube_r31.texOffs(6, 399).addBox(-0.5F, -4.0F, -31.5F, 1.0F, 2.0F, 24.0F, 0.0F, true);
        cube_r31.texOffs(6, 399).addBox(-0.5F, -4.0F, -55.5F, 1.0F, 2.0F, 24.0F, 0.0F, true);

        cube_r32 = new ModelRenderer(this);
        cube_r32.setPos(6.799F, 8.2141F, 41.5F);
        hull.addChild(cube_r32);
        setRotationAngle(cube_r32, 0.0F, 0.0F, -0.5236F);
        cube_r32.texOffs(6, 399).addBox(-1.5F, -3.0F, -7.5F, 2.0F, 1.0F, 18.0F, 0.0F, false);
        cube_r32.texOffs(6, 399).addBox(-1.5F, -3.0F, -31.5F, 2.0F, 1.0F, 24.0F, 0.0F, false);
        cube_r32.texOffs(6, 399).addBox(-1.5F, -3.0F, -55.5F, 2.0F, 1.0F, 24.0F, 0.0F, false);

        cube_r33 = new ModelRenderer(this);
        cube_r33.setPos(6.799F, -8.2141F, 41.5F);
        hull.addChild(cube_r33);
        setRotationAngle(cube_r33, 0.0F, 0.0F, 0.5236F);
        cube_r33.texOffs(6, 399).addBox(-1.5F, 2.0F, -7.5F, 2.0F, 1.0F, 18.0F, 0.0F, false);
        cube_r33.texOffs(6, 399).addBox(-1.5F, 2.0F, -31.5F, 2.0F, 1.0F, 24.0F, 0.0F, false);
        cube_r33.texOffs(6, 399).addBox(-1.5F, 2.0F, -55.5F, 2.0F, 1.0F, 24.0F, 0.0F, false);

        cube_r34 = new ModelRenderer(this);
        cube_r34.setPos(-6.799F, 8.2141F, 41.5F);
        hull.addChild(cube_r34);
        setRotationAngle(cube_r34, 0.0F, 0.0F, 0.5236F);
        cube_r34.texOffs(6, 399).addBox(-0.5F, -3.0F, -7.5F, 2.0F, 1.0F, 18.0F, 0.0F, true);
        cube_r34.texOffs(6, 399).addBox(-0.5F, -3.0F, -31.5F, 2.0F, 1.0F, 24.0F, 0.0F, true);
        cube_r34.texOffs(6, 399).addBox(-0.5F, -3.0F, -55.5F, 2.0F, 1.0F, 24.0F, 0.0F, true);

        cube_r35 = new ModelRenderer(this);
        cube_r35.setPos(-6.799F, -8.2141F, 41.5F);
        hull.addChild(cube_r35);
        setRotationAngle(cube_r35, 0.0F, 0.0F, -0.5236F);
        cube_r35.texOffs(6, 399).addBox(-0.5F, 2.0F, -7.5F, 2.0F, 1.0F, 18.0F, 0.0F, true);
        cube_r35.texOffs(6, 399).addBox(-0.5F, 2.0F, -31.5F, 2.0F, 1.0F, 24.0F, 0.0F, true);
        cube_r35.texOffs(6, 399).addBox(-0.5F, 2.0F, -55.5F, 2.0F, 1.0F, 24.0F, 0.0F, true);

        cube_r36 = new ModelRenderer(this);
        cube_r36.setPos(4.799F, -7.7141F, 41.5F);
        hull.addChild(cube_r36);
        setRotationAngle(cube_r36, 0.0F, 0.0F, -0.5236F);
        cube_r36.texOffs(6, 399).addBox(-0.5F, 2.0F, -7.5F, 1.0F, 2.0F, 18.0F, 0.0F, true);
        cube_r36.texOffs(6, 399).addBox(-0.5F, 2.0F, -31.5F, 1.0F, 2.0F, 24.0F, 0.0F, true);
        cube_r36.texOffs(6, 399).addBox(-0.5F, 2.0F, -55.5F, 1.0F, 2.0F, 24.0F, 0.0F, true);

        receiver = new ModelRenderer(this);
        receiver.setPos(0.0F, 0.0F, 0.0F);
        launcher.addChild(receiver);


        grip = new ModelRenderer(this);
        grip.setPos(-2.4689F, 8.7321F, 9.7859F);
        receiver.addChild(grip);
        grip.texOffs(0, 86).addBox(0.9689F, -1.5F, 5.2141F, 3.0F, 3.0F, 11.0F, 0.0F, false);
        grip.texOffs(0, 86).addBox(0.9689F, 1.5F, 12.2141F, 3.0F, 1.0F, 4.0F, 0.0F, false);

        cube_r37 = new ModelRenderer(this);
        cube_r37.setPos(0.0F, 0.0F, 0.0F);
        grip.addChild(cube_r37);
        setRotationAngle(cube_r37, 0.0F, 0.5236F, 0.0F);
        cube_r37.texOffs(0, 86).addBox(-3.5F, -1.5001F, 4.0F, 2.0F, 3.0F, 4.0F, 0.0F, true);

        cube_r38 = new ModelRenderer(this);
        cube_r38.setPos(4.9378F, 0.0F, 0.0F);
        grip.addChild(cube_r38);
        setRotationAngle(cube_r38, 0.0F, -0.5236F, 0.0F);
        cube_r38.texOffs(0, 86).addBox(1.5F, -1.5001F, 4.0F, 2.0F, 3.0F, 4.0F, 0.0F, false);

        cube_r39 = new ModelRenderer(this);
        cube_r39.setPos(2.4689F, 2.3754F, 14.2719F);
        grip.addChild(cube_r39);
        setRotationAngle(cube_r39, 0.3054F, 0.0F, 0.0F);
        cube_r39.texOffs(0, 86).addBox(-1.5F, -0.5F, -2.0F, 3.0F, 7.0F, 4.0F, 0.0F, false);

        housing = new ModelRenderer(this);
        housing.setPos(-0.0858F, 12.1107F, 1.0F);
        receiver.addChild(housing);
        housing.texOffs(0, 86).addBox(2.5858F, -4.8787F, 5.0F, 1.0F, 7.0F, 9.0F, 0.0F, false);
        housing.texOffs(0, 86).addBox(-2.4142F, -4.8787F, 13.0F, 5.0F, 7.0F, 1.0F, 0.0F, false);
        housing.texOffs(0, 86).addBox(-3.4142F, -4.8787F, 5.0F, 1.0F, 7.0F, 9.0F, 0.0F, false);
        housing.texOffs(0, 86).addBox(-2.4142F, -4.8787F, 5.0F, 5.0F, 7.0F, 1.0F, 0.0F, false);
        housing.texOffs(0, 86).addBox(-1.4142F, 0.1213F, -9.0F, 3.0F, 2.0F, 14.0F, 0.0F, false);
        housing.texOffs(0, 86).addBox(-3.4142F, -4.8787F, -9.0F, 7.0F, 5.0F, 14.0F, 0.0F, false);

        cube_r40 = new ModelRenderer(this);
        cube_r40.setPos(0.0F, 0.0F, 0.0F);
        housing.addChild(cube_r40);
        setRotationAngle(cube_r40, 0.0F, 0.0F, -0.7854F);
        cube_r40.texOffs(0, 86).addBox(-2.5F, -0.5F, -8.999F, 1.0F, 1.0F, 14.0F, 0.0F, true);
        cube_r40.texOffs(0, 86).addBox(-2.5F, -1.5F, -8.999F, 2.0F, 1.0F, 14.0F, 0.0F, true);

        cube_r41 = new ModelRenderer(this);
        cube_r41.setPos(-1.2929F, -1.2929F, 0.0F);
        housing.addChild(cube_r41);
        setRotationAngle(cube_r41, 0.0F, 0.0F, -0.7854F);
        cube_r41.texOffs(0, 86).addBox(-2.5F, -0.5F, -8.999F, 1.0F, 1.0F, 14.0F, 0.0F, true);

        cube_r42 = new ModelRenderer(this);
        cube_r42.setPos(1.4645F, -1.2929F, 0.0F);
        housing.addChild(cube_r42);
        setRotationAngle(cube_r42, 0.0F, 0.0F, 0.7854F);
        cube_r42.texOffs(0, 86).addBox(1.5F, -0.5F, -8.999F, 1.0F, 1.0F, 14.0F, 0.0F, false);

        cube_r43 = new ModelRenderer(this);
        cube_r43.setPos(0.1716F, 0.0F, 0.0F);
        housing.addChild(cube_r43);
        setRotationAngle(cube_r43, 0.0F, 0.0F, 0.7854F);
        cube_r43.texOffs(0, 86).addBox(0.5F, -1.5F, -8.999F, 2.0F, 1.0F, 14.0F, 0.0F, false);
        cube_r43.texOffs(0, 86).addBox(1.5F, -0.5F, -8.999F, 1.0F, 1.0F, 14.0F, 0.0F, false);

        wiring = new ModelRenderer(this);
        wiring.setPos(0.0F, 0.0F, 0.0F);
        receiver.addChild(wiring);
        wiring.texOffs(0, 86).addBox(-2.5F, 7.2321F, 7.0F, 5.0F, 4.0F, 7.0F, 0.0F, false);
        wiring.texOffs(89, 102).addBox(3.2F, 10.0321F, -1.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        wiring.texOffs(89, 102).addBox(3.2F, 12.0321F, 10.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        wiring.texOffs(89, 102).addBox(3.2F, 8.0321F, 5.0F, 1.0F, 3.0F, 6.0F, 0.0F, false);
        wiring.texOffs(45, 499).addBox(3.201F, 8.5321F, 7.3F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        wiring.texOffs(92, 29).addBox(3.5F, 8.5321F, 5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        wiring.texOffs(116, 173).addBox(3.21F, 9.5321F, 5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r44 = new ModelRenderer(this);
        cube_r44.setPos(0.7F, 11.0321F, 2.1F);
        wiring.addChild(cube_r44);
        setRotationAngle(cube_r44, 0.0F, -0.5236F, 0.48F);
        cube_r44.texOffs(297, 24).addBox(0.7F, -7.1F, -2.2F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        cube_r45 = new ModelRenderer(this);
        cube_r45.setPos(0.7F, 11.0321F, 1.3F);
        wiring.addChild(cube_r45);
        setRotationAngle(cube_r45, 0.0F, -0.5236F, 0.48F);
        cube_r45.texOffs(45, 494).addBox(0.7F, -7.1F, -2.2F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        cube_r46 = new ModelRenderer(this);
        cube_r46.setPos(0.7F, 11.0321F, 0.5F);
        wiring.addChild(cube_r46);
        setRotationAngle(cube_r46, 0.0F, -0.5236F, 0.48F);
        cube_r46.texOffs(15, 460).addBox(0.7F, -7.1F, -2.2F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        cube_r47 = new ModelRenderer(this);
        cube_r47.setPos(3.1446F, 10.6156F, 10.1558F);
        wiring.addChild(cube_r47);
        setRotationAngle(cube_r47, 0.0F, 0.0F, 0.7854F);
        cube_r47.texOffs(24, 460).addBox(-0.5F, -1.0F, -1.4729F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r48 = new ModelRenderer(this);
        cube_r48.setPos(3.1446F, 9.6156F, 10.1558F);
        wiring.addChild(cube_r48);
        setRotationAngle(cube_r48, 0.0F, 0.0F, 0.7854F);
        cube_r48.texOffs(24, 460).addBox(-0.5F, -1.0F, -1.4729F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r49 = new ModelRenderer(this);
        cube_r49.setPos(3.1546F, 10.1156F, 10.7729F);
        wiring.addChild(cube_r49);
        setRotationAngle(cube_r49, 0.0F, 0.0F, 0.7854F);
        cube_r49.texOffs(103, 107).addBox(-0.5F, -1.0F, -3.4729F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        cube_r50 = new ModelRenderer(this);
        cube_r50.setPos(2.791F, 9.2621F, 10.39F);
        wiring.addChild(cube_r50);
        setRotationAngle(cube_r50, 0.0F, -0.7854F, 0.0F);
        cube_r50.texOffs(24, 460).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r51 = new ModelRenderer(this);
        cube_r51.setPos(2.791F, 9.2621F, 9.39F);
        wiring.addChild(cube_r51);
        setRotationAngle(cube_r51, 0.0F, -0.7854F, 0.0F);
        cube_r51.texOffs(24, 460).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r52 = new ModelRenderer(this);
        cube_r52.setPos(2.801F, 9.5321F, 9.89F);
        wiring.addChild(cube_r52);
        setRotationAngle(cube_r52, 0.0F, -0.7854F, 0.0F);
        cube_r52.texOffs(103, 107).addBox(-0.5F, -1.0F, -1.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        cube_r53 = new ModelRenderer(this);
        cube_r53.setPos(2.31F, 10.2221F, 12.15F);
        wiring.addChild(cube_r53);
        setRotationAngle(cube_r53, 0.6545F, -0.3927F, -0.2182F);
        cube_r53.texOffs(23, 171).addBox(-0.5F, -1.5F, -3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        cube_r54 = new ModelRenderer(this);
        cube_r54.setPos(0.5F, 9.0021F, 3.5F);
        wiring.addChild(cube_r54);
        setRotationAngle(cube_r54, 0.3927F, -0.1745F, 0.4363F);
        cube_r54.texOffs(23, 166).addBox(2.5F, -1.0F, -2.5F, 1.0F, 1.0F, 5.0F, 0.0F, false);

        cube_r55 = new ModelRenderer(this);
        cube_r55.setPos(0.5F, 9.9021F, 3.5F);
        wiring.addChild(cube_r55);
        setRotationAngle(cube_r55, 0.1745F, -0.1309F, 0.4363F);
        cube_r55.texOffs(23, 171).addBox(2.5F, -1.0F, -2.5F, 1.0F, 1.0F, 5.0F, 0.0F, false);

        laser = new ModelRenderer(this);
        laser.setPos(0.0F, 0.0F, 0.0F);
        launcher.addChild(laser);
        laser.texOffs(90, 113).addBox(-0.5F, -8.2321F, -1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        laser.texOffs(92, 29).addBox(1.0F, -8.7321F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        laser.texOffs(116, 173).addBox(0.0F, -8.2521F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        laser.texOffs(90, 113).addBox(-3.0F, -8.7321F, 1.0F, 6.0F, 2.0F, 8.0F, 0.0F, false);
        laser.texOffs(90, 113).addBox(-2.5F, -8.2321F, 9.0F, 4.0F, 1.0F, 13.0F, 0.0F, false);
        laser.texOffs(90, 113).addBox(-3.5F, -9.2321F, -5.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        laser.texOffs(0, 462).addBox(-3.0F, -8.7321F, -5.002F, 1.0F, 1.0F, 0.0F, 0.0F, false);

        cube_r56 = new ModelRenderer(this);
        cube_r56.setPos(0.3F, -7.2321F, -0.2F);
        laser.addChild(cube_r56);
        setRotationAngle(cube_r56, -0.6545F, 0.7854F, -0.3491F);
        cube_r56.texOffs(32, 168).addBox(-1.5F, -0.5F, -3.5F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        opticmount = new ModelRenderer(this);
        opticmount.setPos(0.0F, 0.0F, 0.0F);
        launcher.addChild(opticmount);


        rail = new ModelRenderer(this);
        rail.setPos(7.5F, -1.0F, -10.0F);
        opticmount.addChild(rail);
        rail.texOffs(73, 82).addBox(2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 19.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(2.5F, -2.0F, 17.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(2.5F, -2.0F, 15.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(2.5F, -2.0F, 13.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(2.5F, -2.0F, 11.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(2.5F, -2.0F, 9.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(2.5F, -2.0F, 7.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(2.5F, -2.0F, 5.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(2.5F, -2.0F, 3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(2.5F, -2.0F, 1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(2.5F, -2.0F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(3.366F, -1.499F, 16.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(3.366F, -1.499F, 14.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(3.366F, -1.499F, 12.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(3.366F, -1.499F, 10.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(3.366F, -1.499F, 8.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(3.366F, -1.499F, 6.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(3.366F, -1.499F, 4.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(3.366F, -1.499F, 2.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(3.366F, -1.499F, 0.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(1.6331F, -1.4995F, 16.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(1.6331F, -1.4995F, 14.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(1.6331F, -1.4995F, 12.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(1.6331F, -1.4995F, 10.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(1.6331F, -1.4995F, 8.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(1.6331F, -1.4995F, 6.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(1.6331F, -1.4995F, 4.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(1.6331F, -1.4995F, 2.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(73, 82).addBox(1.6331F, -1.4995F, 0.0F, 2.0F, 0.0F, 1.0F, 0.0F, false);

        cube_r57 = new ModelRenderer(this);
        cube_r57.setPos(1.884F, -1.067F, 0.5F);
        rail.addChild(cube_r57);
        setRotationAngle(cube_r57, 0.0F, 0.0F, -0.5236F);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 1.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 3.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 5.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 7.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 9.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 11.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 13.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 15.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 6.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 8.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 10.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 12.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 14.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        cube_r57.texOffs(73, 82).addBox(0.0F, -0.5F, 16.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        cube_r58 = new ModelRenderer(this);
        cube_r58.setPos(5.116F, -1.067F, 0.5F);
        rail.addChild(cube_r58);
        setRotationAngle(cube_r58, 0.0F, 0.0F, 0.5236F);
        cube_r58.texOffs(73, 82).addBox(0.0F, -0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(0.0F, -0.5F, 1.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(0.0F, -0.5F, 3.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(0.0F, -0.5F, 5.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(0.0F, -0.5F, 7.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(0.0F, -0.5F, 9.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(0.0F, -0.5F, 11.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(0.0F, -0.5F, 13.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(0.0F, -0.5F, 15.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(-1.0F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(-1.0F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(-1.0F, -0.5F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(-1.0F, -0.5F, 4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(-1.0F, -0.5F, 6.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(-1.0F, -0.5F, 8.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(-1.0F, -0.5F, 10.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(-1.0F, -0.5F, 12.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(-1.0F, -0.5F, 14.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r58.texOffs(73, 82).addBox(-1.0F, -0.5F, 16.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        frame = new ModelRenderer(this);
        frame.setPos(10.1519F, 1.067F, 6.5F);
        opticmount.addChild(frame);
        frame.texOffs(34, 113).addBox(-3.1519F, -2.067F, -16.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        frame.texOffs(34, 113).addBox(-3.1519F, -2.067F, -0.5F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        frame.texOffs(34, 113).addBox(-3.616F, -0.067F, -16.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        frame.texOffs(34, 113).addBox(-3.616F, -0.067F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        frame.texOffs(18, 157).addBox(-2.046F, -1.067F, -16.0F, 1.0F, 1.0F, 16.0F, 0.0F, false);

        cube_r59 = new ModelRenderer(this);
        cube_r59.setPos(0.0F, 0.0F, 0.0F);
        frame.addChild(cube_r59);
        setRotationAngle(cube_r59, 0.0F, 0.0F, -0.5236F);
        cube_r59.texOffs(34, 113).addBox(-1.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        cube_r59.texOffs(34, 113).addBox(-1.0F, -0.5F, -16.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r60 = new ModelRenderer(this);
        cube_r60.setPos(-1.546F, -0.567F, 0.0F);
        frame.addChild(cube_r60);
        setRotationAngle(cube_r60, 0.0F, -0.7854F, 0.0F);
        cube_r60.texOffs(34, 113).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        cube_r61 = new ModelRenderer(this);
        cube_r61.setPos(-1.546F, -0.567F, -16.0F);
        frame.addChild(cube_r61);
        setRotationAngle(cube_r61, 0.0F, -0.7854F, 0.0F);
        cube_r61.texOffs(34, 113).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        cube_r62 = new ModelRenderer(this);
        cube_r62.setPos(0.8481F, -2.067F, -15.0F);
        frame.addChild(cube_r62);
        setRotationAngle(cube_r62, -0.7854F, 0.0F, 0.0F);
        cube_r62.texOffs(23, 155).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r63 = new ModelRenderer(this);
        cube_r63.setPos(0.8481F, -2.067F, 1.0F);
        frame.addChild(cube_r63);
        setRotationAngle(cube_r63, -0.7854F, 0.0F, 0.0F);
        cube_r63.texOffs(23, 155).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r64 = new ModelRenderer(this);
        cube_r64.setPos(0.8481F, -2.067F, -15.6F);
        frame.addChild(cube_r64);
        setRotationAngle(cube_r64, -0.7854F, 0.0F, 0.0F);
        cube_r64.texOffs(23, 155).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r65 = new ModelRenderer(this);
        cube_r65.setPos(0.8481F, -2.067F, 0.4F);
        frame.addChild(cube_r65);
        setRotationAngle(cube_r65, -0.7854F, 0.0F, 0.0F);
        cube_r65.texOffs(23, 155).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        loading_cover = new ModelRenderer(this);
        loading_cover.setPos(-7.2321F, 26.5F, 51.0F);
        loading_cover.texOffs(29, 103).addBox(12.4642F, -7.0F, -1.0F, 2.0F, 9.0F, 3.0F, 0.0F, true);
        loading_cover.texOffs(29, 103).addBox(2.7321F, -9.7321F, -1.0F, 9.0F, 2.0F, 3.0F, 0.0F, false);
        loading_cover.texOffs(29, 103).addBox(0.0F, -7.0F, -1.0F, 2.0F, 9.0F, 3.0F, 0.0F, false);
        loading_cover.texOffs(29, 103).addBox(1.7321F, -8.0F, 0.5F, 11.0F, 11.0F, 1.0F, 0.0F, false);
        loading_cover.texOffs(29, 103).addBox(2.7321F, 2.7321F, -1.0F, 9.0F, 2.0F, 3.0F, 0.0F, false);

        cube_r66 = new ModelRenderer(this);
        cube_r66.setPos(12.0311F, 5.2141F, -11.5F);
        loading_cover.addChild(cube_r66);
        setRotationAngle(cube_r66, 0.0F, 0.0F, 0.5236F);
        cube_r66.texOffs(29, 103).addBox(-1.5F, -4.0F, 10.5F, 2.0F, 2.0F, 3.0F, 0.0F, true);

        cube_r67 = new ModelRenderer(this);
        cube_r67.setPos(14.0311F, 5.7141F, -11.5F);
        loading_cover.addChild(cube_r67);
        setRotationAngle(cube_r67, 0.0F, 0.0F, -0.5236F);
        cube_r67.texOffs(29, 103).addBox(-1.5F, -4.0F, 10.5F, 2.0F, 2.0F, 3.0F, 0.0F, false);

        cube_r68 = new ModelRenderer(this);
        cube_r68.setPos(0.4331F, 5.7141F, -11.5F);
        loading_cover.addChild(cube_r68);
        setRotationAngle(cube_r68, 0.0F, 0.0F, 0.5236F);
        cube_r68.texOffs(29, 103).addBox(-0.5F, -4.0F, 10.5F, 2.0F, 2.0F, 3.0F, 0.0F, true);

        cube_r69 = new ModelRenderer(this);
        cube_r69.setPos(2.4331F, 5.2141F, -11.5F);
        loading_cover.addChild(cube_r69);
        setRotationAngle(cube_r69, 0.0F, 0.0F, -0.5236F);
        cube_r69.texOffs(29, 103).addBox(-0.5F, -4.0F, 10.5F, 2.0F, 2.0F, 3.0F, 0.0F, false);

        cube_r70 = new ModelRenderer(this);
        cube_r70.setPos(0.0F, 0.0F, 0.0F);
        loading_cover.addChild(cube_r70);
        setRotationAngle(cube_r70, 0.0F, 0.0F, -0.7854F);
        cube_r70.texOffs(19, 168).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        cube_r71 = new ModelRenderer(this);
        cube_r71.setPos(2.4331F, -10.2141F, -11.5F);
        loading_cover.addChild(cube_r71);
        setRotationAngle(cube_r71, 0.0F, 0.0F, 0.5236F);
        cube_r71.texOffs(29, 103).addBox(-0.5F, 2.0F, 10.5F, 2.0F, 2.0F, 3.0F, 0.0F, false);

        cube_r72 = new ModelRenderer(this);
        cube_r72.setPos(0.4331F, -10.7141F, -11.5F);
        loading_cover.addChild(cube_r72);
        setRotationAngle(cube_r72, 0.0F, 0.0F, -0.5236F);
        cube_r72.texOffs(29, 103).addBox(-0.5F, 2.0F, 10.5F, 2.0F, 2.0F, 3.0F, 0.0F, true);

        cube_r73 = new ModelRenderer(this);
        cube_r73.setPos(14.0311F, -10.7141F, -11.5F);
        loading_cover.addChild(cube_r73);
        setRotationAngle(cube_r73, 0.0F, 0.0F, 0.5236F);
        cube_r73.texOffs(29, 103).addBox(-1.5F, 2.0F, 10.5F, 2.0F, 2.0F, 3.0F, 0.0F, false);

        cube_r74 = new ModelRenderer(this);
        cube_r74.setPos(12.0311F, -10.2141F, -11.5F);
        loading_cover.addChild(cube_r74);
        setRotationAngle(cube_r74, 0.0F, 0.0F, -0.5236F);
        cube_r74.texOffs(29, 103).addBox(-1.5F, 2.0F, 10.5F, 2.0F, 2.0F, 3.0F, 0.0F, true);

        ironsights = new ModelRenderer(this);
        ironsights.setPos(0.0F, 24.0F, 0.0F);


        rearsight = new ModelRenderer(this);
        rearsight.setPos(18.0F, 0.0F, 1.0F);
        ironsights.addChild(rearsight);
        rearsight.texOffs(0, 0).addBox(-8.7412F, -3.4659F, 4.5F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        rearsight.texOffs(55, 178).addBox(-8.5F, -8.4659F, 5.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        rearsight.texOffs(55, 178).addBox(-8.5F, -5.0517F, 5.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        rearsight.texOffs(55, 178).addBox(-5.7929F, -7.7588F, 5.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        rearsight.texOffs(55, 178).addBox(-9.2071F, -7.7588F, 5.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        rearsight.texOffs(0, 0).addBox(-7.2588F, -3.4659F, 4.5F, 2.0F, 1.0F, 2.0F, 0.0F, true);

        cube_r75 = new ModelRenderer(this);
        cube_r75.setPos(-7.7929F, -4.0517F, 5.5F);
        rearsight.addChild(cube_r75);
        setRotationAngle(cube_r75, 0.0F, 0.0F, 0.7854F);
        cube_r75.texOffs(55, 178).addBox(-1.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r76 = new ModelRenderer(this);
        cube_r76.setPos(-7.7929F, -7.0517F, 5.5F);
        rearsight.addChild(cube_r76);
        setRotationAngle(cube_r76, 0.0F, 0.0F, 0.7854F);
        cube_r76.texOffs(55, 178).addBox(-1.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r77 = new ModelRenderer(this);
        cube_r77.setPos(-4.7929F, -4.0517F, 5.5F);
        rearsight.addChild(cube_r77);
        setRotationAngle(cube_r77, 0.0F, 0.0F, 0.7854F);
        cube_r77.texOffs(55, 178).addBox(-1.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r78 = new ModelRenderer(this);
        cube_r78.setPos(-4.7929F, -7.0517F, 5.5F);
        rearsight.addChild(cube_r78);
        setRotationAngle(cube_r78, 0.0F, 0.0F, 0.7854F);
        cube_r78.texOffs(55, 178).addBox(-1.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r79 = new ModelRenderer(this);
        cube_r79.setPos(-7.5F, -3.5517F, 5.5F);
        rearsight.addChild(cube_r79);
        setRotationAngle(cube_r79, 0.0F, -0.7854F, 0.0F);
        cube_r79.texOffs(34, 115).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r80 = new ModelRenderer(this);
        cube_r80.setPos(-6.5F, -3.5517F, 5.5F);
        rearsight.addChild(cube_r80);
        setRotationAngle(cube_r80, 0.0F, -0.7854F, 0.0F);
        cube_r80.texOffs(32, 113).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r81 = new ModelRenderer(this);
        cube_r81.setPos(-7.7753F, -3.2071F, 4.5F);
        rearsight.addChild(cube_r81);
        setRotationAngle(cube_r81, 0.0F, 0.0F, 0.2618F);
        cube_r81.texOffs(0, 0).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r82 = new ModelRenderer(this);
        cube_r82.setPos(-7.7753F, -1.7929F, 4.5F);
        rearsight.addChild(cube_r82);
        setRotationAngle(cube_r82, 0.0F, 0.0F, -0.2618F);
        cube_r82.texOffs(0, 0).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r83 = new ModelRenderer(this);
        cube_r83.setPos(-5.6124F, -2.8536F, 5.5F);
        rearsight.addChild(cube_r83);
        setRotationAngle(cube_r83, 0.0F, 0.0F, -0.2618F);
        cube_r83.texOffs(0, 0).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r84 = new ModelRenderer(this);
        cube_r84.setPos(-5.6124F, -2.1464F, 5.5F);
        rearsight.addChild(cube_r84);
        setRotationAngle(cube_r84, 0.0F, 0.0F, 0.2618F);
        cube_r84.texOffs(0, 0).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        frontsight = new ModelRenderer(this);
        frontsight.setPos(18.0F, 0.0F, -15.0F);
        ironsights.addChild(frontsight);
        frontsight.texOffs(0, 0).addBox(-8.7412F, -3.4659F, 4.5F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        frontsight.texOffs(55, 178).addBox(-8.5F, -8.4659F, 5.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        frontsight.texOffs(55, 178).addBox(-8.5F, -5.0517F, 5.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        frontsight.texOffs(55, 178).addBox(-5.7929F, -7.7588F, 5.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        frontsight.texOffs(55, 178).addBox(-9.2071F, -7.7588F, 5.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        frontsight.texOffs(0, 0).addBox(-7.2588F, -3.4659F, 4.5F, 2.0F, 1.0F, 2.0F, 0.0F, true);

        cube_r85 = new ModelRenderer(this);
        cube_r85.setPos(-7.7929F, -7.0517F, 5.5F);
        frontsight.addChild(cube_r85);
        setRotationAngle(cube_r85, 0.0F, 0.0F, 0.7854F);
        cube_r85.texOffs(55, 178).addBox(-1.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r86 = new ModelRenderer(this);
        cube_r86.setPos(-7.7929F, -4.0517F, 5.5F);
        frontsight.addChild(cube_r86);
        setRotationAngle(cube_r86, 0.0F, 0.0F, 0.7854F);
        cube_r86.texOffs(55, 178).addBox(-1.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r87 = new ModelRenderer(this);
        cube_r87.setPos(-4.7929F, -7.0517F, 5.5F);
        frontsight.addChild(cube_r87);
        setRotationAngle(cube_r87, 0.0F, 0.0F, 0.7854F);
        cube_r87.texOffs(55, 178).addBox(-1.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r88 = new ModelRenderer(this);
        cube_r88.setPos(-4.7929F, -4.0517F, 5.5F);
        frontsight.addChild(cube_r88);
        setRotationAngle(cube_r88, 0.0F, 0.0F, 0.7854F);
        cube_r88.texOffs(55, 178).addBox(-1.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r89 = new ModelRenderer(this);
        cube_r89.setPos(-7.5F, -3.5517F, 5.5F);
        frontsight.addChild(cube_r89);
        setRotationAngle(cube_r89, 0.0F, -0.7854F, 0.0F);
        cube_r89.texOffs(34, 115).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r90 = new ModelRenderer(this);
        cube_r90.setPos(-6.5F, -3.5517F, 5.5F);
        frontsight.addChild(cube_r90);
        setRotationAngle(cube_r90, 0.0F, -0.7854F, 0.0F);
        cube_r90.texOffs(32, 113).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r91 = new ModelRenderer(this);
        cube_r91.setPos(-7.7753F, -3.2071F, 4.5F);
        frontsight.addChild(cube_r91);
        setRotationAngle(cube_r91, 0.0F, 0.0F, 0.2618F);
        cube_r91.texOffs(0, 0).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r92 = new ModelRenderer(this);
        cube_r92.setPos(-7.7753F, -1.7929F, 4.5F);
        frontsight.addChild(cube_r92);
        setRotationAngle(cube_r92, 0.0F, 0.0F, -0.2618F);
        cube_r92.texOffs(0, 0).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r93 = new ModelRenderer(this);
        cube_r93.setPos(-5.6124F, -2.8536F, 5.5F);
        frontsight.addChild(cube_r93);
        setRotationAngle(cube_r93, 0.0F, 0.0F, -0.2618F);
        cube_r93.texOffs(0, 0).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r94 = new ModelRenderer(this);
        cube_r94.setPos(-5.6124F, -2.1464F, 5.5F);
        frontsight.addChild(cube_r94);
        setRotationAngle(cube_r94, 0.0F, 0.0F, 0.2618F);
        cube_r94.texOffs(0, 0).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
    }
}
