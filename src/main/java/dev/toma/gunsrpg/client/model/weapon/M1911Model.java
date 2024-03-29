package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class M1911Model extends AbstractWeaponModel {

    private final ModelRenderer slide;
    private final ModelRenderer bone21;
    private final ModelRenderer bone22;
    private final ModelRenderer bone8;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone20;
    private final ModelRenderer m1911;
    private final ModelRenderer bone9;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone18;
    private final ModelRenderer bone19;
    private final ModelRenderer bone15;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer magazine;
    private final ModelRenderer hammer;
    private final ModelRenderer bone7;
    private final ModelRenderer bone6;
    private final ModelRenderer bullet;

    public M1911Model() {
        texWidth = 512;
        texHeight = 512;

        slide = new ModelRenderer(this);
        slide.setPos(0.634F, 24.0F, 0.0F);
        slide.texOffs(34, 40).addBox(-1.634F, -5.9F, -10.3516F, 2.0F, 1.0F, 11.0F, 0.0F, false);
        slide.texOffs(3, 76).addBox(-1.134F, -6.7867F, -9.7227F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        slide.texOffs(34, 38).addBox(-3.0F, -4.534F, -10.3516F, 1.0F, 4.0F, 11.0F, 0.0F, false);
        slide.texOffs(42, 43).addBox(0.7321F, -4.534F, -10.3516F, 1.0F, 4.0F, 11.0F, 0.0F, true);
        slide.texOffs(36, 40).addBox(-0.8411F, -0.4608F, -10.3516F, 1.0F, 1.0F, 10.0F, 0.0F, false);
        slide.texOffs(36, 40).addBox(-2.134F, -2.1679F, -10.3516F, 3.0F, 2.0F, 6.0F, 0.0F, false);
        slide.texOffs(36, 40).addBox(-1.4269F, -0.4608F, -10.3516F, 1.0F, 1.0F, 10.0F, 0.0F, true);
        slide.texOffs(35, 40).addBox(-1.634F, -5.9F, 0.6484F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        slide.texOffs(42, 45).addBox(-0.634F, -5.9F, 4.6484F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        slide.texOffs(44, 48).addBox(-1.634F, -5.9F, 9.6484F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        slide.texOffs(44, 48).addBox(-3.0F, -4.534F, 9.6484F, 1.0F, 4.0F, 9.0F, 0.0F, false);
        slide.texOffs(35, 40).addBox(-3.0F, -3.534F, 4.6484F, 1.0F, 3.0F, 5.0F, 0.0F, false);
        slide.texOffs(35, 40).addBox(-3.0F, -4.534F, 0.6484F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        slide.texOffs(43, 42).addBox(0.7321F, -4.534F, 13.6484F, 1.0F, 4.0F, 5.0F, 0.0F, true);
        slide.texOffs(42, 43).addBox(0.7321F, -4.534F, 8.6484F, 1.0F, 4.0F, 5.0F, 0.0F, true);
        slide.texOffs(42, 43).addBox(0.7321F, -4.534F, 0.6484F, 1.0F, 4.0F, 8.0F, 0.0F, true);
        slide.texOffs(42, 43).addBox(-0.4191F, -3.1956F, 18.748F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        slide.texOffs(50, 50).addBox(-2.134F, -3.534F, 18.1609F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        slide.texOffs(42, 43).addBox(-1.4191F, -1.534F, 18.748F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        slide.texOffs(42, 43).addBox(-1.8488F, -3.1956F, 18.748F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        slide.texOffs(42, 43).addBox(-1.8488F, -1.534F, 18.748F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        slide.texOffs(42, 43).addBox(-2.634F, -3.534F, 17.9297F, 4.0F, 3.0F, 1.0F, 0.0F, false);
        slide.texOffs(42, 43).addBox(-1.134F, -5.0082F, 17.9028F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        slide.texOffs(3, 76).addBox(-2.134F, -6.918F, 16.6484F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        slide.texOffs(3, 76).addBox(-2.134F, -6.318F, 16.6484F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        slide.texOffs(3, 76).addBox(-0.134F, -6.918F, 16.6484F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone21 = new ModelRenderer(this);
        bone21.setPos(-0.634F, 0.0F, 0.0F);
        slide.addChild(bone21);
        setRotationAngle(bone21, 0.0F, 0.0F, -0.5236F);
        bone21.texOffs(38, 43).addBox(1.084F, -5.6095F, -10.3516F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone21.texOffs(35, 40).addBox(1.084F, -5.6095F, 0.6484F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone21.texOffs(44, 48).addBox(1.084F, -5.6095F, 9.6484F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        bone21.texOffs(36, 41).addBox(3.316F, -3.7435F, -10.3516F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone21.texOffs(42, 43).addBox(3.316F, -3.7435F, 0.6484F, 1.0F, 1.0F, 8.0F, 0.0F, false);
        bone21.texOffs(42, 43).addBox(3.316F, -3.7435F, 8.6484F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        bone21.texOffs(43, 42).addBox(3.316F, -3.7435F, 13.6484F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        bone21.texOffs(36, 40).addBox(1.45F, -1.5115F, -10.3516F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        bone21.texOffs(36, 40).addBox(-0.7821F, -3.3775F, -10.3516F, 1.0F, 2.0F, 6.0F, 0.0F, false);

        bone22 = new ModelRenderer(this);
        bone22.setPos(-0.634F, 0.0F, 0.0F);
        slide.addChild(bone22);
        setRotationAngle(bone22, 0.0F, 0.0F, 0.5236F);
        bone22.texOffs(38, 43).addBox(-2.084F, -5.6095F, -10.3516F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone22.texOffs(35, 40).addBox(-2.084F, -5.6095F, 0.6484F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone22.texOffs(44, 48).addBox(-2.084F, -5.6095F, 11.6484F, 1.0F, 1.0F, 7.0F, 0.0F, true);
        bone22.texOffs(13, 74).addBox(-2.1361F, -5.3124F, 4.6484F, 1.0F, 1.0F, 7.0F, 0.0F, true);
        bone22.texOffs(32, 41).addBox(-4.316F, -3.7435F, -10.3516F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone22.texOffs(43, 42).addBox(-4.316F, -3.7435F, 9.6484F, 1.0F, 1.0F, 9.0F, 0.0F, true);
        bone22.texOffs(36, 40).addBox(-2.45F, -1.5115F, -10.3516F, 1.0F, 1.0F, 6.0F, 0.0F, true);
        bone22.texOffs(36, 40).addBox(-0.2179F, -3.3775F, -10.3516F, 1.0F, 2.0F, 6.0F, 0.0F, true);

        bone8 = new ModelRenderer(this);
        bone8.setPos(-2.2004F, 0.0F, 0.0F);
        slide.addChild(bone8);
        setRotationAngle(bone8, 0.0F, 0.0F, -0.7854F);
        bone8.texOffs(36, 40).addBox(0.1657F, -0.0718F, -10.3516F, 1.0F, 1.0F, 10.0F, 0.0F, false);
        bone8.texOffs(36, 40).addBox(1.287F, 1.0495F, -10.3516F, 1.0F, 1.0F, 10.0F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(-0.634F, -3.034F, 18.8711F);
        slide.addChild(bone4);
        setRotationAngle(bone4, 0.2618F, 0.0F, 0.0F);
        bone4.texOffs(42, 43).addBox(-1.5F, -2.3725F, -0.5898F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        bone4.texOffs(42, 43).addBox(-2.0F, -1.4678F, -0.814F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        bone4.texOffs(42, 42).addBox(-2.0F, -1.9678F, -0.814F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(-0.634F, -2.746F, 18.8722F);
        slide.addChild(bone5);
        setRotationAngle(bone5, 0.4363F, 0.0F, 0.0F);
        bone5.texOffs(42, 43).addBox(0.2148F, -2.0373F, -0.0163F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        bone5.texOffs(42, 43).addBox(-1.2148F, -2.0373F, -0.0163F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        bone20 = new ModelRenderer(this);
        bone20.setPos(-0.634F, -2.2268F, 19.1777F);
        slide.addChild(bone20);
        setRotationAngle(bone20, -0.3491F, 0.0F, 0.0F);
        bone20.texOffs(41, 42).addBox(0.2148F, -2.5196F, -2.2096F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone20.texOffs(41, 42).addBox(-1.2148F, -2.5196F, -2.2096F, 2.0F, 1.0F, 1.0F, 0.0F, true);

        m1911 = new ModelRenderer(this);
        m1911.setPos(0.0F, 24.0F, 0.0F);
        m1911.texOffs(69, 13).addBox(0.6523F, -1.0742F, -5.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        m1911.texOffs(69, 13).addBox(-1.6523F, -1.0742F, -5.0F, 1.0F, 1.0F, 4.0F, 0.0F, true);
        m1911.texOffs(69, 13).addBox(-0.9452F, -0.3671F, -5.0F, 1.0F, 1.0F, 4.0F, 0.0F, true);
        m1911.texOffs(69, 13).addBox(-0.0548F, -0.3671F, -5.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        m1911.texOffs(69, 13).addBox(-2.0F, -0.975F, -1.0F, 4.0F, 2.0F, 3.0F, 0.0F, false);
        m1911.texOffs(69, 13).addBox(-1.0F, 4.6231F, 2.7321F, 2.0F, 1.0F, 7.0F, 0.0F, false);
        m1911.texOffs(69, 13).addBox(-1.5F, 1.025F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        m1911.texOffs(69, 13).addBox(-1.5F, 1.5602F, -0.0508F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        m1911.texOffs(85, 25).addBox(-2.0F, -0.9219F, 2.0F, 4.0F, 1.0F, 16.0F, 0.0F, false);
        m1911.texOffs(91, 22).addBox(-2.0F, 0.0781F, 8.625F, 4.0F, 1.0F, 7.0F, 0.0F, false);
        m1911.texOffs(91, 22).addBox(-2.0F, 1.0781F, 8.625F, 4.0F, 1.0F, 7.0F, 0.0F, false);
        m1911.texOffs(22, 158).addBox(-1.866F, -3.934F, -10.8516F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        m1911.texOffs(22, 158).addBox(0.866F, -3.934F, -10.8516F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        m1911.texOffs(22, 158).addBox(-0.5F, -5.3F, -10.8516F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        m1911.texOffs(22, 158).addBox(-0.5F, -2.5679F, -10.8516F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        m1911.texOffs(12, 78).addBox(-1.0F, -4.5F, -3.4492F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        m1911.texOffs(13, 74).addBox(-0.8062F, -5.6688F, 4.6484F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        m1911.texOffs(13, 74).addBox(-2.1722F, -4.3027F, 4.6484F, 1.0F, 1.0F, 5.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(1.1523F, 2.3828F, -5.5F);
        m1911.addChild(bone9);
        setRotationAngle(bone9, 0.0F, 0.0F, -0.7854F);
        bone9.texOffs(69, 13).addBox(1.0909F, -2.3838F, 0.5F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone9.texOffs(69, 13).addBox(-0.2458F, -3.7206F, 0.5F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        bone12 = new ModelRenderer(this);
        bone12.setPos(0.5F, 0.0F, 0.0F);
        m1911.addChild(bone12);
        setRotationAngle(bone12, 0.5236F, 0.0F, 0.0F);
        bone12.texOffs(69, 13).addBox(-2.0F, 1.5037F, -1.4455F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        bone12.texOffs(69, 13).addBox(-1.5F, 2.5037F, -1.4455F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone12.texOffs(69, 13).addBox(-1.5F, 8.2358F, 4.7506F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone12.texOffs(69, 13).addBox(-2.0F, 0.8327F, -0.7875F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(0.5F, 0.0F, 0.0F);
        m1911.addChild(bone13);
        setRotationAngle(bone13, -0.5236F, 0.0F, 0.0F);
        bone13.texOffs(69, 13).addBox(-1.5F, 2.5037F, 3.1776F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        bone13.texOffs(69, 13).addBox(-2.0F, 0.406F, 0.8343F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        bone14 = new ModelRenderer(this);
        bone14.setPos(0.0F, 2.7781F, 12.125F);
        m1911.addChild(bone14);
        setRotationAngle(bone14, 0.2094F, 0.0F, 0.0F);
        bone14.texOffs(91, 22).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 2.0F, 6.0F, 0.0F, false);
        bone14.texOffs(91, 22).addBox(-2.0F, 0.1F, -3.5F, 4.0F, 7.0F, 7.0F, 0.0F, false);

        bone10 = new ModelRenderer(this);
        bone10.setPos(-1.0F, 8.0F, 0.0F);
        m1911.addChild(bone10);
        setRotationAngle(bone10, 0.6109F, 0.0F, 0.0F);
        bone10.texOffs(91, 22).addBox(-1.0F, 2.2921F, 16.7695F, 4.0F, 1.0F, 2.0F, 0.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, 10.7921F, 6.7695F);
        m1911.addChild(bone11);
        setRotationAngle(bone11, 0.7854F, 0.0F, 0.0F);
        bone11.texOffs(91, 22).addBox(-2.0F, -11.2789F, 3.5338F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        bone11.texOffs(91, 22).addBox(-2.0F, -7.5567F, 8.1808F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        bone18 = new ModelRenderer(this);
        bone18.setPos(0.0F, 0.0F, 0.0F);
        m1911.addChild(bone18);
        setRotationAngle(bone18, 0.0F, 0.0F, -0.5236F);
        bone18.texOffs(22, 158).addBox(1.217F, -4.8399F, -10.8516F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone18.texOffs(22, 158).addBox(2.583F, -3.4739F, -10.8516F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone18.texOffs(22, 158).addBox(1.217F, -2.1079F, -10.8516F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone18.texOffs(22, 158).addBox(-0.149F, -3.4739F, -10.8516F, 1.0F, 1.0F, 11.0F, 0.0F, false);

        bone19 = new ModelRenderer(this);
        bone19.setPos(0.0F, 0.0F, 0.0F);
        m1911.addChild(bone19);
        setRotationAngle(bone19, 0.0F, 0.0F, 0.5236F);
        bone19.texOffs(22, 158).addBox(-2.217F, -4.8399F, -10.8516F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone19.texOffs(22, 158).addBox(-3.583F, -3.4739F, -10.8516F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone19.texOffs(22, 158).addBox(-2.217F, -2.1079F, -10.8516F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone19.texOffs(22, 158).addBox(-0.851F, -3.4739F, -10.8516F, 1.0F, 1.0F, 11.0F, 0.0F, true);

        bone15 = new ModelRenderer(this);
        bone15.setPos(-1.5F, 0.0F, 0.0F);
        m1911.addChild(bone15);
        setRotationAngle(bone15, 0.1047F, 0.0F, 0.0F);
        bone15.texOffs(91, 22).addBox(-0.5F, 2.9781F, 8.325F, 4.0F, 2.0F, 7.0F, 0.0F, false);
        bone15.texOffs(91, 22).addBox(-0.5F, 10.7256F, 8.9912F, 4.0F, 2.0F, 7.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(0.0F, 0.0F, 0.0F);
        m1911.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.0F, -0.5236F);
        bone2.texOffs(13, 74).addBox(1.1361F, -5.3124F, 4.6484F, 1.0F, 1.0F, 5.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, 0.0F, 0.0F);
        m1911.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, 0.5236F);
        bone3.texOffs(9, 77).addBox(-4.0326F, -3.6401F, 4.6484F, 1.0F, 1.0F, 5.0F, 0.0F, true);

        magazine = new ModelRenderer(this);
        magazine.setPos(-1.5F, 45.0F, 0.0F);
        setRotationAngle(magazine, 0.1047F, 0.0F, 0.0F);
        magazine.texOffs(0, 69).addBox(0.0F, -9.1594F, 10.1863F, 3.0F, 2.0F, 7.0F, 0.0F, false);
        magazine.texOffs(0, 69).addBox(0.0F, -20.1594F, 12.1863F, 3.0F, 11.0F, 5.0F, 0.0F, false);
        magazine.texOffs(0, 69).addBox(1.0F, -20.8594F, 16.0863F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        magazine.texOffs(0, 69).addBox(1.0F, -20.9594F, 12.4863F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        magazine.texOffs(0, 69).addBox(2.0F, -21.1594F, 12.1863F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        magazine.texOffs(0, 69).addBox(0.0F, -21.1594F, 12.1863F, 1.0F, 1.0F, 5.0F, 0.0F, false);

        hammer = new ModelRenderer(this);
        hammer.setPos(0.0F, 21.4852F, 18.6657F);


        bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, 0.288F, 0.512F);
        hammer.addChild(bone7);
        setRotationAngle(bone7, -0.1745F, 0.0F, 0.0F);
        bone7.texOffs(93, 102).addBox(-0.5F, -2.3603F, -0.3723F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, 0.288F, 0.512F);
        hammer.addChild(bone6);
        setRotationAngle(bone6, -0.3491F, 0.0F, 0.0F);
        bone6.texOffs(93, 102).addBox(-0.5F, -2.2598F, -0.7766F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bullet = new ModelRenderer(this);
        bullet.setPos(-1.5F, 45.0F, 0.0F);
        setRotationAngle(bullet, 0.1047F, 0.0F, 0.0F);
        bullet.texOffs(0, 493).addBox(1.0F, -21.4594F, 13.1863F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        setSpecialRenderer(ModAnimations.MAGAZINE, magazine);
        setSpecialRenderer(ModAnimations.SLIDE, slide);
        setBulletRenderer(setSpecialRenderer(ModAnimations.BULLET, bullet));
    }

    @Override
    public void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        m1911.render(matrix, builder, light, overlay);
        hammer.render(matrix, builder, light, overlay);
    }
}
