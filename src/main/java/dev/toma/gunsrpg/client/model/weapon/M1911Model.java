package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class M1911Model extends AbstractWeaponModel {

    private final ModelRenderer gunRenderer;
    private final ModelRenderer bone9;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone8;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone15;
    private final ModelRenderer magazineRenderer;
    private final ModelRenderer bone16;
    private final ModelRenderer bone17;

    public M1911Model() {
        texWidth = 128;
        texHeight = 128;

        gunRenderer = new ModelRenderer(this);
        gunRenderer.setPos(0.0F, 24.0F, 0.0F);
        gunRenderer.texOffs(69, 13).addBox(0.6523F, -2.0742F, -10.0F, 1.0F, 2.0F, 9.0F, 0.0F, false);
        gunRenderer.texOffs(69, 13).addBox(-1.6523F, -2.0742F, -10.0F, 1.0F, 2.0F, 9.0F, 0.0F, true);
        gunRenderer.texOffs(69, 13).addBox(-0.9452F, -0.3671F, -10.0F, 1.0F, 1.0F, 9.0F, 0.0F, true);
        gunRenderer.texOffs(69, 13).addBox(-0.0548F, -0.3671F, -10.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        gunRenderer.texOffs(69, 13).addBox(-2.0F, -0.975F, -1.0F, 4.0F, 2.0F, 3.0F, 0.0F, false);
        gunRenderer.texOffs(69, 13).addBox(-1.0F, 4.6231F, 2.7321F, 2.0F, 1.0F, 7.0F, 0.0F, false);
        gunRenderer.texOffs(69, 13).addBox(-1.5F, 1.025F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(69, 13).addBox(-1.5F, 1.5602F, -0.0508F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(85, 25).addBox(-2.0F, -0.9219F, 2.0F, 4.0F, 1.0F, 16.0F, 0.0F, false);
        gunRenderer.texOffs(91, 22).addBox(-2.0F, 0.0781F, 8.625F, 4.0F, 1.0F, 7.0F, 0.0F, false);
        gunRenderer.texOffs(91, 22).addBox(-2.0F, 1.0781F, 8.625F, 4.0F, 1.0F, 7.0F, 0.0F, false);
        gunRenderer.texOffs(34, 40).addBox(-1.0F, -5.9F, -10.3516F, 2.0F, 1.0F, 11.0F, 0.0F, false);
        gunRenderer.texOffs(35, 40).addBox(-1.0F, -5.9F, 0.6484F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        gunRenderer.texOffs(42, 45).addBox(0.0F, -5.9F, 4.6484F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        gunRenderer.texOffs(13, 74).addBox(-0.8062F, -5.6688F, 4.6484F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        gunRenderer.texOffs(44, 48).addBox(-1.0F, -5.9F, 11.6484F, 2.0F, 1.0F, 7.0F, 0.0F, false);
        gunRenderer.texOffs(3, 76).addBox(-1.5F, -6.318F, 16.6484F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(3, 76).addBox(0.5F, -6.918F, 16.6484F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(3, 76).addBox(-1.5F, -6.918F, 16.6484F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(3, 76).addBox(-0.5F, -6.7867F, -9.7227F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(36, 40).addBox(-1.5F, -2.1679F, -10.3516F, 3.0F, 2.0F, 6.0F, 0.0F, false);
        gunRenderer.texOffs(36, 40).addBox(-0.2071F, -0.4608F, -10.3516F, 1.0F, 1.0F, 10.0F, 0.0F, false);
        gunRenderer.texOffs(36, 40).addBox(-0.7929F, -0.4608F, -10.3516F, 1.0F, 1.0F, 10.0F, 0.0F, true);
        gunRenderer.texOffs(34, 38).addBox(-2.366F, -4.534F, -10.3516F, 1.0F, 4.0F, 11.0F, 0.0F, false);
        gunRenderer.texOffs(35, 40).addBox(-2.366F, -4.534F, 0.6484F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        gunRenderer.texOffs(35, 40).addBox(-2.366F, -3.534F, 4.6484F, 1.0F, 3.0F, 7.0F, 0.0F, false);
        gunRenderer.texOffs(13, 74).addBox(-2.1723F, -4.3027F, 4.6484F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        gunRenderer.texOffs(44, 48).addBox(-2.366F, -4.534F, 11.6484F, 1.0F, 4.0F, 7.0F, 0.0F, false);
        gunRenderer.texOffs(42, 43).addBox(0.2148F, -3.1956F, 18.748F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(42, 43).addBox(-1.2148F, -3.1956F, 18.748F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        gunRenderer.texOffs(42, 43).addBox(-0.7852F, -1.534F, 18.748F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(42, 43).addBox(-0.5F, -5.0082F, 17.9028F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(42, 43).addBox(-1.2148F, -1.534F, 18.748F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        gunRenderer.texOffs(50, 50).addBox(-1.5F, -3.534F, 18.1609F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(42, 43).addBox(-2.0F, -3.534F, 17.9297F, 4.0F, 3.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(42, 43).addBox(1.366F, -4.534F, -10.3516F, 1.0F, 4.0F, 11.0F, 0.0F, true);
        gunRenderer.texOffs(40, 9).addBox(-1.866F, -3.934F, -10.8516F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(40, 9).addBox(0.866F, -3.934F, -10.8516F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        gunRenderer.texOffs(40, 9).addBox(-0.5F, -5.3F, -10.8516F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(40, 9).addBox(-0.5F, -2.5679F, -10.8516F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        gunRenderer.texOffs(42, 43).addBox(1.366F, -4.534F, 0.6484F, 1.0F, 4.0F, 8.0F, 0.0F, true);
        gunRenderer.texOffs(42, 43).addBox(1.366F, -4.534F, 8.6484F, 1.0F, 4.0F, 5.0F, 0.0F, true);
        gunRenderer.texOffs(43, 42).addBox(1.366F, -4.534F, 13.6484F, 1.0F, 4.0F, 5.0F, 0.0F, true);
        gunRenderer.texOffs(12, 78).addBox(-1.0F, -4.5F, -10.4492F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(1.1523F, 2.3828F, -5.5F);
        gunRenderer.addChild(bone9);
        setRotationAngle(bone9, 0.0F, 0.0F, -0.7854F);
        bone9.texOffs(69, 13).addBox(1.0909F, -2.3838F, -4.5F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        bone9.texOffs(69, 13).addBox(-0.2458F, -3.7206F, -4.5F, 1.0F, 1.0F, 9.0F, 0.0F, false);

        bone12 = new ModelRenderer(this);
        bone12.setPos(0.5F, 0.0F, 0.0F);
        gunRenderer.addChild(bone12);
        setRotationAngle(bone12, 0.5236F, 0.0F, 0.0F);
        bone12.texOffs(69, 13).addBox(-2.0F, 1.5037F, -1.4455F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        bone12.texOffs(69, 13).addBox(-1.5F, 2.5037F, -1.4455F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone12.texOffs(69, 13).addBox(-1.5F, 8.2358F, 4.7506F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone12.texOffs(69, 13).addBox(-2.0F, 0.8327F, -0.7875F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(0.5F, 0.0F, 0.0F);
        gunRenderer.addChild(bone13);
        setRotationAngle(bone13, -0.5236F, 0.0F, 0.0F);
        bone13.texOffs(69, 13).addBox(-1.5F, 2.5037F, 3.1776F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        bone13.texOffs(69, 13).addBox(-2.0F, 0.406F, 0.8343F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        bone14 = new ModelRenderer(this);
        bone14.setPos(0.0F, 2.7781F, 12.125F);
        gunRenderer.addChild(bone14);
        setRotationAngle(bone14, 0.2094F, 0.0F, 0.0F);
        bone14.texOffs(91, 22).addBox(-1.5F, -1.5F, -3.0F, 3.0F, 2.0F, 6.0F, 0.0F, false);
        bone14.texOffs(91, 22).addBox(-2.0F, 0.1F, -3.5F, 4.0F, 7.0F, 7.0F, 0.0F, false);

        bone10 = new ModelRenderer(this);
        bone10.setPos(-1.0F, 8.0F, 0.0F);
        gunRenderer.addChild(bone10);
        setRotationAngle(bone10, 0.6109F, 0.0F, 0.0F);
        bone10.texOffs(91, 22).addBox(-1.0F, 2.2921F, 16.7695F, 4.0F, 1.0F, 2.0F, 0.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, 10.7921F, 6.7695F);
        gunRenderer.addChild(bone11);
        setRotationAngle(bone11, 0.7854F, 0.0F, 0.0F);
        bone11.texOffs(91, 22).addBox(-2.0F, -11.2789F, 3.5338F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        bone11.texOffs(91, 22).addBox(-2.0F, -7.5567F, 8.1808F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(-1.5664F, 0.0F, 0.0F);
        gunRenderer.addChild(bone8);
        setRotationAngle(bone8, 0.0F, 0.0F, -0.7854F);
        bone8.texOffs(36, 40).addBox(0.1657F, -0.0718F, -10.3516F, 1.0F, 1.0F, 10.0F, 0.0F, false);
        bone8.texOffs(36, 40).addBox(1.287F, 1.0495F, -10.3516F, 1.0F, 1.0F, 10.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, -2.2268F, 19.1777F);
        gunRenderer.addChild(bone6);
        setRotationAngle(bone6, -0.3491F, 0.0F, 0.0F);
        bone6.texOffs(17, 79).addBox(-0.5F, -2.2598F, -0.7766F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        bone6.texOffs(41, 42).addBox(0.2148F, -2.5196F, -2.2096F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone6.texOffs(41, 42).addBox(-1.2148F, -2.5196F, -2.2096F, 2.0F, 1.0F, 1.0F, 0.0F, true);

        bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, -2.2268F, 19.1777F);
        gunRenderer.addChild(bone7);
        setRotationAngle(bone7, -0.1745F, 0.0F, 0.0F);
        bone7.texOffs(17, 79).addBox(-0.5F, -2.3603F, -0.3723F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(0.0F, -3.034F, 18.8711F);
        gunRenderer.addChild(bone4);
        setRotationAngle(bone4, 0.2618F, 0.0F, 0.0F);
        bone4.texOffs(42, 43).addBox(-1.5F, -2.3725F, -0.5898F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        bone4.texOffs(42, 43).addBox(-2.0F, -1.4678F, -0.814F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        bone4.texOffs(42, 42).addBox(-2.0F, -1.9678F, -0.814F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, -2.746F, 18.8722F);
        gunRenderer.addChild(bone5);
        setRotationAngle(bone5, 0.4363F, 0.0F, 0.0F);
        bone5.texOffs(42, 43).addBox(0.2148F, -2.0373F, -0.0163F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        bone5.texOffs(42, 43).addBox(-1.2148F, -2.0373F, -0.0163F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        bone2 = new ModelRenderer(this);
        bone2.setPos(0.0F, 0.0F, 0.0F);
        gunRenderer.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.0F, -0.5236F);
        bone2.texOffs(38, 43).addBox(1.084F, -5.6095F, -10.3516F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone2.texOffs(46, 10).addBox(-0.149F, -3.4739F, -10.8516F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone2.texOffs(46, 10).addBox(1.217F, -4.8399F, -10.8516F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone2.texOffs(46, 10).addBox(1.217F, -2.1079F, -10.8516F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone2.texOffs(46, 10).addBox(2.583F, -3.4739F, -10.8516F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone2.texOffs(35, 40).addBox(1.084F, -5.6095F, 0.6484F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone2.texOffs(44, 48).addBox(1.084F, -5.6095F, 11.6484F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        bone2.texOffs(13, 74).addBox(1.1361F, -5.3124F, 4.6484F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        bone2.texOffs(36, 41).addBox(3.316F, -3.7435F, -10.3516F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone2.texOffs(42, 43).addBox(3.316F, -3.7435F, 0.6484F, 1.0F, 1.0F, 8.0F, 0.0F, false);
        bone2.texOffs(42, 43).addBox(3.316F, -3.7435F, 8.6484F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        bone2.texOffs(43, 42).addBox(3.316F, -3.7435F, 13.6484F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        bone2.texOffs(36, 40).addBox(1.45F, -1.5115F, -10.3516F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        bone2.texOffs(36, 40).addBox(-0.7821F, -3.3775F, -10.3516F, 1.0F, 2.0F, 6.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, 0.0F, 0.0F);
        gunRenderer.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, 0.5236F);
        bone3.texOffs(34, 38).addBox(-4.316F, -3.7435F, -10.3516F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone3.texOffs(46, 10).addBox(-3.583F, -3.4739F, -10.8516F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone3.texOffs(46, 10).addBox(-2.217F, -4.8399F, -10.8516F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone3.texOffs(46, 10).addBox(-0.851F, -3.4739F, -10.8516F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone3.texOffs(46, 10).addBox(-2.217F, -2.1079F, -10.8516F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone3.texOffs(35, 40).addBox(-4.316F, -3.7435F, 0.6484F, 1.0F, 1.0F, 4.0F, 0.0F, true);
        bone3.texOffs(44, 48).addBox(-4.316F, -3.7435F, 11.6484F, 1.0F, 1.0F, 7.0F, 0.0F, true);
        bone3.texOffs(13, 74).addBox(-4.0326F, -3.6401F, 4.6484F, 1.0F, 1.0F, 7.0F, 0.0F, true);
        bone3.texOffs(36, 40).addBox(-0.2179F, -3.3775F, -10.3516F, 1.0F, 2.0F, 6.0F, 0.0F, true);
        bone3.texOffs(36, 40).addBox(-2.45F, -1.5115F, -10.3516F, 1.0F, 1.0F, 6.0F, 0.0F, true);
        bone3.texOffs(34, 40).addBox(-2.084F, -5.6095F, -10.3516F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone3.texOffs(42, 44).addBox(-2.084F, -5.6095F, 0.6484F, 1.0F, 1.0F, 8.0F, 0.0F, true);
        bone3.texOffs(40, 40).addBox(-2.084F, -5.6095F, 8.6484F, 1.0F, 1.0F, 5.0F, 0.0F, true);
        bone3.texOffs(43, 42).addBox(-2.084F, -5.6095F, 13.6484F, 1.0F, 1.0F, 5.0F, 0.0F, true);

        bone15 = new ModelRenderer(this);
        bone15.setPos(-1.5F, 0.0F, 0.0F);
        gunRenderer.addChild(bone15);
        setRotationAngle(bone15, 0.1047F, 0.0F, 0.0F);
        bone15.texOffs(91, 22).addBox(-0.5F, 2.9781F, 8.325F, 4.0F, 2.0F, 7.0F, 0.0F, false);
        bone15.texOffs(91, 22).addBox(-0.5F, 10.7256F, 8.9912F, 4.0F, 2.0F, 7.0F, 0.0F, false);

        magazineRenderer = new ModelRenderer(this);
        magazineRenderer.setPos(-1.5F, 45.0F, 0.0F);
        setRotationAngle(magazineRenderer, 0.1047F, 0.0F, 0.0F);
        magazineRenderer.texOffs(0, 69).addBox(0.0F, -9.1594F, 10.1863F, 3.0F, 2.0F, 7.0F, 0.0F, false);
        magazineRenderer.texOffs(0, 69).addBox(0.0F, -20.1594F, 12.1863F, 3.0F, 11.0F, 5.0F, 0.0F, false);
        magazineRenderer.texOffs(0, 69).addBox(1.0F, -20.8594F, 16.0863F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        magazineRenderer.texOffs(8, 104).addBox(1.0F, -21.4594F, 13.1863F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        magazineRenderer.texOffs(0, 69).addBox(1.0F, -20.9594F, 12.4863F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        magazineRenderer.texOffs(0, 69).addBox(2.0F, -21.1594F, 12.1863F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        magazineRenderer.texOffs(0, 69).addBox(0.0F, -21.1594F, 12.1863F, 1.0F, 1.0F, 5.0F, 0.0F, false);

        bone16 = new ModelRenderer(this);
        bone16.setPos(-0.5F, 0.0F, 0.0F);
        magazineRenderer.addChild(bone16);
        setRotationAngle(bone16, 0.0F, 0.0F, 0.1745F);
        bone16.texOffs(0, 69).addBox(-3.1819F, -21.9248F, 12.1863F, 1.0F, 1.0F, 5.0F, 0.0F, false);

        bone17 = new ModelRenderer(this);
        bone17.setPos(-0.5F, 0.0F, 0.0F);
        magazineRenderer.addChild(bone17);
        setRotationAngle(bone17, 0.0F, 0.0F, -0.1745F);
        bone17.texOffs(0, 69).addBox(6.1211F, -21.2302F, 12.1863F, 1.0F, 1.0F, 5.0F, 0.0F, false);
    }

    @Override
    public void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        gunRenderer.render(matrix, builder, light, overlay);
        magazineRenderer.render(matrix, builder, light, overlay);
    }
}
